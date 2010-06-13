package org.bbf.vcs;

import org.bbf.vcs.svn.SVNHistory;
import org.joda.time.DateTime;

/**
 * User: robertom
 * Date: Jun 5, 2010
 * Time: 9:58:33 PM
 */
public interface HistoryRetriever {
    SVNHistory getHistory(DateTime dateBeforeStartRevision, DateTime dateAfterEndRevision);
}