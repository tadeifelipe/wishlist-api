# Wishlist API

API para gerenciar lista de desejos dos clientes

# Application Architecture

API feita em **Java** com **SpringFramework**, utilizando padrões **REST** e inspirada no padrão em camadas **MVC**. 

Também utilizando **Redis** para cache e **MongoDB** para persistência de dados


> Para os serviços de Costumers e Products, foram desenvolvidas duas apis para simular a comunicação entre os microserviços.
Foram adicionadas imagens docker em `docker-compose.yml` para simular os serviços.
Os serviços sobem nas portas **8081** e **8082** respectivamente
Imagens disponíveis no [DockerHub/tadeifelipe](https://hub.docker.com/search?q=tadeifelipe)

```
 ╭┄┄┄┄┄┄┄╮      ┌──────────┐      ┌──────────┐       ┌──────────┐ 
 ┆   ☁   ┆  ←→  │    ☕     │  ←→  │    📦    │  ←→   │    💾    │ 
 ┆  Web  ┆ HTTP │ Wishlist │      │   Redis  │       │  MongoDB │
 ╰┄┄┄┄┄┄┄╯      │  Service │      └──────────┘       └──────────┘
                └──────────┘
                     ↑ JSON/HTTP
                     ↓
       ┌──────────┐      ┌──────────┐
       │    ☁     │      │    ☁     │
       │ Costumers│      │ Products │
       │ Service  │      │ Service  │
       └──────────┘      └──────────┘
```

# Get Started
Como pré-requisito para executar o projeto, temos as seguintes tecnologias:
+ [Java 17](https://openjdk.org/projects/jdk/17/)
+ [Docker](https://www.docker.com/products/docker-hub/)

Comece clonando o repositório para sua máquina com `git clone https://github.com/tadeifelipe/wishlist-api.git`

Execute `./mvnw install`, se estiver no Windows use `./mvnw.cmd install` para instalar as dependências

Suba a aplicação com `./mvnw spring-boot:run `

> Não se preocupe em subir os serviços do `docker-compose.yml`, o Spring Boot já faz isso automaticamente para nós 😀

# Tests
A aplicação conta com Swagger para visualização dos end-points, basta acessar 

http://localhost:8000/swagger-ui/index.html?configUrl=/api-docs/swagger-config

Utiliza Basic Authentication, e por padrão, pega o user e password em `application.properties`. 

Configurado por padrão `user:admin password:admin`


Os serviços de Costumers e Products possuem dados fakes para serem utilizados:


**Products**

| ID  | Name | Price |
| ------------- | ------------- | ------------- |
| 5c4fb2b8-0c79-4443-affd-e22815888d7e  | Product One   | 55.90 | 
| aa2eb0b6-ff40-4ad5-9c31-c2c3a2dd2fb6  | Product Two   | 45.90 | 
| 4a8de092-c8a7-4be7-be84-97b9aae3adb7  | Product Three | 25.90 | 

**Customers**

| ID  | Name | Lastname |
| ------------- | ------------- | ------------- |
| 1  | Customer | One | 
| 2  | Customer | Two | 

Utilize `./mvnw test` para executar os cenários de testes.

> O projeto utiliza [JaCoCo](https://www.jacoco.org/jacoco/trunk/index.html) para facilitar a visualização e cobertura dos cenários.

> Atualmente com uma **cobertura de testes em 88%** da aplicação.


# Technologies
+ **spring-boot-docker-compose** para subir automaticamente os serviços em `docker-compose.yml`
+ **spring-boot-starter-data-redis** para integração com Redis
+ **spring-boot-starter-security** para aplicar segurança na aplicação
+ **de.flapdoodle.embed.mongo** banco de dados embutido para testes de integração
+ **wiremock** stub de serviços http para testes de integração
+ **rest-assured** framework de testes para testes end-to-end
+ **embedded-redis** banco de dados embutido para testes de integração e validação de cache
+ **springdoc-openapi-starter-webmvc-ui** para documentar a API


# Pontos de melhorias
> Para melhorar o desacoplamento e separação dos conceitos. Podemos utilizar [Clean Architecture](https://www.baeldung.com/spring-boot-clean-architecture)


> Por se tratar em ambiente microserviços, **para melhorar a observalidade, métricas e análise dos logs**, podemos utilizar um serviço de logs centralizados.
Por exemplo [Elastic](https://www.elastic.co/) ou [Splunk](https://www.splunk.com/)


> Para melhorar a especificação da API e testes, podemos utilizar [Cucumber](https://cucumber.io/), um framework basead em Behavior-Driven Development (BDD).


