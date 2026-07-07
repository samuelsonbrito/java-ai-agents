package com.estudo.agents.parte2.sequencing;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Escopo compartilhado entre todos os agentes de uma sequência.
 *
 * <h2>O que esta classe faz</h2>
 * Funciona como uma "lousa" onde cada agente lê os dados que precisa
 * e escreve o resultado que produziu. É um {@code Map<String, String>}
 * com métodos nomeados para deixar o código mais legível.
 *
 * <h2>Conceito LangChain4j demonstrado</h2>
 * No módulo agentic do LangChain4j (e em frameworks como LangGraph),
 * este objeto é chamado de <strong>AgenticScope</strong> ou <strong>State</strong>.
 * Aqui implementamos a mesma ideia manualmente para que o estudante
 * veja exatamente o que acontece por baixo dos panos.
 *
 * <h2>Tríade @V / outputKey explicada</h2>
 * Nos frameworks de agentes, cada agente declara:
 * <ul>
 *   <li><strong>@V("chave")</strong> — as entradas que ele precisa do escopo.</li>
 *   <li><strong>outputKey</strong> — o nome sob o qual ele grava sua saída.</li>
 * </ul>
 * O erro mais comum é usar nomes que não batem: se o agente A grava em
 * "masterCv" mas o agente B lê de "cvMestre", B recebe {@code null}.
 * Sintoma: a variável chega vazia no prompt e o LLM gera conteúdo genérico.
 *
 * <h2>Como se conecta com as demais classes</h2>
 * {@code Main} cria uma instância, passa para cada agente via
 * {@code colocar()} e {@code obter()}, e chama {@code imprimir()} para
 * mostrar o estado completo após cada passo.
 */
public class AgenticScope {

    // Mapa central de estado — cada agente lê e grava aqui
    private final Map<String, String> estado = new HashMap<>();

    /**
     * Grava um valor no escopo com a chave indicada (equivale a outputKey).
     *
     * @param chave  nome que os agentes seguintes usarão para ler este valor
     * @param valor  conteúdo produzido pelo agente
     */
    public void colocar(String chave, String valor) {
        estado.put(chave, valor);
    }

    /**
     * Lê um valor do escopo (equivale a @V("chave") nos frameworks de agentes).
     *
     * @param chave nome da variável a recuperar
     * @return o valor, ou {@code null} se a chave ainda não foi gravada
     */
    public String obter(String chave) {
        return estado.get(chave);
    }

    /** Retorna as chaves presentes no escopo — útil para debug. */
    public Set<String> chaves() {
        return estado.keySet();
    }

    /**
     * Imprime o estado atual do escopo no console.
     * Use após cada agente para ver o encadeamento acontecendo em tempo real.
     *
     * @param rotulo texto exibido como cabeçalho (ex: "Após CvGenerator")
     */
    public void imprimir(String rotulo) {
        System.out.println("\n── " + rotulo + " ──");
        System.out.println("Chaves no escopo: " + estado.keySet());
        estado.forEach((chave, valor) -> {
            // Trunca valores longos para não poluir o console
            String preview = valor.length() > 200 ? valor.substring(0, 200) + "..." : valor;
            System.out.println("[" + chave + "] → " + preview);
        });
        System.out.println();
    }
}
