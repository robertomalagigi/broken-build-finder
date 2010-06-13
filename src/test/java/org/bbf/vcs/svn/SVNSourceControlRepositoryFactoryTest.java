package org.bbf.vcs.svn;

import org.bbf.vcs.SourceControlRepository;
import org.bbf.vcs.SourceControlRepositoryFactory;
import org.junit.After;
import org.junit.Test;
import org.tmatesoft.svn.core.SVNException;

import static junit.framework.Assert.assertNotNull;

/**
 * User: robertom
 * Date: Jun 2, 2010
 * Time: 9:23:00 AM
 */
public class SVNSourceControlRepositoryFactoryTest {

    @After
    public void tearDown() {
        new SVNCommands().deleteRepository();
    }

    @Test
    public void weCanConnectToASVNLocalRepositoryWithoutUsingPassword() throws SVNException {
        SVNCommands svnCommands = new SVNCommands();
        String path = svnCommands.getRepository().getLocation().getPath();

        svnCommands.addDir("this is a comment", "dir1");

        SourceControlRepositoryFactory iut = new SVNSourceControlRepositoryFactory("file:///" + path);

        SourceControlRepository localSourceControlRepository = iut.createSourceControlRepository("");

        assertNotNull(localSourceControlRepository.log(1, 1));
    }

}