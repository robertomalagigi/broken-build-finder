package org.bbf.svn;

import org.apache.commons.io.FileUtils;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.io.diff.SVNDeltaGenerator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/*
* This is an example of how to commit several types of changes to a repository:
*  - a new directory with a file,
*  - modification to anexisting file,
*  - copying a directory into a branch,
*  - deletion of the directory and its entries.
*
* Main aspects of performing a commit with the help of ISVNEditor:
* 0)initialize the library (this is done in setupLibrary() method);
*
* 1)create an SVNRepository driver for a particular  repository  location, that
* will be the root directory for committing - that is all paths that are  being
* committed will be below that root;
*
* 2)provide user's authentication information - name/password, since committing
* generally requires authentication;
*
* 3)"ask" your SVNRepository for a commit editor:
*
*     ISVNCommitEditor editor = SVNRepository.getCommitEditor();
*
* 4)"edit"  a repository - perform a sequence of edit calls (for
* example, here you "say" to the server that you have added such-and-such a new
* directory at such-and-such a path as well as a new file). First of all,
* ISVNEditor.openRoot()  is called to 'open' the root directory;
*
* 5)at last you close the editor with the  ISVNEditor.closeEdit()  method  that
* fixes your modificaions in the repository finalizing the commit.
*
* For  each  commit  a  new  ISVNEditor is required - that is after having been
* closed the editor can no longer be used!
*
* This example can be run for a locally installed Subversion repository via the
* svn:// protocol. This is how you can do it:
*
* 1)after   you   install  the   Subversion  pack (available  for  download  at
* http://subversion.tigris.org)  you  should  create  a  new  repository in  a
* directory, like this (in a command line under a Windows OS):
*
* >svnadmin create X:\path\to\rep
*
* 2)after the repository is created you can add a new account: navigate to your
* repository root (X:\path\to\rep\),  then  move  to  \conf  and  open the file
* 'passwd'. In the file you'll see the section [users]. Uncomment it and add  a
* new account below the section name, like:
*
* [users]
* userName = userPassword.
*
* In the program you may further use this account as user's credentials.
*
* 3)the next step is to launch the custom Subversion  server  (svnserve)  in  a
* background mode for the just created repository:
*
* >svnserve -d -r X:\path\to
*
* That's all. The repository is now available via svn://localhost/rep.
*
*/

/**
 * User: robertom
 * Date: May 31, 2010
 * Time: 10:33:37 AM
 */
public class SVNCommands {
    private static final File REPOSITORY_LOCATION = new File("target/testRepo").getAbsoluteFile();
    private SVNRepository repository;

    public SVNCommands() {
        FSRepositoryFactory.setup();
        repository = createLocalRepository();
    }

    private SVNRepository createLocalRepository() {
        boolean enableRevisionProperties = true;
        boolean forceRepoCreation = true;
        SVNURL repositoryUrl = null;
        try {
            repositoryUrl = SVNRepositoryFactory.createLocalRepository(REPOSITORY_LOCATION, enableRevisionProperties, forceRepoCreation);
        } catch (SVNException e) {
            throw new RuntimeException("Cannot create repository " + REPOSITORY_LOCATION, e);
        }
        return retrieveRepository(repositoryUrl);
    }

    private SVNRepository retrieveRepository(SVNURL repositoryUrl) {
        try {
            return SVNRepositoryFactory.create(repositoryUrl);
        } catch (SVNException e) {
            throw new RuntimeException("Cannot connect to " + repositoryUrl, e);
        }
    }

    void deleteRepository() {
        try {
            FileUtils.deleteDirectory(REPOSITORY_LOCATION);
        } catch (IOException e) {
            throw new RuntimeException("Cannot delete repository at " + REPOSITORY_LOCATION, e);
        }
    }

    SVNRepository getRepository() {
        return repository;
    }

    /*
     * This method performs commiting an addition of a  directory  containing  a
     * file.
     */

    SVNCommitInfo addDir(String comment, String dirPath) throws SVNException {
        ISVNEditor editor = repository.getCommitEditor(comment, null);

        /*
        * Always called first. Opens the current root directory. It  means  all
        * modifications will be applied to this directory until  a  next  entry
        * (located inside the root) is opened/added.
        *
        * -1 - revision is HEAD (actually, for a comit  editor  this number  is
        * irrelevant)
        */
        editor.openRoot(-1);
        /*
        * Adds a new directory (in this  case - to the  root  directory  for
        * which the SVNRepository was  created).
        * Since this moment all changes will be applied to this new  directory.
        *
        * dirPath is relative to the root directory.
        *
        * copyFromPath (the 2nd parameter) is set to null and  copyFromRevision
        * (the 3rd) parameter is set to  -1  since  the  directory is not added
        * with history (is not copied, in other words).
        */
        editor.addDir(dirPath, null, -1);
        /*
        * Closes the new added directory.
        */
        editor.closeDir();
        /*
        * Closes the root directory.
        */
        editor.closeDir();
        /*
        * This is the final point in all editor handling. Only now all that new
        * information previously described with the editor's methods is sent to
        * the server for committing. As a result the server sends the new
        * commit information.
        */
        return editor.closeEdit();
    }


    SVNCommitInfo addFile(String comment,
                          String filePath, String fileContent) throws SVNException {
        ISVNEditor editor = repository.getCommitEditor(comment, null);
        /*
         * Always called first. Opens the current root directory. It  means  all
         * modifications will be applied to this directory until  a  next  entry
         * (located inside the root) is opened/added.
         *
         * -1 - revision is HEAD (actually, for a comit  editor  this number  is
         * irrelevant)
         */
        editor.openRoot(-1);
        /*
         * Adds a new file to the just added  directory. The  file  path is also
         * defined as relative to the root directory.
         *
         * copyFromPath (the 2nd parameter) is set to null and  copyFromRevision
         * (the 3rd parameter) is set to -1 since  the file is  not  added  with
         * history.
         */
        editor.addFile(filePath, null, -1);
        /*
         * The next steps are directed to applying delta to the  file  (that  is
         * the full contents of the file in this case).
         */
        editor.applyTextDelta(filePath, null);
        /*
         * Use delta generator utility class to generate and send delta
         *
         * Note that you may use only 'target' data to generate delta when there is no
         * access to the 'base' (previous) version of the file. However, using 'base'
         * data will result in smaller network overhead.
         *
         * SVNDeltaGenerator will call editor.textDeltaChunk(...) method for each generated
         * "diff window" and then editor.textDeltaEnd(...) in the end of delta transmission.
         * Number of diff windows depends on the file size.
         *
         */
        SVNDeltaGenerator deltaGenerator = new SVNDeltaGenerator();
        String checksum = deltaGenerator.sendDelta(filePath, new ByteArrayInputStream(fileContent.getBytes()), editor, true);

        /*
         * Closes the new added file.
         */
        editor.closeFile(filePath, checksum);
        /*
         * Closes the root directory.
         */
        editor.closeDir();
        /*
         * This is the final point in all editor handling. Only now all that new
         * information previously described with the editor's methods is sent to
         * the server for committing. As a result the server sends the new
         * commit information.
         */
        return editor.closeEdit();
    }

    /*
     * This method performs committing file modifications.
     */

    SVNCommitInfo modifyFile(String comment, String dirPath,
                             String filePath, String oldData, String newData) throws SVNException {
        ISVNEditor editor = repository.getCommitEditor(comment, null);

        /*
         * Always called first. Opens the current root directory. It  means  all
         * modifications will be applied to this directory until  a  next  entry
         * (located inside the root) is opened/added.
         *
         * -1 - revision is HEAD
         */
        editor.openRoot(-1);
        /*
         * Opens a next subdirectory (in this example program it's the directory
         * added  in  the  last  commit).  Since this moment all changes will be
         * applied to this directory.
         *
         * dirPath is relative to the root directory.
         * -1 - revision is HEAD
         */
        editor.openDir(dirPath, -1);
        /*
         * Opens the file added in the previous commit.
         *
         * filePath is also defined as a relative path to the root directory.
         */
        editor.openFile(filePath, -1);

        /*
         * The next steps are directed to applying and writing the file delta.
         */
        editor.applyTextDelta(filePath, null);

        /*
         * Use delta generator utility class to generate and send delta
         *
         * Note that you may use only 'target' data to generate delta when there is no
         * access to the 'base' (previous) version of the file. However, here we've got 'base'
         * data, what in case of larger files results in smaller network overhead.
         *
         * SVNDeltaGenerator will call editor.textDeltaChunk(...) method for each generated
         * "diff window" and then editor.textDeltaEnd(...) in the end of delta transmission.
         * Number of diff windows depends on the file size.
         *
         */
        SVNDeltaGenerator deltaGenerator = new SVNDeltaGenerator();
        String checksum = deltaGenerator.sendDelta(filePath, new ByteArrayInputStream(oldData.getBytes()),
                0, new ByteArrayInputStream(newData.getBytes()), editor, true);

        /*
         * Closes the file.
         */
        editor.closeFile(filePath, checksum);

        /*
         * Closes the directory.
         */
        editor.closeDir();

        /*
         * Closes the root directory.
         */
        editor.closeDir();

        /*
         * This is the final point in all editor handling. Only now all that new
         * information previously described with the editor's methods is sent to
         * the server for committing. As a result the server sends the new
         * commit information.
         */
        return editor.closeEdit();
    }

    /*
     * This method performs committing a deletion of a directory.
     */

    private static SVNCommitInfo deleteDir(ISVNEditor editor, String dirPath) throws SVNException {
        /*
         * Always called first. Opens the current root directory. It  means  all
         * modifications will be applied to this directory until  a  next  entry
         * (located inside the root) is opened/added.
         *
         * -1 - revision is HEAD
         */
        editor.openRoot(-1);
        /*
         * Deletes the subdirectory with all its contents.
         *
         * dirPath is relative to the root directory.
         */
        editor.deleteEntry(dirPath, -1);
        /*
         * Closes the root directory.
         */
        editor.closeDir();
        /*
         * This is the final point in all editor handling. Only now all that new
         * information previously described with the editor's methods is sent to
         * the server for committing. As a result the server sends the new
         * commit information.
         */
        return editor.closeEdit();
    }

}
