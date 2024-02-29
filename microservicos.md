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
    ```properties
    spring.datasource.url=jdbc:h2:mem:testdb
    spring.datasource.driverClassName=org.h2.Driver
    spring.datasource.username=sa
    spring.datasource.password=password
    spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
    spring.h2.console.enabled=true
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
- Utilizar o `RestTemplate`
    ```java
    RestTemplate restTemplate = new RestTemplate();
    final String url = "http://localhost:8080/aluno/1";
    ResponseEntity<AlunoBean> response = restTemplate.getForEntity(url, AlunoBean.class);
    System.out.println(response.getBody());
    ```
## Exercício
- Implementar um serviço CRUD para turma com os seguintes requisitos:
- Turma possui um id numérico sequencial, um nome de identificação e o total de alunos;
- Criar um endpoint com método POST para criar uma nova turma;
- Criar um endpoint com um método PUT que permita receber um número de alunos para incrementar o número atual de alunos na turma. Exemplo:
    - PUT – `http://localhost:8080/T100/10` – incrementa em 10 o total de alunos na turma T100
- Criar um endpoint com um método DELETE que permita receber um número de alunos para diminuir o número atual de alunos na turma. Exemplo:
    - DELETE – `http://localhost:8080/T100/5` – diminui em 5 o total de alunos na turma T100
- Criar um endpoint para retornar o total de alunos matriculados em todas as turmas
