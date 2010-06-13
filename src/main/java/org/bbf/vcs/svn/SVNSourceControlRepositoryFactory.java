package org.bbf.vcs.svn;

import org.bbf.vcs.SourceControlRepository;
import org.bbf.vcs.SourceControlRepositoryFactory;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;

/**
 * User: robertom
 * Date: Jun 6, 2010
 * Time: 12:47:23 PM
 */
public class SVNSourceControlRepositoryFactory implements SourceControlRepositoryFactory {
    private final String repositoryUrl;
    private final String userName;
    private final String password;

    public SVNSourceControlRepositoryFactory(String repositoryUrl, String userName, String password) {
        this.repositoryUrl = repositoryUrl;
        this.userName = userName;
        this.password = password;
    }

    public SVNSourceControlRepositoryFactory(String repositoryUrl) {
        this(repositoryUrl, null, null);
    }

    @Override
    public SourceControlRepository createSourceControlRepository(String projectSimbolicName) {
        DAVRepositoryFactory.setup();
        SVNRepositoryFactoryImpl.setup();
        FSRepositoryFactory.setup();
        SVNRepository repository = null;
        try {
            repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(repositoryUrl));
        } catch (SVNException e) {
            throw new RuntimeException("Problem connecting to " + repositoryUrl, e);
        }
        if (useAuthentication()) {
            ISVNAuthenticationManager basicAuthManager = new BasicAuthenticationManager(userName, password);
            repository.setAuthenticationManager(basicAuthManager);
        }
        return new SVNSourceControlRepository(repository);
    }

    private boolean useAuthentication() {
        return userName != null && password != null;
    }

}