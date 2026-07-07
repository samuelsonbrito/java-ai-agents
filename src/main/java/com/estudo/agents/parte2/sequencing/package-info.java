/**
 * Parte 2 — Padrão: Sequenciamento (Prompt Chaining).
 *
 * <p>O padrão mais simples de coordenação: a saída de um agente alimenta a entrada
 * do próximo, formando uma cadeia determinística. O LangChain4j implementa isso via
 * {@code AgenticServices.sequenceBuilder}.
 *
 * <p>Conceitos-chave deste pacote:
 * <ul>
 *   <li><strong>AgenticScope</strong> — memória compartilhada (Map de chave→valor) que
 *       passa estado entre os agentes da sequência.</li>
 *   <li><strong>@V("nome")</strong> — anotação que injeta um valor do AgenticScope como
 *       parâmetro de método. O "nome" DEVE casar exatamente com a outputKey de quem
 *       produziu o valor (erro de nome é a causa nº 1 de variável chegar vazia).</li>
 *   <li><strong>outputKey</strong> — chave sob a qual o agente grava sua saída no escopo.</li>
 * </ul>
 *
 * <p>Fluxo deste exemplo: história de vida → CvGenerator (masterCv) → CvTailor (finalCv).
 */
package com.estudo.agents.parte2.sequencing;
