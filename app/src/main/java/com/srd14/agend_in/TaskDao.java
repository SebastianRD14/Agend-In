package com.srd14.agend_in;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM tasks ORDER BY date, time ASC")
    List<Task> getAllTasks();

    @Query("SELECT * FROM tasks WHERE id = :taskId LIMIT 1")
    Task getTaskById(int taskId);

    // Nuevo método para obtener tareas por fecha, ordenadas por hora
    @Query("SELECT * FROM tasks WHERE date = :date ORDER BY time ASC")
    List<Task> getTasksByDate(String date);

}
