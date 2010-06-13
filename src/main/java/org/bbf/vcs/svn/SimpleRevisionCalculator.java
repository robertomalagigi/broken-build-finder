package org.bbf.vcs.svn;

import jedi.functional.FunctionalPrimitives;
import org.bbf.vcs.RevisionCalculator;
import org.bbf.vcs.SourceControlRepository;
import org.joda.time.DateTime;
import org.tmatesoft.svn.core.SVNLogEntry;

import java.util.Collection;

/**
 * User: robertom
 * Date: May 31, 2010
 * Time: 1:14:50 PM
 */
class SimpleRevisionCalculator implements RevisionCalculator {
    private SourceControlRepository sourceControlRepository;

    SimpleRevisionCalculator(SourceControlRepository sourceControlRepository) {
        this.sourceControlRepository = sourceControlRepository;
    }

    public long firstRevisionNumberAfterDate(DateTime dateBeforeRevisionToFind) {
        long revisionByDate = 0;
        revisionByDate = sourceControlRepository.getDatedRevision(dateBeforeRevisionToFind);
        Collection<SVNLogEntry> logEntries = sourceControlRepository.log(revisionByDate, revisionByDate);
        SVNLogEntry logEntry = FunctionalPrimitives.first(logEntries);
        if (dateBeforeRevisionToFind.isAfter(lastRevisionDate(logEntry))) {
            long revisionNumber = logEntry.getRevision() + 1;
            sourceControlRepository.checkThatRevisionNumberExists(revisionNumber, dateBeforeRevisionToFind);
            return revisionNumber;
        }

        return logEntry.getRevision();
    }

    public long lastRevisionNumberBeforeDate(DateTime dateAfterRevisionToFind) {
        long revisionByDate = 0;
        revisionByDate = sourceControlRepository.getDatedRevision(dateAfterRevisionToFind);
        Collection<SVNLogEntry> logEntries = sourceControlRepository.log(revisionByDate, revisionByDate);
        SVNLogEntry logEntry = FunctionalPrimitives.first(logEntries);
        if (dateAfterRevisionToFind.isBefore(lastRevisionDate(logEntry))) {
            return logEntry.getRevision() - 1;
        }

        return logEntry.getRevision();
    }

    private long lastRevisionDate(SVNLogEntry logEntry) {
        return logEntry.getDate().getTime();
    }

}