/**
 * Parte 1 — AI Service Básico.
 *
 * <p>Demonstra o conceito fundamental do LangChain4j: o <strong>AI Service</strong>.
 * Em vez de montar prompts manualmente e chamar a API, você declara uma interface Java
 * e o framework a implementa em runtime, conectando os métodos ao modelo de linguagem.
 *
 * <p>Este é o bloco de construção de tudo que vem a seguir: agentes, RAG e
 * coordenação multiagente se apoiam na mesma abstração de AI Service.
 *
 * <p>Exemplos neste pacote:
 * <ul>
 *   <li>{@code Assistente} — interface declarativa com @SystemMessage</li>
 *   <li>{@code Main} — monta o modelo, constrói o serviço e faz a primeira chamada</li>
 * </ul>
 */
package com.estudo.agents.parte1;
