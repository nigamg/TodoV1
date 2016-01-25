package com.example.todo.sql.db;

public class Todo {
    private String toDoName;
	private int status;
	private int id;
	
	public Todo()
	{
		this.toDoName=null;
		this.status=0;
	}
	public Todo(String toDoName, int status) {
		super();
		this.toDoName = toDoName;
		this.status = status;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
    public String getToDoName() {
        return toDoName;
    }

    public void setToDoName(String toDoName) {
        this.toDoName = toDoName;
    }


}
