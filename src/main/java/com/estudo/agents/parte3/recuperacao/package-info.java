/**
 * Parte 3 — RAG: Recuperação e Geração.
 *
 * <p>Com os documentos ingeridos, este pacote demonstra dois níveis de recuperação:
 * <ul>
 *   <li><strong>Busca semântica pura</strong> ({@code BuscaManual}) — embeda a pergunta
 *       e retorna os chunks mais similares por cosseno. Roda 100% offline. Ainda NÃO é RAG.</li>
 *   <li><strong>RAG completo</strong> ({@code RagMain}) — conecta o retriever ao AI Service:
 *       o LangChain4j injeta automaticamente os chunks recuperados no prompt antes de
 *       enviar ao LLM, que gera a resposta fundamentada no documento.</li>
 * </ul>
 *
 * <p>O "Teste de Controle" documentado no RagMain (comentar o retriever e observar o
 * modelo alucinar) é o experimento mais valioso desta fase.
 */
package com.estudo.agents.parte3.recuperacao;
