/**
 * Parte 2 — Padrão: P2P (Peer-to-Peer / Emergência).
 *
 * <p>Não há hierarquia: cada agente "observa" o AgenticScope e age quando suas entradas
 * estão disponíveis. O planejador ativa, a cada rodada, todos os agentes cujas
 * dependências estão satisfeitas. O sistema converge quando não há mais agentes
 * a ativar ou uma condição de saída é atingida.
 *
 * <p>Conceitos-chave deste pacote:
 * <ul>
 *   <li><strong>Ativação por disponibilidade</strong> — um agente roda quando todas as
 *       suas entradas (@V) existem no escopo, sem ordem pré-definida.</li>
 *   <li><strong>Estado estável</strong> — o sistema para quando nenhum agente novo pode
 *       ser ativado (ponto fixo).</li>
 * </ul>
 *
 * <p>Contraste hierárquico vs. P2P:
 * <pre>
 * | Dimensão          | Hierárquico (supervisor/GOAP)  | P2P                      |
 * |-------------------|-------------------------------|--------------------------|
 * | Coordenação       | Centralizada (um planejador)   | Descentralizada          |
 * | Ordem             | Planejada                     | Emergente                |
 * | Escalabilidade    | Limitada pelo planejador       | Melhor para muitos agentes|
 * | Previsibilidade   | Alta                          | Menor                    |
 * </pre>
 *
 * <p>Cenário: pesquisa científica colaborativa.
 * Agentes: LiteratureAgent → HypothesisAgent → CriticAgent → ValidationAgent → ScorerAgent.
 */
package com.estudo.agents.parte2.p2p;
