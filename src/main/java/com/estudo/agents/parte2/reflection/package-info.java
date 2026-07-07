/**
 * Parte 2 — Padrão: Reflexão (Loop de Melhoria Iterativa).
 *
 * <p>Um agente avalia a saída de outro e devolve um score + feedback. O loop continua
 * até a qualidade atingir um limiar ou o número máximo de iterações ser alcançado.
 * Implementado com {@code AgenticServices.loopBuilder}.
 *
 * <p>Conceitos-chave deste pacote:
 * <ul>
 *   <li><strong>Structured Output</strong> — o LangChain4j força o LLM a devolver JSON
 *       compatível com um POJO Java (ex: {@code CvReview} com score e feedback).</li>
 *   <li><strong>exitCondition</strong> — predicado avaliado após cada iteração; quando
 *       retorna {@code true}, o loop para.</li>
 *   <li><strong>maxIterations</strong> — limite de segurança para evitar loops infinitos.</li>
 *   <li><strong>UntypedAgent</strong> — agente sem interface tipada, útil quando a
 *       saída é dinâmica ou não mapeável diretamente a um POJO.</li>
 * </ul>
 *
 * <p>Fluxo: CV → CvReviewer (score+feedback) → ScoredCvTailor (CV melhorado) → loop.
 */
package com.estudo.agents.parte2.reflection;
