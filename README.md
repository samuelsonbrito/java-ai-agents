# AI Agents with Java — Estudo LangChain4j

Implementação didática dos padrões do livro **"AI Agents with Java"** usando
LangChain4j, Java 21 e Maven. Cada exemplo existe para **ensinar**, não só funcionar.

## Pré-requisitos

| Requisito | Versão mínima | Como verificar |
|-----------|--------------|----------------|
| JDK | 21 | `java -version` |
| Maven | 3.9+ | `mvn -version` |
| `OPENAI_API_KEY` | — | `echo $OPENAI_API_KEY` |

### Configurando a chave de API

```bash
# Linux / macOS
export OPENAI_API_KEY="sk-..."

# Windows (PowerShell)
$env:OPENAI_API_KEY="sk-..."
```

> **Fases offline:** BuscaManual (3A) roda 100% sem chave — embeddings são locais (BGE).

## Como rodar qualquer exemplo

```bash
mvn -q compile exec:java -Dexec.mainClass=<FQCN>
```

Substitua `<FQCN>` pelo nome completo da classe (coluna "Classe Main" na tabela abaixo).

## Tabela de exemplos

| Fase | Padrão | Classe Main | O que demonstra |
|------|--------|-------------|-----------------|
| 1 | AI Service básico | `com.estudo.agents.parte1.Main` | Interface declarativa, AiServices.builder |
| 2A | Sequenciamento | `com.estudo.agents.parte2.sequencing.Main` | AgenticScope, @V, outputKey, prompt chaining |
| 2B | Reflexão | `com.estudo.agents.parte2.reflection.Main` | loopBuilder, structured output, exitCondition |
| 2C | Paralelo + Roteamento | `com.estudo.agents.parte2.parallel.Main` | parallelBuilder, .output(), conditionalBuilder |
| 2D | Supervisor | `com.estudo.agents.parte2.supervisor.Main` | supervisorBuilder, @Tool, planejamento dinâmico |
| 2E-a | GOAP | `com.estudo.agents.parte2.goap.Main` | Planner customizado, BFS sobre agentes |
| 2E-b | P2P | `com.estudo.agents.parte2.p2p.Main` | Ativação por disponibilidade, convergência |
| 3A-a | Ingestão RAG | `com.estudo.agents.parte3.ingestao.Main` | Chunking, embedding BGE local, vector store |
| 3A-b | Busca semântica | `com.estudo.agents.parte3.recuperacao.BuscaManual` | EmbeddingSearchRequest, score de similaridade |
| 3A-c | RAG completo | `com.estudo.agents.parte3.recuperacao.RagMain` | ContentRetriever, geração fundamentada em doc |
| 3B-a | Metadados | `com.estudo.agents.parte3.avancado.MetadadosMain` | documentTransformer, textSegmentTransformer |
| 3B-b | Filtro dinâmico | `com.estudo.agents.parte3.avancado.FiltroDinamicoMain` | Multi-tenant com dynamicFilter por metadado |
| 3B-c | Query Transformer | `com.estudo.agents.parte3.avancado.QueryTransformerMain` | Compressão de query via LLM antes da busca |

## Estrutura do projeto

```
src/main/java/com/estudo/agents/
├── parte1/                  → AI Service básico
├── parte2/
│   ├── sequencing/          → Prompt chaining (AgenticScope)
│   ├── reflection/          → Loop de melhoria iterativa
│   ├── parallel/            → Execução paralela de agentes
│   ├── routing/             → Roteamento condicional
│   ├── supervisor/          → Planejador dinâmico com LLM
│   ├── goap/                → Goal-Oriented Action Planning
│   └── p2p/                 → Ativação por disponibilidade (emergente)
└── parte3/
    ├── ingestao/             → Pipeline de ingestão RAG
    ├── recuperacao/          → Busca semântica e RAG completo
    └── avancado/             → Metadados, filtro dinâmico, query transformer

src/main/resources/
└── docs/
    └── novatech.txt          → Documento fictício usado no RAG (partes 3A e 3B)
```

## Stack

- **Java 21** + **Maven**
- **LangChain4j 1.0.0-beta1** (core, agentic, open-ai)
- **Chat:** OpenAI `gpt-4o-mini` via `OPENAI_API_KEY`
- **Embeddings:** BGE Small EN v1.5 (local, sem API)
- **Vector store:** `InMemoryEmbeddingStore` (didático)

## Compilar o projeto

```bash
mvn -q clean compile
```

`BUILD SUCCESS` confirma que o ambiente está pronto.
