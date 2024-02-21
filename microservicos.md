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

### Obtendo Parâmetros

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

### Retornando JSON

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

### Processando POST

    ```java
    @PostMapping("/cadastrar")
    public ResponseEntity<Integer> cadastrar(@RequestBody AlunoBean aluno) {
        logger.debug("Cadastrando aluno: " + aluno.getNome());
        return new ResponseEntity<Integer>(Integer.parseInt("1"), HttpStatus.OK);
    }
    ```
    