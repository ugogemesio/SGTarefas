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

### 1. Clone este repositório

```bash
git clone https://github.com/ugogemesio/SGTarefas.git
cd SGTaregas
```

### 2. Entre na pasta SGTarefas pelo terminal

```bash
cd SGTaregas
```


### 3. Suba os containers com Docker Compose

```bash
docker-compose up --build
```

Este comando irá:

- Construir as imagens dos microserviços e do frontend
- Baixar e iniciar os containers PostgreSQL
- Subir todos os serviços na rede Docker configurada

### 3. Acesse a aplicação

- Frontend: [http://localhost:4200](http://localhost:4200)
- API do User Service: [http://localhost:8080](http://localhost:8080)
- API do Task Service: [http://localhost:8081](http://localhost:8081)

---

Documentação da API e Relatórios de Testes
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
- Docker e Docker Compose

---

## Contato

Qualquer dúvida, entre em contato pelo email: ugo.gemesio@gmail.com

---
