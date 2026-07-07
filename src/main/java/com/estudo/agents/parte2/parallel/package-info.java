/**
 * Parte 2 — Padrão: Paralelização.
 *
 * <p>Múltiplos agentes independentes rodam simultaneamente sobre o mesmo input.
 * O LangChain4j dispara todas as chamadas em paralelo e aguarda todas terminarem
 * antes de prosseguir. Implementado com {@code AgenticServices.parallelBuilder}.
 *
 * <p>Conceitos-chave deste pacote:
 * <ul>
 *   <li><strong>parallelBuilder</strong> — configura os agentes que rodam em paralelo.</li>
 *   <li><strong>.output()</strong> — bloco de agregação: executa após TODOS os agentes
 *       paralelos completarem; recebe os resultados de cada um via @V e produz um
 *       único valor consolidado no AgenticScope.</li>
 * </ul>
 *
 * <p>Fluxo: CV → [HrReviewer, ManagerReviewer, TeamReviewer] (paralelo) → .output() agregador.
 * Este pacote é composto com {@code parte2.routing} no mesmo exemplo.
 */
package com.estudo.agents.parte2.parallel;
