package com.example.todo.mainscreen;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.todo.R;
import com.example.todo.sql.db.Todo;
import com.example.todo.sql.TodoDbHelper;

public class CreateTodoItemActivity extends FragmentActivity implements EditTodoDialogFragment.EditTodoDialogFragmentListener {
	protected TodoDbHelper db;
	List<Todo> list;
	MyAdapter adapt;

    /**
     * Request code for the edit item intent
     */
    private final int EDIT_ITEM_REQUEST_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_task);
        db = new TodoDbHelper(this);
        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        list = db.getAllTasks();
		adapt = new MyAdapter(this, R.layout.list_inner_view, list);
		ListView listView = (ListView) findViewById(R.id.listView1);
        listView.setAdapter(adapt);


        // set up listeners here
        // set up list view listeners
        // remove item on long click
        listView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id
                    ) {

                        /*Todo item = items.get(position);
                        if (db.deleteItem(item)) {
                            items.remove(position);
                            itemsAdapter.notifyDataSetChanged();
                        }*/
                        return true;
                    }
                });

        // edit item on click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(
                    AdapterView<?> parent,
                    View view,
                    int position,
                    long id) {

                /*// create a new intent to edit the item
                Intent editItemIntent = new Intent(CreateTodoItemActivity.this, EditTodoItemActivity.class);

                // add the item's position and info to the intent
                //Todo selectedItem = list.get(position);

                editItemIntent.putExtra("position", position);
                editItemIntent.putExtra("id", selectedItem.getId());

                startActivityForResult(editItemIntent, EDIT_ITEM_REQUEST_CODE);*/
                showEditDialog(position);
            }
        });
	}

    /***
     * Show edit dialog with selected todo item
     * @param position
     */
    private void showEditDialog(int position) {

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        EditTodoDialogFragment editTodoItemDialogFragmentDialog = EditTodoDialogFragment.newInstance("Edit Todo");

        Todo selectedItem = list.get(position);

        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putInt("id", selectedItem.getId());

        editTodoItemDialogFragmentDialog.setArguments(args);
        editTodoItemDialogFragmentDialog.show(fm, "fragment_edit_todo_dialog");

	}

    /***
     *  Add todo task
     * @param v
     */
	public void addTaskNow(View v) {
		EditText t = (EditText) findViewById(R.id.editText1);
		String s = t.getText().toString();
		if (s.equalsIgnoreCase("")) {
			Toast.makeText(this, "enter the todo description first!!",
					Toast.LENGTH_LONG);
		} else {
			Todo task = new Todo(s, 0);
			db.addTask(task);
			Log.d("tasker", "data added");
			t.setText("");
			adapt.add(task);
			adapt.notifyDataSetChanged();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_view_task, menu);
		return true;
	}

    @Override
    public void onFinishEditTodoDialog(String inputText) {
        Toast.makeText(this, inputText + "successfully updated todo.", Toast.LENGTH_SHORT).show();
    }

    private class MyAdapter extends ArrayAdapter<Todo> {

		Context context;
		List<Todo> toDoList = new ArrayList<Todo>();
		int layoutResourceId;

		public MyAdapter(Context context, int layoutResourceId,
				List<Todo> objects) {
			super(context, layoutResourceId, objects);
			this.layoutResourceId = layoutResourceId;
			this.toDoList = objects;
			this.context = context;
		}

		/**
		 * This method will DEFINe what the view inside the list view will
		 * finally look like Here we are going to code that the checkbox state
		 * is the status of task and check box text is the task name
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CheckBox chk = null;
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.list_inner_view,
						parent, false);
				chk = (CheckBox) convertView.findViewById(R.id.chkStatus);
				convertView.setTag(chk);

				chk.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						Todo changeTask = (Todo) cb.getTag();
						changeTask.setStatus(cb.isChecked() == true ? 1 : 0);
						db.updateTask(changeTask);
						Toast.makeText(
								getApplicationContext(),
								"Clicked on Checkbox: " + cb.getText() + " is "
										+ cb.isChecked(), Toast.LENGTH_LONG)
								.show();
					}

				});
			} else {
				chk = (CheckBox) convertView.getTag();
			}
			Todo current = toDoList.get(position);
			chk.setText(current.getToDoName());
			chk.setChecked(current.getStatus() == 1 ? true : false);
			chk.setTag(current);
			Log.d("listener", String.valueOf(current.getId()));
			return convertView;
		}

	}

}
