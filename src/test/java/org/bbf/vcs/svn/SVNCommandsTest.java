package org.bbf.vcs.svn;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;
import org.tmatesoft.svn.core.io.SVNRepository;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


/**
 * User: robertom
 * Date: May 30, 2010
 * Time: 10:25:02 PM
 */
public class SVNCommandsTest {

    @After
    public void tearDown() {
        new SVNCommands().deleteRepository();
    }

    @Test
    public void repositoryGetDatedRevisionReturnsTheNearestRevisionNumber() throws Exception {
        SVNCommands svnCommands = new SVNCommands();

        svnCommands.addDir("directory test added", "test");
        Date revision1Date = new Date();

        waitForSeconds(2);
        String fileContent = "This is a new file";
        svnCommands.addFile("file.txt added", "test/file.txt", fileContent);
        Date revision2Date = new Date();

        waitForSeconds(3);
        String newFileContent = "This is a another file";
        svnCommands.addFile("newFile.txt added", "test/newFile.txt", newFileContent);
        Date revision3Date = new Date();

        waitForSeconds(2);
        String modifiedContents = "This is the same file but modified a little.";
        svnCommands.modifyFile("file fileContent changed", "test", "test/file.txt", fileContent, modifiedContents);

        SVNRepository repository = svnCommands.getRepository();

        assertThat(repository.getDatedRevision(revision1Date), is(1L));
        assertThat(repository.getDatedRevision(new DateTime(revision1Date).plusSeconds(1).toDate()), is(1L));
        assertThat(repository.getDatedRevision(new DateTime(revision1Date).plusSeconds(2).toDate()), is(1L));

        assertThat(repository.getDatedRevision(revision2Date), is(2L));
        assertThat(repository.getDatedRevision(revision3Date), is(3L));
    }

    private void waitForSeconds(int numberOfSecondsToWaitFor) {
        try {
            Thread.sleep(numberOfSecondsToWaitFor * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}