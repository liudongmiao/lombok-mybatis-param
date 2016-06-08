package lombok.eclipse.handlers;

import org.apache.ibatis.annotations.Param;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.SingleMemberAnnotation;
import org.eclipse.jdt.internal.compiler.ast.StringLiteral;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;

import lombok.MybatisParam;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.core.TypeLibrary;
import lombok.core.TypeResolver;
import lombok.eclipse.Eclipse;
import lombok.eclipse.EclipseAnnotationHandler;
import lombok.eclipse.EclipseNode;

/**
 * implementation for eclipse.
 *
 * Created by Liu DongMiao &lt;liudongmiao@gmail.com&gt; on 2016/06/06.
 *
 * @author thom
 */
public class HandleMybatisParam extends EclipseAnnotationHandler<MybatisParam> {

    private static final String PARAM = Param.class.getName();

    @Override
    public void handle(AnnotationValues<MybatisParam> annotation, org.eclipse.jdt.internal.compiler.ast.Annotation ast, EclipseNode annotationNode) {
        handle(annotationNode);
    }

    private static void handle(EclipseNode annotationNode) {
        EclipseNode node = annotationNode.up();
        if (node != null) {
            for (EclipseNode child : node.down()) {
                if (AST.Kind.METHOD.equals(child.getKind())) {
                    handleMethod(child);
                }
            }
        }
    }

    private static void handleMethod(EclipseNode method) {
        char[][] name = Eclipse.fromQualifiedName(PARAM);
        TypeReference type = new QualifiedTypeReference(name, new long[name.length]);
        for (Argument argument : ((AbstractMethodDeclaration) method.get()).arguments) {
            if (!hasParam(method, argument.annotations)) {
                SingleMemberAnnotation annotation = new SingleMemberAnnotation(type, 0);
                annotation.memberValue = new StringLiteral(argument.name, 0, 0, 0);
                argument.annotations = copyAnnotations(argument.annotations, annotation);
            }
        }
    }

    private static Annotation[] copyAnnotations(Annotation[] annotations, SingleMemberAnnotation annotation) {
        if (annotations == null || annotations.length == 0) {
            return new Annotation[] {annotation};
        } else {
            int length = annotations.length;
            Annotation[] newAnnotations = new Annotation[length + 1];
            System.arraycopy(annotations, 0, newAnnotations, 0, length);
            newAnnotations[length] = annotation;
            return newAnnotations;
        }
    }

    private static boolean hasParam(EclipseNode method, Annotation[] annotations) {
        if (annotations == null || annotations.length == 0) {
            return false;
        }
        TypeLibrary typeLibrary = TypeLibrary.createLibraryForSingleType(PARAM);
        TypeResolver typeResolver = new TypeResolver(method.getImportList());
        for (Annotation annotation : annotations) {
            String type = Eclipse.toQualifiedName(annotation.type.getTypeName());
            String fqn = typeResolver.typeRefToFullyQualifiedName(method, typeLibrary, type);
            if (PARAM.equals(fqn)) {
                return true;
            }
        }
        return false;
    }

}
