package org.bbf.maven;

import org.junit.Test;

import java.io.File;

import static org.bbf.maven.ArtifactBuilder.mavenArtifactory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * User: robertom
 * Date: May 29, 2010
 * Time: 3:21:54 PM
 */
public class MavenArtifactsFinderTest {

    @Test
    public void getArtifactsReturnsAllProjectDependenciesInCaseOfProjectWithNoParent() throws Exception {
        String projectWithNoParent = "app-with-no-parent";
        MavenProjectInstaller mavenProjectInstaller = new MavenProjectInstaller();

        mavenProjectInstaller.installProjectJar(projectWithNoParent);

        ArtifactsFinder mavenDependenciesFinder = new MavenArtifactsFinder(createAbsolutePathProjectDir(projectWithNoParent));

        assertThat(mavenDependenciesFinder.getArtifacts().size(), is(1));

        SimpleArtifact junitArtifact = mavenArtifactory().withGroupId("junit").withArtifactId("junit").withVersion("4.8.1").build();

        assertThat("Project should have " + junitArtifact + " as dependency",
                mavenDependenciesFinder.getArtifacts().hasArtifact(junitArtifact), is(true));
    }

    @Test
    public void getArtifactsReturnsAllProjectDependenciesInCaseOfProjectWithParent() throws Exception {
        String projectWithParent = "app-with-parent";
        MavenProjectInstaller mavenProjectInstaller = new MavenProjectInstaller();
        mavenProjectInstaller.installProjectJar(projectWithParent);

        mavenProjectInstaller.installProjectJar("external-module");

        ArtifactsFinder mavenDependenciesFinder = new MavenArtifactsFinder(createAbsolutePathProjectDir(projectWithParent + "/module-core"));

        Artifacts actualArtifacts = mavenDependenciesFinder.getArtifacts();

        assertThat(mavenDependenciesFinder.getArtifacts().size(), is(7));

        asserThatProjectArtifactsHaveJar(actualArtifacts, mavenArtifactory().withGroupId("mycompany").withArtifactId("module-api").withVersion("1"));
        asserThatProjectArtifactsHaveJar(actualArtifacts, mavenArtifactory().withGroupId("junit").withArtifactId("junit").withVersion("4.8.1"));
        asserThatProjectArtifactsHaveJar(actualArtifacts, mavenArtifactory().withGroupId("junit").withArtifactId("junit-dep").withVersion("4.5"));
        asserThatProjectArtifactsHaveJar(actualArtifacts, mavenArtifactory().withGroupId("org.hamcrest").withArtifactId("hamcrest-core").withVersion("1.1"));
        asserThatProjectArtifactsHaveJar(actualArtifacts, mavenArtifactory().withGroupId("jmock").withArtifactId("jmock").withVersion("1.1.0"));
        asserThatProjectArtifactsHaveJar(actualArtifacts, mavenArtifactory().withGroupId("org.easymock").withArtifactId("easymock").withVersion("2.2"));
        asserThatProjectArtifactsHaveJar(actualArtifacts, mavenArtifactory().withGroupId("mycompany").withArtifactId("external-module").withVersion("1"));
    }

    @Test
    public void getArtifactsByGroupIdReturnsAllProjectDependenciesForThatGroupIdInCaseOfProjectWithParent() throws Exception {
        String projectWithParent = "app-with-parent";
        MavenProjectInstaller mavenProjectInstaller = new MavenProjectInstaller();
        mavenProjectInstaller.installProjectJar(projectWithParent);

        ArtifactsFinder mavenDependenciesFinder = new MavenArtifactsFinder(createAbsolutePathProjectDir(projectWithParent + "/module-core"));

        Artifacts actualArtifacts = mavenDependenciesFinder.getArtifacts();

        assertThat(mavenDependenciesFinder.getArtifactsByGroupId("mycompany").size(), is(2));

        asserThatProjectArtifactsHaveJar(actualArtifacts, mavenArtifactory().withGroupId("mycompany").withArtifactId("module-api").withVersion("1"));
        asserThatProjectArtifactsHaveJar(actualArtifacts, mavenArtifactory().withGroupId("mycompany").withArtifactId("external-module").withVersion("1"));
    }

    private void asserThatProjectArtifactsHaveJar(Artifacts actualArtifacts, ArtifactBuilder artifactBuilder) {
        SimpleArtifact jarToBeChecked = artifactBuilder.build();
        assertThat("Project should have " + jarToBeChecked + " as dependency", actualArtifacts.hasArtifact(jarToBeChecked), is(true));
    }

    private File createAbsolutePathProjectDir(String projectIdentifier) {
        return new File("/Users/robertom/IdeaProjects/broken-build-finder/src/test/resources/" + projectIdentifier);
    }
}
