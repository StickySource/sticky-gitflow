package net.stickycode.gitflow;

import static org.assertj.core.api.StrictAssertions.assertThat;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.assertj.core.util.Files;
import org.junit.Test;

public class GitFlowExtensionTest {

  @Test
  public void branch() throws MojoExecutionException, MojoFailureException {
    assertThat(new GitFlowExtension().findBranch(Files.currentFolder())).isEqualTo("master");
  }

  @Test(expected = RuntimeException.class)
  public void noGit() throws MojoExecutionException, MojoFailureException {
    assertThat(new GitFlowExtension().findBranch(Files.temporaryFolder())).isEqualTo("master");
  }

  @Test(expected = RuntimeException.class)
  public void noPermission() throws MojoExecutionException, MojoFailureException {
    assertThat(new GitFlowExtension().findBranch(new File("/sbin/"))).isEqualTo("master");
  }

  @Test(expected = NullPointerException.class)
  public void mojo() throws MojoExecutionException, MojoFailureException {
    new GitFlowExtension().afterProjectsRead(null);
  }

  @Test
  public void masterIsStable() throws MojoExecutionException, MojoFailureException {
    assertThat(ReleaseStability.GitFlow.transform("master")).isEqualTo("stable");
    assertThat(ReleaseStability.GitFlow.transform("develop")).isEqualTo("unstable");
    assertThat(ReleaseStability.GitFlow.transform("something")).isEqualTo("chaos");
  }

  @Test
  public void groupIsStable() {
    assertThat(new GitFlowExtension().applyStability("com.example.stable.a", "stable")).isEqualTo("com.example.stable.a");
    assertThat(new GitFlowExtension().applyStability("com.example.stable.a", "unstable")).isEqualTo("com.example.unstable.a");
    assertThat(new GitFlowExtension().applyStability("com.example.stable.a", "chaos")).isEqualTo("com.example.chaos.a");
  }
  
}
