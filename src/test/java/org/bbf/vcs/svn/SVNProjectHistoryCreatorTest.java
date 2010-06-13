package org.bbf.vcs.svn;

import org.bbf.vcs.*;
import org.easymock.IMocksControl;
import org.joda.time.DateTime;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.bbf.vcs.svn.SVNHistoryBuilder.svnHistory;
import static org.bbf.vcs.svn.SVNLogEntryBuilder.svnHistoryBuilder;
import static org.bbf.vcs.svn.SVNLogEntryPathBuilder.svnLogEntryPath;
import static org.easymock.EasyMock.createControl;
import static org.easymock.EasyMock.expect;

/**
 * User: robertom
 * Date: Jun 5, 2010
 * Time: 12:12:20 PM
 */
public class SVNProjectHistoryCreatorTest {

    public IMocksControl control = createControl();

    private final HistoryRetriever historyRetriever = control.createMock(HistoryRetriever.class);

    private final HistoryFormatter historyFormatter = control.createMock(HistoryFormatter.class);

    @Test
    public void asStringDelegatesToTheHistoryFormatter() throws Exception {

        DateTime startDate = new DateTime();

        DateTime endDate = startDate.plusHours(1);

        String projectSymbolicName = "project name";

        HistoryRetrieverFactory historyRetrieverFactory = control.createMock(HistoryRetrieverFactory.class);

        expect(historyRetrieverFactory.createHistoryRetriever(projectSymbolicName)).andReturn(historyRetriever);

        DateTime version1Date = new DateTime();
        SVNLogEntryBuilder svnLogEntryBuilder = svnHistoryBuilder().withAuthor("author1").withMessage("message1").withVersion(1L)
                .withVersionDate(version1Date)
                .addPath(svnLogEntryPath().withType('M').withPath("a/b/myclass.java"));

        SVNHistory sourceControlHistory = svnHistory().addEntry(svnLogEntryBuilder).build();

        expect(historyRetriever.getHistory(startDate, endDate)).andReturn(sourceControlHistory);
        String expectedHistoryText = "THis is the formatted mock Result";
        expect(historyFormatter.format(sourceControlHistory)).andReturn(expectedHistoryText);

        SVNProjectHistoryCreator iut = new SVNProjectHistoryCreator(projectSymbolicName,
                historyRetrieverFactory,
                historyFormatter);

        control.replay();

        assertEquals(expectedHistoryText, iut.asString(startDate, endDate));

        control.verify();
    }


}