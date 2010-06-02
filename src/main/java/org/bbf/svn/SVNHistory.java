package org.bbf.svn;

import org.tmatesoft.svn.core.SVNLogEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: robertom
 * Date: May 31, 2010
 * Time: 4:45:10 PM
 */
public class SVNHistory {
    private List<SVNLogEntry> history;

    public SVNHistory(Collection<SVNLogEntry> history) {
        this.history = new ArrayList(history);
    }

    public List<SVNLogEntry> getEntries() {
        return history;
    }
}
