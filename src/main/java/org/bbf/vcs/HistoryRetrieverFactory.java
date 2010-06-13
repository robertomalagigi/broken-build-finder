package org.bbf.vcs;

/**
 * User: robertom
 * Date: Jun 5, 2010
 * Time: 8:37:40 PM
 */
public interface HistoryRetrieverFactory {
    HistoryRetriever createHistoryRetriever(String projectIdentifier);
}