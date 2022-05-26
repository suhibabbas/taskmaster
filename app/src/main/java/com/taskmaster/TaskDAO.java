package com.taskmaster;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.taskmaster.data.Task;

import java.util.List;

@Dao
public interface TaskDAO {

    @Query("SELECT * FROM Task")
    List<Task> getAll();


    @Query("SELECT * FROM Task WHERE id = :id")
    Task getTaskByID(Long id);

    @Insert
    Long insertTask(Task task);

}
