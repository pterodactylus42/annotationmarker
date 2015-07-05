package de.gbtec.eclipse.markers;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.powermock.api.mockito.PowerMockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public final class EclipseMock {

    private final IWorkspace workspace;
    private final IWorkspaceRoot workspaceRoot;
    private final IPath workspaceRootLocation;

    /**
     * Instantiates a new eclipse mock.
     */
    public EclipseMock() {
        PowerMockito.mockStatic(ResourcesPlugin.class);
        PowerMockito.mockStatic(Platform.class);
        workspace = mock(IWorkspace.class);
        when(ResourcesPlugin.getWorkspace()).thenReturn(workspace);
        workspaceRoot = mock(IWorkspaceRoot.class);
        when(workspace.getRoot()).thenReturn(workspaceRoot);
        workspaceRootLocation = mock(IPath.class);
        when(workspaceRoot.getLocation()).thenReturn(workspaceRootLocation);
    }

    /**
     * Write file.
     *
     * @param filename
     *            the filename
     * @param content
     *            the content
     */
    public void writeFile(final String filename, final String content) {
        try {
            // Create file
            FileWriter fstream = new FileWriter(filename);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(content);
            // Close the output stream
            out.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage()); //$NON-NLS-1$
        }
    }

    /**
     * Mock file with contents.
     *
     * @param file
     *            the file name
     * @return the i file
     * @throws FileNotFoundException
     *             the file not found exception
     * @throws CoreException
     *             the core exception
     */
    public IFile mockFileWithContents(final File file) throws FileNotFoundException, CoreException {
        FileInputStream fstream = new FileInputStream(file); //$NON-NLS-1$
        final IFile iFile = mockEmptyFile(file.getName());
        when(iFile.getContents()).thenReturn(new DataInputStream(fstream));
        return iFile;
    }

    /**
     * Mock empty file.
     *
     * @param fileName
     *            the file name
     * @return the i file
     * @throws FileNotFoundException
     *             the file not found exception
     * @throws CoreException
     *             the core exception
     */
    public IFile mockEmptyFile(final String fileName) throws FileNotFoundException, CoreException {

        final IPath name = mock(Path.class);
        final IFile iFile = mock(IFile.class);
        when(iFile.getLocation()).thenReturn(name);
        String[] split = fileName.split("\\.");
        String extension = split[split.length - 1];
        when(iFile.getFileExtension()).thenReturn(extension);
        when(iFile.getName()).thenReturn(fileName);
        when(iFile.getCharset(true)).thenReturn("UTF-8"); //$NON-NLS-1$

        when(iFile.toString()).thenReturn(fileName);

        // put file in workspace
        final IWorkspaceRoot mockRoot = workspaceRoot();
        when(mockRoot.findMember(name)).thenReturn(iFile);

        return iFile;
    }

    /**
     * @return workspace
     */
    public IWorkspace workspace() {
        return workspace;
    }

    /**
     * @return workspace root
     */
    public IWorkspaceRoot workspaceRoot() {
        return workspaceRoot;
    }

    /**
     * @return workspace root location
     */
    public IPath workspaceRootLocation() {
        return workspaceRootLocation;
    }
}
