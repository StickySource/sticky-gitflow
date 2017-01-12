package net.stickycode.gitflow;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.maven.plugin.MojoExecutionException;
import org.assertj.core.util.Files;
import org.junit.Test;

public class GitFlowMojoTest {

  @Test
  public void branch() throws MojoExecutionException {
    assertThat(new GitflowMojo().findBranch(Files.currentFolder())).isEqualTo("master");
  }
}
