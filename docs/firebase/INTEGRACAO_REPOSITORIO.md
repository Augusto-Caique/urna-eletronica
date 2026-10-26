# Integração com o repositório urna-eletronica

## Contexto confirmado (26/10/2026)

Repositório consultado: https://github.com/Jacksonfvsantos/urna-eletronica  
Projeto principal: `urna/`, que usa Gradle (`build.gradle.kts`), pacote `com.lumi2021.urna`, Javalin 6.7.0 como dependência e Java Swing nas classes da aplicação.  
O programa principal `urna/src/main/java/com/lumi2021/urna/Program.java` instancia `BallotBox`, telas Swing e controladores.  
`VoterController.java` ainda é um esqueleto na versão consultada. **Não presumir API pronta ou gravação de votos implementada.**

## Onde ficam os arquivos desta entrega

- `docs/firebase/README.md`: contrato de dados do Cloud Firestore;
- `docs/firebase/diagramas/`: diagrama para revisão;
- `docs/firebase/CHECKLIST_ENTREGA.md`: validações antes da integração;
- `tools/firebase-importador/`: projeto Maven **separado**, utilizado apenas para a carga dos 151 Pokémon;
- `tools/firebase-consulta-exemplo/`: exemplo Maven de leitura, **não integrado** ao Gradle e não destinado ao app desktop distribuído.

Os subprojetos Maven em `tools/` **não substituem o projeto Gradle** em `urna/`.

## Contrato mínimo para a equipe

- Leitura de Pokémon: `/pokemons/{pokemonId}` (ID string com três dígitos, ex. `001`).
- Leitura de partido: `/partidos/{partidoId}` (IDs em português sem acentos, ex. `planta`).
- Leitura de candidato: `/eleicoes/eleicao-001/candidatos`, filtrando por `cargo`, `numero` e `ativo`.
- Apuração: `/eleicoes/eleicao-001/apuracao/VEREADOR` e `/PREFEITO`.
- `numero` eleitoral é **string**: vereador com cinco dígitos; prefeito com dois.
- `imagemUrl` é string no documento do Pokémon.
- Município/zoneamento e modelo `VoteEntry` existentes em `BallotBox` ainda não foram mapeados ao Firestore; combinar com os responsáveis pelo domínio/controle antes de introduzir novos campos.

## Responsabilidades sugeridas

- Responsável pelo banco: estrutura do Firestore, dados, documentação e consultas de demonstração.
- Responsáveis pela aplicação: regras de início/encerramento, confirmação de cada voto, prevenção de votação duplicada, integrações de interfaces e emissão do boletim.
- Integração: acordar uma camada de persistência/serviço Java; não colocar consultas Firestore diretamente nas views Swing.

## Segurança

O Firebase Admin SDK utiliza credenciais de alto privilégio. **Não** incluir chaves JSON em Git, em `.jar` distribuído ou em clientes Swing de usuários. O projeto deve executar operações privilegiadas em ambiente confiável (por exemplo, serviço Java/backend). O aplicativo Swing se comunicará com esse serviço ou outra solução de acesso seguro aprovada pela equipe.

## Pendências antes do PR

1. Validar `imagemUrl` dos documentos `001`, `004` e `025` no Console.
2. Verificar array `cargos = ["VEREADOR", "PREFEITO"]` com dois elementos.
3. Verificar os documentos reais de candidatos e as duas apurações zeradas.
4. Documentar o ID público do projeto Firebase, **sem** revelar credenciais.
5. Confirmar qual repositório será destino do PR: `lumi2021-academic/urna-eletronica` (origem do fork) ou `Jacksonfvsantos/urna-eletronica`.

## Pull Request sugerido

Título: `docs(firebase): documenta estrutura Firestore e ferramentas de consulta/importação`

O PR deve conter apenas `docs/firebase/` e `tools/firebase-*/`; nenhum arquivo da aplicação principal é alterado nesta entrega.
