package com.example.todowithziro.database;


import com.example.todowithziro.DaoTodo;
import com.example.todowithziro.pojo.Todo;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Todo.class}, version = 1, exportSchema = false)
public abstract class TodoDatabase extends RoomDatabase {

    public static final String DB_NAME_ZIRO = "ziro_db";
    public static final String TABLE_NAME_TODO_ZIRO = "todo_ziro";

    public abstract DaoTodo daoAccess();

}