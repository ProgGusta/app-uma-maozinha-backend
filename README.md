# Uma Mãozinha

## Descrição

Uma plataforma FullStack para contratação de serviços freelance, conectando usuários com profissionais independentes. O backend é desenvolvido em Java com Spring Boot, fornecendo uma API RESTful para gerenciamento de usuários, perfis de freelancers, serviços, avaliações e mais. O frontend é implementado em Angular, oferecendo uma interface amigável para interação com a plataforma.

## Tecnologias Utilizadas

- **Backend:**
  - Java 21
  - Spring Boot 3.3.5
  - Spring Data JPA
  - Hibernate
  - Spring Security
  - PostgreSQL
  - JWT para autenticação
  - Docker e Docker Compose
  - Lombok
  - Spring Dotenv
  - SpringDoc OpenAPI para documentação da API

- **Frontend:**
  - Angular

## Funcionalidades Principais

- **Autenticação e Autorização:** Cadastro e login de usuários com autenticação JWT. Controle de acesso baseado em usuário autenticado (apenas dados próprios podem ser gerenciados).
- **Gestão de Usuários:** Criação, atualização e visualização de perfis de usuários, incluindo endereços e telefones.
- **Perfis de Freelancers:** Usuários podem criar perfis de freelancers, associando-se a categorias de serviços.
- **Serviços:** Criação e gerenciamento de serviços oferecidos pelos freelancers, com status (PENDING, WAITING_USER, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED).
- **Categorias:** Organização de serviços em categorias como Tecnologia, Serviços Domésticos, Saúde e Bem-Estar, etc.
- **Avaliações:** Sistema de ratings e comentários para serviços realizados, calculando média de avaliação para freelancers.
- **Validação de Endereços:** Controle de endereços com validação futura de CEP via API externa.

## Pré-requisitos

- Java 21 ou superior
- Maven 3.6+
- Docker e Docker Compose
- PostgreSQL (opcional, pode ser executado via Docker)
- Node.js e Angular CLI (para desenvolvimento do frontend)

## Instalação e Configuração

### Backend

1. **Clone o repositório:**
   ```
   git clone https://github.com/seu-usuario/app-uma-maozinha-backend.git
   cd app-uma-maozinha-backend
   ```

2. **Configure as variáveis de ambiente:**
   Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis:
   ```
   DB_URL=jdbc:postgresql://localhost:5431/uma_maozinha
   DB_USERNAME=seu_usuario_postgres
   DB_PASSWORD=sua_senha_postgres
   DB_NAME=uma_maozinha
   ```
   Ajuste os valores conforme sua configuração do PostgreSQL.

3. **Execute o banco de dados com Docker Compose:**
   ```
   docker-compose up -d
   ```
   Isso iniciará um container PostgreSQL na porta 5431.

4. **Compile e execute a aplicação:**
   ```
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```
   A aplicação estará rodando em `http://localhost:8080`.

## Uso

### API Backend

A API RESTful está documentada com Swagger. Após iniciar o backend, acesse `http://localhost:8080/swagger-ui.html` para explorar os endpoints.

#### Endpoints Principais

- **Autenticação:**
  - `POST /api/login` - Realizar login e obter token JWT

- **Usuários:**
  - `POST /api/users` - Criar novo usuário
  - `GET /api/users/{id}` - Obter detalhes do usuário (autenticado)
  - `PUT /api/users/{id}` - Atualizar usuário (autenticado)

- **Endereços:**
  - `POST /api/users/{id}/addresses` - Adicionar endereço ao usuário (autenticado)
  - `GET /api/users/{id}/addresses` - Listar endereços do usuário (autenticado)
  - `PUT /api/addresses/{addressId}` - Atualizar endereço (autenticado)
  - `DELETE /api/addresses/{addressId}` - Deletar endereço (autenticado)

- **Telefones:**
  - `POST /api/users/{id}/phones` - Adicionar telefone ao usuário (autenticado)
  - `GET /api/users/{id}/phones` - Listar telefones do usuário (autenticado)
  - `PUT /api/phones/{phoneId}` - Atualizar telefone (autenticado)
  - `DELETE /api/phones/{phoneId}` - Deletar telefone (autenticado)

- **Perfis de Freelancers:**
  - `POST /api/freelancers` - Criar perfil de freelancer (usuário autenticado)
  - `GET /api/freelancers/{id}` - Obter perfil de freelancer
  - `PUT /api/freelancers/{id}` - Atualizar perfil (dono do perfil)

- **Serviços:**
  - `POST /api/services` - Criar novo serviço (freelancer)
  - `GET /api/services` - Listar serviços (com filtros)
  - `PUT /api/services/{id}` - Atualizar serviço (dono)
  - `DELETE /api/services/{id}` - Deletar serviço (dono)

- **Avaliações:**
  - `POST /api/ratings` - Criar avaliação para um serviço
  - `GET /api/ratings/service/{serviceId}` - Listar avaliações de um serviço

- **Categorias:**
  - `GET /api/categories` - Listar todas as categorias

Todos os endpoints que modificam dados requerem autenticação via JWT no header `Authorization: Bearer <token>`.

## Estrutura do Projeto Backend

```
uma-maozinha/
├── src/main/java/br/com/umamanzinha/uma_maozinha/
│   ├── config/          # Configurações do Spring (JWT, etc.)
│   ├── controller/      # Controladores REST
│   ├── dtos/            # Data Transfer Objects
│   ├── entities/        # Entidades JPA
│   ├── enums/           # Enums (CategoryType, ServiceStatus)
│   ├── exceptions/      # Exceções customizadas
│   ├── mapper/          # Mapeadores (ex: AddressMapper)
│   ├── repository/      # Repositórios JPA
│   └── services/        # Lógica de negócio
├── src/main/resources/
│   ├── application.yml  # Configurações da aplicação
│   ├── authz.pem        # Chave privada JWT
│   └── authz.pub        # Chave pública JWT
└── pom.xml              # Dependências Maven
```


## Contribuição

Contribuições são bem-vindas! Siga estes passos:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

Por favor, siga as convenções de código e adicione testes para novas funcionalidades.

## Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## Contato

Para dúvidas ou sugestões, entre em contato:
- Email: brennosantosflorencio@gmail.com
- GitHub: [brenno-bsf](https://github.com/brenno-bsf) e [ProgGusta](https://github.com/ProgGusta) 

---

**Nota:** Este projeto foi desenvolvido como parte da matéria de PROGRAMAÇÃO PARA APLICAÇÕES WEB.
