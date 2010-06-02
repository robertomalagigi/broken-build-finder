package org.bbf.svn;

import org.joda.time.DateTime;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.io.SVNRepository;

import java.util.Collections;

/**
 * User: robertom
 * Date: May 31, 2010
 * Time: 4:36:26 PM
 */
public class SVNHistoryRetriever {
    private SVNRepository svnRepository;
    private RevisionCalculator revisionCalculator;

    public SVNHistoryRetriever(SVNRepository svnRepository, RevisionCalculator revisionCalculator) {
        this.svnRepository = svnRepository;
        this.revisionCalculator = revisionCalculator;
    }

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
        try {
            return new SVNHistory(svnRepository.log(new String[]{""}, null, startRevision, endRevision, true, true));
        } catch (SVNException e) {
            throw new RuntimeException("Exception retrieving history from revision " + startRevision +
                    " (" + dateBeforeStartRevision + ") to revision " + endRevision + " (" + dateAfterEndRevision + ")", e);
        }
    }
}
