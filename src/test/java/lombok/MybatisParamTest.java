package lombok;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.ibatis.annotations.Param;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by Liu DongMiao &lt;liudongmiao@gmail.com&gt; on 2016/06/22.
 *
 * @author thom
 */
public class MybatisParamTest {

    @Test
    public void test() throws NoSuchMethodException {
        checkNull("selectX", 0, "x", int.class);
        check("selectY", 0, "y", int.class);
        check("selectZ", 0, "x", int.class, int.class, int.class);
        check("selectZ", 1, "y", int.class, int.class, int.class);
        check("selectZ", 2, "z", int.class, int.class, int.class);
    }

    private void check(String methodName, int index, String paramValue, Class<?>... parameterTypes) throws NoSuchMethodException {
        Param param = getParam(methodName, index, paramValue, parameterTypes);
        Assert.assertNotNull(param);
        Assert.assertEquals(param.value(), paramValue);
    }

    private void checkNull(String methodName, int index, String paramValue, Class<?>... parameterTypes) throws NoSuchMethodException {
        Param param = getParam(methodName, index, paramValue, parameterTypes);
        Assert.assertNull(param);
    }

    private Param getParam(String methodName, int index, String paramValue, Class<?>... parameterTypes) throws NoSuchMethodException {
        Method method = Mapper.class.getMethod(methodName, parameterTypes);
        Assert.assertEquals(method.getParameterTypes().length, parameterTypes.length);
        Annotation[] annotations = method.getParameterAnnotations()[index];
        return getAnnotation(annotations, Param.class);
    }

    private <T extends Annotation> T getAnnotation(Annotation[] annotations, Class<T> annotationClass) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(annotationClass)) {
                return annotationClass.cast(annotation);
            }
        }
        return null;
    }

    @MybatisParam
    private interface Mapper {
        int selectX(int x);
        int selectY(@Param("y") int x);
        int selectZ(int x, @Param("y") int y, @Deprecated int z);
    }

}
