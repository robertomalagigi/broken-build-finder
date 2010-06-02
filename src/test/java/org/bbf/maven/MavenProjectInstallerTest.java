package org.bbf.maven;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * User: robertom
 * Date: May 29, 2010
 * Time: 3:03:51 PM
 */
public class MavenProjectInstallerTest {
    private static final String USER_HOME = System.getProperty("user.home");
    private static final String M2_REPOSITORY_PREFIX = USER_HOME + "/.m2/repository/";
    private static final String JAR_UNDER_TEST_M2_REPOSITORY_PREFIX = M2_REPOSITORY_PREFIX + "mycompany/app-with-no-parent";
    private static final String JAR_TO_BE_INSTALLED = JAR_UNDER_TEST_M2_REPOSITORY_PREFIX + "/1/app-with-no-parent-1.jar";

    @Test
    public void installMovesJarIntoMavenRepositoryInCaseOfProjectWithNoParent() throws Exception {
        String jarToBeInstalled = JAR_UNDER_TEST_M2_REPOSITORY_PREFIX + "/1/app-with-no-parent-1.jar";
        FileUtils.deleteDirectory(new File(JAR_UNDER_TEST_M2_REPOSITORY_PREFIX));

        assertThatFileDoesntExistInTheRepository(jarToBeInstalled);

        MavenProjectInstaller mavenProjectInstaller = new MavenProjectInstaller();

        mavenProjectInstaller.installProjectJar("app-with-no-parent");

        assertThatFileExistsInTheRepository(jarToBeInstalled);
    }

    @Test
    public void installMovesJarIntoMavenRepositoryInCaseOfProjectWithParentAndExternlModule() throws Exception {
        String m2ParentProject = M2_REPOSITORY_PREFIX + "mycompany";
        String m2ModuleCoreJar = M2_REPOSITORY_PREFIX + "mycompany/module-core/1/module-core-1.jar";
        String m2ModuleApiJar = M2_REPOSITORY_PREFIX + "mycompany/module-api/1/module-api-1.jar";

        FileUtils.deleteDirectory(new File(m2ParentProject));

        assertThatFileDoesntExistInTheRepository(m2ParentProject);
        assertThatFileDoesntExistInTheRepository(m2ModuleCoreJar);
        assertThatFileDoesntExistInTheRepository(m2ModuleApiJar);
        assertThatFileDoesntExistInTheRepository(M2_REPOSITORY_PREFIX+"/external-module");

        MavenProjectInstaller mavenProjectInstaller = new MavenProjectInstaller();

        mavenProjectInstaller.installProjectJar("external-module");

        mavenProjectInstaller.installProjectJar("app-with-parent");

        assertThatFileExistsInTheRepository(m2ParentProject);
        assertThatFileExistsInTheRepository(m2ModuleCoreJar);
        assertThatFileExistsInTheRepository(m2ModuleApiJar);
        assertThatFileExistsInTheRepository(M2_REPOSITORY_PREFIX + "mycompany/external-module/1/external-module-1.jar");
    }

    private void assertThatFileExistsInTheRepository(String fileToCheck) {
        assertThat("file " + fileToCheck + " should exist", new File(fileToCheck).exists(), is(true));
    }

    private void assertThatFileDoesntExistInTheRepository(String fileToCheck) {
        assertThat("file " + fileToCheck + " should not exist", new File(fileToCheck).exists(), is(false));
    }
}
