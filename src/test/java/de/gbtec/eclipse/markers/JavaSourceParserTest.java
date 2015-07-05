package de.gbtec.eclipse.markers;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Platform;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.gbtec.eclipse.markers.JavaSourceParser;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ResourcesPlugin.class, Platform.class})
public class JavaSourceParserTest {

    URL configFile = getClass().getResource("A.java");
    EclipseMock mock = new EclipseMock();
    
    @Test
    public void test() throws Exception {
        IFile mockFileWithContents = mock.mockFileWithContents(new File(configFile.toURI()));
        assertThat(mockFileWithContents, notNullValue());
        JavaSourceParser parser = new JavaSourceParser(mockFileWithContents);
    }
}
