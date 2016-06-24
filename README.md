In [mybatis](http://www.mybatis.org/mybatis-3)'s Mapper, there are two ways to specify param:

- `@Param`

  ```java
  import org.apache.ibatis.annotations.Param;

  public interface Mapper {

      int select(@Param("x") int x, @Param("y") int y);

  }
  ```

- Java 8's `-parameters`

  However, not all project can use Java 8, and it requires extra option `-parameters`.


With `lombok.MybatisParam`, it can be simple:

```java
@lombok.MybatisParam
public interface Mapper {

    int count(int x, int y);

}
```

Use it in `maven`: (you need to specify mybatis dependency)

```xml
<dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <!-- lombok 1.16.X use shadow class loader, which prevents load lombok extension -->
        <version>1.14.8</version>
        <scope>provided</scope>
</dependency>
<dependency>
        <groupId>me.piebridge</groupId>
        <artifactId>lombok-mybatis-param</artifactId>
        <version>0.1</version>
        <scope>provided</scope>
</dependency>
```

For `eclipse`, please compile it with profile `eclipse`, then run `java -jar target/lombok-*-eclipse.jar`:

```shell
mvn clean package -P eclipse
java -jar target/lombok-*-eclipse.jar
```


