package org.bbf.svn;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.io.SVNRepository;

import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


/**
 * User: robertom
 * Date: May 30, 2010
 * Time: 10:25:02 PM
 */
public class SVNHistoryRetrieverTest {

    @After
    public void tearDown() {
        new SVNCommands().deleteRepository();
    }

    @Test
    public void historyReturnsRightValuesInCaseOfTwoCommits() throws Exception {
        SVNCommands svnCommands = new SVNCommands();

        DateTime dateBeforeFirstRevision = new DateTime();
        String commentRevision1 = "comment revision 1";
        svnCommands.addDir(commentRevision1, "dir1");

        String commentRevision2 = "comment revision 2";
        svnCommands.addDir(commentRevision2, "dir2");
        DateTime dateAfterLastRevision = new DateTime();

        SVNRepository svnRepository = svnCommands.getRepository();

        SVNHistoryRetriever historyRetriever = new SVNHistoryRetriever(svnRepository, new SimpleRevisionCalculator(svnRepository));

        SVNHistory history = historyRetriever.getHistory(dateBeforeFirstRevision, dateAfterLastRevision);

        List<SVNLogEntry> historyEntries = history.getEntries();

        SVNLogEntry firstHistoryEntry = historyEntries.get(0);
        assertThat(firstHistoryEntry.getMessage(), is(commentRevision1));
        assertThat(firstHistoryEntry.getRevision(), is(1L));
        assertThat(firstHistoryEntry.getAuthor(), is(getUser()));
        assertThat(firstHistoryEntry.getDate(), isSlightlyGreaterThan(dateBeforeFirstRevision));
        assertThatChangedPathIs(firstHistoryEntry.getChangedPaths().get("/dir1"), "A /dir1");

        SVNLogEntry secondHistoryEntry = historyEntries.get(1);
        assertThat(secondHistoryEntry.getMessage(), is(commentRevision2));
        assertThat(secondHistoryEntry.getRevision(), is(2L));
        assertThat(secondHistoryEntry.getAuthor(), is(getUser()));
        assertThat(secondHistoryEntry.getDate(), isSlightlyGreaterThan(dateBeforeFirstRevision));
        assertThatChangedPathIs(secondHistoryEntry.getChangedPaths().get("/dir2"), "A /dir2");

    }

    @Test
    public void historyReturnsNoEntriesIfThereAreNotCommitBetweenDatesButOnlyBefore() throws Exception {
        SVNCommands svnCommands = new SVNCommands();

        svnCommands.addDir("comment revision 1", "dir1");

        DateTime dateAfterLastCommit = new DateTime();

        SVNRepository svnRepository = svnCommands.getRepository();

        SVNHistoryRetriever historyRetriever = new SVNHistoryRetriever(svnRepository, new SimpleRevisionCalculator(svnRepository));

        SVNHistory history = historyRetriever.getHistory(dateAfterLastCommit, dateAfterLastCommit.plusDays(1));

        assertThat(history.getEntries().size(), is(0));
    }

    @Test
    public void historyReturnsNoEntriesIfThereAreNotCommitBetweenDatesButOnlyBeforeAndAfter() throws Exception {
        SVNCommands svnCommands = new SVNCommands();

        svnCommands.addDir("comment revision 1", "dir1");

        DateTime dateAfterStartRevision = new DateTime();

        waitForSeconds(3);

        svnCommands.addDir("comment revision 2", "dir2");

        SVNRepository svnRepository = svnCommands.getRepository();

        SVNHistoryRetriever historyRetriever = new SVNHistoryRetriever(svnRepository, new SimpleRevisionCalculator(svnRepository));

        SVNHistory history = historyRetriever.getHistory(dateAfterStartRevision, dateAfterStartRevision.plusSeconds(2));

        assertThat(history.getEntries().size(), is(0));
    }


    @Test
    public void historyReturnsNoEntriesIfThereAreNotCommitBetweenDatesButOnlyAfter() throws Exception {
        SVNCommands svnCommands = new SVNCommands();

        DateTime dateBeforeFirstCommit = new DateTime();

        waitForSeconds(3);

        svnCommands.addDir("comment revision 1", "dir1");

        SVNRepository svnRepository = svnCommands.getRepository();

        SVNHistoryRetriever historyRetriever = new SVNHistoryRetriever(svnRepository, new SimpleRevisionCalculator(svnRepository));

        SVNHistory history = historyRetriever.getHistory(dateBeforeFirstCommit, dateBeforeFirstCommit.plusSeconds(2));

        assertThat(history.getEntries().size(), is(0));
    }

    private void assertThatChangedPathIs(Object changedPath, String details) {
        assertThat(changedPath.toString(), is(details));
    }

    private String getUser() {
        String userHome = System.getProperty("user.home");
        return userHome.substring(userHome.lastIndexOf("/") + 1);
    }

    private org.hamcrest.Matcher isSlightlyGreaterThan(final DateTime dateTime) {
        return new BaseMatcher() {
            private Date actualDate;
            public long gap = 500L;

            public boolean matches(Object actualDate) {
                this.actualDate = (Date) actualDate;
                return this.actualDate.getTime() - dateTime.toDate().getTime() < gap;
            }

            public void describeTo(Description description) {
                description.appendText(actualDate + " - " + dateTime.toDate() + " should be less than " + gap);
            }
        };
    }

    private void waitForSeconds(int numberOfSecondsToWaitFor) {
        try {
            Thread.sleep(numberOfSecondsToWaitFor * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}