package com.estudo.agents.parte2.sequencing;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * Agente 2 da sequência: adapta o CV mestre para uma vaga específica.
 *
 * <h2>O que esta classe faz</h2>
 * Recebe o CV completo gerado pelo {@code GeradorDeCv} e os requisitos
 * da vaga, e produz uma versão adaptada: reorganiza prioridades, destaca
 * experiências relevantes e ajusta a linguagem ao perfil pedido.
 *
 * <h2>Conceito LangChain4j demonstrado</h2>
 * <strong>Encadeamento via escopo interno</strong>: este agente não precisa
 * conhecer o {@code GeradorDeCv} — ele só precisa que o escopo contenha
 * o valor gravado sob a chave {@code "cvMestre"}. O desacoplamento entre
 * quem produz e quem consome é a essência do padrão de sequenciamento.
 *
 * <h2>Como se conecta com as demais classes</h2>
 * O {@code sequenceBuilder} injeta {@code "cvMestre"} (saída do agente anterior)
 * e {@code "descricaoVaga"} (entrada original) no escopo antes de invocar
 * este agente. O resultado é gravado como {@code "cvFinal"}.
 */
public interface AdaptadorDeCv {

    /**
     * Adapta um CV genérico para uma vaga específica.
     *
     * <p>Os nomes entre {@code {{}}} no {@code @UserMessage} devem corresponder
     * exatamente aos parâmetros do método — erro de nome é a causa nº 1 de
     * variável chegar vazia no prompt.
     *
     * @param cvMestre      CV completo gerado pelo GeradorDeCv (lido do escopo)
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
    @Agent("Adapta um CV conforme instruções específicas")
    String adaptarCv(@V("cvMestre") String cvMestre, @V("descricaoVaga") String descricaoVaga);
}
