/**
 * Parte 3 — RAG: Ingestão de Documentos.
 *
 * <p>O pipeline de ingestão transforma documentos brutos em vetores pesquisáveis.
 * As etapas numeradas (1 a 5) correspondem às fases clássicas do RAG:
 * <ol>
 *   <li>Carregar e parsear o documento (texto bruto)</li>
 *   <li>Pré-processamento (limpeza, metadados)</li>
 *   <li>Chunking — dividir em pedaços menores para melhor recuperação</li>
 *   <li>Embedding — converter cada chunk em vetor numérico (BGE local)</li>
 *   <li>Armazenar no vector store (InMemoryEmbeddingStore)</li>
 * </ol>
 *
 * <p>Destaque didático: o embedding roda 100% localmente (sem API), enquanto
 * o chat usa OpenAI. Isso separa claramente as responsabilidades e reduz custo.
 */
package com.estudo.agents.parte3.ingestao;
