package lombok;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Add Mybatis's @Param to interface's parameter.
 *
 * Created by Liu DongMiao &lt;liudongmiao@gmail.com&gt; on 2016/06/06.
 *
 * @author thom
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface MybatisParam {

}
