package com.example.todowithziro.activities;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todowithziro.R;
import com.example.todowithziro.database.TodoDatabase;
import com.example.todowithziro.pojo.Todo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

public class TodoNoteActivity extends AppCompatActivity implements
        View.OnClickListener {

    EditText inTitle, inDesc;
    Button btnDone, btnDelete;
    Toolbar toolbar;
    boolean isNewTodo = false;
    Button btnDatePicker, btnTimePicker;
    private int mYear, mMonth, mDay, mHour, mMinute;

    private String[] categories = {
            "Monday",
            "Tuesday",
            "wednesday",
            "Thursday",
            "Friday"
    };

    public ArrayList<String> spinnerList = new ArrayList<>(Arrays.asList(categories));
    TodoDatabase myDatabase;

    Todo updateTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_note);

        initWidget();



        myDatabase = Room.databaseBuilder(getApplicationContext(), TodoDatabase.class, TodoDatabase.DB_NAME_ZIRO).build();

        int todo_id = getIntent().getIntExtra("id", -100);

        if (todo_id == -100)
            isNewTodo = true;

        if (!isNewTodo) {
            fetchTodoById(todo_id);
            btnDelete.setVisibility(View.VISIBLE);
            btnDone.setText("Update");
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNewTodo) {
                    if (btnDatePicker.getText().toString().equalsIgnoreCase("select date")){
                        Toast.makeText(TodoNoteActivity.this, "Please select date", Toast.LENGTH_SHORT).show();
                    }else {
                        Todo todo = new Todo();
                        todo.name = inTitle.getText().toString();
                        todo.description = inDesc.getText().toString();
                        todo.date = btnDatePicker.getText().toString();

                        insertRow(todo);  
                    }
                   
                } else {

                    updateTodo.name = inTitle.getText().toString();
                    updateTodo.description = inDesc.getText().toString();
                    updateTodo.date = btnDatePicker.getText().toString();


                    updateRow(updateTodo);
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRow(updateTodo);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void fetchTodoById(final int todo_id) {
        new AsyncTask<Integer, Void, Todo>() {
            @Override
            protected Todo doInBackground(Integer... params) {

                return myDatabase.daoAccess().fetchTodoListById(params[0]);

            }

            @Override
            protected void onPostExecute(Todo todo) {
                super.onPostExecute(todo);
                inTitle.setText(todo.name);
                inDesc.setText(todo.description);
                btnDatePicker.setText(todo.date);

                updateTodo = todo;
            }
        }.execute(todo_id);

    }

    @SuppressLint("StaticFieldLeak")
    private void insertRow(Todo todo) {
        new AsyncTask<Todo, Void, Long>() {
            @Override
            protected Long doInBackground(Todo... params) {
                return myDatabase.daoAccess().insertTodo(params[0]);
            }

            @Override
            protected void onPostExecute(Long id) {
                super.onPostExecute(id);

                Intent intent = getIntent();
                intent.putExtra("isNew", true).putExtra("id", id);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(todo);

    }

    @SuppressLint("StaticFieldLeak")
    private void deleteRow(Todo todo) {
        new AsyncTask<Todo, Void, Integer>() {
            @Override
            protected Integer doInBackground(Todo... params) {
                return myDatabase.daoAccess().deleteTodo(params[0]);
            }

            @Override
            protected void onPostExecute(Integer number) {
                super.onPostExecute(number);

                Intent intent = getIntent();
                intent.putExtra("isDeleted", true).putExtra("number", number);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(todo);

    }


    @SuppressLint("StaticFieldLeak")
    private void updateRow(Todo todo) {
        new AsyncTask<Todo, Void, Integer>() {
            @Override
            protected Integer doInBackground(Todo... params) {
                return myDatabase.daoAccess().updateTodo(params[0]);
            }

            @Override
            protected void onPostExecute(Integer number) {
                super.onPostExecute(number);

                Intent intent = getIntent();
                intent.putExtra("isNew", false).putExtra("number", number);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(todo);

    }
    private void initWidget(){
        inTitle = findViewById(R.id.inTitle);
        inDesc = findViewById(R.id.inDescription);
        btnDone = findViewById(R.id.btnDone);
        btnDelete = findViewById(R.id.btnDelete);
        btnDatePicker = findViewById(R.id.calendarView);
        btnDatePicker.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            btnDatePicker.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

    }
}