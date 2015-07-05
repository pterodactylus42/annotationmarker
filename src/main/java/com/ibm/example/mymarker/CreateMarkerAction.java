package com.ibm.example.mymarker;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

public class CreateMarkerAction implements IEditorActionDelegate {

    public CreateMarkerAction() {
        super();
    }

    @Override
    public void setActiveEditor(IAction action, IEditorPart editor) {
        // TODO Auto-generated method stub

    }

    /*
     * This action creates a new marker for the given IFile
     */
    @Override
    public void run(IAction action) {
        try {
            IFile file = (IFile) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorInput().getAdapter(IFile.class);
            MyMarkerFactory.createMarker(file);
        } catch (CoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
        // TODO Auto-generated method stub

    }

}
