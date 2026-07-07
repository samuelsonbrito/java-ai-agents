# Prompt Otimizado + Plano de Desenvolvimento
## Projeto: AI Agents with Java (LangChain4j) — via Claude Code

Este documento tem 3 partes:
1. **CLAUDE.md** — arquivo de contexto permanente do projeto (crie ele PRIMEIRO)
2. **Prompts por fase** — um prompt pronto para cada etapa do desenvolvimento
3. **Plano de desenvolvimento** — cronograma, ordem, critérios de aceite e como validar

> **Por que fases em vez de um prompt único gigante?** O Claude Code trabalha melhor
> com tarefas incrementais e verificáveis. Cada fase compila e roda antes da próxima,
> então erros não se acumulam. O CLAUDE.md garante que as regras valem para todas as fases.

---

## PARTE 1 — Crie o arquivo CLAUDE.md na raiz do projeto

Antes de qualquer prompt, crie a pasta do projeto e salve este conteúdo em `CLAUDE.md`.
O Claude Code lê esse arquivo automaticamente em toda sessão — é o "contrato" do projeto.

```markdown
# Projeto: ai-agents-java — Guia de estudo LangChain4j

## Objetivo
Implementar, de forma didática, os padrões de coordenação multiagente e RAG
do livro "AI Agents with Java" usando LangChain4j 1.8.0, Java 21 e Maven.
O público é um estudante: o código existe para ENSINAR, não só para funcionar.

## Regras de código (obrigatórias)
1. TODO arquivo Java começa com um bloco Javadoc explicando:
   - O QUE a classe faz
   - QUAL conceito do LangChain4j ela demonstra
   - COMO ela se conecta com as outras classes do exemplo
2. Comentários inline em português explicando o PORQUÊ de cada passo
   não óbvio (não comente o óbvio como "// cria variável").
3. Cada padrão/exemplo tem sua própria classe Main executável com:
   - Instruções de execução no Javadoc (comando mvn exec:java completo)
   - Saída esperada descrita em comentário
4. Nomes de variáveis em português quando forem conceito de negócio
   (ex: historicoDeVida), em inglês quando forem conceito do framework
   (ex: chatModel, embeddingStore) — para o estudante mapear com a doc oficial.
5. Nunca colocar chaves de API no código. Sempre System.getenv().
6. Depois de criar cada exemplo, SEMPRE rodar `mvn -q compile` e corrigir
   erros antes de seguir. Se o exemplo depende de API externa, criar também
   um teste "offline" ou explicar em comentário como validar sem chave.

## Stack fixa (não mudar sem me perguntar)
- Java 21, Maven, LangChain4j 1.8.0 (agentic, open-ai, embeddings-bge-small-en-v15-q
  na versão 1.8.0-beta15, document-parser-apache-pdfbox 1.8.0-beta15)
- Chat: OpenAI gpt-4o-mini via OPENAI_API_KEY
- Embeddings: BGE local (in-process, sem API)
- Vector store: InMemoryEmbeddingStore (didático)

## Estrutura de pacotes
com.estudo.agents
├── parte1          -> AI Service básico
├── parte2.sequencing
├── parte2.reflection
├── parte2.parallel
├── parte2.routing
├── parte2.supervisor
├── parte2.goap
├── parte2.p2p
└── parte3.ingestao / parte3.recuperacao / parte3.avancado

## Comandos úteis
- Compilar: mvn -q clean compile
- Rodar exemplo: mvn -q compile exec:java -Dexec.mainClass=<FQCN>
- Se um artefato Maven não existir na versão indicada, buscar a versão
  mais próxima em central.sonatype.com e me avisar da troca no resumo.

## Estilo de entrega por fase
Ao final de cada tarefa, me dê:
1. Lista dos arquivos criados/alterados
2. Comando exato para eu rodar
3. Um parágrafo "O que estudar neste código" apontando as 2-3 linhas
   mais importantes do exemplo e o conceito que elas demonstram
```

---

## PARTE 2 — Prompts por fase (copie e cole um por vez no Claude Code)

### FASE 0 — Bootstrap do projeto

```text
Crie do zero um projeto Maven chamado ai-agents-java seguindo o CLAUDE.md.

Tarefas:
1. Gere o pom.xml completo com: Java 21, langchain4j 1.8.0 (core + agentic +
   open-ai), langchain4j-embeddings-bge-small-en-v15-q 1.8.0-beta15,
   langchain4j-document-parser-apache-pdfbox 1.8.0-beta15, slf4j-simple,
   e o exec-maven-plugin. Comente CADA dependência no pom explicando
   para que fase do estudo ela serve.
2. Crie a estrutura de pacotes definida no CLAUDE.md (com .gitkeep ou
   package-info.java explicando o propósito de cada pacote).
3. Crie src/main/resources/docs/novatech.txt com um relatório fictício
   contendo: receita total de uma empresa fictícia, política de cancelamento
   de reservas (7 dias, multa de 30%) e política de reembolso — esse arquivo
   alimenta o RAG da parte 3.
4. Crie um README.md com pré-requisitos (JDK 21, Maven, OPENAI_API_KEY)
   e a tabela de "qual Main roda qual exemplo" (deixe a tabela preparada,
   preenchemos a cada fase).
5. Rode mvn -q clean compile e confirme BUILD SUCCESS.

Não crie nenhum código de agente ainda. Pare após o build passar.
```

### FASE 1 — Primeiro AI Service

```text
Implemente a parte1: o menor AI Service possível, ultraexplicado.

1. Interface Assistente com @SystemMessage em português e um método
   String responder(String pergunta). No Javadoc, explique o conceito
   de AI Service: interface declarativa que o LangChain4j implementa
   em runtime, e por que isso é a fundação de agentes e RAG.
2. Classe Main que monta OpenAiChatModel (gpt-4o-mini, temperature 0.3,
   chave via env) e materializa a interface com AiServices.builder().
   Comente o papel de cada um dos 3 blocos: modelo, builder, uso.
3. Adicione a linha na tabela do README.
4. Compile. Como este exemplo precisa da API, escreva no Javadoc do Main
   a saída esperada aproximada e um aviso do erro 401 se a chave faltar.
```

### FASE 2A — Sequenciamento

```text
Implemente parte2.sequencing: o padrão de prompt chaining com dois agentes
(CvGenerator gera um CV a partir de uma história de vida; CvTailor adapta
o CV a uma vaga), combinados com AgenticServices.sequenceBuilder.

Requisitos didáticos específicos:
- No Javadoc do pacote, explique o AgenticScope (memória compartilhada) e
  a tríade @V / {{placeholder}} / outputKey — inclusive o erro clássico de
  nomes que não batem, com um exemplo de sintoma ("variável chega vazia").
- No Main, imprima também o estado intermediário: mostre o masterCv gerado
  antes do CV final adaptado, com separadores visuais, para o estudante
  VER o encadeamento acontecendo.
- Atualize o README e compile.
```

### FASE 2B — Reflexão (loop)

```text
Implemente parte2.reflection: loop de melhoria iterativa de CV.

1. CvReview (POJO com @Description em score e feedback) — no Javadoc,
   explique structured output: como o LangChain4j força o LLM a devolver
   JSON compatível com a classe.
2. CvReviewer (nota 0-1 + feedback) e ScoredCvTailor (melhora o CV usando
   o feedback). 
3. Main com loopBuilder: exitCondition score > 0.8, maxIterations 3.
   IMPORTANTE: dentro da exitCondition, adicione um System.out.println
   da iteração e do score atual — o estudante precisa ver o loop convergindo.
4. Explique em comentário o que é UntypedAgent e quando usá-lo.
5. README + compile.
```

### FASE 2C — Paralelização + Roteamento (juntas, pois se compõem)

```text
Implemente parte2.parallel e parte2.routing como um fluxo único de contratação:

1. Três revisores paralelos (HrReviewer, ManagerReviewer, TeamReviewer) —
   mesma estrutura do CvReviewer mas com @SystemMessage de perspectivas
   diferentes e outputKeys distintos (hrReview, managerReview, teamReview).
2. parallelBuilder com .output() agregando: média das notas + feedbacks
   concatenados com rótulos. Comente que .output() é o ponto de agregação
   e roda só depois que TODOS os paralelos terminam.
3. Dois agentes de destino: InterviewOrganizer (gera convite de entrevista)
   e EmailAssistant (gera e-mail de recusa educado).
4. conditionalBuilder roteando por combinedCvReview.score >= 0.8.
5. sequenceBuilder colando paralelo -> roteador. No Javadoc do Main,
   destaque que este exemplo COMBINA 3 padrões e desenhe o fluxo em
   ASCII art no comentário.
6. README + compile.
```

### FASE 2D — Supervisor

```text
Implemente parte2.supervisor: o padrão dinâmico onde um LLM planeja.

1. BankTool com @Tool para withdraw e credit sobre um Map de contas em
   memória, mais um ExchangeTool com taxa fixa EUR->USD. Comente que a
   DESCRIÇÃO do @Tool é o que o LLM lê para decidir usá-la.
2. Três agentes de interface (WithdrawAgent, CreditAgent, ExchangeAgent),
   cada um com @Agent cuja descrição seja rica — explique em comentário
   que no padrão supervisor a descrição É a API de descoberta.
3. supervisorBuilder com responseStrategy SUMMARY. No Main, invoque
   "Transferir 100 EUR da conta do Mario para a do Georgios" e imprima
   o resultado. Adicione um comentário-exercício: "rode 2x e compare a
   ordem das ações — por que pode variar?".
4. No Javadoc do pacote, contraste controle vs. flexibilidade
   (workflow fixo vs supervisor) em uma tabela markdown.
5. README + compile.
```

### FASE 2E — Planner customizado (GOAP) + P2P

```text
Implemente parte2.goap e parte2.p2p — os dois padrões avançados via
interface Planner.

GOAP (parte2.goap):
1. Cinco agentes do cenário horóscopo: PersonExtractor (prompt->person),
   SignExtractor (prompt->sign), HoroscopeGenerator (person,sign->horoscope),
   StoryFinder (person,horoscope->story), Writer (person,story,horoscope->writeup).
   Para não depender de busca real, o StoryFinder deve PEDIR ao LLM notícias
   plausíveis fictícias (deixe isso claro em comentário).
2. GoalOrientedSearchGraph: BFS onde um agente é alcançável quando todas
   as suas @V estão no conjunto de estados; executá-lo adiciona seu outputKey.
   Comente cada passo do algoritmo — este é o coração didático da fase.
3. GoalOrientedPlanner implementando Planner (init/firstAction/nextAction),
   com log do caminho calculado antes de executar.
4. Main que imprime o plano encontrado e depois o resultado.

P2P (parte2.p2p):
5. Cinco agentes de pesquisa (Literature, Hypothesis, Critic, Validation,
   Scorer) trocando topic/researchFindings/hypothesis/critique/score.
6. P2PPlanner: a cada rodada ativa todo agente cujas entradas existem no
   escopo e que ainda não rodou com esses valores; para quando estado
   estável, condição de saída (score >= 0.85) ou 10 invocações. Logue
   cada ativação: "rodada N: ativando X porque [entradas] presentes".
7. Javadoc do pacote comparando hierárquico vs P2P (vantagens/desvantagens).
8. README + compile. Esta é a fase mais complexa — se algo da API agentic
   divergir da versão, me mostre a assinatura real encontrada e proponha
   a adaptação antes de implementar tudo.
```

### FASE 3A — RAG básico

```text
Implemente parte3.ingestao e parte3.recuperacao: RAG completo sobre o
docs/novatech.txt criado na fase 0.

1. Ingestao.java: método estático ingerir() com os 5 passos comentados
   COM NÚMEROS correspondendo às fases do RAG (1 carregar/parsear,
   3 dividir em chunks de 300, 4 embeddings BGE local, 5 guardar no
   InMemoryEmbeddingStore). Imprima a quantidade de chunks. No Javadoc,
   explique por que o embedding roda local e o chat usa API.
2. BuscaManual.java: embeda uma pergunta, busca com EmbeddingSearchRequest
   (maxResults 3, minScore 0.8) e imprime score + texto de cada match.
   Comente: "isto ainda NÃO é RAG, é só busca semântica".
3. RagMain.java: EmbeddingStoreContentRetriever + AI Service com
   .contentRetriever(). Pergunte sobre cancelar reserva 3 dias antes.
   Adicione um bloco comentado "TESTE DE CONTROLE": instrução para
   comentar a linha do retriever e observar o modelo errar.
4. README + compile. BuscaManual deve rodar 100% offline — confirme.
```

### FASE 3B — RAG avançado

```text
Implemente parte3.avancado com as técnicas que NÃO exigem serviços externos,
e deixe as demais como esqueletos documentados:

1. MetadadosMain: EmbeddingStoreIngestor com documentTransformer (tag fixa)
   e textSegmentTransformer (timestamp), depois uma busca mostrando os
   metadados de cada chunk retornado.
2. FiltroDinamicoMain: simule multi-tenant — ingira o novatech.txt 2x com
   tenant_id "123" e "456" nos metadados, e mostre que a mesma pergunta
   com dynamicFilter só retorna chunks do tenant certo. Este é o exemplo
   mais importante da fase: capriche nos comentários.
3. QueryTransformerMain: implemente CompressingQuery (QueryTransformer
   customizado que condensa a pergunta via LLM) e mostre lado a lado a
   pergunta original prolixa e a comprimida.
4. Esqueletos comentados (arquivos compiláveis com o código dentro de
   comentários-bloco + instruções de setup no Javadoc): WebSearchSkeleton
   (Tavily), RerankingSkeleton (Cohere), PresidioSkeleton (Docker) —
   cada um dizendo qual dependência adicionar ao pom e onde obter a chave.
5. README final completo + mvn -q clean compile com BUILD SUCCESS.
```

### FASE 4 — Fechamento

```text
Fase final de consolidação:

1. Revise TODOS os Javadocs: cada Main precisa ter o comando de execução
   completo e a saída esperada. Corrija os que faltarem.
2. Gere um GUIA_DE_ESTUDO.md na raiz mapeando: padrão -> pacote -> Main
   -> conceitos-chave -> exercício proposto (um exercício de extensão
   por exemplo, ex.: "adicione um 4º revisor no paralelo").
3. Rode mvn -q clean compile uma última vez.
4. Me entregue um resumo final: o que foi implementado por completo,
   o que ficou como esqueleto e por quê, e a ordem de estudo sugerida.
```

---

## PARTE 3 — Plano de Desenvolvimento

### Visão geral

| Fase | Entrega | Depende de | Esforço estimado* | Valida com |
|------|---------|-----------|-------------------|------------|
| 0 | Projeto Maven + pom + recursos + README | — | 1 sessão curta | `mvn clean compile` |
| 1 | AI Service básico | 0 | 1 sessão curta | Rodar Main com chave |
| 2A | Sequenciamento | 1 | 1 sessão | Ver CV mestre → CV adaptado |
| 2B | Reflexão | 2A | 1 sessão | Ver scores subindo por iteração |
| 2C | Paralelo + Roteamento | 2B | 1-2 sessões | CV bom → convite; CV ruim → recusa |
| 2D | Supervisor | 2C | 1 sessão | 2 execuções, comparar planos |
| 2E | GOAP + P2P | 2D | 2-3 sessões | Plano impresso bate com o grafo; P2P converge |
| 3A | RAG básico | 0 | 1 sessão | BuscaManual offline + teste de controle |
| 3B | RAG avançado | 3A | 1-2 sessões | Filtro por tenant isolando resultados |
| 4 | Consolidação | todas | 1 sessão curta | Compilação limpa + GUIA_DE_ESTUDO.md |

\* "Sessão" = uma interação focada com o Claude Code (15-45 min incluindo sua revisão).

### Ordem recomendada e marcos

- **Marco 1 (fases 0-1):** ambiente provado. Se o Main da fase 1 responde, toda a
  infraestrutura (Java, Maven, chave, dependências) está correta. Não avance sem isso.
- **Marco 2 (fases 2A-2C):** você domina o AgenticScope. A partir daqui os erros
  são quase sempre de nomes de variáveis (@V/outputKey) — o log intermediário
  pedido nos prompts existe para você depurar isso visualmente.
- **Marco 3 (fases 2D-2E):** padrões dinâmicos. 2E é a fase com maior risco de
  divergência de API entre versões do LangChain4j — por isso o prompt instrui o
  Claude Code a verificar as assinaturas reais antes de implementar tudo.
- **Marco 4 (fases 3A-3B):** RAG. A fase 3A tem componentes offline de propósito:
  você consegue estudar ingestão/busca sem gastar créditos de API.
- **Marco 5 (fase 4):** o repositório vira material de estudo permanente.

### Como trabalhar com o Claude Code nessas fases

1. **Uma fase por vez.** Cole o prompt da fase, deixe terminar, rode você mesmo o
   comando de validação da tabela, e só então passe para a próxima.
2. **Se compilar mas se comportar estranho:** peça "adicione logs no AgenticScope
   mostrando as chaves presentes antes e depois de cada agente" — resolve 90%
   dos problemas de multiagente.
3. **Se um artefato Maven não existir:** o CLAUDE.md já instrui o Claude Code a
   buscar a versão vigente e avisar. Aceite a troca se for patch/minor.
4. **Commits:** peça um commit por fase ("faça commit com mensagem descritiva
   desta fase") — assim você pode voltar a qualquer marco.
5. **Custo de API:** as fases 1, 2A-2E e 3A-3B fazem chamadas reais ao gpt-4o-mini.
   Para estudo, o custo é baixo (centavos por fase), mas se quiser zero custo,
   peça na fase 0: "adapte o projeto para usar Ollama local (llama3.1) em vez
   de OpenAI" — é uma troca de builder, o CLAUDE.md permite com aviso.

### Critérios de aceite globais (cheque no final de cada fase)

- [ ] `mvn -q clean compile` retorna BUILD SUCCESS
- [ ] Cada classe nova tem Javadoc com propósito + conceito + conexões
- [ ] Cada Main tem comando de execução e saída esperada documentados
- [ ] README atualizado com a linha do novo exemplo
- [ ] Nenhuma chave de API em código-fonte
- [ ] Você consegue explicar em voz alta o que o exemplo demonstra
      (se não consegue, peça ao Claude Code: "explique este código como
      se eu fosse iniciante em LangChain4j, arquivo por arquivo")
