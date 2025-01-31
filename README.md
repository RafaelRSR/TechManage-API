
# TechManage API

API RESTful para operações básicas de gerenciamento de usuários desenvolvida com Spring Boot.

## Tecnologias Utilizadas

* Java 17
* Spring Boot 3.3.4
* MySQL
* Maven
* Lombok
* MapStruct


## Pré-requisitos

* JDK 17
* MySQL 8.x


## Configuração do Ambiente

1 . Clone o repositório Git:
   ```bash
   git clone https://github.com/RafaelRSR/TechManage-API.git
   cd tech-manage
   ``` 
2 . Build do projeto:
```bash
.\mvnw clean install
```
3 . Inicie a aplicação
```bash
.\mvnw spring-boot:run
```
4 . Sua API estará disponível no endereço:
``` 
http://localhost:8080/api/users
```

## Endpoints da API

### Gerenciamento de Usuários
- `GET /api/users` : Retorna todos os usuários.
-  `GET /api/users/{id}` : Retorna o usuário através de seu ID.
- `POST /api/users` : Adiciona um novo usuário com os dados do corpo da requisição.
- `PUT /api/users/{id}` : Atualiza os dados de um usuário através de seu ID, através dos dados inseridos no corpo da requisição.
- `DELETE /api/users/{id}` : Deleta um usuário através de seu ID.

## Testes

1 . Garanta que seu projeto esteja rodando e funcionando conforme a aba "Configuração do Ambiente".

2 . Abra o terminal e vá até o diretório do projeto.

3 . Rode os testes através do comando:

```bash
  ./mvnw test
```

Esse comando executará todos os casos de testes e dará um resumo de sucesso ou falha dos mesmos.

**Nota: Se você está utilizando Windows, utilize mvnw.cmd ao invés de ./mvnw nos comandos de configuração.**

Após finalização dos testes, você verá os indicadores de testes rodados, bem sucedidos e com falha.