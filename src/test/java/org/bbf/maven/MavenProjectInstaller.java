package org.bbf.maven;

import org.apache.maven.embedder.DefaultConfiguration;
import org.apache.maven.embedder.MavenEmbedder;
import org.apache.maven.embedder.MavenEmbedderException;
import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequest;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;

import static org.codehaus.plexus.PlexusTestCase.getBasedir;


/**
 * User: robertom
 * Date: May 23, 2010
 * Time: 4:11:47 PM
 */
public class MavenProjectInstaller {

    public void installProjectJar(String projectName) {
        File projectDirectory = new File(getBasedir() + "/src/test/resources/", projectName);

        org.apache.maven.embedder.Configuration configuration = new DefaultConfiguration()
                .setClassLoader(Thread.currentThread().getContextClassLoader());

        MavenEmbedder embedder = null;
        try {
            embedder = new MavenEmbedder(configuration);
        } catch (MavenEmbedderException e) {
            throw new RuntimeException("Problem installing project located to " + projectDirectory, e);
        }

        Properties mavenProperties = new Properties();
        mavenProperties.setProperty("test.compiler.output", getBasedir());
        MavenExecutionRequest request = new DefaultMavenExecutionRequest()
                .setBaseDirectory(projectDirectory)
                .setGoals(Arrays.asList(new String[]{"clean", "install"}))
                .setProperties(mavenProperties);

        embedder.execute(request);

    }

}
