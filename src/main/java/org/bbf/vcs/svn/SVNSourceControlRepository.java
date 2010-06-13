package org.bbf.vcs.svn;

import org.bbf.vcs.NoRevisionAvailableException;
import org.bbf.vcs.SourceControlRepository;
import org.joda.time.DateTime;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.io.SVNRepository;

import java.util.Collection;

/**
 * User: robertom
 * Date: Jun 5, 2010
 * Time: 10:19:03 PM
 */
public class SVNSourceControlRepository implements SourceControlRepository {

    private SVNRepository svnRepository;


    public SVNSourceControlRepository(SVNRepository svnRepository) {
        this.svnRepository = svnRepository;
    }

    @Override
    public Collection log(long startRevision, long endRevision) {
        try {
            return svnRepository.log(new String[]{""}, null, startRevision, endRevision, true, true);
        } catch (SVNException e) {
            throw new RuntimeException("Exception retrieving history from revision " + startRevision +
                    " to revision " + endRevision + " from repository " + svnRepository.getLocation(), e);
        }
    }

    @Override
    public long getDatedRevision(DateTime date) {
        try {
            return svnRepository.getDatedRevision(date.toDate());
        } catch (SVNException e) {
            throw new RuntimeException("Exception finding getDatedRevision for " + date + " for repository " + svnRepository.getLocation(), e);
        }
    }

    @Override
    public void checkThatRevisionNumberExists(long potentialValidRevision, DateTime dateBeforeRevisionToFind) {
        try {
            svnRepository.log(new String[]{""}, null, potentialValidRevision, potentialValidRevision, true, true);
        } catch (SVNException e) {
            if (e.getMessage().toLowerCase().contains("no such revision")) {
                throw new NoRevisionAvailableException(dateBeforeRevisionToFind, svnRepository.getLocation().toString());
            }
            throw new RuntimeException("Exception finding log entries after " + dateBeforeRevisionToFind + " for repository " + svnRepository.getLocation(), e);
        }
    }
}