## Instalação

- [Spring Boot para VS Code](https://code.visualstudio.com/docs/java/java-spring-boot)

## Injeção de Dependência

- Incluir as dependências do projeto (`pom.xml`)
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

