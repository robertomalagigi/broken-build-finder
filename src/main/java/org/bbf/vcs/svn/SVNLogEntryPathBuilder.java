package org.bbf.vcs.svn;

import org.bbf.utils.Builder;
import org.tmatesoft.svn.core.SVNLogEntryPath;

/**
 * User: robertom
 * Date: Jun 5, 2010
 * Time: 11:22:18 PM
 */
public class SVNLogEntryPathBuilder implements Builder<SVNLogEntryPath> {
    private char type;
    private String path;

    public SVNLogEntryPathBuilder withType(char type) {
        this.type = type;
        return this;
    }

    public SVNLogEntryPathBuilder withPath(String path) {
        this.path = path;
        return this;
    }

    public static SVNLogEntryPathBuilder svnLogEntryPath() {
        return new SVNLogEntryPathBuilder();
    }

    public SVNLogEntryPath build() {
        return new SVNLogEntryPath(path, type, null, 1L);
    }

}