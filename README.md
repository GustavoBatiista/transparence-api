# Transparence API

API REST desenvolvida em Java com Spring Boot para gerenciamento de usuários, pessoas cuidadas, contratos, gastos e recebimentos.

![Status](https://img.shields.io/badge/status-online-success)
![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.x-brightgreen)
![Deploy](https://img.shields.io/badge/deploy-Railway-purple)

## 🌐 API em Produção

A aplicação está disponível em ambiente cloud e pode ser testada em tempo real.

🔗 [https://transparence-api-production.up.railway.app](https://transparence-api-production.up.railway.app)

📄 Swagger:
🔗 [https://transparence-api-production.up.railway.app/swagger-ui.html](https://transparence-api-production.up.railway.app/swagger-ui.html)

A API está publicada em ambiente de produção utilizando Railway, com banco MySQL em cloud e configuração por variáveis de ambiente.

---

## 🛠️ Tecnologias Utilizadas

* Java 17+
* Spring Boot
* Spring Web
* Spring Data JPA
* Hibernate
* Swagger / OpenAPI
* Maven
* JUnit / Mockito
* Docker
* MySQL
* H2 Database

---

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas, separando responsabilidades de forma clara.

### 📦 Camadas

#### Controller

* Responsável por receber e responder requisições HTTP
* Trabalha exclusivamente com DTOs

#### DTO (Data Transfer Object)

* RequestDTO → dados de entrada
* ResponseDTO → dados de saída
* Evita exposição direta das entidades JPA

#### Service

* Contém as regras de negócio
* Controla estados e validações
* Possui interfaces e implementações separadas
* Possui logs para rastreabilidade das operações

#### Repository

* Camada de acesso a dados com Spring Data JPA

#### Entity

* Representa o domínio da aplicação
* Mapeada com JPA/Hibernate

---

## 📌 Funcionalidades

### 👤 Usuário

* Criar usuário
* Atualizar usuário
* Buscar usuário por ID
* Excluir usuário

### 👥 Pessoa Cuidada

* Criar pessoa cuidada
* Atualizar pessoa cuidada
* Buscar pessoa cuidada por ID
* Excluir pessoa cuidada

### 📄 Contrato

* Criar contrato
* Buscar contrato por ID
* Suspender contrato
* Reativar contrato
* Encerrar contrato
* Excluir contrato

O contrato possui controle de status (ATIVO, SUSPENSO, ENCERRADO) e suas transições são controladas exclusivamente na camada de serviço.

### 💰 Gasto

* Criar gasto
* Atualizar gasto
* Buscar gasto por ID
* Listar gastos
* Excluir gasto

Gastos só podem ser criados, atualizados ou excluídos se o contrato estiver ativo.

### 💵 Recebimento

* Criar recebimento
* Atualizar recebimento
* Buscar recebimento por ID
* Listar recebimentos
* Excluir recebimento

Recebimentos também dependem do status do contrato.

---

## 🔐 Regras de Negócio

* Não é permitido criar contratos ativos duplicados para o mesmo usuário e pessoa cuidada
* Não é permitido criar gastos ou recebimentos para contratos inativos
* Não é permitido cadastrar usuários com CPF ou e-mail duplicados
* O status do contrato é controlado apenas pelo sistema
* Datas de início, suspensão, reativação e encerramento são definidas automaticamente

---

## ⚠️ Tratamento de Exceções

A aplicação possui um `GlobalExceptionHandler` responsável por:

* Padronizar as respostas de erro
* Retornar os status HTTP corretos
* Adicionar rastreabilidade nas respostas

---

## 📊 Observabilidade e Logs

* Logs na camada de service
* Correlation ID para rastreamento de requisições
* Melhor rastreabilidade de erros

---

## 🗄️ Banco de Dados

* Suporte a H2 para ambiente de desenvolvimento
* Suporte a MySQL para ambiente de produção
* Criação de índices para otimização de consultas
* Constraints para integridade dos dados

---

## ⚙️ Profiles de Execução

A aplicação utiliza profiles para separar ambientes:

* `h2` → desenvolvimento
* `mysql` → produção

A troca de banco ocorre apenas por configuração, sem necessidade de alterar código.

---

## 🐳 Docker

O projeto possui `docker-compose` para subir o ambiente completo:

```bash
docker-compose up -d
```

Serviços:

* MySQL
* Aplicação Spring Boot

---

## ☁️ Deploy

Projeto preparado para deploy em cloud (Railway), com:

* Configuração do driver MySQL
* Uso de variáveis de ambiente
* Ajustes de profile para produção

---

## 🧪 Testes

Foram implementados testes unitários para a camada de service, garantindo:

* Validação das regras de negócio
* Confiabilidade das operações
* Facilidade de manutenção

---

## 📄 Documentação da API (Swagger)

Após subir a aplicação, acesse:

```
http://localhost:8080/swagger-ui.html
```

---

## ▶️ Como Executar o Projeto

### Pré-requisitos

* Java 17+
* Maven
* Docker (opcional)

### Executando com Maven

```bash
mvn spring-boot:run
```

### Executando com Docker

```bash
docker-compose up -d
```

---

## 🧠 Conceitos Aplicados

* Arquitetura em camadas
* DTO Pattern
* Separação entre contrato de API e domínio
* Regras de negócio centralizadas na camada de service
* Tratamento global de exceções
* Logs e rastreabilidade
* Testes unitários
* Múltiplos profiles de ambiente
* Preparação para deploy em cloud

---

## 📌 Status do Projeto

🚀 Em evolução contínua com foco em boas práticas de desenvolvimento backend.

---

## 👨‍💻 Autor

**Gustavo Batista**
Projeto desenvolvido com foco em aprendizado prático, arquitetura limpa e boas práticas de desenvolvimento backend.
