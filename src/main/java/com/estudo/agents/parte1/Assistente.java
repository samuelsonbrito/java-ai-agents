package com.estudo.agents.parte1;

import dev.langchain4j.service.SystemMessage;

/**
 * Interface que define o contrato do nosso primeiro AI Service.
 *
 * <h2>O que é um AI Service?</h2>
 * Um AI Service é uma interface Java comum — você a declara, o LangChain4j a
 * <em>implementa em runtime</em>. Não há código de implementação: o framework
 * usa proxy dinâmico para interceptar as chamadas de método e traduzi-las em
 * requisições ao modelo de linguagem.
 *
 * <h2>Conceito LangChain4j demonstrado</h2>
 * <strong>Interface declarativa de AI Service</strong>: em vez de construir
 * strings de prompt manualmente e chamar {@code model.generate(...)}, você
 * anota métodos com {@code @SystemMessage} e {@code @UserMessage}, e o
 * framework monta o prompt, faz a chamada e devolve a resposta como tipo Java.
 *
 * <p>Esta abstração é a <em>fundação</em> de tudo que vem a seguir:
 * <ul>
 *   <li>Os agentes das fases 2A–2E são AI Services com anotações extras
 *       ({@code @Agent}, {@code @V}, {@code outputKey}).</li>
 *   <li>O RAG da fase 3 conecta um {@code ContentRetriever} ao mesmo
 *       {@code AiServices.builder}, sem mudar a interface.</li>
 * </ul>
 *
 * <h2>Como se conecta com as demais classes</h2>
 * {@code Main} usa {@code AiServices.builder(Assistente.class)} para obter
 * uma instância concreta desta interface em tempo de execução.
 */
public interface Assistente {

    /**
     * Responde a uma pergunta do usuário.
     *
     * <p>O {@code @SystemMessage} define a "personalidade" do assistente: é enviado
     * ao modelo como mensagem de sistema antes de qualquer pergunta do usuário.
     * O parâmetro {@code pergunta} é automaticamente mapeado como mensagem do usuário.
     *
     * @param pergunta texto digitado pelo usuário
     * @return resposta gerada pelo modelo de linguagem
     */
    @SystemMessage("""
            Você é um assistente técnico especializado em Java e inteligência artificial.
            Responda sempre em português, de forma clara e didática.
            Se não souber a resposta, diga que não sabe — nunca invente informações.
            """)
    String responder(String pergunta);
}
