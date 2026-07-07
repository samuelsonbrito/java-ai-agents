/**
 * Parte 2 — Padrão: Roteamento Condicional.
 *
 * <p>Uma condição avalia o estado do AgenticScope e decide qual ramo (agente)
 * executar a seguir. Implementado com {@code AgenticServices.conditionalBuilder}.
 *
 * <p>Conceitos-chave deste pacote:
 * <ul>
 *   <li><strong>conditionalBuilder</strong> — define um predicado e dois destinos:
 *       o ramo "true" e o ramo "false".</li>
 *   <li><strong>Composição de padrões</strong> — este pacote é intencionalmente
 *       integrado ao {@code parte2.parallel}: o resultado agregado dos revisores
 *       paralelos alimenta o roteador, que escolhe entre aprovar ou recusar o
 *       candidato.</li>
 * </ul>
 *
 * <p>Fluxo completo (paralelo → roteamento):
 * [3 revisores paralelos] → agregação → conditionalBuilder → InterviewOrganizer OU EmailAssistant.
 */
package com.estudo.agents.parte2.routing;
