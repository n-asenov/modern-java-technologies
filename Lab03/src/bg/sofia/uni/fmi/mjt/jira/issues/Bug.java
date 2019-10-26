package bg.sofia.uni.fmi.mjt.jira.issues;

import bg.sofia.uni.fmi.mjt.jira.enums.IssuePriority;
import bg.sofia.uni.fmi.mjt.jira.enums.IssueResolution;
import bg.sofia.uni.fmi.mjt.jira.enums.WorkAction;

public class Bug extends Issue {
	private boolean fixActionTaken;
	private boolean testsActionTaken;
	
	public Bug(IssuePriority priority, Component component, String description) {
		super(priority, component, description);
		
		fixActionTaken = false;
		testsActionTaken = false;
	}

	@Override
	public void resolve(IssueResolution resolution) {
		if (!fixActionTaken) {
			throw new RuntimeException("You must have action with Fix tag");
		}

		if (!testsActionTaken) {
			throw new RuntimeException("You must have action with Tests tag");
		}

		super.setResolution(resolution);
	}
	
	@Override
	public void addAction(WorkAction action, String description) {
		if (action == WorkAction.FIX) {
			fixActionTaken = true;
		} else if (action == WorkAction.TESTS) {
			testsActionTaken = true;
		}
		
		super.addAction(action, description);
	}
}
