# Urna Pokémon — Importação para Firebase Firestore

**Objetivo:** preencher a coleção `pokemons` com os Pokémon `001` a `151` da primeira geração, preservando os registros que já existem (Bulbasaur `001`, Charmander `004`, Pikachu `025`). **Não altera votos, eleições, candidatos, apuração ou partidos.**

## Conteúdo

- `pokemon_151.csv`: nomes originais, tipos atuais em português, partido primário, Pokédex e endereço de imagem.
- `src/main/java/br/com/urnapokemon/ImportarPokemons.java`: importador Java seguro, com prévia antes de gravar.
- `pom.xml`: projeto Java 17 + Maven + Firebase Admin SDK.

A coluna `tipoPrimario` corresponde ao tipo no **slot 1**. A coluna `tipoSecundario` fica vazia nos Pokémon com um só tipo e é importada como `null`. A URL da imagem aponta para o repositório público de artes da PokéAPI.

## Passo 1 — Pré-requisitos

Instale **JDK 17 ou superior** e **Apache Maven**. No terminal, teste:

```powershell
java -version
mvn -version
```

Crie/abra o projeto Firebase onde você cadastrou `partidos`, `pokemons` e `eleicoes`. Localize o **ID do projeto** em Configurações do projeto (não é necessariamente o nome de exibição).

## Passo 2 — Credencial para execução local

No Console Firebase, abra **Configurações do projeto > Contas de serviço** e gere uma chave privada JSON para uma conta de serviço com acesso ao Firestore, somente se necessário para seu ambiente de desenvolvimento.

Salve a chave **fora desta pasta**, não a envie ao GitHub e não a incorpore ao aplicativo Swing que será distribuído. Se possível, prefira credenciais de curta duração ou `gcloud auth application-default login` em um ambiente confiável.

No **PowerShell** (ajuste caminhos e ID):

```powershell
$env:GOOGLE_APPLICATION_CREDENTIALS="C:\firebase-seguro\chave-firebase.json"
$env:FIREBASE_PROJECT_ID="ID_REAL_DO_SEU_PROJETO"
```

## Passo 3 — Testar primeiro sem gravar

Abra um terminal dentro da pasta que contém `pom.xml` e `pokemon_151.csv` e execute:

```powershell
mvn compile exec:java
```

Ele deve exibir algo semelhante a:

```
CSV validado: 151 Pokemon da primeira geracao.
Projeto: ... | Pokemon ja cadastrados: 3 | Ausentes: 148
SIMULACAO: nenhum documento foi criado. Para importar, execute com --gravar.
```

Se algum partido não existir ou tiver ID diferente, o programa interrompe antes de gravar. Os IDs de partidos esperados são: `normal`, `lutador`, `planta`, `fogo`, `agua`, `eletrico`, `venenoso`, `terrestre`, `pedra`, `inseto`, `fantasma`, `psiquico`, `gelo`, `dragao`, `fada`.

## Passo 4 — Gravar os documentos ausentes

**Após conferir que o ID do projeto está correto**, execute:

```powershell
mvn compile exec:java "-Dexec.args=--gravar"
```

O importador cria **somente documentos que ainda não existem** usando um lote atômico. Se `001`, `004` e `025` já existem, eles serão preservados sem alterações. Depois, abra **Firestore Database > Dados > pokemons**, e verifique, por exemplo, `002` (Ivysaur), `003` (Venusaur) e `151` (Mew).

**Atenção:** como os três registros cadastrados manualmente são ignorados, se ainda não possuírem `imagemUrl`, você pode acrescentar esse campo diretamente no console mais tarde.

## Sobre o modelo

Cada Pokémon possui `partidoId` em português e o tipo secundário não interfere na escolha do partido. O número da Pokédex **não é o número eleitoral** do candidato. Os números de votação continuam na subcoleção `eleicoes/eleicao-001/candidatos`.

## Segurança

Esta rotina administrativa deve ser executada em um computador confiável, apenas durante a importação inicial. O Firebase Admin SDK usa privilégios elevados e **não deve ser distribuído como parte de um aplicativo Swing público**. Para votação, projete um componente de serviço confiável, regras de acesso, autenticação e proteção contra votos duplicados.

## Fontes dos dados

- PokéAPI, `pokemon.csv`: https://raw.githubusercontent.com/PokeAPI/pokeapi/master/data/v2/csv/pokemon.csv
- PokéAPI, `pokemon_types.csv`: https://raw.githubusercontent.com/PokeAPI/pokeapi/master/data/v2/csv/pokemon_types.csv
- Pokémon sprites — artes oficiais hospedadas no repositório PokéAPI: https://github.com/PokeAPI/sprites
- SDK Admin Java: https://firebase.google.com/docs/admin/setup
