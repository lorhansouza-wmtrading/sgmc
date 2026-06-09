# SGMC - Sistema de Gerenciamento de Moto Clube 🏍️

O **SGMC** é uma solução de backend desenvolvida em Java com Spring Boot, projetada para automatizar e organizar a gestão administrativa de Moto Clubes. O sistema permite o controle de membros, eventos, participações, frotas de motos e muito mais.

## 🚀 Tecnologias Utilizadas

Este projeto utiliza o que há de mais moderno no ecossistema Java:

*   **Java 21**: Linguagem base.
*   **Spring Boot 4.0.6**: Framework para agilidade e configuração simplificada.
*   **Spring Data JPA**: Abstração de persistência de dados.
*   **Hibernate 7.2**: Implementação ORM robusta.
*   **MySQL**: Banco de dados relacional usado em produção.
*   **H2 Database**: Banco de dados em memória para desenvolvimento e testes.
*   **Scalar**: Documentação interativa da API.
*   **JUnit 5 & Mockito**: Garantia de qualidade via testes automatizados.
*   **Lombok**: Redução de código boilerplate.

## 🛠️ Funcionalidades Principais

### Gestão de Membros
*   Cadastro completo de membros com validações.
*   **Exclusão Lógica**: Sistema de inativação (campo `ativo`) que mantém o histórico dos dados.
*   Associação de membros a **Cargos** e **Sedes**.
*   Filtragem dinâmica de membros por status.

### Eventos e Participações
*   Criação e gestão de eventos.
*   Controle de presença e participações de membros em eventos.
*   Mapeamento complexo de chaves compostas para participações.

### Gestão de Frota
*   Cadastro de motos associadas a membros.
*   Controle de marcas, modelos e seguros.

## 🏁 Como Executar o Projeto

### Pré-requisitos
*   JDK 21 ou superior.
*   Maven 3.8+ instalado (ou use o `./mvnw` incluso).
*   MySQL 8.0 ou superior instalado.

### Passo a Passo
1.  Clone o repositório:
    ```bash
    git clone https://github.com/seu-usuario/sgmc.git
    ```
2.  Acesse a pasta do projeto:
    ```bash
    cd sgmc
    ```
3.  Execute a aplicação:
    ```bash
    ./mvnw spring-boot:run
    ```
4.  Acesse a documentação da API (Scalar):
    ```
    http://localhost:8080/api/docs/index.html
    ```

## 🧪 Rodando os Testes

Para garantir que tudo está funcionando corretamente:

```bash
./mvnw test
```

Os testes utilizam o novo padrão do **Spring Boot 4**, com `@MockitoBean` para garantir isolamento e performance.

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---
*Desenvolvido para facilitar a gestão de paixão por duas rodas.* 🤘
