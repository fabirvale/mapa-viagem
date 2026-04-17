## Mapa de Viagem – API REST (Spring Boot)
Sistema para gerenciamento de viagens da rede pública de saúde, permitindo o controle de pacientes, agendamentos, motoristas, veículos e ocorrências durante o deslocamento.

## Funcionalidades
- Cadastro de viagens, motoristas, acompanhantes, veículos e agendamentos
- Controle de ocorrências durante a viagem
- Validação de regras de negócio (horários, quilometragem e fechamento de viagem)
- Integração com frontend em JavaScript

## Tecnologias
- Java
- Spring Boot
- Spring Data JPA / Hibernate
- H2 Database
- JavaScript
- Git / GitHub

##  Estrutura do Projeto
- src/main → backend (API REST)
- src/frontend → interface web consumindo a API

##  Como executar o projeto

### Backend
1. Rodar a aplicação Spring Boot
2. A API estará disponível em:  
   http://localhost:8080

### Frontend
1. Abrir o arquivo `index.html` no navegador

## 🗄 Banco de Dados
O projeto utiliza H2 (banco em memória) para facilitar execução e testes.
A estrutura é compatível com MySQL, podendo ser adaptada para uso em ambiente real.

##  Exemplos de endpoints
- GET /viagens
- POST /viagens
- PUT /viagens/{id}
- DELETE /viagens/{id}

## Status do Projeto
Em evolução, com melhorias contínuas e implementação de novas funcionalidades.
