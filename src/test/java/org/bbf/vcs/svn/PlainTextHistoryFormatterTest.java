package org.bbf.vcs.svn;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.bbf.vcs.svn.SVNHistoryBuilder.svnHistory;
import static org.bbf.vcs.svn.SVNLogEntryBuilder.svnHistoryBuilder;
import static org.bbf.vcs.svn.SVNLogEntryPathBuilder.svnLogEntryPath;

/**
 * User: robertom
 * Date: Jun 6, 2010
 * Time: 10:45:31 AM
 */
public class PlainTextHistoryFormatterTest {
    private PlainTextHistoryFormatter iut;

    @Before
    public void setUp() {
        iut = new PlainTextHistoryFormatter();
    }

    @Test
    public void formatProducesRightTextInCaseOfOnlyOneChange() throws Exception {
        SVNLogEntryBuilder author1Change = svnHistoryBuilder().withAuthor("author1").withMessage("message1").withVersion(1L)
                .withVersionDate(new DateTime("2010-12-03T21:00:00"))
                .addPath(svnLogEntryPath().withType('M').withPath("/a/b/myClass.java"));

        SVNHistory sourceControlHistory = svnHistory().addEntry(author1Change).build();

        String actualHistoryContent = iut.format(sourceControlHistory);

        StringBuilder expHistoryContent = new StringBuilder();
        expHistoryContent.append("---------------------------------------------------\n");
        expHistoryContent.append("revision: 1\n");
        expHistoryContent.append("author: author1\n");
        expHistoryContent.append("date: Fri Dec 03 21:00:00 GMT 2010\n");
        expHistoryContent.append("log message: message1\n");
        expHistoryContent.append("\n");
        expHistoryContent.append("changed paths:\n");
        expHistoryContent.append("M /a/b/myClass.java\n");

        assertEquals(expHistoryContent.toString(), actualHistoryContent);
    }


    @Test
    public void formatProducesRightTextInCaseOfTwoChanges() throws Exception {
        SVNLogEntryBuilder author1Change = svnHistoryBuilder().withAuthor("author1").withMessage("message1").withVersion(1L)
                .withVersionDate(new DateTime("2010-12-03T21:00:00"))
                .addPath(svnLogEntryPath().withType('M').withPath("/a/b/myClass.java"));

        SVNLogEntryBuilder author2Change = svnHistoryBuilder().withAuthor("author2").withMessage("message2").withVersion(2L)
                .withVersionDate(new DateTime("2010-12-03T21:10:00"))
                .addPath(svnLogEntryPath().withType('D').withPath("/a/b/yourClass.java"));

        SVNHistory sourceControlHistory = svnHistory().addEntry(author1Change).addEntry(author2Change).build();

        String actualHistoryContent = iut.format(sourceControlHistory);

        StringBuilder expHistoryContent = new StringBuilder();
        expHistoryContent.append("---------------------------------------------------\n");
        expHistoryContent.append("revision: 1\n");
        expHistoryContent.append("author: author1\n");
        expHistoryContent.append("date: Fri Dec 03 21:00:00 GMT 2010\n");
        expHistoryContent.append("log message: message1\n");
        expHistoryContent.append("\n");
        expHistoryContent.append("changed paths:\n");
        expHistoryContent.append("M /a/b/myClass.java\n");
        expHistoryContent.append("\n");
        expHistoryContent.append("---------------------------------------------------\n");
        expHistoryContent.append("revision: 2\n");
        expHistoryContent.append("author: author2\n");
        expHistoryContent.append("date: Fri Dec 03 21:10:00 GMT 2010\n");
        expHistoryContent.append("log message: message2\n");
        expHistoryContent.append("\n");
        expHistoryContent.append("changed paths:\n");
        expHistoryContent.append("D /a/b/yourClass.java\n");

        assertEquals(expHistoryContent.toString(), actualHistoryContent);
    }

    @Test
    public void formatReturnEmptyStringIfThereAreNotEntries() throws Exception {
        SVNHistory sourceControlHistory = svnHistory().build();

        String actualHistoryContent = iut.format(sourceControlHistory);

        assertEquals("", actualHistoryContent);
    }


}