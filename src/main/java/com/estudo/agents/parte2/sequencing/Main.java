package com.estudo.agents.parte2.sequencing;

import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.model.openai.OpenAiChatModel;

/**
 * Ponto de entrada da Fase 2A: padrão Sequenciamento (Prompt Chaining).
 *
 * <h2>O que este exemplo demonstra</h2>
 * Dois agentes especializados em série:
 * <ol>
 *   <li>{@code GeradorDeCv} converte história de vida → CV mestre (genérico)</li>
 *   <li>{@code AdaptadorDeCv} adapta o CV mestre → CV final (aderente à vaga)</li>
 * </ol>
 * O {@code sequenceBuilder} coordena os agentes via escopo interno,
 * passando automaticamente a saída de um como entrada do próximo.
 *
 * <h2>Como executar</h2>
 * <pre>
 * export OPENAI_API_KEY="sk-..."
 * mvn -q compile exec:java -Dexec.mainClass=com.estudo.agents.parte2.sequencing.Main
 * </pre>
 *
 * <h2>Saída esperada</h2>
 * <pre>
 * === Fase 2A: Sequenciamento ===
 *
 * ╔══════════════════════════════════════╗
 *   CV FINAL ADAPTADO PARA A VAGA
 * ╚══════════════════════════════════════╝
 *
 * RESUMO PROFISSIONAL
 * Engenheiro de software sênior com 8 anos de experiência...
 * </pre>
 */
public class Main {

    public static void main(String[] args) {

        // Modelo compartilhado entre todos os agentes da sequência.
        // Temperatura baixa = mais determinismo, melhor para tarefas estruturadas.
        var modeloDeChat = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("gpt-4o-mini")
                .temperature(0.3)
                .build();

        // outputKey deve bater com o @V que o próximo agente usa para consumir o resultado.
        // AdaptadorDeCv espera @V("cvMestre"), então o gerador grava nessa chave.
        var gerador = AgenticServices.agentBuilder(GeradorDeCv.class)
                .chatModel(modeloDeChat)
                .outputKey("cvMestre")
                .build();

        // O resultado do adaptador é a saída final; "cvFinal" é o que a sequência devolve.
        var adaptador = AgenticServices.agentBuilder(AdaptadorDeCv.class)
                .chatModel(modeloDeChat)
                .outputKey("cvFinal")
                .build();

        // ---------------------------------------------------------------
        // DADOS DE ENTRADA
        // Nomes em português = conceitos de negócio (regra do projeto).
        // ---------------------------------------------------------------
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

        // sequenceBuilder monta a cadeia: GeradorDeCv → AdaptadorDeCv.
        // O escopo interno passa "cvMestre" automaticamente entre eles.
        var sequencia = AgenticServices
                .sequenceBuilder(GeradorSequencialDeCv.class)
                .subAgents(gerador, adaptador)
                .outputKey("cvFinal")
                .build();

        System.out.println("\n=== Fase 2A: Sequenciamento ===\n");
        String cvFinal = sequencia.gerarEAdaptarCv(historicoDeVida, descricaoVaga);
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("        CV FINAL ADAPTADO PARA A VAGA   ");
        System.out.println("╚══════════════════════════════════════╝\n");
        System.out.println(cvFinal);
    }
}
