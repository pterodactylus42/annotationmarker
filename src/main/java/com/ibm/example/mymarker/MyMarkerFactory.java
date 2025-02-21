package com.ibm.example.mymarker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.PlatformUI;

public class MyMarkerFactory {

    public static final String MARKER = "com.ibm.mymarkers.mymarker";

    /*
     * Creates a Marker
     */
    public static IMarker createMarker(IResource res) throws CoreException {
	IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(res.getFullPath());
	ICompilationUnit icu = (ICompilationUnit) JavaCore.create(file);

	ASTParser parser = ASTParser.newParser(AST.JLS8);
	parser.setSource(icu);
	parser.setResolveBindings(true);
	ASTNode createAST = parser.createAST(null);
	CompilationUnit javaSource = (CompilationUnit) createAST;
	javaSource.accept(new ASTVisitor() {
			@Override
			public boolean visit(MethodDeclaration methodDeclaration) {
				String methodName = methodDeclaration.getName().toString();
				System.out.println("ASTVisitor.visit " + methodName);
				asdsf(methodDeclaration);
				return false;
			}
	    });

	IMarker marker = null;
	// note: you use the id that is defined in your plugin.xml
	marker = res.createMarker("com.ibm.mymarkers.mymarker");
	marker.setAttribute("description", "this is one of my markers");
	marker.setAttribute(IMarker.MESSAGE, "My Marker");
	marker.setAttribute(IMarker.LINE_NUMBER, 16);
	marker.setAttribute(IMarker.CHAR_START, 25);
	marker.setAttribute(IMarker.CHAR_END, 36);
	return marker;
    }
    
    private static void asdsf(MethodDeclaration node) {
	IMethodBinding binding = (IMethodBinding) node.getName().resolveBinding();
	ICompilationUnit unit = (ICompilationUnit) binding.getJavaElement().getAncestor( IJavaElement.COMPILATION_UNIT );
	if ( unit == null ) {
	   // not available, external declaration
	}
	ASTParser parser = ASTParser.newParser( AST.JLS8 );
	parser.setKind( ASTParser.K_COMPILATION_UNIT );
	parser.setSource( unit );
	parser.setResolveBindings( true );
	CompilationUnit cu = (CompilationUnit) parser.createAST( null );
	MethodDeclaration decl = (MethodDeclaration)cu.findDeclaringNode( binding.getKey() );
	IMethodBinding resolveBinding = decl.resolveBinding();
	
	CompilationUnit javaSource = (CompilationUnit)node.getRoot();
	
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
		    Arrays.stream(
			    iAnnotationBinding.getAllMemberValuePairs())
			    .forEach(
			            a -> {
			                System.out.println("  -> "
			                        + a.getName() + ":"
			                        + a.getValue());
			            });
		}
	    }
	}
    
    private void asd() {
	List<AbstractTypeDeclaration> types = null;
	for (AbstractTypeDeclaration type : types) {
	    System.out.println(type.getName().getFullyQualifiedName());
	    if (type.getNodeType() == ASTNode.TYPE_DECLARATION) {
		// Class def found
		List<BodyDeclaration> bodies = type.bodyDeclarations();
		for (BodyDeclaration body : bodies) {
		    if (body.getNodeType() == ASTNode.METHOD_DECLARATION) {
			MethodDeclaration method = (MethodDeclaration) body;
			IMethodBinding resolveBinding = method.resolveBinding();

			System.out.println("method declaration: ");
			IAnnotationBinding[] annotations = resolveBinding
			        .getAnnotations();
			if (annotations.length > 0) {
			    System.out.println("annotations:");
			}
			for (int i = 0; i < annotations.length; i++) {
			    IAnnotationBinding iAnnotationBinding = annotations[i];
			    Arrays.stream(
				    iAnnotationBinding.getAllMemberValuePairs())
				    .forEach(
				            a -> {
				                System.out.println(" --> "
				                        + a.getName() + ":"
				                        + a.getValue());
				            });
			}
			System.out.println("name: "
			        + method.getName().getFullyQualifiedName());
			System.out.println("modifiers: "
			        + method.getModifiers());
			System.out.println("return type: "
			        + method.getReturnType2().toString());
			System.out.println("Annotations: "
			        + annotations.toString());
			System.out.println("");
		    }
		}
	    }
	}
    }

    /*
     * returns a list of a resources markers
     */
    public static List<IMarker> findMarkers(IResource resource) {
	try {
	    return Arrays.asList(resource.findMarkers(MARKER, true,
		    IResource.DEPTH_ZERO));
	} catch (CoreException e) {
	    return new ArrayList<IMarker>();
	}
    }

    /*
     * Returns a list of markers that are linked to the resource or any sub
     * resource of the resource
     */
    public static List<IMarker> findAllMarkers(IResource resource) {
	try {
	    return Arrays.asList(resource.findMarkers(MARKER, true,
		    IResource.DEPTH_INFINITE));
	} catch (CoreException e) {
	    return new ArrayList<IMarker>();
	}
    }

    /*
     * Returns the selection of the package explorer
     */
    public static TreeSelection getTreeSelection() {

	ISelection selection = PlatformUI.getWorkbench()
	        .getActiveWorkbenchWindow().getSelectionService()
	        .getSelection();
	if (selection instanceof TreeSelection) {
	    return (TreeSelection) selection;
	}
	return null;
    }

}
