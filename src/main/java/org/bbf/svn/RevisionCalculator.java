package org.bbf.svn;

import org.joda.time.DateTime;

/**
 * User: robertom
 * Date: May 31, 2010
 * Time: 1:04:31 PM
 */
public interface RevisionCalculator {

    long firstRevisionNumberAfterDate(DateTime dateBeforeRevisionToFind);

    long lastRevisionNumberBeforeDate(DateTime dateAfterRevisionToFind);

}
