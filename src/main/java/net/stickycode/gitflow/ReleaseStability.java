package net.stickycode.gitflow;

public enum ReleaseStability {

  GitFlow {
    @Override
    public String transform(String branch) {
      if ("master".equals(branch))
        return "stable";

      if ("develop".equals(branch))
        return "unstable";

      return "chaos";
    }
  },
  Unstable {

    @Override
    public String transform(String branch) {
      return "unstable";
    }
  };

  abstract public String transform(String branch);

}
