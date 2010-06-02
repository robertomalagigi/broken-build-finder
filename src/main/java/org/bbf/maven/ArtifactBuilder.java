package org.bbf.maven;

/**
 * User: robertom
 * Date: May 29, 2010
 * Time: 7:10:05 PM
 */
public class ArtifactBuilder {
    private String artifactId;
    private String groupId;
    private String version;
    private String scope = "compile";
    private String type = "jar";

    public ArtifactBuilder withScope(String scope) {
        this.scope = scope;
        return this;
    }

    public ArtifactBuilder withArtifactId(String artifactId) {
        this.artifactId = artifactId;
        return this;
    }

    public ArtifactBuilder withGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public ArtifactBuilder withVersion(String version) {
        this.version = version;
        return this;
    }

    public ArtifactBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public static ArtifactBuilder mavenArtifactory() {
        return new ArtifactBuilder();
    }

    public SimpleArtifact build() {
        return new SimpleArtifact(groupId, artifactId, version, scope, type);
    }
}
