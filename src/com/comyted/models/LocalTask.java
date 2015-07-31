package com.comyted.models;

public class LocalTask {

	public TaskEdit Task;
	public  int Id;
	public  int Action;

	public LocalTask(TaskEdit task, int id, int action) {
		this.Task = task;
		this.Id= id;
		this.Action = action;
	}

}
