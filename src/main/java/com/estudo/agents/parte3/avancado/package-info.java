/**
 * Parte 3 — RAG: Técnicas Avançadas.
 *
 * <p>Extensões do RAG básico que resolvem problemas reais de produção:
 * <ul>
 *   <li><strong>Metadados</strong> ({@code MetadadosMain}) — enriquecer chunks com
 *       tags e timestamps durante a ingestão para filtragem posterior.</li>
 *   <li><strong>Filtro Dinâmico</strong> ({@code FiltroDinamicoMain}) — isolamento
 *       multi-tenant via metadados; o exemplo mais importante desta fase.</li>
 *   <li><strong>Query Transformer</strong> ({@code QueryTransformerMain}) — comprime
 *       perguntas prolixas via LLM antes de buscar, melhorando a qualidade dos chunks.</li>
 *   <li><strong>Esqueletos</strong> — WebSearch (Tavily), Reranking (Cohere) e
 *       Anonimização (Presidio) documentados como código comentado com instruções
 *       de setup para quando os serviços estiverem disponíveis.</li>
 * </ul>
 */
package com.estudo.agents.parte3.avancado;
