package org.bbf.maven;

/**
 * User: robertom
 * Date: May 29, 2010
 * Time: 3:29:19 PM
 */
public interface ArtifactsFinder {
    Artifacts getArtifacts();

    Artifacts getArtifactsByGroupId(String groupId);
}
