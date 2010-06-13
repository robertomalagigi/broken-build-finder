package org.bbf.vcs.svn;

import org.bbf.vcs.HistoryRetrieverFactory;
import org.bbf.vcs.SVNProjectHistoryCreator;
import org.bbf.vcs.SourceControlRepositoryFactory;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * User: robertom
 * Date: Jun 5, 2010
 * Time: 12:12:20 PM
 */
public class SVNProjectHistoryCreatorIntegrationTest {

    @After
    public void tearDown() {
        new SVNCommands().deleteRepository();
    }

    @Test
    public void asStringReturnsRigthStringInCaseOfFilesAdditionAndModificationAgainstALocalRepositoryWithNoLogin() throws Exception {
        SVNCommands svnCommands = new SVNCommands();
        svnCommands.addDirWithDefaultComment("trunk");
        svnCommands.addDirWithDefaultComment("trunk/parent-project");
        svnCommands.addDirWithDefaultComment("trunk/parent-project/project1");
        svnCommands.addDirWithDefaultComment("trunk/parent-project/project1/module-api");
        svnCommands.addDirWithDefaultComment("trunk/parent-project/project1/module-core");
        svnCommands.addDirWithDefaultComment("trunk/parent-project/project1/module-integration-test");
        svnCommands.addDirWithDefaultComment("trunk/parent-project/project2");
        svnCommands.addDirWithDefaultComment("trunk/parent-project/project2/module-api");
        svnCommands.addDirWithDefaultComment("trunk/parent-project/project2/module-core");
        svnCommands.addDirWithDefaultComment("trunk/parent-project/project2/module-integration-test");

        DateTime startDate = new DateTime();

        waitForSeconds(1);

        svnCommands.addFileWithDefaultContentAndComment("trunk/parent-project/project1/module-api/file.txt");

        svnCommands.addFileWithDefaultContentAndComment("trunk/parent-project/project1/module-core/file.txt");

        waitForSeconds(1);

        svnCommands.modifyFile("trunk/parent-project/project1/module-api", "file.txt");

        String repositoryPath = svnCommands.getRepository().getLocation().getPath();

        SourceControlRepositoryFactory sourceControlRepositoryFactory = new SVNSourceControlRepositoryFactory("file:///" + repositoryPath);

        HistoryRetrieverFactory historyRetrieverFactory = new HistoryRetrieverFactoryImpl(sourceControlRepositoryFactory);

        SVNProjectHistoryCreator iut = new SVNProjectHistoryCreator("trunk/parent-project/project1",
                historyRetrieverFactory,
                new PlainTextHistoryFormatter());

        String actualHistoryContent = iut.asString(startDate, new DateTime());

        StringBuilder expContent = new StringBuilder();
        expContent.append("---------------------------------------------------\n");
        expContent.append("revision: 11\n");
        expContent.append("author: XXX\n");
        expContent.append("date: YYY\n");
        expContent.append("log message: trunk/parent-project/project1/module-api/file.txt comment\n");
        expContent.append("\n");
        expContent.append("changed paths:\n");
        expContent.append("A /trunk/parent-project/project1/module-api/file.txt\n");
        expContent.append("\n");
        expContent.append("---------------------------------------------------\n");
        expContent.append("revision: 12\n");
        expContent.append("author: XXX\n");
        expContent.append("date: YYY\n");
        expContent.append("log message: trunk/parent-project/project1/module-core/file.txt comment\n");
        expContent.append("\n");
        expContent.append("changed paths:\n");
        expContent.append("A /trunk/parent-project/project1/module-core/file.txt\n");
        expContent.append("\n");
        expContent.append("---------------------------------------------------\n");
        expContent.append("revision: 13\n");
        expContent.append("author: XXX\n");
        expContent.append("date: YYY\n");
        expContent.append("log message: trunk/parent-project/project1/module-api/file.txt modified\n");
        expContent.append("\n");
        expContent.append("changed paths:\n");
        expContent.append("M /trunk/parent-project/project1/module-api/file.txt\n");

        actualHistoryContent = actualHistoryContent.replaceAll("author:(.)*", "author: XXX");
        actualHistoryContent = actualHistoryContent.replaceAll("date:(.)*", "date: YYY");

        assertEquals(expContent.toString(), actualHistoryContent);
    }


    @Test
    public void asStringCanBeCalledMoreTimesOnSameInstance() throws Exception {
        SVNCommands svnCommands = new SVNCommands();
        svnCommands.addDirWithDefaultComment("trunk");
        svnCommands.addDirWithDefaultComment("trunk/parent-project");
        svnCommands.addDirWithDefaultComment("trunk/parent-project/project2");
        svnCommands.addDirWithDefaultComment("trunk/parent-project/project2/module-api");
        svnCommands.addDirWithDefaultComment("trunk/parent-project/project2/module-core");
        svnCommands.addDirWithDefaultComment("trunk/parent-project/project2/module-integration-test");

        DateTime startDate = new DateTime();

        waitForSeconds(1);

        svnCommands.addFileWithDefaultContentAndComment("trunk/parent-project/project2/module-api/file.txt");

        String repositoryPath = svnCommands.getRepository().getLocation().getPath();

        SourceControlRepositoryFactory sourceControlRepositoryFactory = new SVNSourceControlRepositoryFactory("file:///" + repositoryPath);

        HistoryRetrieverFactory historyRetrieverFactory = new HistoryRetrieverFactoryImpl(sourceControlRepositoryFactory);

        SVNProjectHistoryCreator iut = new SVNProjectHistoryCreator("trunk/parent-project/project1",
                historyRetrieverFactory,
                new PlainTextHistoryFormatter());

        String actualHistoryContent = iut.asString(startDate, new DateTime());

        StringBuilder expContent = new StringBuilder();
        expContent.append("---------------------------------------------------\n");
        expContent.append("revision: 7\n");
        expContent.append("author: XXX\n");
        expContent.append("date: YYY\n");
        expContent.append("log message: trunk/parent-project/project2/module-api/file.txt comment\n");
        expContent.append("\n");
        expContent.append("changed paths:\n");
        expContent.append("A /trunk/parent-project/project2/module-api/file.txt\n");

        actualHistoryContent = actualHistoryContent.replaceAll("author:(.)*", "author: XXX");
        actualHistoryContent = actualHistoryContent.replaceAll("date:(.)*", "date: YYY");

        assertEquals(expContent.toString(), actualHistoryContent);

        actualHistoryContent = iut.asString(startDate, new DateTime());

        expContent = new StringBuilder();
        expContent.append("---------------------------------------------------\n");
        expContent.append("revision: 7\n");
        expContent.append("author: XXX\n");
        expContent.append("date: YYY\n");
        expContent.append("log message: trunk/parent-project/project2/module-api/file.txt comment\n");
        expContent.append("\n");
        expContent.append("changed paths:\n");
        expContent.append("A /trunk/parent-project/project2/module-api/file.txt\n");

        actualHistoryContent = actualHistoryContent.replaceAll("author:(.)*", "author: XXX");
        actualHistoryContent = actualHistoryContent.replaceAll("date:(.)*", "date: YYY");

        assertEquals(expContent.toString(), actualHistoryContent);
    }

    @Test
    public void asStringReturnsEmptyStringIfThereAreNotChangesBetweenDatesSpecifiedInCaseOfLocalRepositoryWithNoLogin() throws Exception {
        SVNCommands svnCommands = new SVNCommands();
        svnCommands.addDirWithDefaultComment("trunk");
        svnCommands.addDirWithDefaultComment("trunk/parent-project");
        svnCommands.addDirWithDefaultComment("trunk/parent-project/project1");
        svnCommands.addDirWithDefaultComment("trunk/parent-project/project1/module-api");
        svnCommands.addDirWithDefaultComment("trunk/parent-project/project1/module-core");
        svnCommands.addDirWithDefaultComment("trunk/parent-project/project1/module-integration-test");

        svnCommands.addFileWithDefaultContentAndComment("trunk/parent-project/project1/module-api/file.txt");

        svnCommands.addFileWithDefaultContentAndComment("trunk/parent-project/project1/module-core/file.txt");

        svnCommands.modifyFile("trunk/parent-project/project1/module-api", "file.txt");

        String repositoryPath = svnCommands.getRepository().getLocation().getPath();

        SourceControlRepositoryFactory sourceControlRepositoryFactory = new SVNSourceControlRepositoryFactory("file:///" + repositoryPath);

        HistoryRetrieverFactory historyRetrieverFactory = new HistoryRetrieverFactoryImpl(sourceControlRepositoryFactory);

        SVNProjectHistoryCreator iut = new SVNProjectHistoryCreator("trunk/parent-project/project1",
                historyRetrieverFactory,
                new PlainTextHistoryFormatter());

        DateTime startDate = new DateTime().plusSeconds(1);

        assertEquals("", iut.asString(startDate, startDate.plusSeconds(1)));
    }

    private void waitForSeconds(int numberOfSecondsToWaitFor) {
        try {
            Thread.sleep(numberOfSecondsToWaitFor * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}