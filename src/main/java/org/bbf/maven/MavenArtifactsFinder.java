package org.bbf.maven;

import jedi.functional.Filter;
import jedi.functional.FunctionalPrimitives;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.embedder.DefaultConfiguration;
import org.apache.maven.embedder.MavenEmbedder;
import org.apache.maven.embedder.MavenEmbedderConsoleLogger;
import org.apache.maven.embedder.MavenEmbedderException;
import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;

import static org.codehaus.plexus.PlexusTestCase.getBasedir;

/**
 * User: robertom
 * Date: May 29, 2010
 * Time: 3:27:46 PM
 */
public class MavenArtifactsFinder implements ArtifactsFinder {
    private Set artifacts;

    public MavenArtifactsFinder(File projectDirectory) {
        org.apache.maven.embedder.Configuration configuration = new DefaultConfiguration()
                .setClassLoader(Thread.currentThread().getContextClassLoader());

        MavenEmbedder embedder = null;
        try {
            embedder = new MavenEmbedder(configuration);
        } catch (MavenEmbedderException e) {
            throw new RuntimeException("Problem installing project located to " + projectDirectory, e);
        }

        embedder.setLogger(new MavenEmbedderConsoleLogger());

        Properties mavenProperties = new Properties();
        mavenProperties.setProperty("test.compiler.output", getBasedir());

        MavenExecutionRequest request = new DefaultMavenExecutionRequest()
                .setBaseDirectory(projectDirectory)
                .setGoals(Arrays.asList(new String[]{"dependency:tree"}))
                .setProperties(mavenProperties);

        artifacts = embedder.execute(request).getProject().getArtifacts();

    }

    public Artifacts getArtifacts() {
        return new Artifacts(new ArrayList(artifacts));
    }

    public Artifacts getArtifactsByGroupId(final String groupId) {
        return new Artifacts(FunctionalPrimitives.select(artifacts, new Filter<Artifact>() {
            public Boolean execute(Artifact internalArtifact) {
                return internalArtifact.getGroupId().equals(groupId);
            }
        }));
    }
}
