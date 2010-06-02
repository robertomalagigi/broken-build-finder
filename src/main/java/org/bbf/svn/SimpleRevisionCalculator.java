package org.bbf.svn;

import jedi.functional.FunctionalPrimitives;
import org.joda.time.DateTime;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.io.SVNRepository;

import java.util.Collection;

/**
 * User: robertom
 * Date: May 31, 2010
 * Time: 1:14:50 PM
 */
class SimpleRevisionCalculator implements RevisionCalculator {
    private SVNRepository repository;

    SimpleRevisionCalculator(SVNRepository repository) {
        this.repository = repository;
    }

    public long firstRevisionNumberAfterDate(DateTime dateBeforeRevisionToFind) {
        long revisionByDate = 0;
        Collection<SVNLogEntry> logEntries = null;
        try {
            revisionByDate = repository.getDatedRevision(dateBeforeRevisionToFind.toDate());
            logEntries = repository.log(new String[]{""}, null, revisionByDate, revisionByDate, true, true);
        } catch (SVNException e) {
            throw new RuntimeException("Exception finding log entries after " + dateBeforeRevisionToFind + " for repository " + repository);
        }
        SVNLogEntry logEntry = FunctionalPrimitives.first(logEntries);
        if (dateBeforeRevisionToFind.isAfter(lastRevisionDate(logEntry))) {
            long revisionNumber = logEntry.getRevision() + 1;
            checkThatRevisionNumberExists(revisionNumber, dateBeforeRevisionToFind);
            return revisionNumber;
        }

        return logEntry.getRevision();
    }

    public long lastRevisionNumberBeforeDate(DateTime dateAfterRevisionToFind) {
        long revisionByDate = 0;
        Collection<SVNLogEntry> logEntries = null;
        try {
            revisionByDate = repository.getDatedRevision(dateAfterRevisionToFind.toDate());
            logEntries = repository.log(new String[]{""}, null, revisionByDate, revisionByDate, true, true);
        } catch (SVNException e) {
            throw new RuntimeException("Exception finding log entries after " + dateAfterRevisionToFind + " for repository " + repository);
        }
        SVNLogEntry logEntry = FunctionalPrimitives.first(logEntries);
        if (dateAfterRevisionToFind.isBefore(lastRevisionDate(logEntry))) {
            return logEntry.getRevision() - 1;
        }

        return logEntry.getRevision();
    }

    private long lastRevisionDate(SVNLogEntry logEntry) {
        return logEntry.getDate().getTime();
    }

    private void checkThatRevisionNumberExists(long potentialValidRevision, DateTime dateBeforeRevisionToFind) {
        try {
            repository.log(new String[]{""}, null, potentialValidRevision, potentialValidRevision, true, true);
        } catch (SVNException e) {
            if (e.getMessage().toLowerCase().contains("no such revision")) {
                throw new NoRevisionAvailableException(dateBeforeRevisionToFind, repository.getLocation().toString());
            }
            throw new RuntimeException("Exception finding log entries after " + dateBeforeRevisionToFind + " for repository " + repository);
        }
    }
}
