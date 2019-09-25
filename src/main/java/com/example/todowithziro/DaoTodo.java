package com.example.todowithziro;



import com.example.todowithziro.database.TodoDatabase;
import com.example.todowithziro.pojo.Todo;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


@Dao
public interface DaoTodo {

    @Insert
    long insertTodo(Todo todo);

    @Insert
    void insertTodoList(List<Todo> todoList);

    @Query("SELECT * FROM " + TodoDatabase.TABLE_NAME_TODO_ZIRO)
    List<Todo> fetchAllTodos();

    @Query("SELECT * FROM " + TodoDatabase.TABLE_NAME_TODO_ZIRO + " WHERE date = :type")
    List<Todo> getAllTodoListByCategory(String type);

    @Query("SELECT * FROM " + TodoDatabase.TABLE_NAME_TODO_ZIRO + " WHERE todo_id = :id")
    Todo fetchTodoListById(int id);

    @Update
    int updateTodo(Todo todo);

    @Delete
    int deleteTodo(Todo todo);
}
