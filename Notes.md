# TODO

1. [ ] Cliente
2. [ ] Search Module
3. [ ] Fila
4. [ ] Downloader
5. [ ] Storage Barrel

## Client

- **Utilizado pelo utilizador para pesquisar palavras**
- **Serve para invocar métodos remotos no Servidor RMI**
- **Apenas comunica com o RMISearchModule (porta de entrada)**
- **Também recebe as respostas do search module**

### Ações:

1. Receber URL do utilizador
2. Enviar URL para o Search Module
3. Aguarda pela resposta do Search Module

## Search Module

- **Comunica com o IndexStorageBarrel através de RMI**
- **Visível para o cliente através de RMI**
- **Não armazena os dados, sendo dependente dos IndexStorageBarrels**
- **Escolhe aleatoriamente um dos IndexStorageBarrels para fazer a pesquisa de forma a distribuir a carga**

### Ações:

1. Recebe URL do RMIClient
2. Envia URL para a lista de URLs para os Downloaders
3. Aguarda pela resposta do IndexStorageBarrel com o index
4. Envia resposta para o RMIClient

## Fila

- **Um ou mais componentes capazes de armazenar URLs encontrados pelos Downloaders**
- **Pode ser um programa à parte mas não é obrigatório**
- **Tem de ter acesso distribuído para todos os processos (Search Module e Downloader)**
- **Recurso crítico para Search Module e Downloaders**
- **Independente de qualquer componente**

### Ações:

1. Tem de estar a correr sempre que o programa estiver a correr
2. Pronta a receber URLs do Search Module e dos Downloaders
3. Pronta a enviar URLs para os Downloaders

## Downloader

- **Trabalha em paralelo (maior desempenho)**
- **Um URL -> Um Downloader**
- **Envia informação processada para o IndexStorageBarrel usando Multicast**
- **Segue uma fila de URL para escalonar futuras páginas a visitar**
- **Atualizam o index através de Multi**
- **Sempre à escuta da fila**

### Ações:

1. Vai buscar URL à fila
2. Descarrega página
3. Guarda conteúdo da página no índice
4. Envia URLs encontrados para a fila
5. Envia conteúdo da página para o IndexStorageBarrel
6. Vai buscar nova URL à fila

## Storage Barrel

- **Trabalha em paralelo (maior desempenho)**
- **Recebe informação processada pelo Downloader usando Multicast**
- **Sempre à escuta tanto dos downloaders como do Search Module**
- **É o servidor central (replicado) que armazena todos os dados da aplicação**
- **Recebe os elementos do índice (palavras e URLs) através de multicast envidado pelos Downloaders**
- **Protocolo de Multicast fiável para que todos os IndexStorageBarrels tenham informação idêntica (podendo haver omissões)**

### Ações:

1. Recebe informação do Downloader
2. Guarda informação do index num ficheiro de texto
3. Ler ficheiro de texto e guarda informação no index
4. Envia informação para os Search Modules

# Estutura do que fazer
