# Fase 2A — Sequenciamento (Prompt Chaining)

```
╔══════════════════════════════════════════════════════════════════════╗
║              FASE 2A — SEQUENCIAMENTO (Prompt Chaining)             ║
╚══════════════════════════════════════════════════════════════════════╝

  Chamada pelo Main
  ┌─────────────────────────────────────┐
  │       GeradorSequencialDeCv         │
  │  gerarEAdaptarCv(                   │
  │    historicoDeVida,  ───────────────┼──┐
  │    descricaoVaga     ───────────────┼──┼──┐
  │  )                                  │  │  │
  └─────────────────────────────────────┘  │  │
                                           │  │
  ╔══════════════════════════════════════╗  │  │
  ║         ESCOPO INTERNO               ║  │  │
  ║  ┌────────────────┬────────────────┐ ║  │  │
  ║  │ historicoDeVida│ descricaoVaga  │◄║──┘  │
  ║  └────────────────┴────────────────┘ ║     │
  ║                                      ║◄────┘
  ╚══════════════════════════════════════╝
       │                    │
       │ @V("historicoDeVida")
       ▼                    │
  ┌──────────────┐          │
  │  GeradorDeCv │          │
  │  gerarCv()   │          │
  └──────┬───────┘          │
         │ outputKey("cvAtual")
         ▼                  │
  ╔══════════════════════════════════════╗
  ║         ESCOPO INTERNO               ║
  ║  ┌─────────┬────────────┬──────────┐ ║
  ║  │ historico│ descricao │ cvAtual  │ ║
  ║  └─────────┴────────────┴──────────┘ ║
  ╚══════════════════════════════════════╝
       │@V("cvAtual")    │@V("descricaoVaga")
       └────────┬────────┘
                ▼
  ┌──────────────────┐
  │  AdaptadorDeCv   │
  │  adaptarCv()     │
  └────────┬─────────┘
           │ outputKey("cvFinal")
           ▼
  ╔══════════════════════════════════════╗
  ║         ESCOPO INTERNO               ║
  ║  ┌─────────┬───────────┬──────────┐  ║
  ║  │ historico│ descricao │ cvAtual  │  ║
  ║  ├──────────────────────┴──────────┤  ║
  ║  │            cvFinal              │  ║
  ║  └─────────────────────────────────┘  ║
  ╚══════════════════════════════════════╝
           │ sequenceBuilder.outputKey("cvFinal")
           ▼
      String cvFinal   ◄── resultado devolvido ao Main
```

## Pontos-chave

- O escopo cresce a cada agente — nenhuma chave é apagada, só adicionada
- `descricaoVaga` viaja do início ao fim sem ser tocada pelo `GeradorDeCv`, e o `AdaptadorDeCv` a lê diretamente do escopo
- O `sequenceBuilder` extrai apenas a chave declarada em `outputKey("cvFinal")` para devolver ao chamador
