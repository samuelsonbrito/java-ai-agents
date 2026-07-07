/**
 * Parte 2 — Padrão: GOAP (Goal-Oriented Action Planning).
 *
 * <p>Um planejador customizado constrói um grafo de agentes, onde cada agente declara
 * suas entradas necessárias (@V) e sua saída (outputKey). O planejador faz uma busca
 * BFS no grafo para encontrar o caminho mínimo até o estado-objetivo, depois executa
 * esse caminho. Implementado via interface {@code Planner}.
 *
 * <p>Conceitos-chave deste pacote:
 * <ul>
 *   <li><strong>Planner (interface)</strong> — contrato para planners customizados:
 *       {@code init()}, {@code firstAction()} e {@code nextAction()}.</li>
 *   <li><strong>BFS sobre agentes</strong> — um agente é "alcançável" quando todas as
 *       suas @V estão no conjunto de estados atuais; executá-lo adiciona seu outputKey.</li>
 *   <li><strong>Grafo de dependências</strong> — o GOAP é útil quando a ordem de execução
 *       não é linear e depende de quais dados já foram produzidos.</li>
 * </ul>
 *
 * <p>Cenário: geração de horóscopo personalizado.
 * Agentes: PersonExtractor → SignExtractor → HoroscopeGenerator → StoryFinder → Writer.
 */
package com.estudo.agents.parte2.goap;
