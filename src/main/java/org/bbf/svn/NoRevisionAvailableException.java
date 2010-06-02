package org.bbf.svn;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * User: robertom
 * Date: May 31, 2010
 * Time: 7:11:06 PM
 */
class NoRevisionAvailableException extends RuntimeException {
    NoRevisionAvailableException(DateTime dateToCheckForRevisions, String repositoryLocation) {
        super("There are not commit after " + DateTimeFormat.forPattern("dd MM yyyy HH:mm:ss.SS").print(dateToCheckForRevisions) + " in " + repositoryLocation);
    }
}
