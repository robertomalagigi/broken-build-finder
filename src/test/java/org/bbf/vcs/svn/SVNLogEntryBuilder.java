package org.bbf.vcs.svn;

import org.bbf.utils.Builder;
import org.joda.time.DateTime;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;

import java.util.HashMap;
import java.util.Map;

/**
 * User: robertom
 * Date: Jun 5, 2010
 * Time: 11:06:19 PM
 */
public class SVNLogEntryBuilder implements Builder<SVNLogEntry> {
    private Long revision;
    private String author;
    private DateTime versionDate;
    private String message;
    private Map<String, SVNLogEntryPath> pathMap = new HashMap<String, SVNLogEntryPath>();

    private SVNLogEntryPathBuilder svnLogEntryPathBuilder;

    public static SVNLogEntryBuilder svnHistoryBuilder() {
        return new SVNLogEntryBuilder();
    }

    public SVNLogEntryBuilder withVersion(Long revision) {
        this.revision = revision;
        return this;
    }

    public SVNLogEntryBuilder withVersionDate(DateTime date) {
        this.versionDate = date;
        return this;
    }

    public SVNLogEntryBuilder withAuthor(String author) {
        this.author = author;
        return this;
    }

    public SVNLogEntryBuilder withMessage(String message) {
        this.message = message;
        return this;
    }

    public SVNLogEntryBuilder addPath(Builder<SVNLogEntryPath> builder) {
        SVNLogEntryPath path = builder.build();
        pathMap.put(path.getPath(), path);
        return this;
    }

    public SVNLogEntry build() {
        return new SVNLogEntry(pathMap, revision, author , versionDate.toDate(), message);
    }
}