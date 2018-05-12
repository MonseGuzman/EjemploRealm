package com.monse.andrea.ejemplorealm.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.monse.andrea.ejemplorealm.R;
import com.monse.andrea.ejemplorealm.adapters.NoteAdapter;
import com.monse.andrea.ejemplorealm.models.Board;
import com.monse.andrea.ejemplorealm.models.Note;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

public class NoteActivity extends AppCompatActivity implements RealmChangeListener<Board> {

    private FloatingActionButton fab;
    private Realm realm; //bd
    private ListView listView;
    private NoteAdapter adapter;
    private RealmList<Note> notes;
    private int boardId;
    private Board board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        //Creador de la bad
        realm = Realm.getDefaultInstance();
        if(getIntent().getExtras() != null)
            boardId = getIntent().getExtras().getInt("id");

        board = realm.where(Board.class).equalTo("id", boardId).findFirst(); //consulta en realm
        board.addChangeListener(this);
        notes = board.getNotas();

        this.setTitle(board.getTitulo()); //nombre de la actividad

        listView = (ListView)findViewById(R.id.listViewNote);
        fab = (FloatingActionButton)findViewById(R.id.fabAddNote);

        adapter = new NoteAdapter(this, notes, R.layout.list_view_notes_item);
        listView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ShowAlertForCreatingNote("Notas", "Ingrese notas para la pizarrz " + board.getTitulo());
            }
        });

        registerForContextMenu(listView); //para que aparezaca el menú en los items
    }

    private void ShowAlertForCreatingNote(String titulo, String mensaje)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(titulo != null)
            builder.setTitle(titulo);
        if(mensaje != null)
            builder.setMessage(mensaje);

        View v = LayoutInflater.from(this).inflate(R.layout.dialog_create_note, null);
        builder.setView(v);

        final EditText etNuevaNota = (EditText)v.findViewById(R.id.etNuevaNota);
        builder.setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nombre = etNuevaNota.getText().toString().trim();

                if(nombre.length() > 0)
                    createNewNote(nombre);
                else
                    Toast.makeText(getApplicationContext(), "No puede estar en blanco", Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void ShowAlertForEditBoard(String titulo, String mensaje, final Note note)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(titulo != null)
            builder.setTitle(titulo);
        if(mensaje != null)
            builder.setMessage(mensaje);

        View v = LayoutInflater.from(this).inflate(R.layout.dialog_create_note, null);
        builder.setView(v);

        final EditText etTitulo = (EditText)v.findViewById(R.id.etNuevaNota);
        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nombre = etTitulo.getText().toString();

                if(nombre.length() == 0)
                    Toast.makeText(getApplicationContext(), "El nombre es requerido para editar o algo así", Toast.LENGTH_SHORT).show();
                else if(nombre.equals(board.getTitulo()))
                    Toast.makeText(getApplicationContext(), "No ingreso nombre", Toast.LENGTH_SHORT).show();
                else
                    editNote(nombre, note);

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //métodos que manejan los board
    private void createNewNote(String nombre)
    {
        realm.beginTransaction();
        //proceso de una transacción
        Note prueba = new Note(nombre);
        realm.copyToRealm(prueba);
        board.getNotas().add(prueba);

        realm.commitTransaction();
    }

    private void deleteNote(Note note)
    {
        realm.beginTransaction();
        note.deleteFromRealm();
        realm.commitTransaction();
    }

    private void editNote(String descripcion, Note note)
    {
        realm.beginTransaction();
        note.setDescripcion(descripcion);
        realm.copyToRealmOrUpdate(note);
        realm.commitTransaction();

        board.addChangeListener(this);
    }

    private void deleteAll()
    {
        realm.beginTransaction();
        board.getNotas().deleteAllFromRealm();
        realm.commitTransaction();
    }

    @Override
    public void onChange(Board board) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.delete_all_note:
                deleteAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //editar o borrar elementos
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu_note, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.delete_note:
                deleteNote(notes.get(info.position));
                return true;
            case  R.id.edit_note:
                ShowAlertForEditBoard("Editar", "Cambiar el nombre de la nota", notes.get(info.position));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
