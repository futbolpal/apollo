package framework;

import project.Project;

public interface ApolloFrameworkListener extends FrameworkListener{
	public void projectChanged(Project p);
}
