package bg.sofia.uni.fmi.mjt.jira.issues;

import java.time.LocalDateTime;

import bg.sofia.uni.fmi.mjt.jira.enums.IssuePriority;
import bg.sofia.uni.fmi.mjt.jira.enums.IssueResolution;
import bg.sofia.uni.fmi.mjt.jira.enums.IssueStatus;
import bg.sofia.uni.fmi.mjt.jira.enums.WorkAction;

public abstract class Issue {
	private final LocalDateTime createdOn;

	private String issueID;
	private String description;
	private IssuePriority priority;
	private IssueResolution resolution;
	private IssueStatus status;
	private Component component;
	private String[] actionLog;
	private LocalDateTime lastModifiedOn;

	private static int counter = 0;
	private int actionLogCounter;

	public Issue(IssuePriority priority, Component component, String description) {
		issueID = component.getShortName() + "-" + counter;
		counter++;
	
		this.priority = priority;
		this.component = component;
		this.description = description;

		status = IssueStatus.OPEN;
		resolution = IssueResolution.UNRESOLVED;

		actionLog = new String[20];
		actionLogCounter = 0;

		createdOn = LocalDateTime.now();
		lastModifiedOn = LocalDateTime.now();
	}

	public String getIssueID() {
		return issueID;
	}

	public String getDescription() {
		return description;
	}

	public IssuePriority getPriority() {
		return priority;
	}

	public IssueResolution getResolution() {
		return resolution;
	}

	public IssueStatus getStatus() {
		return status;
	}

	public Component getComponent() {
		return component;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public LocalDateTime getLastModifiedOn() {
		return lastModifiedOn;
	}

	public String[] getActionLog() {
		return actionLog;
	}
	
	public int getNumberOfActionsTaken() {
		return actionLogCounter;
	}

	protected void setResolution(IssueResolution resolution) {
		this.resolution = resolution;
		lastModifiedOn = LocalDateTime.now();
	}
	
	public void setStatus(IssueStatus status) {
		this.status = status;
		lastModifiedOn = LocalDateTime.now();
	}

	public void addAction(WorkAction action, String description) {
		if (description == null) {
			throw new RuntimeException("Action must have a description");
		}
		
		if (actionLogCounter == 20) {
			throw new RuntimeException("You reached the maximum possible actions");
		}
		
		actionLog[actionLogCounter] = action.toString().toLowerCase() + ": " + description;
		actionLogCounter++;
		lastModifiedOn = LocalDateTime.now();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null) {
			return false;
		}
		
		Issue other = (Issue) obj;
		
		return this.issueID.equals(other.issueID);
	}
	
	public abstract void resolve(IssueResolution resolution);
}
