package com.monse.andrea.ejemplorealm.app;

import android.app.Application;

import com.monse.andrea.ejemplorealm.models.Board;
import com.monse.andrea.ejemplorealm.models.Note;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmConfiguration.Builder;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class MyApplication extends Application
{
    public static AtomicInteger BoardID = new AtomicInteger();
    public static AtomicInteger NoteId = new AtomicInteger();
    @Override
    public void onCreate()
    {
        super.onCreate();
        Realm.init(this);
        //configuración de la bd Realm
        setUpRealmConfig();
        //se ejecuta antes del MainActivity
        Realm realm = Realm.getDefaultInstance();
        BoardID = getIdByTable(realm, Board.class);
        NoteId = getIdByTable(realm, Note.class);
        realm.close();
    }

    private void setUpRealmConfig() {
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);
    }

    private <T extends RealmObject> AtomicInteger getIdByTable(Realm realm, Class<T> anyClass)
    {
        //consulta en REALM de la tabla
        RealmResults<T> results = realm.where(anyClass).findAll();
        //si contiene resultados, recupera el último id o manda null
        return (results.size() > 0) ? new AtomicInteger(results.max("id").intValue()) : new AtomicInteger();
    }
}
