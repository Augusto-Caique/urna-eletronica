# Checklist — Entrega do banco para a equipe

> Marque apenas depois de conferir no Console do Firebase. Não envie credenciais.

## A. Dados

- [ ] Confirmar 15 documentos em `/partidos`, cada um com `nome`, `sigla`, `numero`, `tipoPrimario`, `ativo`.
- [ ] Confirmar IDs `001` até `151` em `/pokemons` (a importação reportou 148 criados e 3 preservados).
- [ ] Conferir `imagemUrl` em `/pokemons/001`, `/pokemons/004`, `/pokemons/025`.
- [ ] Conferir nomes originais e `partidoId` alinhado ao `tipoPrimario`.

## B. Eleição municipal

- [ ] Confirmar `eleicoes/eleicao-001.modalidade == "MUNICIPAL"`.
- [ ] Confirmar que `cargos` é um array com **dois** elementos: `VEREADOR`, `PREFEITO`.
- [ ] Conferir `status == "CONFIGURACAO"`, `ativa == false` e `segundoTurno == false` antes da votação.
- [ ] Confirmar os candidatos realmente existentes e seus números/cargos (os três exemplos são sugestões de teste).
- [ ] Verificar `votos = 0` em todos os candidatos habilitados.
- [ ] Verificar documentos `apuracao/VEREADOR` e `apuracao/PREFEITO` com `validos`, `brancos`, `nulos`, `total` zerados.

## C. Compartilhamento

- [ ] Preencher o **ID do projeto Firebase** no `README.md`.
- [ ] Enviar `README.md`, `CHECKLIST_ENTREGA.md`, `diagramas/` e exemplos de leitura Java.
- [ ] Conceder acesso aos colegas por **Configurações do projeto → Usuários e permissões**, com princípio de mínimo privilégio.
- [ ] Não compartilhar chaves `.json`, tokens, senhas reais ou credenciais administrativas.
- [ ] Definir o responsável pela lógica de confirmação dos votos e pelas transações do backend.
- [ ] Definir com a equipe a lista final de candidatos, com mais de uma candidatura por cargo.
- [ ] Confirmar autorização do professor para a tematização Pokémon.

## D. Testes de integração (pela equipe)

- [ ] Executar o exemplo Java de **somente leitura** em ambiente confiável.
- [ ] Consultar candidato pelo cargo e número, exibir nome/foto/partido.
- [ ] Implementar sessão com dois votos e teclas BRANCO/CORRIGE/CONFIRMA.
- [ ] Implementar gravação atômica e prevenção de duplo registro.
- [ ] Conferir relatório inicial zerado e final separado por cargo.
- [ ] Bloquear novos votos após o encerramento.

## E. Entrega da AV3

- [ ] Repositório GitHub com código e documentação (sem credenciais).
- [ ] Prazo do enunciado: **04/12/2026 às 18h**, apresentação a partir de 19h.
