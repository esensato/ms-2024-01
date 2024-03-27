## Instalação

- [Spring Boot para VS Code](https://code.visualstudio.com/docs/java/java-spring-boot)

## Injeção de Dependência

- Incluir as dependências do projeto (`pom.xml`) - opcional se estiver utilizando *Spring Boot*
    ```xml
    <dependency>
    	<groupId>org.springframework</groupId>
    	<artifactId>spring-beans</artifactId>
    	<version>4.3.3.RELEASE</version>
    </dependency>
    <dependency>
    	<groupId>org.springframework</groupId>
    	<artifactId>spring-context</artifactId>
    	<version>4.3.3.RELEASE</version>
    </dependency>
    <dependency>
    	<groupId>org.springframework</groupId>
    	<artifactId>spring-core</artifactId>
    	<version>4.3.3.RELEASE</version>
    </dependency>
    ```
- Criar uma interface `Operacao`

    ```java
    package beans;
    
    public interface Operacao {
        float executar(float v1, float v2);
    }
    ```
- Criar uma classe `Somar`
    ```java
    package beans;
    
    public class Somar implements Operacao {
    
        @Override
        public float executar(float v1, float v2) {
            return v1 + v2;
        }
    
    }
    ```
- Criar uma classe `Multiplicar`
    ```java
    package beans;
    
    public class Multiplicar implements Operacao {
    
    @Override
    public float executar(float v1, float v2) {
      return v1 * v2;
    }
    
    }
    ```
- Criar um arquivo `beans.xml` (dentro da pasta `/src/main/java`)

```xml
<?xml version="1.0" encoding="UTF-8"?>

    <beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:p="http://www.springframework.org/schema/p"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation=
    "http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context-3.2.xsd"
    xmlns:context="http://www.springframework.org/schema/context">

    <bean id="somar" class="beans.Somar" />
    <bean id="multiplicar" class="beans.Multiplicar" />

</beans>
```

- Class principal
```java
public class DemoApplication {

@SuppressWarnings("resource")
public static void main(String[] args) {

 ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
 Operacao op1 = (Operacao) ctx.getBean("somar");
 Operacao op2 = (Operacao) ctx.getBean("multiplicar");

 System.out.println(op1.executar(10.0f, 20.0f));
 System.out.println(op2.executar(10.0f, 20.0f));

}
```

## Autowire

- Classe `Calculadora`
    ```java
    public class Calculadora {
    
     @Autowired
     @Qualifier("operacao")
     private Operacao operacao;
    
     public void executar() {
      System.out.println(operacao.executar(10, 20));
     }
    
    }
    ```
- Configurar o *autowire* no `beans.xml`

    ```xml
    <?xml version="1.0" encoding="UTF-8"?>

    <beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:p="http://www.springframework.org/schema/p"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation=
    "http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context-3.2.xsd"
    xmlns:context="http://www.springframework.org/schema/context">

        <context:annotation-config />
        
        <bean id="operacao" class="beans.Multiplicar" />
        <bean id="calculadora" class="beans.Calculadora" />
        <bean id="somar" class="beans.Somar" />
        <bean id="multiplicar" class="beans.Multiplicar" />

    </beans>
    ```
- Testar
    ```java
    Calculadora calc = (Calculadora) ctx.getBean("calculadora");
    calc.executar();
    ```
## Injeção por Propriedades

- Alterar a classe `Calculadora`
    ```java
    public class Calculadora {
    
     @Autowired
     @Qualifier("operacao")
     private Operacao operacao;
    
     private int n1;
     private int n2;
    
     public void executar() {
      System.out.println(operacao.executar(n1, n2));
     }
    ```
- Alterar o `beans.xml`
    ```xml
    <bean id="calculadora" class="beans.Calculadora">
     <property name="n1" value="100" />
     <property name="n2" value="100" />
     <property name="operacao" ref="somar" />
    </bean>
    ```
## Auto-Scan

- Considerar o `beans.xml`
    ```xml
    <context:annotation-config />
    <context:component-scan base-package="beans.Calculadora" />
    ```
- Classe `Calculadora`
    ```java
    @Service
    public class Calculadora {
    
        @Autowired()
        private Operacao operacao;
    
        public float executar(float v1, float v2) {
            return operacao.executar(v1, v2);
        }
    }
    ```
- Classe `Somar`
    ```java
    @Service
    public class Somar implements Operacao {
    
        @Override
        public float executar(float v1, float v2) {
            return v1 + v2;
        }
    
    }
    ```
- Testar
    ```java
    ApplicationContext ctx = new ClassPathXmlApplicationContext("beans2.xml");
    Calculadora calc = ctx.getBean(Calculadora.class);
    System.out.println(calc.executar(10, 20));
    ```
## Criando um End-Point
- Adicionar as dependências abaixo no `pom.xml`
    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>
    ```
- Criar um *endpoint* para uma requisição *GET*
    ```java
    import org.springframework.web.bind.annotation.RestController;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    
    @RestController
    @RequestMapping("/aluno")
    public class Aluno {
        private static final Logger logger = LoggerFactory.getLogger(Aluno.class);
    
        @GetMapping("/obter")
        public String getAluno() {
            logger.debug("Retornando aluno Joao...");
            return "Joao";
        }
    
    }
    ```
- Utilizando `ResponseEntity`
    ```java
    @GetMapping("/obter")
    public ResponseEntity<String> getAluno() {
      return new ResponseEntity<String>("Joao", HttpStatus.OK);
    }
    ```
- [Códigos Status HTTP](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status)

## Trabalhando com Propriedades
- Editar o arquivo `application.properties`
- Por exemplo, definir o nível de *log*

    ```properties
    logging.level.org.springframework.web: DEBUG
    logging.level.nome.pacote: DEBUG
    ```
- Pode-se criar qualquer tipo de propriedade
- Para ler uma propriedade basta utilizar a injeção de dependência com `@Value`
    ```java
    @Value("${mensagem}")
    private String mensagem;
    ```
- Lê o valor da propriedade `mensagem` definida no arquivo `application.properties`
## Obtendo Parâmetros

- Na própria URL

    ```java
    @GetMapping("/obter/{id}")
    public String getAlunoPorNome(@PathVariable String id) {
      logger.debug("Retornando aluno com id = " + id);
      return "Encontrado aluno: " + id;
    }
    ```
- Via parâmetros

    ```java
    @GetMapping("/obter")
    public String getAlunoPorNome(@RequestParam(name="id", required = false, defaultValue = "0") String id) {
      logger.debug("Retornando aluno com id = " + id);
      return "Encontrado aluno: " + id;
    }
    ```

## Retornando JSON

- Criar uma classe para encapsular os atributos
    ```java
    public class AlunoBean {
    
    private String id;
    private String nome;
    
    @JsonIgnore
    private String senha;

    public AlunoBean(String id, String nome) {
      super();
      this.id = id;
      this.nome = nome;
    }
    // criar os gets / sets
    
    }
    ```
- Para retornar *XML*
    ```xml
    <dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-xml</artifactId>
    </dependency>
    ```
- Alterar o parâmetro do *header* da requisição `Accept: application/xml`
## Processando POST
- Processando requisições do tipo *POST*
    ```java
    @PostMapping("/cadastrar")
    public ResponseEntity<Integer> cadastrar(@RequestBody AlunoBean aluno) {
        logger.debug("Cadastrando aluno: " + aluno.getNome());
        return new ResponseEntity<Integer>(Integer.parseInt("1"), HttpStatus.OK);
    }
    ```
## Tratamento de Erros
- Criar uma classe para encapsular o tipo de exceção
    ```java
    public class AlunoNaoLocalizadoException extends Throwable {
    }
    ```
- Definir uma classe para lidar com as exceções
    ```java
    @ControllerAdvice
    public class ApplicationExceptionHandler {
        @ResponseStatus(
                value = HttpStatus.NOT_FOUND,
                reason = "Aluno não localizado!")
        @ExceptionHandler(AlunoNaoLocalizadoException.class)
        public void handleException(AlunoNaoLocalizadoException e) {
    
        }
    
    }
    ```
- Lançar a exceção, quando aplicado
    ```java
    throw new AlunoNaoLocalizadoException();
    ```
## Validação de Dados
- Utilizar o `spring-boot-starter-validation`
    ```xml
    <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    ```
- [Referência](https://docs.oracle.com/javaee/7/api/javax/validation/constraints/package-summary.html)
- Exemplo
    ```java
    @PostMapping
    public ResponseEntity<AlunoBean> criar(@Valid @RequestBody AlunoBean aluno) {
    return new ResponseEntity<AlunoBean>(aluno, HttpStatus.OK);
    }
    ```
- Na classe `AlunoBean` definir as restrições
    ```java
    @Size(min = 10, message="Nome deve conter no minimo 10 caracteres")
    private String nome;
    ```
- Definir o tratamento de erro no *Controller*
    ```java
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<String, String>();
     
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage()));
         
        return errors;
    }
    ```
## Persistência
- Utilizar o `spring-boot-starter-data-jpa`
    ```xml
    <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <dependency>
     <groupId>com.h2database</groupId>
     <artifactId>h2</artifactId>
     <scope>runtime</scope>
    </dependency>
    ```
- [Console H2](http://localhost:8080/h2-console)
- Configurar o **H2**
    ```javascript
    spring.datasource.url=jdbc:h2:mem:testdb
    spring.datasource.driverClassName=org.h2.Driver
    spring.datasource.username=sa
    spring.datasource.password=password
    spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
    spring.h2.console.enabled=true
    spring.jpa.defer-datasource-initialization=true
    ```
- Criar um arquivo `data.sql` dentro da pasta `resources`
    ```sql
    DROP TABLE IF EXISTS tab_aluno;
     
    CREATE TABLE tab_aluno (
      id_aluno INT AUTO_INCREMENT PRIMARY KEY,
      nome VARCHAR(30) NOT NULL,
      turma VARCHAR(10) NOT NULL,
      curso VARCHAR(50) DEFAULT NULL
    );
    ```
- Definir o *Bean* para a persistência
    ```java
    @Entity(name="TAB_ALUNO")
    public class AlunoEntity {
    @Id
    @Column(name = "ID_ALUNO")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private String nome;
    private String turma;
    private String curso; 
    // getters e setters
    ```
- Criar o repositório
    ```java
    @Repository
    public interface AlunoRepository extends CrudRepository<AlunoEntity, Integer>{
    }
    ```
- Métodos disponíveis de `CrudRepository`
    - `save(entidade)` – persiste uma entidade (insert / update)
    - `deleteById(id)` – remove um registro por meio do id
    - `deleteAll()` – remove todos os registros
    - `findAll()` – retorna todos os registros
    - `findById(id)` – localiza um registro por meio do id
    - `count()` – retorna o número total de registros
    - `existis(id)` – verifica se um registro existe com base em seu id
- Utilizando no *controller*
    ```java
    @RestController
    @RequestMapping(value = "aluno")
    public class AlunoService {
    
    @Autowired
    private AlunoRepository alunoRepo = null;
    
    @PostMapping(value="/cadastrar")
    public AlunoEntity cadastrar(@RequestBody AlunoEntity aluno) {
      return alunoRepo.save(aluno);
    }
    
    }
    ```
- Retornando uma lista
    ```java
    @GetMapping(value="/obter")
    public List<AlunoEntity> getAluno() {
    List<AlunoEntity> ret = new ArrayList<AlunoEntity>();
     for (AlunoEntity aluno:alunoRepo.findAll()) {
      ret.add(aluno);
     }
      return ret;
    
    }
    ```
- Outra opção
    ```java
    @GetMapping("/obter")
    public ResponseEntity<Iterable<AlunoBean>> obterTodos() {
    return new ResponseEntity<Iterable<AlunoBean>>(dao.findAll(), HttpStatus.OK);
    }
    ```
## Consultas Derivadas
- [Referência](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods)
- É possível criar consultas simples porém de forma muito eficiente utilizando-se consultas derivadas, isto é, que seguem um determinado padrão de nomenclatura envolvendo nomes de atributos da entidade
- Basta declarar a assinatura do método desejado na interface `@Repository`
- Por exemplo, `findByNome (String nome)` efetua uma busca utilizando como chave o atributo nome
- Outros exemplos:
    - `findByNomeIsNot(String nome)` – nomes diferentes de...
    - `findByTurmaIsNull()` – alunos onde o atributo turma seja nulo
    - `findByNomeContaining(String prefixo)` – alunos cujo nome contenha o prefixo informado
    - `findByNomeLike(String expressao)` – efetua um like no nome, por exemplo, %Joao%
## Consultas Personalizadas
- Além das consultas derivadas também é possível criar consultas personalizadas informando o código SQL diretamente
    ```java
    @Query("select a from GASTO_BEAN a where a.username = ?1")
    List<AlunoEntity> alunosPorTurma(String username);
    ```
- Exemplos:
    ```java
    @Repository
    public interface AlunoDAO extends CrudRepository<AlunoBean, Integer> {
    
        // sempre inicia com findBy...
        // incluir nome do atributo
        Iterable<AlunoBean> findByCurso(String curso);
    
        // SELECT * FROM TAB_CURSO WHERE CURSO = ? AND TURMA = ?
        Iterable<AlunoBean> findByCursoAndTurma(String curso, String turma);
    
        // SELECT * FROM TAB_ALUNO WHERE NOME LIKE ?
        Iterable<AlunoBean> findByNomeLike(String nome);
    
        @Query("select a.id from TAB_ALUNO a")
        Iterable<Integer> minhaConsulta();
    
    }
    ```
## Data Rest
- Permite criar endpoints diretamente do repositório sem a necessidade de um *controller*
    ```xml
    <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-rest</artifactId>
    </dependency>
    ```
- Criar apenas a interface anotada como `@Repository` e estendendo `CrudRepository`
- Adicionar uma configuração `spring.data.rest.basePath=/api`
- Para testar: `http://localhost:8080/api`
- [Referência](https://docs.spring.io/spring-data/rest/docs/current-SNAPSHOT/reference/html/#reference)
- Utilizar a anotação `@RestResource`
- Para incluir o atribudo `id` nas respostas das APIs mapeadas pelo *Data Rest*
    ```java
    @Component
    public class DataRestConfig implements RepositoryRestConfigurer {
    
        @Override
        public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
            // Incluir as classes Bean que terão seus ids expostos
            config.exposeIdsFor(MatriculaBean.class, DisciplinaBean.class);
        }
    
    }
    ```
- Para maiores configurações [Vide](https://docs.spring.io/spring-data/rest/docs/current/api/org/springframework/data/rest/core/config/RepositoryRestConfiguration.html)
## Aplicação Console
- Para executar uma aplicação Spring Boot no console
    ```java
    @SpringBootApplication
    public class ConsoleSpring implements CommandLineRunner {
    
        public static void main(String[] args) {
            SpringApplication.run(ConsoleSpring.class, args);
        }
    
        @Override
        public void run(String... args) throws Exception {
    
        }
    
    }
    ```
## Efetuando Requisições HTTP
- Alterar a dependência no `pom.xml` para não incluir o *tomcat* como servidor:
    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <exclusions>
            <exclusion>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-tomcat</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    ```
- Utilizar o `RestClient`
    ```java
    RestClient restClient = RestClient.create();

    ResponseEntity<String> result = restClient.get()
            .uri("http://localhost:8080/aluno/1")
            .retrieve()
            .toEntity(String.class);
    System.out.println("Status: " + result.getStatusCode());
    System.out.println("Headers: " + result.getHeaders());
    System.out.println("Conteudo: " + result.getBody());
    ```
- Encapsular o resultado em um objeto da classe `AlunoBean`:
    ```java
    RestClient restClient = RestClient.create();
    
    ResponseEntity<AlunoBean> result = restClient.get()
            .uri("http://localhost:8080/aluno/101")
            .retrieve()
            .toEntity(AlunoBean.class);
    System.out.println("Status: " + result.getStatusCode());
    System.out.println("Headers: " + result.getHeaders());
    System.out.println("Conteudo: " + result.getBody().getNome());
    ```
- Efetuando um *POST* (sem retorno esperado no corpo da resposta)
    ```java
    AlunoBean aluno = new AlunoBean();
    ResponseEntity<Void> response = restClient.post()
      .uri("ttp://localhost:8080/aluno")
      .contentType(APPLICATION_JSON)
      .body(aluno)
      .retrieve()
      .toBodilessEntity();
    ```
# Trabalhando com JSON
- Incluir nas dependências do projeto `pom.xml`
    ```xml
    <dependency>
        <groupId>org.json</groupId>
        <artifactId>json</artifactId>
        <version>20240303</version>
    </dependency>
    ```
- Principais classes e métodos:
    - `JSONObject`: representa um objeto JSON
    - `JSONArray`: representa um array JSON
    - `length()`: retorna o total de objetos contidos no `JSONArray`
    - `getJSONObject()`, `getJSONArray()`, `getString()`, etc...: retornam o valor de um atributo de um objeto JSON
# Mapeando Atributos JSON para Objetos
- Utilizar o `ObjectMapper`:
    ```java
    ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    ```
- Exemplo:
    ```java
    DisciplinaBean disciplina = objectMapper.readValue(disciplinas.getJSONObject(i).toString(), DisciplinaBean.class);
    ```
## Exercício
- Implementar um serviço CRUD para disciplina com os seguintes requisitos (utilizar *Data Rest*):
    - Disciplina possui um id numérico sequencial, um nome e carga horária;
    - Criar um endpoint com método POST para criar uma nova disciplina (POST `http://localhost:8080/api/disciplina`);
    - Criar um endpoint GET para retornar os dados de uma disciplina por id (GET `http://localhost:8080/api/disciplina/{id}`)
    - Criar um endpoint GET para retornar uma lista contendo todas as disciplinas (GET `http://localhost:8080/api/disciplina`);
    - Criar um endpoint com um método PUT que permita alterar o nome e a carga horária de uma disciplina (PUT `http://localhost:8080/api/disciplina`);
    - Criar um endpoint com um método DELETE que permita excluir uma disciplina (DELETE `http://localhost:8080/api/disciplina/{id}`);;
- Criar um serviço para controle de matrícula:
    - Matrícula possui id do aluno, id da disciplina e um status (ATIVO, CANCELADO)
    - Definir um endpoint para associar o id de um aluno ao id de uma disciplina (POST `http://localhost:8080/matricula/{idAluno}/{idDisciplina}`);
    - Definir um endpoint para cancelar a matrícula do aluno de uma disciplina alterando o status (DELETE `http://localhost:8080/matricula/{idAluno}/{idDisciplina}`);
- Criar um serviço orquestrador chamado faculdade para:
    - Retornar todas as disciplinas ativas de um determinado aluno (GET `http://localhost:8080/faculdade/disciplinas/{idAluno}`);
    - Retornar a carga horária total de um aluno (GET `http://localhost:8080/faculdade/carga/{idAluno}`);
    - Remover uma disciplina (somente permitir excluir uma disciplina que não possua alunos associados) (DELETE `http://localhost:8080/faculdade/disciplinas/{idDisciplina}`);

## Documentação Endpoints
- Padrão [OpenAPI](https://spec.openapis.org/oas/latest.html)
- Exemplo [Strava API](https://developers.strava.com/swagger/swagger.json)
- Utilizar o [Swagger](https://swagger.io/) para visualizar a documentação no padrão *OpenAPI*
- Adicionar a dependência:
    ```xml
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.2.0</version>
    </dependency>
    ```
- Visualizando os *endpoints*:
    - Formato JSON: http://localhost:8080/v3/api-docs
    - Formato YAML: http://localhost:8080/v3/api-docs.yaml
    - Interface HTML (Swagger): http://localhost:8080/swagger-ui/index.html
- Anotações para documentação:
    - End points:
    ```java
    @OpenAPIDefinition(info=@Info(title="Controle de Alunos"))
    ```
    - Operações: 
    ```java
    @Operation(summary = "Lista alunos", description = "Obtem a lista de todos os alunos", tags = { "alunos" })
    @Parameters(value={@Parameter(name = "id")})
    ```
    - Respostas:
    ```java
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Encontrou o aluno", content = { @Content(mediaType = "application/json",
    schema = @Schema(implementation = AlunoBean.class)) }), @ApiResponse(responseCode = "400", description = "Id do aluno inválido",
    content = @Content),
    @ApiResponse(responseCode = "404", description = "Aluno não localizado",
    content = @Content) })
    ```
- Documentando as entidades
    ```java
    @Schema(name="Aluno", description = "Representa um aluno")
    public class AlunoBeanV1 {
    @Schema(name = "matricula", description = "Número de matrícula", required = true, example = "M12345")
    private String matricula;
    }
    ```
## Geração Clientes
- Em nodejs utilizar o pacote[openapi-client-axios](https://www.npmjs.com/package/openapi-client-axios)
- Criar um projeto *nodejs*
```bash
mkdir cliente-node
cd cliente-node
npm init -y
```
- Importar o `openape-client-axios`
```bash
npm i --save openapi-client-axios
```
- Gerar o cliente
```bash
npx openapicmd typegen http://localhost:8080/v3/api-docs > openapi.d.ts
```
- Efetuar as chamadas aos *endpoints*
    ```javascript
    const OpenAPIClientAxios = require("openapi-client-axios").default;
    
    const api = new OpenAPIClientAxios({
        definition: "http://localhost:8080/v3/api-docs",
    });
    api.init()
        .then((client) =>
            client.getAluno(101)
        )
        .then((res) => console.log("Resultado:", res.data));
    ```
## Actuator
- Permite verificar a "saúde" (health) de um serviço, por exemplo, se ele está em execução, sua disponibilidade, configuração, etc...
- Incluir a seguinte dependência ao projeto:
    ```xml
    <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    ```
- A porta do actuator pode ser alterada por meio da propriedade `management.server.port` (por exemplo, `8081`)
- Acessar a URL `http://localhost:8081/actuator` ou `http://localhost:8081/actuator/health`

# Health Check Personalizado
- Um health check personalizado permite especificar quando um determinado serviço está acessível
    ```java
    @Component
    public class AlunoMonitor implements HealthIndicator {
    
        @Override
        public Health health() {
            return Health.down().build();
        }
    
    }
    ```
# Métricas
- Para habilitar mais *endpoints* alterar `management.endpoints.web.exposure.include=*`
- Exemplo de uma métrica persinalizada:
    ```java
    @Component
    public class MetricaAluno {
        MetricaAluno(MeterRegistry registry) {
            registry.counter("total.alunos", Tags.of("teste", "10"));
        }
    }
    ```
# Informações
- Algumas configurações podem ser habilitadas:
    ```java
    management.info.java.enabled=true
    info.app.name=@project.name@
    info.app.description=@project.description@
    info.app.version=@project.version@
    info.app.encoding=@project.build.sourceEncoding@
    info.app.java.version=@java.version@
    ```
# Outros End Points
- Por meio do actuator é também possível terminar um serviço (propriedade `management.endpoint.shutdown.enabled=true`)
# Spring Admin Server
- É uma interface web para administração das aplicações *Spring Boot*
- Criar um novo projeto *Sprint Boot* e adicionar as dependências:
    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <dependency>
        <groupId>de.codecentric</groupId>
        <artifactId>spring-boot-admin-starter-server</artifactId>
        <version>3.1.5</version>
    </dependency>
    ```
- Especificamente para o **MacOS**
    ```xml
    <dependency>
        <groupId>io.netty</groupId>
        <artifactId>netty-all</artifactId>
    </dependency>
    ```
- Especificar a porta na qual o *Admin Server* irá executar: `server.port=8082`
- Na classe principal do projeto adicionar as seguintes anotações:
    ```java
    @EnableAdminServer
    @SpringBootApplication
    public class AdminApplication {
    
    	public static void main(String[] args) {
    		SpringApplication.run(AdminApplication.class, args);
    	}
    
    }
    ```
- Acessar a interface administrativa: `http://localhost:8082`
- Registrar as aplicações que serão monitoradas, adicionando a dependência:
    ```xml
    <dependency>
        <groupId>de.codecentric</groupId>
        <artifactId>spring-boot-admin-starter-client</artifactId>
        <version>3.1.5</version>
    </dependency>
    ```
- Apontar para o *Admin Server* com as propriedades `spring.boot.admin.client.url=http://localhost:8082` e `management.endpoint.health.show-details=always`
# Segurança Básica
- Para garantir um nível de segurança mínimo para os *endpoints* é possível ativar um *starter*
    ```xml
    <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    ```
- Observar após o início do serviço que é getada uma senha automaticamnte para acesso:

    `Using generated security password: 2cee0e0c-3e35-453d-ac8b-f3f040c849cf`

- Por padrão o usuário definido é *user*
- Ao tentar acessar qualquer *endpoint* sem fornecer as credenciais será gerado um erro HTTP 401 (Unauthorized)
- Para que seja autorizado o acesso deve-se definir no cabeçalho da requisição o tipo de Autorização *Basic*
- Caso seja necessário fornecer um usuário e senha padrão basta editar o `application.properties` e definir as seguintes propriedades:
    ```javascript
    spring.security.user.name = teste
    spring.security.user.password = 123
    ```
# RestClient
- Definir no Header da requisição:
    ```java
    String plainCreds = "teste:123";
    byte[] plainCredsBytes = plainCreds.getBytes();
    byte[] base64CredsBytes = Base64.getEncoder().encode(plainCredsBytes);
    String base64Creds = new String(base64CredsBytes);
    ResponseEntity<AlunoBean> response = restClient.get()
                    .uri("http://localhost:8080/aluno/{idAluno}", idAluno)
                    .header("Authorization", "Basic " + base64Creds)
                    .retrieve()
                    .toEntity(AlunoBean.class);
    ```
# Cross Site Request Forgery (CSRF)
- É uma proteção de segurança que impede que serviços hospedados em servidores distintos sejam acessados mutuamente
- Para desabilitar o *CSRF*:
    ```javascript
    @Configuration
    public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
        @Override
        protected void configure(HttpSecurity http) throws Exception {
           super.configure(http);
           http.csrf().disable();
        }
    }
    ```

