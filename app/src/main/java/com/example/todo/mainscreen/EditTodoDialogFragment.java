package com.example.todo.mainscreen;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.example.todo.R;
import com.example.todo.sql.TodoDbHelper;
import com.example.todo.sql.db.Todo;

import java.sql.SQLException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditTodoDialogFragment.EditTodoDialogFragmentListener} interface
 * to handle interaction events.
 * Use the {@link EditTodoDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditTodoDialogFragment extends DialogFragment implements OnEditorActionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "title";


    Button editDialogSaveButton;
    protected TodoDbHelper db;
    private int id;

    /**
     * Request code for the edit item intent
     */
    private final int EDIT_ITEM_REQUEST_CODE = 1;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param param1 Parameter 1.
     * @return A new instance of fragment EditTodoDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditTodoDialogFragment newInstance(String param1) {
        EditTodoDialogFragment fragment = new EditTodoDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public EditTodoDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.edit_todo_dialog_fragment, container, false);

        editDialogSaveButton = (Button)v.findViewById(R.id.editDialogSaveButton);

        try {
            db = new TodoDbHelper(getActivity());
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        getDialog().setTitle("Edit Todo");

        // get the item's position and id from the intent extra data
        int position = getArguments().getInt("position", 0);
        id = getArguments().getInt("id", -1);

        Todo todo = db.getTodoTask(id);


        // update the edit text fields and set focus on the title
        EditText editText = (EditText) v.findViewById(R.id.editText3);

        editText.setText(todo.getToDoName());
        editText.requestFocus();
        editText.setSelection(todo.getToDoName().length());

        //set the save button listener
        editDialogSaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View vv) {
                // When button is clicked, call up to owning activity.
                // When back button is pressed
                // Create an intent
                Intent intent = new Intent(getActivity(), CreateTodoItemActivity.class);

                // update the edit text fields and set focus on the title
                EditText editText = (EditText) v.findViewById(R.id.editText3);

                Todo todo = db.getTodoTask(id);
                String s = editText.getText().toString();

                // update todo
                todo.setToDoName(s);

                db.updateTask(todo);

                // Start activity
                startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE);
                // Finish this activity
                getActivity().finish();
            }
        });

        return v;
    }


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return false;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface EditTodoDialogFragmentListener {
        // TODO: Update argument type and name
        void onFinishEditTodoDialog(String inputText);
    }

}
