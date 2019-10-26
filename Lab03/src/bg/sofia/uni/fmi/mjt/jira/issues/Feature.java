package bg.sofia.uni.fmi.mjt.jira.issues;

import bg.sofia.uni.fmi.mjt.jira.enums.IssuePriority;
import bg.sofia.uni.fmi.mjt.jira.enums.IssueResolution;
import bg.sofia.uni.fmi.mjt.jira.enums.WorkAction;

public class Feature extends Issue {
	private boolean designActionTaken;
	private boolean implementationActionTaken;
	private boolean testsActionTaken;
	
	public Feature(IssuePriority priority, Component component, String description) {
		super(priority, component, description);

		designActionTaken = false;
		implementationActionTaken = false;
		testsActionTaken = false;
	}

	@Override
	public void resolve(IssueResolution resolution) {
		if (!designActionTaken) {
			throw new RuntimeException("You mush have action with Desing tag");
		} 
		
		if (!implementationActionTaken) {
			throw new RuntimeException("You must have action with Implementation tag");
		}
		
		if (!testsActionTaken) {
			throw new RuntimeException("You must have action with Tests tag");
		}

		super.setResolution(resolution);
	}
	
	@Override
	public void addAction(WorkAction action, String description) {
		if (action == WorkAction.DESIGN) {
			designActionTaken = true;
		} else if (action == WorkAction.IMPLEMENTATION) {
			implementationActionTaken = true;
		} else if (action == WorkAction.TESTS) {
			testsActionTaken = true;
		}
		
		super.addAction(action, description);
	}
}
