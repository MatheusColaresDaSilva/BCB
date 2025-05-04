
# 📬 Sistema de Mensagens

Este projeto é uma API RESTful desenvolvida com Java e Spring Boot para o envio de mensagens entre clientes, com controle de fila, prioridades e verificação de saldo. Foi implementado com foco em cobrir o maior número possível de regras e pontos de entrega, respeitando as limitações de tempo e ambiente.

---

## ⚙️ Tecnologias Utilizadas

- Java 17  
- Spring Boot  
- Spring Data JPA  
- Spring Security
- Hibernate  
- Lombok  
- PostgreSQL  
- Swagger/OpenAPI (Springdoc)  
- Maven  
- Docker (futuramente Docker Compose)  

---

## 🚀 Instruções de Instalação/Execução

1. **Clonar o repositório:**

```bash
git clone https://github.com/seu-usuario/seu-repo.git
cd seu-repo
```

2. **Construir a aplicação e subir os containers com Docker Compose:**

Execute o comando abaixo para construir o projeto e subir os containers:

```bash
docker-compose up --build -d
```

Isso vai:
- Construir a aplicação usando o `Dockerfile`.
- Subir o container da aplicação (com a API) e o container do PostgreSQL.
- Subir a aplicação na porta 8080 e o banco de dados na porta 5432.

3. **Acessar Swagger UI:**

Depois de subir os containers, acesse o Swagger UI via navegador:

```
http://localhost:8080/swagger-ui.html
```

## 🧠 Decisões Técnicas e Limitações

- O objetivo principal foi **cobrir o maior número possível de regras e pontos de entrega** da aplicação.  
- Limitações:
  - **Tempo** de desenvolvimento e **capacidade computacional local** influenciaram algumas decisões.
- A **fila de mensagens** foi implementada usando `PriorityQueue`, que é uma estrutura de **árvore balanceada (min-heap)** do Java.  
- A **comparação** das mensagens na fila é baseada em:
  - `urgency`: mensagens com urgência 0 (URGENT) têm prioridade sobre as com urgência 1 (NORMAL).  
  - `timestamp`: entre mensagens com mesma urgência, as mais antigas vêm primeiro.  
- Um **endpoint assíncrono** inicia o processamento da fila, facilitando testes e visualização do comportamento.

---

## ✅ Funcionalidades Implementadas

- **Clientes**
  - Criação e autenticação por documento  
  - Validação de documento  
  - Validação e débito de saldo  

- **Mensagens**
  - Criação de mensagens com prioridade  
  - Listagem com filtros (campo, operador, valor)  
  - Detalhamento por ID  
  - Consulta de status  

- **Conversas**
  - Obtenção de mensagens por conversa  
  - Detalhamento de conversas  

- **Fila de Mensagens**
  - Inserção de mensagens com ordenação automática  
  - Execução assíncrona da fila via endpoint `/api/v1/queue/process`  
  - Impressão e status das filas disponíveis  

---

## 🔑 Autenticação

A autenticação inicial para o sistema é feita via **Basic Auth**. O usuário admin pode ser utilizado para autenticar e chamar os endpoints necessários para criação de clientes.

**Credenciais:**
- **Usuário**: `1`
- **Senha**: `password`

Isso é para criar o primeiro Client pelo endpoint. Depois disso vc pode pegar o número do Documento do Cliente e usar pra chamar o /auth e pegar o token!
