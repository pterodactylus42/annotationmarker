package de.gbtec.eclipse.markers;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class JavaSourceParser {
    private static final Logger logger = LogManager.getLogger();

    private IFile srcFile = null;

    public JavaSourceParser(IFile srcFile) {
        this.srcFile = srcFile;
        astParser();
    }

    void astParser() {
        ICompilationUnit icu = (ICompilationUnit) JavaCore.create(srcFile);
        ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setSource(icu);
        parser.setBindingsRecovery(true);
        parser.setResolveBindings(true);
        parser.setStatementsRecovery(true);

        ASTNode createAST = parser.createAST(null);
        CompilationUnit javaSource = (CompilationUnit) createAST;

        IProblem[] problems = javaSource.getProblems();

        for (IProblem problem : problems) {
            logger.debug(problem.toString());
        }

        if (javaSource.getAST().hasBindingsRecovery()) {
            logger.warn("Binding activated.");
        }
        
        javaSource.accept(new ASTVisitor() {
            public boolean visit(MethodInvocation methodInvocation) {
                asdsf(methodInvocation);
                return false;
            }
        });
    }

    private void asdsf(MethodInvocation node) {
        System.out.println(node.getName());
        if(true) return;
        IMethodBinding binding = (IMethodBinding) node.getName().resolveBinding();
        if (binding != null) {
            IJavaElement javaElement = binding.getJavaElement();
            System.out.println(binding.getDefaultValue());
            if(javaElement != null) {
                
                ICompilationUnit unit = (ICompilationUnit) javaElement.getAncestor(IJavaElement.COMPILATION_UNIT);
                
                if (unit != null) {
                    ASTParser parser = ASTParser.newParser(AST.JLS8);
                    parser.setKind(ASTParser.K_COMPILATION_UNIT);
                    parser.setSource(unit);
                    
                    parser.setResolveBindings(true);
                    CompilationUnit cu = (CompilationUnit) parser.createAST(null);
                    MethodDeclaration decl = (MethodDeclaration) cu.findDeclaringNode(binding.getKey());
                    IMethodBinding resolveBinding = decl.resolveBinding();
                    
                    CompilationUnit javaSource = (CompilationUnit) node.getRoot();
                    
                    int lineNumber = javaSource.getLineNumber(node.getStartPosition());
                    int startPos = 0;
                    int endPos = javaSource.getColumnNumber(node.getStartPosition());
                    
                    System.out.println("method declaration: " + decl.getName().getFullyQualifiedName());
                    System.out.println("line: " + lineNumber);
                    System.out.println("pos: " + startPos + "/" + endPos);
                    
                    IAnnotationBinding[] annotations = resolveBinding.getAnnotations();
                    if (annotations.length > 0 || false) {
                        System.out.println("annotations:");
                        
                        for (int i = 0; i < annotations.length; i++) {
                            IAnnotationBinding iAnnotationBinding = annotations[i];
                            String nameOfAnnotation = iAnnotationBinding.getName();
                            System.out.println(" - " + nameOfAnnotation);
                            Arrays.stream(iAnnotationBinding.getAllMemberValuePairs()).forEach(a -> {
                                System.out.println("  -> " + a.getName() + ":" + a.getValue());
                            });
                        }
                    }
                }
            }
        }
    }
}
