package net.stickycode.gitflow;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class GitflowMojo
    extends AbstractMojo {

  private File baseDirectory;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    String branch = findBranch(baseDirectory);
    getLog().info("Active branch is " + branch);

  }

  String findBranch(File baseDirectory) throws MojoExecutionException {
    FileRepositoryBuilder builder = new FileRepositoryBuilder()
        .setWorkTree(baseDirectory)
        .readEnvironment()
        .findGitDir();

    try (Repository repository = builder.build()) {
      return repository.getBranch();
    }
    catch (IOException e) {
      throw new MojoExecutionException("Failed to discover the current branch", e);
    }
  }

}
