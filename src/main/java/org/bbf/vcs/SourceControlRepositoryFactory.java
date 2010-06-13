package org.bbf.vcs;

/**
 * User: robertom
 * Date: Jun 5, 2010
 * Time: 4:57:25 PM
 */
public interface SourceControlRepositoryFactory {

    SourceControlRepository createSourceControlRepository(String projectSimbolicName);

}