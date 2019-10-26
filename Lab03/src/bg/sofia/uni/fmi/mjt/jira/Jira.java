package bg.sofia.uni.fmi.mjt.jira;

import bg.sofia.uni.fmi.mjt.jira.enums.IssueResolution;
import bg.sofia.uni.fmi.mjt.jira.enums.WorkAction;
import bg.sofia.uni.fmi.mjt.jira.interfaces.Filter;
import bg.sofia.uni.fmi.mjt.jira.interfaces.Repository;
import bg.sofia.uni.fmi.mjt.jira.issues.Issue;

public class Jira implements Filter, Repository {
	private Issue[] issues;
	private int numberOfIssues;

	public Jira() {
		issues = new Issue[100];
		numberOfIssues = 0;
	}

	@Override
	public void addIssue(Issue issue) {
		if (numberOfIssues == 100) {
			throw new RuntimeException("You cannot have more issues");
		}

		for (int index = 0; index < numberOfIssues; index++) {
			if (issues[index].equals(issue)) {
				throw new RuntimeException("Issue is already added");
			}
		}

		issues[numberOfIssues] = issue;
		numberOfIssues++;
	}

	@Override
	public Issue find(String issueID) {
		for (int index = 0; index < numberOfIssues; index++) {
			if (issues[index].getIssueID().equals(issueID)) {
				return issues[index];
			}
		}

		return null;
	}

	public void addActionToIssue(Issue issue, WorkAction action, String actionDescription) {
		for (int index = 0; index < numberOfIssues; index++) {
			if (issues[index].equals(issue)) {
				issue.addAction(action, actionDescription);
				return;
			}
		}
	}

	public void resolveIssue(Issue issue, IssueResolution resolution) {
		for (int index = 0; index < numberOfIssues; index++) {
			if (issues[index].equals(issue)) {
				issue.resolve(resolution);
				return;
			}
		}
	}
}
