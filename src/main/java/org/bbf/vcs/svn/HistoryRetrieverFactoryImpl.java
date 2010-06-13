package org.bbf.vcs.svn;

import org.bbf.vcs.HistoryRetriever;
import org.bbf.vcs.HistoryRetrieverFactory;
import org.bbf.vcs.SourceControlRepository;
import org.bbf.vcs.SourceControlRepositoryFactory;

/**
 * User: robertom
 * Date: Jun 6, 2010
 * Time: 12:43:13 PM
 */
public class HistoryRetrieverFactoryImpl implements HistoryRetrieverFactory {
    private SourceControlRepositoryFactory sourceControlRepositoryFactory;

    public HistoryRetrieverFactoryImpl(SourceControlRepositoryFactory sourceControlRepositoryFactory) {
        this.sourceControlRepositoryFactory = sourceControlRepositoryFactory;
    }

    @Override
    public HistoryRetriever createHistoryRetriever(String projectIdentifier) {
        SourceControlRepository sourceControlRepository = sourceControlRepositoryFactory.createSourceControlRepository(projectIdentifier);
        return new SimpleHistoryRetriever(sourceControlRepository, new SimpleRevisionCalculator(sourceControlRepository));
    }
}