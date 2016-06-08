package lombok.javac.handlers;

import org.apache.ibatis.annotations.Param;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;

import lombok.MybatisParam;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.TypeLibrary;
import lombok.core.TypeResolver;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;

/**
 * implementation for javac.
 *
 * Created by Liu DongMiao &lt;liudongmiao@gmail.com&gt; on 2016/06/06.
 *
 * @author thom
 */
public class HandleMybatisParam extends JavacAnnotationHandler<MybatisParam> {

    private static final String PARAM = Param.class.getName();

    @Override
    public void handle(AnnotationValues<MybatisParam> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) {
        handle(annotationNode);
    }

    private static void handle(JavacNode annotationNode) {
        JavacNode node = annotationNode.up();
        if (node != null) {
            for (JavacNode child : node.down()) {
                if (AST.Kind.METHOD.equals(child.getKind())) {
                    handleMethod(child);
                }
            }
        }
    }

    private static void handleMethod(JavacNode method) {
        JavacTreeMaker maker = method.getTreeMaker();
        JCTree.JCExpression type = JavacHandlerUtil.chainDotsString(method, PARAM);
        for (JCTree.JCVariableDecl param : ((JCTree.JCMethodDecl) method.get()).params) {
            if (!hasParam(method, param.mods.annotations)) {
                List<JCTree.JCExpression> args = List.<JCTree.JCExpression>of(maker.Literal(param.name.toString()));
                JCTree.JCAnnotation annotation = maker.Annotation(type, args);
                param.mods.annotations = param.mods.annotations.append(annotation);
            }
        }
    }

    private static boolean hasParam(JavacNode method, List<JCTree.JCAnnotation> annotations) {
        if (annotations == null || annotations.isEmpty()) {
            return false;
        }
        TypeLibrary typeLibrary = TypeLibrary.createLibraryForSingleType(PARAM);
        TypeResolver typeResolver = new TypeResolver(method.getImportList());
        for (JCTree.JCAnnotation annotation : annotations) {
            String type = annotation.annotationType.toString();
            String fqn = typeResolver.typeRefToFullyQualifiedName(method, typeLibrary, type);
            if (PARAM.equals(fqn)) {
                return true;
            }
        }
        return false;
    }

}
