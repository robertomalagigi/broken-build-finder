package org.bbf.maven;

import org.junit.Test;

/**
 * User: robertom
 * Date: May 30, 2010
 * Time: 11:36:56 AM
 */
public class SimpleArtifact {
    private String groupId;
    private String artifactId;
    private String version;
    private String scope;
    private String type;

    public SimpleArtifact(String groupId, String artifactId, String version, String scope, String type) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.scope = scope;
        this.type = type;
    }

    public boolean hasGroupId(String groupId) {
        return groupId.equals(this.groupId);
    }

    public boolean hasArtifactId(String artifactId) {
        return artifactId.equals(this.artifactId);
    }

    public boolean hasVersion(String version) {
        return version.equals(this.version);
    }

    public boolean hasScope(String scope) {
        return scope.equals(this.scope);
    }

    public boolean hasType(String type) {
        return type.equals(this.type);
    }

    @Override
    public String toString() {
        return "groupId :" + groupId + " artifactId :" + artifactId + " version :" + version + " scope : " + scope + " type : " + type;
    }
}
