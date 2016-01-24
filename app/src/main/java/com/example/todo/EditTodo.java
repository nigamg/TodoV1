package com.example.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;

import java.sql.SQLException;


public class EditTodo extends Activity implements OnClickListener {

    Button editSaveButton;
    protected TodoDbHelper db;
    private int id;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);

        editSaveButton = (Button)findViewById(R.id.editSaveButton);
        editSaveButton.setOnClickListener(this);

        try {
            db = new TodoDbHelper(this);
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // get the item's position and id from the intent extra data
        position = getIntent().getIntExtra("position", 0);
        id = (int)getIntent().getIntExtra("id", -1);

        Todo todo = db.getTodoTask(id);

        // update the edit text fields and set focus on the title
        EditText editText = (EditText) findViewById(R.id.editText2);

        editText.setText(todo.getToDoName());
        editText.requestFocus();
        editText.setSelection(todo.getToDoName().length());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (editSaveButton.isPressed()) {
            // When back button is pressed
            // Create an intent
            Intent intent = new Intent(this, TodoView.class);

            // update the edit text fields and set focus on the title
            EditText editText = (EditText) findViewById(R.id.editText2);

            Todo todo = db.getTodoTask(id);
            String s = editText.getText().toString();

            // update todo
            todo.setToDoName(s);

            db.updateTask(todo);

            // Start activity
            startActivity(intent);
            // Finish this activity
            this.finish();
        }
    }
}
