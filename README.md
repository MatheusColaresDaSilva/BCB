
# 📬 Sistema de Mensagens

Este projeto é uma API RESTful desenvolvida com Java e Spring Boot para o envio de mensagens entre clientes, com controle de fila, prioridades e verificação de saldo. Foi implementado com foco em cobrir o maior número possível de regras e pontos de entrega, respeitando as limitações de tempo e ambiente.

---

## ⚙️ Tecnologias Utilizadas

- Java 17  
- Spring Boot  
- Spring Data JPA  
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

2. **Executar com Maven (sem Docker):**

```bash
./mvnw spring-boot:run
```

3. **(Opcional) Compilar o projeto:**

```bash
./mvnw clean install
```

4. **Swagger UI:**

Acesse via navegador:

```
http://localhost:8080/swagger-ui.html
```

5. **(Futuro) Docker Compose:**

Em breve será incluído um `docker-compose.yml` para facilitar a execução com PostgreSQL + aplicação containerizada.

---

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
