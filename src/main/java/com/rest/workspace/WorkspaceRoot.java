package com.rest.workspace;

public class WorkspaceRoot {
    Workspace workspace;

    // when de-serialising jackson looks for default constructor s
    public WorkspaceRoot() {
    }
    public WorkspaceRoot(Workspace workspace) {
        this.workspace = workspace;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }
}
