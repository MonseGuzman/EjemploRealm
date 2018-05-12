package com.monse.andrea.ejemplorealm.models;

import com.monse.andrea.ejemplorealm.app.MyApplication;

import java.util.Date;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Note extends RealmObject
{
    @PrimaryKey
    private int id;
    @Required
    private String descripcion;
    @Required
    private Date creado;

    public Note() {
    }

    public Note(String descripcion) {
        //auto id, incrementa y lo obtiene
        this.id = MyApplication.NoteId.incrementAndGet();
        this.descripcion = descripcion;
        this.creado = new Date(0);
    }

    public int getid() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getCreado() {
        return creado;
    }
}
