package org.bbf.maven;

import jedi.functional.Filter;
import jedi.functional.FirstOrderLogic;
import org.apache.maven.artifact.Artifact;

import java.util.List;

/**
 * User: robertom
 * Date: May 29, 2010
 * Time: 3:33:19 PM
 */
public class Artifacts {
    private List<Artifact> artifacts;

    public Artifacts(List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    public int size() {
        return artifacts.size();
    }

    public boolean hasArtifact(final SimpleArtifact simpleArtifact) {
        return FirstOrderLogic.exists(artifacts, new Filter<Artifact>() {
            public Boolean execute(Artifact internalArtifact) {
                return simpleArtifact.hasArtifactId(internalArtifact.getArtifactId()) &&
                        simpleArtifact.hasGroupId(internalArtifact.getGroupId()) &&
                        simpleArtifact.hasVersion(internalArtifact.getVersion()) &&
                        simpleArtifact.hasScope(internalArtifact.getScope()) &&
                        simpleArtifact.hasType(internalArtifact.getType());
            }
        });
    }
}
