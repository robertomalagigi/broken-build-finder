package org.bbf.vcs.svn;

import org.bbf.vcs.HistoryRetriever;
import org.bbf.vcs.NoRevisionAvailableException;
import org.bbf.vcs.RevisionCalculator;
import org.bbf.vcs.SourceControlRepository;
import org.joda.time.DateTime;

import java.util.Collections;

/**
 * User: robertom
 * Date: May 31, 2010
 * Time: 4:36:26 PM
 */
public class SimpleHistoryRetriever implements HistoryRetriever {
    private SourceControlRepository sourceControlRepository;
    private RevisionCalculator revisionCalculator;

    public SimpleHistoryRetriever(SourceControlRepository sourceControlRepository, RevisionCalculator revisionCalculator) {
        this.sourceControlRepository = sourceControlRepository;
        this.revisionCalculator = revisionCalculator;
    }

    @Override
    public SVNHistory getHistory(DateTime dateBeforeStartRevision, DateTime dateAfterEndRevision) {
        long startRevision = 0;
        try {
            startRevision = revisionCalculator.firstRevisionNumberAfterDate(dateBeforeStartRevision);
        } catch (NoRevisionAvailableException e) {
            return new SVNHistory(Collections.EMPTY_LIST);
        }

        long endRevision = revisionCalculator.lastRevisionNumberBeforeDate(dateAfterEndRevision);
        if (endRevision < startRevision) {
            return new SVNHistory(Collections.EMPTY_LIST);
        }
        return new SVNHistory(sourceControlRepository.log(startRevision, endRevision));
    }
}