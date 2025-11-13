package com.srd14.agend_in;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {

    @PrimaryKey(autoGenerate = true)
    public int id;

    private String name;
    private String date;
    private String time;
    private String description;
    private String priority; // Añadido para la prioridad

    // Constructor, getters y setters

    public Task() {
        // Constructor vacío requerido para crear nuevas tareas
    }

    public Task(String name, String date, String time, String description, String priority) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.description = description;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }



    public void setPriority(String priority) {
        this.priority = priority;
    }
}
