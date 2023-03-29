# Fila

- [x] Histórico de URLs visitadas
- [x] Enviar URLs para os Downloaders
- [x] Ze tá a dar hate aos ports :(

# Downloader

- [x] Receber URLs da fila
- [x] Enviar multicast para o IndexStorageBarrel
- [x] Enviar Lista de URLs para a Fila em vez de 1 URL
- [ ] Mudar Multicast

# Barrels

- [x] Receber multicast do Downloader
- [x] Confirmar cenas do Zé
- [x] RMI para o Search Module
- [ ] Mudar Multicast
- [ ] Particionamento do índice

# Search Module

- [x] Comunicação RMI com Cliente
- [x] Comunicar RMI com os Barrels
- [x] Comunicar com Fila para enviar URLs

# Client

- [x] Comunicar RMI com o Search Module
- [x] Interface para o utilizador
- [ ] Admin Page
- [ ] Paginação na pesquisa

# Métodos

- [x] **Indexar novo URL**: Utilizador envia URL a fazer download para indexar no índice.
- [x] **Indexador recursivo**: Indexar automaticamente todos os URLs encontrados no URL inicial.
- [ ] **Pesquisa**: Utilizador envia uma ou várias palavras chaves e é retornado uma lista de páginas que contenham essas palavras. O resultado deve mostrar o título da página, o URL e uma pequena citação da página onde aparece a palavra chave. Os resultados devem ser agrupados 10 a 10
- [ ] **Resultados ordenados por importância**: A ordem por importância é considerada pelo número de ligações de outras páginas que apontam para a página em questão. Manter para cada URL a lista de URLs que apontam para ele.
- [ ] **Consultar lista de páginas com ligação para página pesquisa**: Quando o utilizador tem o login efetuado, deve ser possível consultar a lista de páginas que apontam para a página que está a ser visualizada.
- [ ] **Página de Administração**: Todos os utilizadores devem ter acesso a consultar informações do sistema, sendo apenas atualizada quando houver alterações. Pretende-se saber a lista de Downloaders e Barrels ativos (IP e Port), tal como as 10 pesquisas mais comuns realizadas pelos utilizadores.
- [ ] **Particionamento do índice**: (PARA GRUPOS DE 3) O índice está particionado em (pelo menos) duas metades: uma com as palavras de A-M e outra de N-Z. É necessário fazer "merge" de resultados de pesquisas que envolvam Barrels distintos.

# Relatório

- Nada de código.
- Explicar raciocínio e decisões tomadas.
- [Notas_Relatorio.md](Notas_Relatorio.md)
