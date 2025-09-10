# 🎬 Cooperfilme API

API REST desenvolvida em Java com Spring Boot para gerenciar o fluxo de aprovação de roteiros enviados por clientes. A aplicação simula o processo interno da Cooperfilme, passando por etapas de análise, revisão e votação por aprovadores.

---

## 🚀 Tecnologias Utilizadas

- Java 17
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- Spring Security + JWT
- H2 Database (em memória)
- Swagger/OpenAPI
- Lombok

---

## 📦 Como Rodar o Projeto


   git clone https://github.com/seu-usuario/cooperfilme-api.git
   cd cooperfilme-api
   mvn clean install
   mvn spring-boot:run


## 👥 Usuários pré-cadastrados

O sistema inicializa com os seguintes usuários para fins de teste:

| Nome              | E-mail                      | Senha         | Papel      |
|-------------------|-----------------------------|---------------|------------|
| Ana Analista      | analista@cooperfilme.com    | analista123   | ANALISTA   |
| Rui Revisor       | revisor@cooperfilme.com     | revisor123    | REVISOR    |
| Aline Aprovadora  | aprovador1@cooperfilme.com  | aprovador123  | APROVADOR  |
| Alex Aprovador    | aprovador2@cooperfilme.com  | aprovador123  | APROVADOR  |
| Amanda Aprovadora | aprovador3@cooperfilme.com  | aprovador123  | APROVADOR  |
