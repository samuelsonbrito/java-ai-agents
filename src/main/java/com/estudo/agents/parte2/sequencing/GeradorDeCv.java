package com.estudo.agents.parte2.sequencing;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * Agente 1 da sequência: transforma uma história de vida em um CV estruturado.
 *
 * <h2>O que esta classe faz</h2>
 * Recebe um texto livre descrevendo a trajetória profissional de uma pessoa
 * e devolve um CV formatado — sem saber nada sobre a vaga de destino.
 * Este é o "CV mestre": completo, genérico e bem organizado.
 *
 * <h2>Conceito LangChain4j demonstrado</h2>
 * <strong>Agente especializado com escopo único</strong>: cada agente da
 * sequência tem uma responsabilidade estreita. O {@code GeradorDeCv} só sabe
 * transformar história em CV — a adaptação para vaga é responsabilidade
 * do próximo agente.
 *
 * <h2>Como se conecta com as demais classes</h2>
 * {@code Main} registra este agente no {@code sequenceBuilder} com
 * {@code outputKey("cvMestre")}. O resultado fica disponível no escopo
 * para que o {@code AdaptadorDeCv} o leia via {@code @V("cvMestre")}.
 */
public interface GeradorDeCv {

    /**
     * Gera um CV estruturado a partir de uma descrição livre da trajetória profissional.
     *
     * @param historicoDeVida texto descrevendo experiências, formação e habilidades
     * @return CV formatado em texto estruturado (seções: Resumo, Experiência, Formação, Competências)
     */
    @SystemMessage("""
            Você é um especialista em recursos humanos com 20 anos de experiência
            elaborando currículos profissionais de alto impacto.

            Ao receber a história de vida de uma pessoa, crie um CV estruturado com:
            - Resumo Profissional (3-4 linhas)
            - Experiência Profissional (cada cargo com empresa, período e realizações)
            - Formação Acadêmica
            - Competências Técnicas e Comportamentais

            O CV deve ser completo e genérico — não mencione vaga específica ainda.
            Escreva em português formal. Seja direto e objetivo.
            """)
    @UserMessage("Crie um CV profissional a partir desta história de vida:\n\n{{historicoDeVida}}")
    @Agent("Gera um CV limpo com base nas informações do usuário")
    String gerarCv(@V("historicoDeVida") String historicoDeVida);
}
