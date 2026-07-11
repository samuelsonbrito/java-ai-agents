# Prompt Chaining com LangChain4j: encadeando agentes para gerar e adaptar CVs

*Parte 2A da série AI Agents with Java*

---

## Por que dividir em agentes?

Imagine pedir para uma única pessoa gerar um CV completo E adaptá-lo para uma vaga específica ao mesmo tempo. O resultado costuma ser medíocre dos dois lados: o CV fica nem genérico nem direcionado.

A mesma lógica vale para LLMs. Quando você empilha responsabilidades demais em um único prompt, a qualidade cai. A solução é a mesma que aplicamos em código: separar responsabilidades.

O padrão **Sequenciamento** (ou *Prompt Chaining*) faz exatamente isso: cada agente faz uma coisa bem feita, e a saída de um vira a entrada do próximo. Determinístico, rastreável, fácil de depurar.

Neste artigo vou mostrar como implementar esse padrão usando o `sequenceBuilder` do **LangChain4j 1.17.0**, com um exemplo real: gerar um CV a partir de uma história de vida e adaptá-lo para uma vaga específica.

---

## A estrutura do exemplo

O fluxo tem três peças principais:

```
história de vida + descrição da vaga
          │
          ▼
   [ GeradorDeCv ]  ──── grava "cvAtual" no escopo
          │
          ▼
  [ AdaptadorDeCv ] ──── lê "cvAtual", lê "descricaoVaga", grava "cvFinal"
          │
          ▼
      CV adaptado ← retorno para o chamador
```

O **escopo interno** gerenciado pelo `sequenceBuilder` é o que conecta os agentes. Nenhum agente conhece o outro diretamente — eles só leem e gravam valores em chaves nomeadas.

---

## O contrato público: `GeradorSequencialDeCv`

A sequência inteira é exposta como uma única interface:

```java
public interface GeradorSequencialDeCv {

    @Agent("Gera um CV baseado nas informações do usuário e o adapta a uma vaga")
    String gerarEAdaptarCv(@V("historicoDeVida") String historicoDeVida,
                           @V("descricaoVaga") String descricaoVaga);
}
```

Repare no `@V("historicoDeVida")` e `@V("descricaoVaga")`. Essas anotações colocam os valores no escopo interno **antes** da sequência começar. Qualquer agente da cadeia pode ler essas chaves, não apenas o primeiro — isso é importante para o `AdaptadorDeCv`, que precisará de `descricaoVaga` mesmo sendo o segundo agente.

---

## Agente 1: `GeradorDeCv`

```java
public interface GeradorDeCv {

    @SystemMessage("""
            Você é um especialista em recursos humanos com 20 anos de experiência
            elaborando currículos profissionais de alto impacto.

            Ao receber a história de vida de uma pessoa, crie um CV estruturado com:
            - Resumo Profissional (3-4 linhas)
            - Experiência Profissional (cada cargo com empresa, período e realizações)
            - Formação Acadêmica
            - Competências Técnicas e Comportamentais

            O CV deve ser completo e genérico — não mencione vaga específica ainda.
            """)
    @UserMessage("Crie um CV profissional a partir desta história de vida:\n\n{{historicoDeVida}}")
    @Agent("Gera um CV limpo com base nas informações do usuário")
    String gerarCv(@V("historicoDeVida") String historicoDeVida);
}
```

Esse agente tem uma responsabilidade estreita: pegar um texto livre e estruturá-lo como CV. Ele não sabe nada sobre a vaga. O resultado vai para o escopo sob a chave `"cvAtual"` — definida no `Main` com `.outputKey("cvAtual")`.

---

## Agente 2: `AdaptadorDeCv`

```java
public interface AdaptadorDeCv {

    @SystemMessage("""
            Você é um especialista em recrutamento estratégico.
            Sua tarefa é adaptar CVs existentes para vagas específicas,
            maximizando a aderência do candidato ao perfil buscado.

            Diretrizes:
            - Reordene as seções para destacar o que a vaga valoriza
            - Adapte o Resumo Profissional para mencionar a vaga diretamente
            - Destaque as experiências e competências mais relevantes
            - Mantenha apenas informações verdadeiras — não invente nada
            """)
    @UserMessage("""
            Adapte o CV abaixo para a vaga descrita.

            === CV ATUAL ===
            {{cvAtual}}

            === DESCRIÇÃO DA VAGA ===
            {{descricaoVaga}}

            Produza o CV adaptado completo.
            """)
    @Agent("Adapta um CV conforme instruções específicas")
    String adaptarCv(@V("cvAtual") String cvAtual, @V("descricaoVaga") String descricaoVaga);
}
```

Observe que `@V("cvAtual")` lê do escopo o valor que o `GeradorDeCv` gravou. E `@V("descricaoVaga")` lê o valor original que o chamador colocou no escopo lá no início — sem precisar passar por nenhum agente intermediário.

---

## Montando tudo no `Main`

```java
public class Main {

    public static void main(String[] args) {

        // Modelo compartilhado — temperatura baixa = mais determinismo
        var modeloDeChat = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("gpt-4o-mini")
                .temperature(0.3)
                .build();

        // outputKey deve bater EXATAMENTE com o @V que o próximo agente usa
        var gerador = AgenticServices.agentBuilder(GeradorDeCv.class)
                .chatModel(modeloDeChat)
                .outputKey("cvAtual")   // ← grava sob essa chave
                .build();

        var adaptador = AgenticServices.agentBuilder(AdaptadorDeCv.class)
                .chatModel(modeloDeChat)
                .outputKey("cvFinal")
                .build();

        // sequenceBuilder une os dois agentes e expõe a interface pública
        var sequencia = AgenticServices
                .sequenceBuilder(GeradorSequencialDeCv.class)
                .subAgents(gerador, adaptador)
                .outputKey("cvFinal")   // ← retorno da sequência inteira
                .build();

        String historicoDeVida = """
                Me chamo Lucas Ferreira, tenho 32 anos. Sou formado em Ciência da
                Computação pela USP (2014). Trabalhei 5 anos na startup Loggi como
                engenheiro backend, onde liderei a migração de um monolito Python para
                microsserviços em Go — reduzimos o tempo de resposta das APIs em 40%.
                Depois fui para o Nubank por 3 anos como engenheiro sênior no time de
                fraude, implementando modelos de ML em produção com Java e Kotlin.
                Sou fluente em inglês (morei 1 ano em Dublin) e tenho certificação AWS
                Solutions Architect. Gosto de mentorar desenvolvedores júnior.
                """;

        String descricaoVaga = """
                Vaga: Engenheiro de Software Sênior — Time de Plataforma
                Empresa: PicPay
                Requisitos: Java, microsserviços, experiência em fintechs, inglês avançado.
                Diferenciais: experiência com detecção de fraudes, AWS, liderança técnica.
                Responsabilidades: desenhar arquitetura de serviços de pagamento,
                mentorar times, participar de entrevistas técnicas.
                """;

        System.out.println("\n=== Fase 2A: Sequenciamento ===\n");
        String cvFinal = sequencia.gerarEAdaptarCv(historicoDeVida, descricaoVaga);
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("        CV FINAL ADAPTADO PARA A VAGA   ");
        System.out.println("╚══════════════════════════════════════╝\n");
        System.out.println(cvFinal);
    }
}
```

Duas linhas merecem atenção especial aqui:

1. `.outputKey("cvAtual")` no `gerador` — essa string precisa casar **exatamente** com `@V("cvAtual")` no `AdaptadorDeCv`. Um caractere errado e a variável chega vazia no prompt sem nenhum erro explícito. É a causa número um de bugs silenciosos nesse padrão.

2. `.outputKey("cvFinal")` aparece duas vezes: no `adaptador` (onde o agente grava) e no `sequenceBuilder` (de onde a sequência lê para devolver ao chamador). Sem isso, o método `gerarEAdaptarCv` retorna `null`.

---

## O erro mais comum: chaves que não batem

Suponha que você escreva `.outputKey("cvMestre")` no `gerador`, mas no `AdaptadorDeCv` o parâmetro está anotado como `@V("cvAtual")`. O que acontece?

O LangChain4j não lança exceção. O prompt do `AdaptadorDeCv` chega assim:

```
=== CV ATUAL ===


=== DESCRIÇÃO DA VAGA ===
Vaga: Engenheiro de Software Sênior...
```

O agente vai inventar um CV do zero, ignorando o trabalho do agente anterior. O resultado parece plausível, mas está errado.

**Regra prática:** trate `outputKey` e `@V(...)` como um contrato de API. Quando você muda um, precisa mudar o outro.

---

## Para rodar

```bash
export OPENAI_API_KEY="sk-..."
mvn -q compile exec:java \
  -Dexec.mainClass=com.estudo.agents.parte2.sequencing.Main
```

---

## O que este padrão ensina

O sequenciamento é o padrão mais simples de multiagentes, mas ele estabelece os fundamentos de todos os outros:

- **Escopo interno como barramento** — agentes se comunicam por chaves nomeadas, não por chamadas diretas. Isso os desacopla.
- **Especialização estreita** — um agente que faz uma coisa bem é mais fácil de ajustar, trocar ou reutilizar do que um agente que faz tudo.
- **Fluxo determinístico** — diferente do supervisor (que decide dinamicamente quais agentes chamar), o sequenciamento sempre executa os agentes na mesma ordem. Isso facilita a depuração e o raciocínio sobre o comportamento do sistema.

No próximo artigo vamos além do fluxo linear: o padrão de **reflexão**, onde um agente avalia a própria saída em loop até atingir um critério de qualidade. A mágica acontece quando o score sobe a cada iteração no console.

---

*O código completo está disponível no repositório do projeto. Qualquer dúvida ou correção, é só comentar.*
