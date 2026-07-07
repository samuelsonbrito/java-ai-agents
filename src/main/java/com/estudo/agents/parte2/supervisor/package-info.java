/**
 * Parte 2 — Padrão: Supervisor.
 *
 * <p>Um LLM atua como planejador dinâmico: lê as descrições dos agentes disponíveis
 * e das ferramentas (@Tool), decide a ordem de execução em tempo real e adapta o
 * plano a cada nova saída. Implementado com {@code AgenticServices.supervisorBuilder}.
 *
 * <p>Conceitos-chave deste pacote:
 * <ul>
 *   <li><strong>@Tool</strong> — anota métodos Java que o LLM pode invocar como ações.
 *       A descrição da anotação é o contrato: o LLM a lê para decidir quando usar a ferramenta.</li>
 *   <li><strong>@Agent (descrição)</strong> — no padrão supervisor, a descrição do agente
 *       é sua API de descoberta: o supervisor a lê para saber para que serve cada agente.</li>
 *   <li><strong>responseStrategy SUMMARY</strong> — o supervisor consolida todos os
 *       resultados intermediários em uma resposta final única.</li>
 * </ul>
 *
 * <p>Contraste com workflows fixos (fases 2A-2C):
 * <pre>
 * | Dimensão        | Workflow fixo (sequência/paralelo) | Supervisor          |
 * |-----------------|-----------------------------------|---------------------|
 * | Ordem           | Determinística, definida em código | Dinâmica, pelo LLM  |
 * | Controle        | Total                             | Parcial             |
 * | Flexibilidade   | Baixa                             | Alta                |
 * | Custo de tokens | Baixo                             | Mais alto           |
 * | Debug           | Fácil (logs lineares)             | Mais difícil        |
 * </pre>
 */
package com.estudo.agents.parte2.supervisor;
