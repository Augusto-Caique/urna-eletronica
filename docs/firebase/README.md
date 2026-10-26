# Urna Eletrônica Pokémon — Documentação do Cloud Firestore

**Projeto:** AV3 — Programação Orientada a Objetos (Java + Swing + NoSQL/Firebase)  
**Modalidade escolhida:** eleição municipal (VEREADOR e PREFEITO), em turno único  
**Versão da documentação:** 1.0 — 26/10/2026  
**Responsável pelo banco:** Jackson Vieira  
**ID do projeto Firebase:** av3-poo---urna

## 1. Finalidade e limites

Este documento descreve o **modelo já utilizado** no Cloud Firestore para a simulação da urna Pokémon e as **regras propostas para integração**. Não é um relato de execução de consultas ou teste da aplicação Java Swing. O sistema foi baseado nos requisitos da AV3: duas votações por eleitor (vereador e prefeito), tela do candidato com número/nome/partido/foto, teclas BRANCO/CORRIGE/CONFIRMA, voto nulo para número inexistente, senha de demonstração `99999` para abrir e encerrar, relatórios separados por cargo e ausência de segundo turno.

A temática Pokémon é uma adaptação acadêmica; o enunciado pede autorização do professor para mudanças de tema/escopo. **A equipe deve confirmar essa autorização.**

## 2. Situação declarada até esta versão

| Item | Estado | Observações |
|---|---|---|
| `partidos` | Criado pelo responsável | 15 IDs em português |
| `pokemons` | Importação executada | Saída exibida: 148 criados, 3 preservados = 151 esperados |
| `eleicoes/eleicao-001` | Criado | Modalidade `MUNICIPAL`, status inicial `CONFIGURACAO` |
| Subcoleção `candidatos` | Exemplos definidos e cadastro orientado | **Validar** no console os IDs `001`, `004`, `025` e seus campos |
| Subcoleção `apuracao` | Responsável confirmou criação | `VEREADOR` e `PREFEITO` (validar que contadores estejam zerados) |
| `imagemUrl` em `001`, `004`, `025` | Pendente de verificação | Importador preserva os três documentos sem acrescentar o campo |
| Array `cargos` | Pendente de verificação | Deve possuir **dois elementos**, e não um texto único |
| Subcoleção `votos` | Não implementada | Opcional; exige acordo sobre privacidade, auditoria e prevenção de repetição |

**Não confundir “previsto” com “verificado”:** antes de compartilhar como pronto, confira os documentos diretamente no console.

## 3. Organização do banco

```text
(default)
├── partidos/                                  # Coleção raiz
│   ├── planta
│   ├── fogo
│   └── ... (15 documentos)
├── pokemons/                                  # Coleção raiz
│   ├── 001    (Bulbasaur)
│   ├── 004    (Charmander)
│   ├── 025    (Pikachu)
│   └── ... até 151
└── eleicoes/                                  # Coleção raiz
    └── eleicao-001                            # Documento da eleição
        ├── candidatos/                        # Subcoleção
        │   ├── 001    (Bulbasaur)
        │   ├── 004    (Charmander)
        │   └── 025    (Pikachu)
        └── apuracao/                          # Subcoleção
            ├── VEREADOR
            └── PREFEITO
```

**Atenção:** subcoleções estão associadas a `/eleicoes/eleicao-001`, não ficam na raiz do Firestore. O Firestore não impõe chaves estrangeiras; a consistência dos IDs é responsabilidade da aplicação/serviço.

### 3.1 `partidos/{partidoId}`

Exemplo: documento `/partidos/planta`

| Campo | Tipo Firestore | Exemplo | Regra |
|---|---|---|---|
| `nome` | string | `Partido Planta` | Exibição |
| `sigla` | string | `PPL` | Exibição |
| `numero` | string | `12` | String para preservar convenção eleitoral |
| `tipoPrimario` | string | `planta` | ID padronizado em português, igual ao doc ID |
| `ativo` | boolean | `true` | Elegibilidade do partido |

IDs dos 15 partidos cadastrados: `normal`, `lutador`, `planta`, `fogo`, `agua`, `eletrico`, `venenoso`, `terrestre`, `pedra`, `inseto`, `fantasma`, `psiquico`, `gelo`, `dragao`, `fada`.

Números convencionados: normal 10, lutador 11, planta 12, fogo 13, água 14, elétrico 15, venenoso 16, terrestre 17, pedra 18, inseto 19, fantasma 20, psíquico 21, gelo 22, dragão 23, fada 24. São **fictícios**.

### 3.2 `pokemons/{pokemonId}`

Exemplo: documento `/pokemons/001`

| Campo | Tipo Firestore | Exemplo | Regra |
|---|---|---|---|
| `numeroPokedex` | number (inteiro) | `1` | ID numérico da Pokédex |
| `nome` | string | `Bulbasaur` | Nome original, sem tradução |
| `tipoPrimario` | string | `planta` | Define o partido |
| `tipoSecundario` | string **ou null** | `venenoso` | Não define o partido |
| `partidoId` | string | `planta` | Referência lógica a `/partidos/planta` |
| `geracao` | number (inteiro) | `1` | Somente primeira geração |
| `disponivel` | boolean | `true` | Elegível para seleção |
| `imagemUrl` | string | URL PNG da PokéAPI | Exibir a foto na urna |

O documento é identificado por uma **string de três dígitos**, `001` a `151`. Pokémon de um único tipo podem ter `tipoSecundario: null` (ou ausência do campo nos cadastros anteriores); **não** cadastrar a string literal `"null"`.

**Regra central:** use somente o **tipo primário** para a filiação. Ex.: `Venusaur` tem `tipoPrimario = planta`, `tipoSecundario = venenoso` e `partidoId = planta`.

Para completar `imagemUrl` nos três registros preservados, as URLs são:

```text
001: https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png
004: https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/4.png
025: https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png
```

### 3.3 `eleicoes/eleicao-001`

| Campo | Tipo Firestore | Exemplo | Regra |
|---|---|---|---|
| `nome` | string | `Eleição Pokémon 2026` | Nome de exibição |
| `modalidade` | string | `MUNICIPAL` | Modalidade fixa da simulação |
| `cargos` | array de string | `["VEREADOR", "PREFEITO"]` | **Dois elementos separados** |
| `status` | string | `CONFIGURACAO` | `CONFIGURACAO` → `ABERTA` → `ENCERRADA` |
| `ativa` | boolean | `false` | Deve ser coerente com o status |
| `segundoTurno` | boolean | `false` | Sem segundo turno |

**Pendência de validação:** uma captura anterior sugeria que `cargos` poderia estar salvo como `["VEREADOR, PREFEITO"]` (um elemento). Ao expandir o campo no console, conferir `0 = VEREADOR` e `1 = PREFEITO`.

O uso conjunto de `status` e `ativa` exige regra clara: sugerimos `ativa = true` apenas quando `status = ABERTA`. A equipe pode optar por eliminar `ativa` no futuro, mas só após ajustar os consumidores.

### 3.4 `eleicoes/eleicao-001/candidatos/{candidatoId}`

| Campo | Tipo Firestore | Exemplo | Regra |
|---|---|---|---|
| `pokemonId` | string | `001` | Referencia `/pokemons/001` |
| `partidoId` | string | `planta` | Referencia `/partidos/planta` |
| `cargo` | string | `VEREADOR` | Apenas `VEREADOR` ou `PREFEITO` |
| `numero` | string | `12001` | Único **dentro do cargo** na eleição |
| `votos` | number (inteiro) | `0` | Contador individual inicial |
| `ativo` | boolean | `true` | Candidato habilitado |

**Pré-candidatos escolhidos como exemplos de teste:**

| Documento | Pokémon | Cargo | Número | Partido |
|---|---|---|---|---|
| `001` | Bulbasaur | VEREADOR | `12001` | `planta` |
| `025` | Pikachu | VEREADOR | `15025` | `eletrico` |
| `004` | Charmander | PREFEITO | `13` | `fogo` |

Esses dados foram propostos ao longo da configuração. **Conferir os documentos reais** antes de usá-los nos testes.

Os números são fictícios; foi adotada convenção de cinco dígitos para vereador e dois para prefeito. O **ID do Pokémon** não é seu **número eleitoral**. O ID do documento de candidato foi igual ao ID do Pokémon somente por simplicidade no protótipo; se futuramente houver candidaturas repetidas do mesmo Pokémon na mesma eleição, esse ID precisará ser revisto.

### 3.5 `eleicoes/eleicao-001/apuracao/{cargo}`

Dois documentos: `VEREADOR` e `PREFEITO`.

| Campo | Tipo Firestore | Inicial |
|---|---|---|
| `validos` | number (inteiro) | `0` |
| `brancos` | number (inteiro) | `0` |
| `nulos` | number (inteiro) | `0` |
| `total` | number (inteiro) | `0` |

Invariantes: `total = validos + brancos + nulos`; a soma de `votos` dos candidatos ativos do cargo deve corresponder a `validos` (respeitadas eventuais regras futuras sobre desativação). A contagem de **cada cargo** é independente: um eleitor que conclua ambos os votos deve acrescentar 1 ao `total` de VEREADOR **e** 1 ao `total` de PREFEITO.

## 4. Como a urna deve consultar o banco

**Exemplo para o número `12001` no cargo VEREADOR:**

1. Buscar em `/eleicoes/eleicao-001/candidatos` o documento com `numero == "12001"` e `cargo == "VEREADOR"`, além de `ativo == true`.
2. Ler o campo `pokemonId` do candidato (`001`) e abrir `/pokemons/001` para obter `nome` e `imagemUrl`.
3. Ler `partidoId` (`planta`) e abrir `/partidos/planta` para obter `nome` e `sigla`.
4. Mostrar na tela número, nome, partido e imagem.
5. Se não encontrar candidato habilitado para o número/cargo, sinalizar **voto nulo** quando o usuário confirmar; **CORRIGE** apenas limpa a entrada, não grava voto.

## 5. Regras de gravação de votos (a implementar pela equipe)

- Antes do início, mostrar apuração inicial zerada; somente iniciar após a senha de demonstração `99999`.
- Para voto **válido**, incrementar em uma única transação `candidatos/{id}.votos`, `apuracao/{cargo}.validos` e `apuracao/{cargo}.total`.
- Para voto **em branco**, incrementar `brancos` e `total` no documento de apuração do cargo.
- Para voto **nulo**, incrementar `nulos` e `total` no documento de apuração do cargo.
- **CONFIRMA** registra voto; **BRANCO** seleciona voto em branco; **CORRIGE** apenas reinicia a digitação.
- Ao concluir VEREADOR, passar para PREFEITO; somente depois dos dois cargos a sessão do eleitor estará completa.
- Ao encerrar a eleição com senha `99999`, alterar `status` para `ENCERRADA` e bloquear escritas de votos. A senha do enunciado é apenas parte da simulação; **não** equivale a autenticação real.
- Guardar apenas os totais agregados pode atender ao relatório da AV3. Se a equipe optar por registrar votos individuais, é indispensável definir como impedir repetição, como preservar o anonimato e quais dados realmente precisam ser armazenados. Não cadastrar informações pessoais do eleitor sem necessidade.
- **Evitar duplo clique/confirmar repetido**: a atualização atômica de contadores, sozinha, não garante que o mesmo voto seja registrado uma única vez. É necessário um mecanismo de idempotência ou controle da sessão de votação no serviço confiável.

## 6. Consultas Java (exemplo somente para ambiente confiável)

O diretório `exemplos-java/` contém um mini projeto Maven com a classe `ConsultaFirestore.java`. Ela realiza **somente leituras** para testar a estrutura atual: `/pokemons/001`, `/partidos/planta`, busca do candidato `12001` e `/apuracao/VEREADOR`. Não contém chave privada nem grava votos.

Requisitos: Java 17+, Maven (o NetBeans 13 suporta projetos Maven), credenciais **fora do repositório** e estas variáveis de ambiente:

```powershell
$env:GOOGLE_APPLICATION_CREDENTIALS="C:\firebase-seguro\chave-firebase.json"
$env:FIREBASE_PROJECT_ID="ID_REAL_DO_PROJETO"
```

Em um ambiente confiável, abra `exemplos-java` como projeto Maven no NetBeans e execute a classe `ConsultaFirestore` ou execute no terminal:

```powershell
mvn -f exemplos-java/pom.xml compile exec:java
```

**ATENÇÃO DE SEGURANÇA:** este exemplo utiliza Firebase Admin SDK, que possui privilégios administrativos. Ele serve **somente para teste local confiável ou backend**. **Não embutir a chave JSON nem distribuir o Admin SDK com credenciais dentro do aplicativo Java Swing.** Para um uso além da demonstração local, colocar as operações de leitura/escrita protegidas num serviço/backend confiável e definir autenticação/autorização. As bibliotecas de servidor do Firestore usam IAM e **não** as regras de segurança do SDK cliente.

## 7. Padrões e possíveis limitações

- Convenção: `pokemonId`, `partidoId`, `cargo`, `numero`, `votos` (camelCase para campos). IDs de tipos em português **sem acentos** e minúsculos.
- A existência dos documentos referenciados deve ser verificada pela aplicação; não há join automático.
- `numero` sempre é `string` (preserva zeros e evita interpretação numérica).
- A consulta por `numero` com `cargo` e `ativo` pode exigir **índice composto**, dependendo da combinação de filtros; o console apresenta o link para criá-lo se necessário.
- Não apagar/alterar candidatos ou contadores durante uma eleição aberta sem regra explícita.
- Para interface Swing, operações remotas devem executar fora da *Event Dispatch Thread*, para não congelar a UI.
- Os relatórios devem informar o **denominador do percentual**. Para cada cargo, recomenda-se `percentual = votos_da_opcao / total_de_votos_do_cargo * 100`; assim, a soma de candidatos + branco + nulo é aproximadamente 100% (diferenças apenas de arredondamento). Se a equipe escolher outro denominador, documentar a mudança.
- A região do banco foi mostrada no console como `southamerica-east1`.

## 8. Verificações antes de entregar à equipe

Use `CHECKLIST_ENTREGA.md` para marcar a conferência no console. Os pontos mais importantes são: `imagemUrl` dos três cadastros manuais, array `cargos`, candidatos reais cadastrados e apuração zerada. Registrar o ID do Firebase no README (nunca adicionar credenciais).

## 9. Referências

- Enunciado da AV3, **Programação Orientada a Objetos**, págs. 1–5 (fornecido pelo professor).
- Firebase Admin SDK: https://firebase.google.com/docs/admin/setup
- Firestore server quickstart: https://firebase.google.com/docs/firestore/quickstart-server
- Firestore — consultas: https://firebase.google.com/docs/firestore/query-data/queries
- Firestore — transações: https://firebase.google.com/docs/firestore/manage-data/transactions
- Segurança — bibliotecas servidor: https://firebase.google.com/docs/firestore/client/libraries
- Dataset usado na importação: `pokemon_151.csv` do projeto de importação, gerado com nomes/tipos de Pokémon da primeira geração e imagens do projeto PokéAPI.
