package org.bbf.vcs;

import org.joda.time.DateTime;

import java.util.Collection;

/**
 * User: robertom
 * Date: Jun 5, 2010
 * Time: 10:15:09 PM
 */
public interface SourceControlRepository {

    Collection log(long startRevision, long endRevision);

    long getDatedRevision(DateTime date);

    void checkThatRevisionNumberExists(long potentialValidRevision, DateTime dateBeforeRevisionToFind);
}