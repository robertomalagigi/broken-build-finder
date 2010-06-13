package org.bbf.vcs;

import org.bbf.vcs.svn.SVNHistory;

/**
 * User: robertom
 * Date: Jun 6, 2010
 * Time: 10:41:08 AM
 */
public interface HistoryFormatter {
    String format(SVNHistory history);
}