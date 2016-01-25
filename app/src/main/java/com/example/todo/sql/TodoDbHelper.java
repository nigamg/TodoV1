package com.example.todo.sql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todo.sql.db.Todo;

public class TodoDbHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "todoManager";

	// todo table name
	private static final String TABLE_TASKS = "todos";

	// tasks Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_TASKNAME = "todoName";
	private static final String KEY_STATUS = "status";

    SQLiteDatabase db;

	public TodoDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

    public TodoDbHelper open()throws SQLException
    {

        db = this.getWritableDatabase();
        return this;
    }

	@Override
	public void onCreate(SQLiteDatabase db) {

		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_TASKS + " ( "
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TASKNAME
				+ " TEXT, " + KEY_STATUS + " INTEGER)";
		db.execSQL(sql);
	
		//db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
		// Create tables again
		onCreate(db);
	}

	// Adding new task
	public void addTask(Todo task) {

		ContentValues values = new ContentValues();
		values.put(KEY_TASKNAME, task.getToDoName()); // task name
		// status of task- can be 0 for not done and 1 for done
		values.put(KEY_STATUS, task.getStatus());

		// Inserting Row
		db.insert(TABLE_TASKS, null, values);
		db.close(); // Closing database connection
	}

	public List<Todo> getAllTasks() {
		List<Todo> taskList = new ArrayList<Todo>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_TASKS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Todo todo = new Todo();
                todo.setId(cursor.getInt(0));
                todo.setToDoName(cursor.getString(1));
                todo.setStatus(cursor.getInt(2));
				// Adding contact to list
				taskList.add(todo);
			} while (cursor.moveToNext());
		}

		// return task list
		return taskList;
	}

    /***
     *  Return todo
     * @param id
     * @return
     */
    public Todo getTodoTask(int id){
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE id="+id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Todo todo=null;
        if(cursor.moveToNext()){
            do{
                todo = new Todo();
                todo.setId(cursor.getInt(0));
                todo.setToDoName(cursor.getString(1));
                todo.setStatus(cursor.getInt(2));
                return todo;
            }while (cursor.moveToNext());
        }
        return todo;
    }

	public void updateTask(Todo todo) {
		// updating row
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_TASKNAME, todo.getToDoName());
		values.put(KEY_STATUS, todo.getStatus());
		db.update(TABLE_TASKS, values, KEY_ID + " = ?",new String[] {String.valueOf(todo.getId())});
		db.close();
	}

}
