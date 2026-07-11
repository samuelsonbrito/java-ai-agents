package com.estudo.agents.parte2.sequencing;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.V;

/**
 * Interface de entrada da sequência: recebe os dados brutos e devolve o CV final adaptado.
 *
 * <h2>O que esta interface faz</h2>
 * Define o contrato público da cadeia de agentes. O {@code sequenceBuilder}
 * usa esta interface para expor um método único que internamente executa
 * {@code GeradorDeCv} → {@code AdaptadorDeCv} em sequência.
 *
 * <h2>Conceito LangChain4j demonstrado</h2>
 * Os parâmetros anotados com {@code @V} são colocados no escopo interno
 * antes da sequência começar. Cada agente da cadeia pode ler qualquer
 * chave do escopo, não apenas a saída do agente imediatamente anterior.
 */
public interface GeradorSequencialDeCv {

    /**
     * Executa a sequência completa: gera o CV mestre e o adapta à vaga.
     *
     * @param historicoDeVida texto livre com a trajetória profissional do candidato
     * @param descricaoVaga   requisitos e responsabilidades da vaga alvo
     * @return CV final adaptado à vaga
     */
    @Agent("Gera um CV baseado nas informações do usuário e o adapta a uma vaga")
    String gerarEAdaptarCv(@V("historicoDeVida") String historicoDeVida,
                           @V("descricaoVaga") String descricaoVaga);
}
