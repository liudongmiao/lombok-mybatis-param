```java
@lombok.MybatisParam
public interface Mapper {

    int count(int x, int y);

}
```

is the same with:

```java
import org.apache.ibatis.annotations.Param;

public interface Mapper {

    int count(@Param("x") int x, @Param("y") int y);

}
```

