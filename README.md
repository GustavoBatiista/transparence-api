# Transparence API

API REST desenvolvida em **Java com Spring Boot** para gerenciamento de **usuários, pessoas cuidadas, contratos, gastos e recebimentos**.

O objetivo do projeto é garantir **organização, transparência e regras claras de negócio**, utilizando boas práticas de arquitetura e evitando a exposição direta das entidades do domínio.

---

## 🛠️ Tecnologias Utilizadas

- Java 17+
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- Swagger / OpenAPI
- Maven
- Git

---

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas, separando responsabilidades de forma clara:


### 📦 Camadas

- **Controller**
  - Responsável por receber e responder requisições HTTP
  - Trabalha exclusivamente com DTOs

- **DTO (Data Transfer Object)**
  - `RequestDTO`: dados de entrada da API
  - `ResponseDTO`: dados de saída
  - Evita a exposição direta das entidades JPA

- **Service**
  - Contém as regras de negócio
  - Controla estados e validações
  - Trabalha diretamente com entidades do domínio

- **Repository**
  - Camada de acesso a dados usando Spring Data JPA

- **Entity**
  - Representa o domínio da aplicação
  - Mapeada com JPA/Hibernate

---

## 📌 Funcionalidades

### 👤 Usuário
- Criar usuário
- Atualizar usuário
- Buscar usuário por ID
- Excluir usuário

### 👥 Pessoa Cuidada
- Criar pessoa cuidada
- Atualizar pessoa cuidada
- Buscar pessoa cuidada por ID
- Excluir pessoa cuidada

### 📄 Contrato
- Criar contrato
- Buscar contrato por ID
- Suspender contrato
- Reativar contrato
- Encerrar contrato
- Excluir contrato

> O contrato possui controle de status (`ATIVO`, `SUSPENSO`, `ENCERRADO`) e suas transições são controladas exclusivamente na camada de serviço.

### 💰 Gasto
- Criar gasto
- Atualizar gasto
- Buscar gasto por ID
- Listar gastos
- Excluir gasto

> Gastos só podem ser criados, atualizados ou excluídos se o contrato estiver ativo.

### 💵 Recebimento
- Criar recebimento
- Atualizar recebimento
- Buscar recebimento por ID
- Listar recebimentos
- Excluir recebimento

> Recebimentos também dependem do status do contrato.

---

## 🔐 Regras de Negócio

- Não é permitido:
  - Criar contratos ativos duplicados para o mesmo usuário e pessoa cuidada
  - Criar gastos ou recebimentos para contratos inativos
  - Cadastrar usuários com CPF ou e-mail duplicados
- O status do contrato é controlado apenas pelo sistema
- Datas de início, suspensão, reativação e encerramento são definidas automaticamente quando aplicável

---

## 📄 Documentação da API (Swagger)

A API possui documentação interativa utilizando Swagger.

Após subir a aplicação, acesse:

[Swagger UI](http://localhost:8080/swagger-ui.html)


---

## ▶️ Como Executar o Projeto

### Pré-requisitos
- Java 17 ou superior
- Maven

### Passos

```bash
git clone https://github.com/GustavoBatiista/transparence-api.git
cd transparence-api
mvn spring-boot:run

👨‍💻 Autor

Gustavo Batista
Projeto desenvolvido com foco em aprendizado prático, arquitetura limpa e boas práticas de desenvolvimento backend.
