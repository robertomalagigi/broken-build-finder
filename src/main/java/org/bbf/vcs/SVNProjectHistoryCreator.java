package org.bbf.vcs;

import org.joda.time.DateTime;

/**
 * User: robertom
 * Date: Jun 5, 2010
 * Time: 10:39:51 PM
 */
public class SVNProjectHistoryCreator {
    private final String projectName;
    private final HistoryRetrieverFactory historyRetrieverFactory;
    private final HistoryFormatter historyFormatter;

    public SVNProjectHistoryCreator(String projectName,
                                    HistoryRetrieverFactory historyRetrieverFactory, HistoryFormatter historyFormatter) {
        this.projectName = projectName;
        this.historyRetrieverFactory = historyRetrieverFactory;
        this.historyFormatter = historyFormatter;
    }

    public String asString(DateTime startDate, DateTime endDate) {
        HistoryRetriever historyRetriever = historyRetrieverFactory.createHistoryRetriever(projectName);
        return historyFormatter.format(historyRetriever.getHistory(startDate, endDate));
    }
}