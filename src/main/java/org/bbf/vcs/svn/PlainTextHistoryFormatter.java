package org.bbf.vcs.svn;

import org.bbf.vcs.HistoryFormatter;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * User: robertom
 * Date: Jun 6, 2010
 * Time: 10:44:03 AM
 */
public class PlainTextHistoryFormatter implements HistoryFormatter {
    public String format(SVNHistory history) {
        List<SVNLogEntry> logEntries = history.getEntries();
        StringBuilder content = new StringBuilder();
        for (SVNLogEntry logEntry : logEntries) {
            appendLineEntrySeparator(content);
            content.append("---------------------------------------------------\n");
            content.append("revision: " + logEntry.getRevision() + "\n");
            content.append("author: " + logEntry.getAuthor() + "\n");
            content.append("date: " + logEntry.getDate() + "\n");
            content.append("log message: " + logEntry.getMessage() + "\n");

            if (logEntry.getChangedPaths().size() > 0) {
                content.append("\n");
                content.append("changed paths:\n");

                Set changedPathsSet = logEntry.getChangedPaths().keySet();

                for (Iterator changedPaths = changedPathsSet.iterator(); changedPaths.hasNext();) {
                    SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry.getChangedPaths().get(changedPaths.next());
                    content.append(entryPath.getType());
                    content.append(" ");
                    content.append(entryPath.getPath());
                    content.append("\n");
                }

            }
        }
        return content.toString();
    }

    private void appendLineEntrySeparator(StringBuilder content) {
        if (content.length() > 1) {
            content.append("\n");
        }
    }

}