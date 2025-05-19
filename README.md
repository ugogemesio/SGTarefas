# Sistema de Gerenciamento de Tarefas

Este projeto consiste em um sistema simples para gerenciamento de tarefas, composto por:

- Backend Java dividido em dois microserviços: **user-service** e **task-service**.
- Dois bancos de dados PostgreSQL separados para cada serviço.
- Frontend Angular servido via Nginx.

---

## Pré-requisitos

- [Docker](https://docs.docker.com/get-docker/) instalado
- [Docker Compose](https://docs.docker.com/compose/install/) instalado
- [Git](https://git-scm.com/downloads) instalado

---

## Como executar localmente

Antes de executar o projeto tente seguir o passo-a-passo mais seguro que é o de subir primeiro os bancos, depois build dos serviços banckends, depois subir o serviço backend, depois buildar o frontend depois subir por fim o frontend;

Observe também as portas vagas da sua rede 4200, 8080, 8081, 5432, 5433;


### 1. Clone este repositório

```bash
git clone https://github.com/ugogemesio/SGTarefas.git
```

### 2. Entre na pasta SGTarefas pelo terminal

```bash
cd SGTaregas
```


### 3. Suba os bancos de dados de usuários com Docker Compose

```bash
docker compose up postgres-user -d
```

### 4. Suba os bancos de dados de tarefas de usuários com Docker Compose

```bash
docker compose up postgres-task -d
```

### 5. Faça build do backend de usuarios
```bash
docker compose build user-service
```
### 6. Suba o backend de usuarios

```bash
docker compose up user-service -d
```

### 7. Faça build do backend de  tarefas de usuarios
```bash
docker compose build task-service
```
### 8. Suba o backend de tarefas de usuarios
```bash
docker compose up task-service -d
```
### 9. Faça build do frontend
```bash
docker compose build frontend
```
### 10. Suba o frontend
```bash
docker compose up frontend -d
```
Este cadeia de comandos comando irá:

- Construir as imagens dos microserviços e do frontend
- Baixar e iniciar os containers PostgreSQL
- Subir todos os serviços na rede Docker configurada

### 11. Acesse a aplicação

- Frontend: http://localhost:4200
- API do User Service: http://localhost:8080/swagger-ui/index.html#/
- API do Task Service: http://localhost:8081/swagger-ui/index.html#/

---

### Documentação da API e Relatórios de Testes
A documentação interativa das APIs está disponível via Swagger UI em:

User Service: http://localhost:8080/swagger-ui/index.html#/

Task Service: http://localhost:8081/swagger-ui/index.html#/

Os relatórios de cobertura dos testes (Jacoco) podem ser acessados em:

User Service: http://localhost:8080/site/jacoco/index.html

Task Service: http://localhost:8081/site/jacoco/index.html


## Estrutura do projeto

```
/
├── docker-compose.yml
├── user-service/        # Backend do serviço de usuários
│   ├── Dockerfile
│   └── src/ ...
├── task-service/        # Backend do serviço de tarefas
│   ├── Dockerfile
│   └── src/ ...
├── frontend/            # Frontend Angular
│   ├── Dockerfile
│   ├── nginx.conf
│   └── src/ ...
└── volumes/             # Volumes Docker para dados persistentes
```

---

## Detalhes técnicos

- O banco PostgreSQL do user-service está exposto na porta `5432`.
- O banco PostgreSQL do task-service está exposto na porta `5433`.
- Os backends aguardam a saúde dos bancos antes de iniciar.
- O frontend está disponível na porta `4200` e é servido pelo Nginx.

---

## Comandos úteis

- Parar e remover containers:

```bash
docker-compose down
```

- Visualizar logs dos containers:

```bash
docker-compose logs -f
```

---

## Ambiente

- Java 17
- Maven 3.8.7
- Node 18
- Angular CLI 19
- PostgreSQL 15
- Docker 28.1.1
- Docker Compose 2.35.1

---

## Contato

Qualquer dúvida, entre em contato pelo email: ugo.gemesio@gmail.com

---
