package net.stickycode.gitflow;

import java.io.File;
import java.io.IOException;

import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

@Component(role = AbstractMavenLifecycleParticipant.class, hint = "gitflow")
public class GitFlowExtension
    extends AbstractMavenLifecycleParticipant {

  @Requirement
  private Logger logger;

  public void afterProjectsRead(MavenSession session) {
    MavenProject project = session.getCurrentProject();
    String branch = findBranch(project.getBasedir());
    ReleaseStability releaseStability = deriveStrategy(project);
    logger.info("Active branch is " + branch + " with naming strategy " + releaseStability.name());
    String stability = releaseStability.transform(branch);
    String gitflowGroup = applyStability(project.getGroupId(), stability);
    logger.info("Setting group to " + gitflowGroup);
    project.setGroupId(gitflowGroup);
  }

  private ReleaseStability deriveStrategy(MavenProject project) {
    String property = project.getProperties().getProperty("releaseStabilityStrategy");
    if (null == property) {
      logger.warn("releaseStabilityStrategy is not set defaulting to GitFlow");
      return ReleaseStability.GitFlow;
    }
    return ReleaseStability.valueOf(property);
  }

  String applyStability(String groupId, String stability) {
    return groupId.replaceAll("stable", stability);
  }

  String findBranch(File baseDirectory) {
    FileRepositoryBuilder builder = new FileRepositoryBuilder()
        .setWorkTree(baseDirectory)
        .findGitDir(baseDirectory);

    if (builder.getGitDir() == null)
      throw new RuntimeException("Git directory not found in " + baseDirectory.getAbsolutePath());

    try (Repository repository = builder.build()) {
      return repository.getBranch();
    }
    catch (IOException e) {
      throw new RuntimeException("Failed to discover the current branch", e);
    }
  }

}
