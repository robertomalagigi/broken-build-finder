package org.bbf.vcs.svn;

import org.bbf.utils.Builder;
import org.tmatesoft.svn.core.SVNLogEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * User: robertom
 * Date: Jun 6, 2010
 * Time: 12:28:57 AM
 */
public class SVNHistoryBuilder {

    private List entries = new ArrayList();

    public static SVNHistoryBuilder svnHistory(){
        return new SVNHistoryBuilder();
    }

    public SVNHistoryBuilder addEntry(Builder<SVNLogEntry> builder){
        SVNLogEntry svnLogEntry = builder.build();
        entries.add(svnLogEntry);
        return this;
    }

    public SVNHistory build() {
        return new SVNHistory(entries);
    }

}