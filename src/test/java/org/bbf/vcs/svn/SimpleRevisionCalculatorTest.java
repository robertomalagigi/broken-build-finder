package org.bbf.vcs.svn;

import org.bbf.vcs.NoRevisionAvailableException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;


/**
 * User: robertom
 * Date: May 30, 2010
 * Time: 10:25:02 PM
 */
public class SimpleRevisionCalculatorTest {
    private SVNCommands svnCommands;
    private SimpleRevisionCalculator simpleRevisionCalculator;


    @Before
    public void setUp() {
        svnCommands = new SVNCommands();
        simpleRevisionCalculator = new SimpleRevisionCalculator(new SVNSourceControlRepository(svnCommands.getRepository()));
    }

    @After
    public void tearDown() {
        svnCommands.deleteRepository();
    }

    @Test
    public void firstRevisionNumberAfterDateReturnsTheFirstRevisionInCaseOfOneCommitsAfterThatDate() throws Exception {
        svnCommands.addDir("revision 1", "dir1");

        Date dateAfterFirstRevision = new Date();

        waitForSeconds(2);
        svnCommands.addDir("revision 2", "dir2");

        assertThat(simpleRevisionCalculator.firstRevisionNumberAfterDate(new DateTime(dateAfterFirstRevision)), is(2L));
    }

    @Test
    public void firstRevisionNumberAfterDateReturnsTheFirstRevisionInCaseOfTwoCommitsAfterThatDate() throws Exception {
        Date dateBeforeFirstRevision = new Date();
        svnCommands.addDir("revision 1", "dir1");

        svnCommands.addDir("revision 2", "dir2");

        assertThat(simpleRevisionCalculator.firstRevisionNumberAfterDate(new DateTime(dateBeforeFirstRevision)), is(1L));
    }


    @Test
    public void lastRevisionNumberBeforeDateReturnsRightRevisionInCaseOfThreeCommitsBeforeThatDateAndOneAfter() throws Exception {
        svnCommands.addDir("revision 1", "dir1");
        svnCommands.addDir("revision 2", "dir2");
        svnCommands.addDir("revision 3", "dir3");
        DateTime dateAfterThirdRevision = new DateTime();
        svnCommands.addDir("revision 4", "dir4");

        assertThat(simpleRevisionCalculator.lastRevisionNumberBeforeDate(dateAfterThirdRevision), is(3L));
    }

    @Test
    public void lastRevisionNumberBeforeDateReturnsZeroIfThereAreNoCommits() throws Exception {
        assertThat(simpleRevisionCalculator.lastRevisionNumberBeforeDate(new DateTime()), is(0L));
    }

    @Test
    public void firstRevisionNumberAfterDateThrowsNoRevisionAvailableExceptionIfThereAreNoCommits() throws Exception {
        DateTime dateToCheckForRevisions = new DateTime();
        String expDateMessage = DateTimeFormat.forPattern("dd MM yyyy HH:mm:ss.SS").print(dateToCheckForRevisions);
        try {
            simpleRevisionCalculator.firstRevisionNumberAfterDate(dateToCheckForRevisions);
            fail(NoRevisionAvailableException.class + " should have been thrown");
        } catch (NoRevisionAvailableException e) {
            assertThat(e.getMessage(), containsString("There are not commit after " + expDateMessage + " in "));
        }
    }

    private void waitForSeconds(int numberOfSecondsToWaitFor) {
        try {
            Thread.sleep(numberOfSecondsToWaitFor * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}