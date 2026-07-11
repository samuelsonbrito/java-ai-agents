package com.estudo.agents.parte1;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

/**
 * Ponto de entrada do primeiro exemplo: AI Service básico.
 *
 * <h2>O que este exemplo demonstra</h2>
 * Os três blocos essenciais de qualquer uso do LangChain4j:
 * <ol>
 *   <li><strong>Modelo</strong> — configura qual LLM será usado e com quais parâmetros.</li>
 *   <li><strong>Builder</strong> — conecta o modelo à interface declarativa.</li>
 *   <li><strong>Uso</strong> — chama o método como se fosse Java puro.</li>
 * </ol>
 *
 * <h2>Como executar</h2>
 * <pre>
 * export OPENAI_API_KEY="sk-..."
 * mvn -q compile exec:java -Dexec.mainClass=com.estudo.agents.parte1.Main
 * </pre>
 *
 * <h2>Saída esperada (aproximada)</h2>
 * <pre>
 * === Fase 1: Primeiro AI Service ===
 *
 * Pergunta: O que é o LangChain4j e para que ele serve?
 *
 * Resposta:
 * O LangChain4j é um framework Java para construção de aplicações baseadas em
 * modelos de linguagem (LLMs). Ele fornece abstrações para conectar LLMs a
 * ferramentas, memória e fontes de dados externas...
 * </pre>
 *
 * <h2>Erros comuns</h2>
 * <ul>
 *   <li><strong>401 Unauthorized</strong> — a variável OPENAI_API_KEY não está
 *       definida ou está incorreta. Verifique com {@code echo $OPENAI_API_KEY}.</li>
 *   <li><strong>NullPointerException no builder</strong> — a chave retornou
 *       {@code null} de {@code System.getenv()}. Exporte a variável na mesma
 *       sessão de terminal onde roda o Maven.</li>
 * </ul>
 */
public class Main {

    public static void main(String[] args) {

        // ----------------------------------------------------------------
        // BLOCO 1 — MODELO
        // Configura o cliente OpenAI. temperature(0.3) = respostas mais
        // focadas e menos criativas, adequado para assistentes técnicos.
        // A chave NUNCA fica no código — sempre via variável de ambiente.
        // ----------------------------------------------------------------
        var chatModel = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("gpt-4o-mini")
                .temperature(0.3)
                .build();

        // ----------------------------------------------------------------
        // BLOCO 2 — BUILDER
        // AiServices.builder recebe a interface e devolve um objeto que a
        // implementa em runtime usando proxy dinâmico. A partir daqui, o
        // framework é responsável por: montar o prompt, chamar o modelo,
        // parsear a resposta e devolvê-la como String Java.
        // ----------------------------------------------------------------
        var assistente = AiServices.builder(Assistente.class)
                .chatModel(chatModel)
                .build();

        // ----------------------------------------------------------------
        // BLOCO 3 — USO
        // Parece uma chamada de método Java normal — e é exatamente isso.
        // Por trás, o LangChain4j combina @SystemMessage + o texto de
        // "pergunta" em um prompt e envia ao modelo.
        // ----------------------------------------------------------------
        String pergunta = "O que é o LangChain4j e para que ele serve?";

        System.out.println("=== Fase 1: Primeiro AI Service ===\n");
        System.out.println("Pergunta: " + pergunta);
        System.out.println();

        String resposta = assistente.responder(pergunta);

        System.out.println("Resposta:");
        System.out.println(resposta);
    }
}
