package com.ejemplo.model;

/**
 * Modelo de una tarea de la lista. Es un POJO: solo guarda datos
 * y no contiene lógica de negocio ni acceso a la petición HTTP.
 */
public class Tarea {

    private int id;
    private String titulo;
    private boolean completada;

    public Tarea(int id, String titulo) {
        this.id = id;
        this.titulo = titulo;
        this.completada = false;
    }

    // Getters y setters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public boolean isCompletada() { return completada; }
    public void setCompletada(boolean completada) {
        this.completada = completada;
    }
}