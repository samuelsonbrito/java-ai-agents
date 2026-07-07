package com.estudo.agents.parte2.sequencing;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

/**
 * Ponto de entrada da Fase 2A: padrão Sequenciamento (Prompt Chaining).
 *
 * <h2>O que este exemplo demonstra</h2>
 * Dois agentes especializados em série:
 * <ol>
 *   <li>{@code CvGenerator} converte história de vida → CV mestre (genérico)</li>
 *   <li>{@code CvTailor} adapta o CV mestre → CV final (aderente à vaga)</li>
 * </ol>
 * O estado intermediário fica visível no console para o estudante
 * acompanhar o encadeamento passo a passo.
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
 * [PASSO 1] Chamando CvGenerator com história de vida...
 *
 * ── Após CvGenerator ──
 * Chaves no escopo: [masterCv]
 * [masterCv] → RESUMO PROFISSIONAL
 * Engenheiro de software com 8 anos de experiência...
 *
 * [PASSO 2] Chamando CvTailor para adaptar à vaga...
 *
 * ── Após CvTailor ──
 * Chaves no escopo: [masterCv, finalCv]
 * ...
 *
 * ╔══════════════════════════════════════╗
 *   CV FINAL ADAPTADO PARA A VAGA
 * ╚══════════════════════════════════════╝
 * ...
 * </pre>
 */
public class Main {

    public static void main(String[] args) {

        // Modelo compartilhado entre todos os agentes da sequência.
        // Temperatura baixa = mais determinismo, melhor para tarefas estruturadas.
        var chatModel = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("gpt-4o-mini")
                .temperature(0.3)
                .build();

        // Cada agente é um AI Service independente — não sabem da existência um do outro.
        // A coordenação acontece no Main, via AgenticScope.
        var gerador = AiServices.builder(CvGenerator.class)
                .chatLanguageModel(chatModel)
                .build();

        var adaptador = AiServices.builder(CvTailor.class)
                .chatLanguageModel(chatModel)
                .build();

        // ---------------------------------------------------------------
        // DADOS DE ENTRADA
        // "historicoDeVida" é nome de negócio (português); o conceito
        // é o mesmo que "input" ou "userInput" — mas nomear assim
        // deixa claro o que o dado representa.
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

        // ---------------------------------------------------------------
        // EXECUÇÃO DA SEQUÊNCIA
        // ---------------------------------------------------------------
        var escopo = new AgenticScope();

        System.out.println("=== Fase 2A: Sequenciamento ===\n");

        // PASSO 1 — agente 1 grava sua saída no escopo com a chave "masterCv"
        System.out.println("[PASSO 1] Chamando CvGenerator com história de vida...");
        String masterCv = gerador.gerarCv(historicoDeVida);
        escopo.colocar("masterCv", masterCv);  // outputKey = "masterCv"

        // Exibe estado intermediário — o estudante VÊ o CV mestre antes do final
        escopo.imprimir("Após CvGenerator");

        // PASSO 2 — agente 2 lê "masterCv" do escopo (@V equivalente) e produz "finalCv"
        System.out.println("[PASSO 2] Chamando CvTailor para adaptar à vaga...");
        String cvMestre = escopo.obter("masterCv");  // equivale a @V("masterCv")
        String finalCv = adaptador.adaptarCv(cvMestre, descricaoVaga);
        escopo.colocar("finalCv", finalCv);  // outputKey = "finalCv"

        escopo.imprimir("Após CvTailor");

        // ---------------------------------------------------------------
        // RESULTADO FINAL
        // ---------------------------------------------------------------
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("        CV FINAL ADAPTADO PARA A VAGA   ");
        System.out.println("╚══════════════════════════════════════╝\n");
        System.out.println(escopo.obter("finalCv"));
    }
}
