package com.example.todowithziro.pojo;


import com.example.todowithziro.database.TodoDatabase;

import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = TodoDatabase.TABLE_NAME_TODO_ZIRO)
public class Todo implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int todo_id;

    public String name;

    public String description;


    public String date;


    @Ignore
    public String priority;

}
