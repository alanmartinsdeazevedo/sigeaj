# SIGEAJ - Sistema de Gestão de Setores Produtivos da EAJ

Sistema desenvolvido em Java com JavaFX para gerenciar os setores produtivos da Escola Agrícola de Jundiaí (EAJ).

## Tecnologias Utilizadas

- **Java 21 LTS**
- **JavaFX 21** - Interface gráfica
- **FXML / Scene Builder** - Design de interfaces
- **PostgreSQL** - Banco de dados
- **JDBC** - Acesso a dados
- **Maven** - Gerenciamento de dependências

## Arquitetura

O projeto segue rigorosamente o padrão **MVC (Model-View-Controller)** e os princípios de **Programação Orientada a Objetos (POO)**:

### Camadas do Sistema

```
src/br/edu/ufrn/sigeaj/
│
├── Main.java                      # Classe principal JavaFX
│
├── model/                         # MODEL - Entidades de domínio
│   ├── Usuario.java               # Entidade usuário (encapsulamento)
│   ├── SetorProdutivo.java        # Entidade setor produtivo
│   ├── Atividade.java             # Entidade atividade (associação)
│   ├── RegistroProducao.java      # Entidade registro de produção
│   └── PerfilUsuario.java         # Enum de perfis
│
├── dao/                           # PERSISTÊNCIA - Acesso a dados
│   ├── UsuarioDAO.java            # CRUD de usuários
│   ├── SetorProdutivoDAO.java     # CRUD de setores
│   ├── AtividadeDAO.java          # CRUD de atividades
│   └── RegistroProducaoDAO.java   # CRUD de registros
│
├── service/                       # NEGÓCIO - Regras de negócio
│   ├── UsuarioService.java        # Validações de usuário
│   ├── SetorProdutivoService.java # Validações de setor
│   ├── AtividadeService.java      # Validações de atividade
│   └── RegistroProducaoService.java # Validações de registro
│
├── controller/                    # CONTROLLER - Lógica da interface
│   ├── LoginController.java       # Gerencia tela de login
│   ├── MainController.java        # Gerencia tela principal
│   ├── SetorProdutivoController.java
│   ├── AtividadeController.java
│   └── RegistroProducaoController.java
│
└── util/                          # UTILITÁRIOS
    └── ConnectionFactory.java     # Gerencia conexões com BD

src/main/resources/br/edu/ufrn/sigeaj/
└── view/                          # VIEW - Interfaces FXML
    ├── login.fxml                 # Tela de login
    ├── main.fxml                  # Tela principal/menu
    ├── setor_produtivo.fxml       # CRUD de setores
    ├── atividade.fxml             # CRUD de atividades
    └── registro_producao.fxml     # CRUD de registros
```

## Conceitos de POO Demonstrados

1. **Encapsulamento**: Atributos privados com getters/setters em todas as entidades
2. **Associação**: Atividade e RegistroProducao associados a SetorProdutivo
3. **Composição**: Relacionamentos entre entidades via IDs
4. **Abstração**: Separação clara entre camadas (MVC)
5. **Enum**: PerfilUsuario como enumeração

## Padrão MVC Implementado

- **Model**: Classes de entidades em `model/`
- **View**: Arquivos FXML em `view/`
- **Controller**: Controllers JavaFX em `controller/`
- **Service**: Lógica de negócio separada em `service/`
- **DAO**: Acesso a dados isolado em `dao/`

## Pré-requisitos

1. **Java JDK 21 LTS** (ou superior)
2. **PostgreSQL 12+**
3. **Maven 3.6+**
4. **IDE**: IntelliJ IDEA ou Eclipse (opcional)
5. **Scene Builder** (opcional, para editar FXMLs visualmente)

## Configuração do Banco de Dados

### 1. Instalar PostgreSQL

### 2. Criar o banco de dados

```bash
psql -U postgres
CREATE DATABASE sigeaj;
\c sigeaj
```

### 3. Executar o script SQL

```bash
psql -U postgres -d sigeaj -f database/create_tables.sql
```

Ou copie o conteúdo de `database/create_tables.sql` e execute no pgAdmin.

### 4. Configurar a conexão

Edite o arquivo `src/br/edu/ufrn/sigeaj/util/ConnectionFactory.java`:

```java
private static final String URL = "jdbc:postgresql://localhost:5432/sigeaj";
private static final String USER = "postgres";
private static final String PASSWORD = "sua_senha_aqui";
```

## Como Executar

### Usando Maven (Recomendado)

```bash
# Compilar o projeto
mvn clean compile

# Executar a aplicação
mvn javafx:run
```

### Usando IDE

1. Importe o projeto como projeto Maven
2. Configure o JDK 11+
3. Execute a classe `Main.java`

### Compilar JAR

```bash
mvn clean package
java -jar target/sigeaj-1.0-SNAPSHOT.jar
```

## Credenciais Padrão

Após executar o script SQL, você pode fazer login com:

- **Email**: admin@ufrn.edu.br
- **Senha**: admin123
- **Email**: operador@ufrn.edu.br
- **Senha**: operador123

## Funcionalidades

### 1. Autenticação
- Login com email e senha
- Validação de credenciais no banco
- Perfis: ADMIN e OPERADOR

### 2. Gerenciamento de Setores Produtivos
- Cadastrar setores (Piscicultura, Avicultura, etc)
- Editar setores existentes
- Excluir setores (com CASCADE para atividades e registros)
- Listar todos os setores

### 3. Gerenciamento de Atividades
- Registrar atividades realizadas nos setores
- Tipo: Alimentação, Limpeza, Vacinação, etc
- Data de execução
- Associação com setor via ComboBox

### 4. Gerenciamento de Registros de Produção
- Registrar produção dos setores
- Produto, quantidade e unidade
- Data de registro
- Validação de quantidades (BigDecimal)

## Validações Implementadas

### UsuarioService
- Email único
- Formato de email válido
- Senha mínimo 6 caracteres
- Campos obrigatórios

### SetorProdutivoService
- Nome único
- Nome e descrição obrigatórios
- Validação de duplicidade

### AtividadeService
- Setor deve existir
- Data não pode ser muito antiga (>5 anos)
- Data não pode ser muito futura (>6 meses)
- Campos obrigatórios

### RegistroProducaoService
- Quantidade deve ser > 0
- Data não pode ser futura
- Setor deve existir
- Campos obrigatórios

## Estrutura de Banco de Dados

### Tabelas

- **usuario**: Usuários do sistema
- **setor_produtivo**: Setores da EAJ
- **atividade**: Atividades realizadas (FK: setor_id)
- **registro_producao**: Registros de produção (FK: setor_id)

### Relacionamentos

- `atividade.setor_id` → `setor_produtivo.id` (CASCADE)
- `registro_producao.setor_id` → `setor_produtivo.id` (CASCADE)

## Boas Práticas Aplicadas

1. **Separação de responsabilidades**: MVC bem definido
2. **Try-with-resources**: Fechamento automático de conexões
3. **PreparedStatement**: Prevenção de SQL Injection
4. **Validações na camada Service**: Não no Controller
5. **Mensagens de erro amigáveis**: Alerts informativos
6. **Comentários em português**: Documentação clara
7. **Nomenclatura consistente**: Padrão Java

## Autor

Projeto desenvolvido para fins acadêmicos - EAJ/UFRN
