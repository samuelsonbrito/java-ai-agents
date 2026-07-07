package com.estudo.agents.parte2.sequencing;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

/**
 * Agente 2 da sequência: adapta o CV mestre para uma vaga específica.
 *
 * <h2>O que esta classe faz</h2>
 * Recebe o CV completo gerado pelo {@code CvGenerator} e os requisitos
 * da vaga, e produz uma versão adaptada: reorganiza prioridades, destaca
 * experiências relevantes e ajusta a linguagem ao perfil pedido.
 *
 * <h2>Conceito LangChain4j demonstrado</h2>
 * <strong>Encadeamento via AgenticScope</strong>: este agente não precisa
 * conhecer o {@code CvGenerator} — ele só precisa que o escopo contenha
 * o valor gravado sob a chave {@code "masterCv"}. O desacoplamento entre
 * quem produz e quem consome é a essência do padrão de sequenciamento.
 *
 * <h2>Como se conecta com as demais classes</h2>
 * {@code Main} lê {@code "masterCv"} do {@code AgenticScope} e passa
 * como argumento {@code cvMestre} neste método. O resultado é gravado
 * como {@code "finalCv"} no escopo.
 */
public interface CvTailor {

    /**
     * Adapta um CV genérico para uma vaga específica.
     *
     * <p>O {@code @UserMessage} com múltiplos parâmetros nomeados requer que
     * os nomes entre {@code {{}}} correspondam exatamente aos parâmetros do método.
     *
     * @param cvMestre   CV completo gerado pelo CvGenerator (lido do escopo)
     * @param descricaoVaga texto com os requisitos e responsabilidades da vaga
     * @return CV reformatado e adaptado à vaga, pronto para envio
     */
    @SystemMessage("""
            Você é um especialista em recrutamento estratégico.
            Sua tarefa é adaptar CVs existentes para vagas específicas,
            maximizando a aderência do candidato ao perfil buscado.

            Diretrizes:
            - Reordene as seções para destacar o que a vaga valoriza
            - Adapte o Resumo Profissional para mencionar a vaga diretamente
            - Destaque as experiências e competências mais relevantes
            - Mantenha apenas informações verdadeiras — não invente nada
            - Tom: profissional, confiante e direto
            """)
    @UserMessage("""
            Adapte o CV abaixo para a vaga descrita.

            === CV ATUAL ===
            {{cvMestre}}

            === DESCRIÇÃO DA VAGA ===
            {{descricaoVaga}}

            Produza o CV adaptado completo.
            """)
    String adaptarCv(String cvMestre, String descricaoVaga);
}
