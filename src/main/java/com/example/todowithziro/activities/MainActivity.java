package com.example.todowithziro.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.todowithziro.R;
import com.example.todowithziro.adapter.RecyclerAdapter;
import com.example.todowithziro.database.TodoDatabase;
import com.example.todowithziro.pojo.Todo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.ClickListener {

    TodoDatabase myDatabase;
    RecyclerView recyclerView;
    Button  btnSelectDateFilt,btnReset;
    RecyclerAdapter recyclerViewAdapter;
    FloatingActionButton floatingActionButton;

    private int mYear, mMonth, mDay;

    ArrayList<Todo> todoArrayList = new ArrayList<>();

    public static final int NEW_TODO_REQUEST_CODE = 200;
    public static final int UPDATE_TODO_REQUEST_CODE = 300;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        myDatabase = Room.databaseBuilder(getApplicationContext(), TodoDatabase.class, TodoDatabase.DB_NAME_ZIRO).fallbackToDestructiveMigration().build();
        checkIfAppLaunchedFirstTime();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, TodoNoteActivity.class), NEW_TODO_REQUEST_CODE);
            }
        });

    }

    private void initViews() {
        floatingActionButton = findViewById(R.id.fab);
        btnSelectDateFilt = findViewById(R.id.btn_selectDate);
        btnReset = findViewById(R.id.btn_reset);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new RecyclerAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAllTodos();
            }
        });

        btnSelectDateFilt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                loadFilteredTodos(date);
                                btnReset.setVisibility(View.VISIBLE);



                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

    }

    @Override
    public void launchIntent(int id) {
        startActivityForResult(new Intent(MainActivity.this, TodoNoteActivity.class).putExtra("id", id), UPDATE_TODO_REQUEST_CODE);
    }



    @SuppressLint("StaticFieldLeak")
    private void loadFilteredTodos(String category) {
        new AsyncTask<String, Void, List<Todo>>() {
            @Override
            protected List<Todo> doInBackground(String... params) {
                return myDatabase.daoAccess().getAllTodoListByCategory(params[0]);

            }

            @Override
            protected void onPostExecute(List<Todo> todoList) {
                recyclerViewAdapter.updateTodoList(todoList);
            }
        }.execute(category);

    }


    @SuppressLint("StaticFieldLeak")
    private void fetchTodoByIdAndInsert(int id) {
        new AsyncTask<Integer, Void, Todo>() {
            @Override
            protected Todo doInBackground(Integer... params) {
                return myDatabase.daoAccess().fetchTodoListById(params[0]);

            }

            @Override
            protected void onPostExecute(Todo todoList) {
                recyclerViewAdapter.addRow(todoList);
            }
        }.execute(id);

    }

    @SuppressLint("StaticFieldLeak")
    private void loadAllTodos() {
        new AsyncTask<String, Void, List<Todo>>() {
            @Override
            protected List<Todo> doInBackground(String... params) {
                return myDatabase.daoAccess().fetchAllTodos();
            }

            @Override
            protected void onPostExecute(List<Todo> todoList) {
                recyclerViewAdapter.updateTodoList(todoList);
            }
        }.execute();
    }

    private void buildDummyTodos() {
        Todo todo = new Todo();
        todo.name = "What is Lorem Ipsum?";
        todo.description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s";
        todo.date = "30-9-2019";


        todoArrayList.add(todo);

        todo = new Todo();
        todo.name = "Why do we use it?";
        todo.description = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters,";
        todo.date = "29-9-2019";


        todoArrayList.add(todo);

        todo = new Todo();
        todo.name = "Kotlin Arrays";
        todo.description = "Cover the concepts of Arrays in Kotlin and how they differ from the Java ones.";
        todo.date = "30-9-2019";


        todoArrayList.add(todo);

        todo = new Todo();
        todo.name = "Where does it come from?";
        todo.description = "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, ";
        todo.date = "28-9-2019";


        todoArrayList.add(todo);

        todo.name = "What is Lorem Ipsum?";
        todo.description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s";
        todo.date = "30-9-2019";


        todoArrayList.add(todo);

        todo = new Todo();
        todo.name = "Why do we use it?";
        todo.description = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters,";
        todo.date = "29-09-2019";


        todoArrayList.add(todo);

        todo = new Todo();
        todo.name = "Kotlin Arrays";
        todo.description = "Cover the concepts of Arrays in Kotlin and how they differ from the Java ones.";
        todo.date = "30-9-2019";


        todoArrayList.add(todo);

        todo = new Todo();
        todo.name = "Where does it come from?";
        todo.description = "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, ";
        todo.date = "28-9-2019";


        todoArrayList.add(todo);
        insertList(todoArrayList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            //reset spinners

            if (requestCode == NEW_TODO_REQUEST_CODE) {
                long id = data.getLongExtra("id", -1);
                Toast.makeText(getApplicationContext(), "Row inserted", Toast.LENGTH_SHORT).show();
                fetchTodoByIdAndInsert((int) id);

            } else if (requestCode == UPDATE_TODO_REQUEST_CODE) {

                boolean isDeleted = data.getBooleanExtra("isDeleted", false);
                int number = data.getIntExtra("number", -1);
                if (isDeleted) {
                    Toast.makeText(getApplicationContext(), number + " rows deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), number + " rows updated", Toast.LENGTH_SHORT).show();
                }

                loadAllTodos();

            }


        } else {
            Toast.makeText(getApplicationContext(), "No action done by user", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void insertList(List<Todo> todoList) {
        new AsyncTask<List<Todo>, Void, Void>() {
            @Override
            protected Void doInBackground(List<Todo>... params) {
                myDatabase.daoAccess().insertTodoList(params[0]);

                return null;

            }

            @Override
            protected void onPostExecute(Void voids) {
                super.onPostExecute(voids);
            }
        }.execute(todoList);

    }

    private void checkIfAppLaunchedFirstTime() {
        final String PREFS_NAME = "SharedPrefs";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("firstTime", true)) {
            settings.edit().putBoolean("firstTime", false).apply();
            buildDummyTodos();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllTodos();
    }
}
