package bg.sofia.uni.fmi.mjt.jira.issues;

import bg.sofia.uni.fmi.mjt.jira.enums.IssuePriority;
import bg.sofia.uni.fmi.mjt.jira.enums.IssueResolution;
import bg.sofia.uni.fmi.mjt.jira.enums.WorkAction;

public class Task extends Issue {
	public Task(IssuePriority priority, Component component, String description) {
		super(priority, component, description);
	}

	@Override
	public void resolve(IssueResolution resolution) {
		super.setResolution(resolution);
	}

	@Override
	public void addAction(WorkAction action, String description) {
		if (action == WorkAction.FIX || action == WorkAction.IMPLEMENTATION 
				|| action == WorkAction.TESTS) {
			throw new RuntimeException("Action cannot be " + action);
		}

		super.addAction(action, description);
	}
}
