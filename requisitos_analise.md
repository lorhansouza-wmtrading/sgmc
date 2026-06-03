# Relatório de Análise de Requisitos Funcionais - Backend SGMC (Atualizado)

Este relatório apresenta uma análise atualizada da cobertura dos requisitos funcionais especificados no projeto backend SGMC após a implementação das correções de build, endpoints de eventos, exclusão de motocicletas e testes automatizados.

---

## Tabela de Resumo de Conformidade

| ID | Nome do Requisito | Descrição | Status de Conformidade | Observações/Lacunas |
| :--- | :--- | :--- | :--- | :--- |
| **RF01** | Autenticação de Usuários | Login/logout seguro via Keycloak | ❌ **Não Atendido** | Sem dependências de segurança ou integração com Keycloak/OAuth2. |
| **RF02** | Controle de Acesso Corporativo | Restrição de telas e ações por papel (*Role*) | ❌ **Não Atendido** | Ausência total de controle de acessos (Spring Security, anotações `@PreAuthorize`, etc.). |
| **RF03** | Manter Cadastro de Membros | CRUD completo dos motociclistas integrantes | ⚠️ **Parcialmente Atendido** | Implementado via inativação lógica (`DELETE` altera status para `INATIVO`). Estrutura básica funcional. |
| **RF04** | Vincular Cargo/Hierarquia | Associar cada membro à sua respectiva patente | 🟢 **Atendido** | Relação `@ManyToOne` funcional entre `Membro` e `Cargo` com validação de existência no serviço. |
| **RF05** | Manter Motocicletas | Cadastrar e vincular veículos ao perfil de um membro | 🟢 **Atendido** | **Resolvido!** Mapeamento funcional e agora com o endpoint de exclusão (`DELETE /motos/{idMembro}/{placa}`) validando propriedade e cobertura completa de testes. |
| **RF06** | Gerenciar Eventos | Agendamento, edição e cancelamento de eventos | 🟢 **Atendido** | **Resolvido!** Implementamos Edição (`PUT`) e Cancelamento (`DELETE`) de eventos, criamos a interface OpenAPI e testamos a camada inteira com 100% de sucesso. |
| **RF07** | Registrar Presença | Chamada e registro de presença em eventos | ❌ **Não Atendido** | Entidade `Participacao` armazena apenas dados de inscrição e veículo, sem controle de presença confirmada ou endpoints de chamada dedicados. |

---

## Detalhamento Técnico por Requisito

### RF01: Autenticação de Usuários
* **Descrição:** Login e logout seguro através do Keycloak.
* **Análise:** 
  * O arquivo [pom.xml](file:///c:/Users/ronye/Documents/sgmc/pom.xml) não possui dependências de segurança (ex. `spring-boot-starter-security`, `spring-boot-starter-oauth2-resource-server`).
  * Não existem classes de filtro de segurança ou controle de sessões.

### RF02: Controle de Acesso Corporativo
* **Descrição:** Restringir telas e ações de acordo com o papel (*Role*) atribuído ao usuário.
* **Análise:**
  * Depende diretamente do **RF01**. Não há rotas protegidas ou anotações como `@PreAuthorize`.

### RF03: Manter Cadastro de Membros
* **Descrição:** CRUD de motociclistas integrantes.
* **Análise:**
  * Coberto pelas classes [MembroController.java](file:///c:/Users/ronye/Documents/sgmc/src/main/java/br/com/mam/sgmc/api/controller/MembroController.java) e [MembroService.java](file:///c:/Users/ronye/Documents/sgmc/src/main/java/br/com/mam/sgmc/services/MembroService.java).
  * A exclusão é tratada de forma lógica alterando o status do membro para `INATIVO`.

### RF04: Vincular Cargo/Hierarquia
* **Descrição:** Associar cada membro à sua patente estatutária.
* **Análise:**
  * Relacionamento funcional e mapeado no modelo de dados.

### RF05: Manter Motocicletas
* **Descrição:** Cadastro, vínculo e remoção de veículos ao perfil de um membro específico.
* **Análise:**
  * **Status:** Totalmente Coberto.
  * O endpoint `DELETE /motos/{idMembro}/{placa}` valida a propriedade da moto antes da deleção no banco de dados.
  * Teste automatizado valida que após a deleção a listagem do membro retorna vazia.

### RF06: Gerenciar Eventos
* **Descrição:** Agendamento, edição e cancelamento de encontros, viagens e reuniões oficiais.
* **Análise:**
  * **Status:** Totalmente Coberto.
  * Corrigidos os erros no [EventoService](file:///c:/Users/ronye/Documents/sgmc/src/main/java/br/com/mam/sgmc/services/EventoService.java) e implementados os métodos de busca, edição (`PUT`), deleção (`DELETE`) e OpenAPI.
  * Testes automatizados (`EventoControllerTest` e `EventoServiceTest`) criados e integrados com sucesso.
  * Corrigido o bug de mapeamento no [Membro.java](file:///c:/Users/ronye/Documents/sgmc/src/main/java/br/com/mam/sgmc/model/Membro.java) para `pk.membro`.

### RF07: Registrar Presença
* **Descrição:** Realizar a chamada e registrar a presença dos membros nos eventos.
* **Análise:**
  * **Status:** Não Atendido.
  * Não há campos para salvar se a presença de fato ocorreu na entidade `Participacao` nem lógica exposta por rotas HTTP.
