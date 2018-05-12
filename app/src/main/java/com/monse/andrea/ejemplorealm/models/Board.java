package com.monse.andrea.ejemplorealm.models;

import com.monse.andrea.ejemplorealm.app.MyApplication;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Board extends RealmObject
{
    @PrimaryKey
    private int id;
    @Required
    private String titulo;
    @Required
    private Date creado;

    private RealmList<Note> notas;

    public Board() {
    }

    public Board(String titulo) {
        this.id = MyApplication.BoardID.incrementAndGet();
        this.titulo = titulo;
        this.creado = new Date();
        this.notas = new RealmList<Note>();
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getCreado() {
        return creado;
    }


    public RealmList<Note> getNotas() {
        return notas;
    }

}
