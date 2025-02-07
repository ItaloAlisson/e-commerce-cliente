# API de Clientes - Projeto de E-commerce

Esta é a primeira API do projeto de e-commerce, desenvolvida com Java 17 e Spring Boot. A API gerencia dados de clientes, incluindo criação, leitura, atualização e exclusão de registros, com recursos de cache e integração com banco de dados PostgreSQL.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.4.1**
- **Maven**
- **Lombok** para simplificação de código
- **SpringBoot DevTools** para desenvolvimento mais ágil
- **Spring Data JPA** para integração com banco de dados
- **Redis** para cache (Write-through, validação após cada consulta e invalidação após cada alteração no banco ou TTL de 15 minutos)
- **PostgreSQL** como banco de dados relacional
- **Hibernate Validator** para validação de dados
- **MapStruct** para mapeamento de objetos
- **JUnit 5** para testes unitários
- **Mockito** para criação de mocks em testes

## Como Rodar a Aplicação

1. Clone o repositório:
   ```bash
   git clone https://github.com/ItaloAlisson/e-commerce-cliente.git
   ```

2. Crie e configure as variáveis de ambiente no arquivo `env.properties`:
   ```properties
    SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/seubanco
    SPRING_DATASOURCE_USERNAME=seuusuario
    SPRING_DATASOURCE_PASSWORD=suasenha
   ```

3. Para rodar a aplicação, utilize o Maven:
   ```bash
   mvn spring-boot:run
   ```

4. A API estará rodando em `http://localhost:8080`.

## Endpoints

A API oferece os seguintes endpoints para interação com os dados dos clientes:

- `POST /clientes` - Registra um novo cliente
- `GET /clientes` - Retorna a lista de clientes ativos
- `GET /clientes/{cpf}` - Retorna um cliente específico
- `GET /clientes/inativos` - Retorna a lista de clientes inativos
- `GET /clientes/inativo/{cpf}` - Retorna um cliente inativo específico
- `PUT /clientes/{id}` - Atualiza os dados de um cliente
- `PATCH /clientes/{id}` - Atualiza o status de um cliente
- `DELETE /clientes/{id}` - Deleta um cliente

### Exemplos de Uso

#### Registrar Cliente
```bash
POST /api/clientes
Content-Type: application/json
{
    "nome": "Rodrigo Lima",
  "dataNascimento": "06/10/1996",
  "email": "rodrigosss@gmail.com",
  "cpf": "075.643.376-21",
  "endereco": {
    "logradouro": "Rua Agenor de Freitas",
    "numero": "288",
    "bairro": "Praia Grande ",
    "cidade": "Salvador",
    "estado": "BA",
    "cep": "40725-010"
  }
}
```

#### Obter Clientes Ativos
```bash
GET /api/clientes
```

#### Obter Cliente Ativo
```bash
GET /api/clientes/123.456.789-99
```

#### Obter Clientes Inativos
```bash
GET /api/clientes/inativos
```

#### Obter Cliente Inativo
```bash
GET /api/clientes/123.456.789-99
```

#### Atualizar Registro Cliente
```bash
PUT /api/clientes/5133ef76-a626-47ed-899b-8beea56bf326
Content-Type: application/json
{
    "nome": "Rodrigo Silva ",
    "dataNascimento": "10/06/1966",
    "email": "rodrigo@gmail.com",
    "cpf": "075.643.376-21",
    "endereco": {
        "logradouro": "Rua Agenor",
        "numero": "444",
        "bairro": "Praia Grande ",
        "cidade": "Salvador",
        "estado": "BA",
        "cep": "40725-010"
  }
}
```
#### Atualizar Status Cliente
```bash
PATCH /clientes/5133ef76-a626-47ed-899b-8beea56bf326
Content-Type: application/json
{
"ativo": false
}
```

#### Deletar Cliente
```bash
DELETE /clientes/5133ef76-a626-47ed-899b-8beea56bf326
```




## Configuração de Cache

A API utiliza o Redis para caching. O mecanismo de caching está configurado para usar **write-through**, ou seja, todas as operações de gravação no banco de dados também atualizam o cache. O cache é invalidado quando ocorre uma alteração no banco de dados ou após 15 minutos de TTL (time-to-live).

## Testes

Para rodar os testes unitários, execute:

```bash
mvn test
```