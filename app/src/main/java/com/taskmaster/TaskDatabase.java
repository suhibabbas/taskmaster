package com.taskmaster;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.taskmaster.data.Task;

@Database(entities = {Task.class},version = 1)
public abstract class TaskDatabase extends RoomDatabase {
    public abstract TaskDAO taskDAO();

    // create a private constructor
    private static TaskDatabase taskDatabase; //declaration for the instance

    // create a private static instance
    public TaskDatabase(){
    }

    // create public static methode (return instance)
    public static synchronized TaskDatabase getInstance(Context context){
        if(taskDatabase == null){
            taskDatabase = Room.databaseBuilder(context,
                    TaskDatabase.class,"TaskDatabase").allowMainThreadQueries().build();
        }
        return taskDatabase;
    }
}
