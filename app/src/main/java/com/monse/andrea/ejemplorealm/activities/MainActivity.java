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
import com.monse.andrea.ejemplorealm.adapters.BoardAdapter;
import com.monse.andrea.ejemplorealm.models.Board;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<Board>>, AdapterView.OnItemClickListener{

    private FloatingActionButton fab;
    private Realm realm;
    private ListView listView;
    private BoardAdapter adapter;
    private RealmResults<Board> boards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creador de la bad
        realm = Realm.getDefaultInstance();
        boards = realm.where(Board.class).findAll(); //consulta en realm
        boards.addChangeListener(this);

        listView = (ListView) findViewById(R.id.listViewBoard);
        fab = (FloatingActionButton) findViewById(R.id.fabAddBoard);

        adapter = new BoardAdapter(this, boards, R.layout.list_view_board_item);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAlertForCreatingBoard("titulo", "mensaje");
            }
        });

        registerForContextMenu(listView); //para que aparezaca el menú en los items
    }

    //dialogos de alerta
    private void ShowAlertForCreatingBoard(String titulo, String mensaje)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(titulo != null)
            builder.setTitle(titulo);
        if(mensaje != null)
            builder.setMessage(mensaje);

        View v = LayoutInflater.from(this).inflate(R.layout.dialog_create_board, null);
        builder.setView(v);

        final EditText etTitulo = (EditText)v.findViewById(R.id.etTitulo);
        builder.setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nombre = etTitulo.getText().toString();

                if(nombre.length() > 0)
                    createNewBoard(nombre);
                else
                    Toast.makeText(getApplicationContext(), "No ingreso nombre", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void ShowAlertForEditBoard(String titulo, String mensaje, final Board board)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(titulo != null)
            builder.setTitle(titulo);
        if(mensaje != null)
            builder.setMessage(mensaje);

        View v = LayoutInflater.from(this).inflate(R.layout.dialog_create_board, null);
        builder.setView(v);

        final EditText etTitulo = (EditText)v.findViewById(R.id.etTitulo);
        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nombre = etTitulo.getText().toString();

                if(nombre.length() == 0)
                    Toast.makeText(getApplicationContext(), "El nombre es requerido para editar o algo así", Toast.LENGTH_SHORT).show();
                else if(nombre.equals(board.getTitulo()))
                    Toast.makeText(getApplicationContext(), "No ingreso nombre", Toast.LENGTH_SHORT).show();
                else
                    editBoard(board, nombre);

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //métodos que manejan los board
    private void createNewBoard(String nombre)
    {
        realm.beginTransaction();
        //proceso de una transacción
        Board prueba = new Board(nombre);
        realm.copyToRealm(prueba);
        realm.commitTransaction();
    }

    private void deleteBoard(Board board)
    {
        realm.beginTransaction();
        board.deleteFromRealm(); //borra desde la bd
        realm.commitTransaction();
    }

    private void editBoard(Board board, String nombre)
    {
        realm.beginTransaction();
        board.setTitulo(nombre);
        realm.copyToRealmOrUpdate(board); //actualizar
        realm.commitTransaction();
    }

    private void deleteAll()
    {
        //borra los datos de la bd
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }


    //axtualización del listview
    @Override
    public void onChange(RealmResults<Board> boards) {
        adapter.notifyDataSetChanged(); //para que se actualice la lista
    }

    //clic en el listview
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra("id", boards.get(position).getId());
        startActivity(intent);
    }

    //menu de la actividad
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_board,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.delete_all:
                deleteAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //editar o borrar elementos en un menú desplegable
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(boards.get(info.position).getTitulo());
        getMenuInflater().inflate(R.menu.context_menu_board ,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.delete_board:
                deleteBoard(boards.get(info.position));
                return true;
            case  R.id.edit_board:
                ShowAlertForEditBoard("Editar", "Cambiar el nombre del board", boards.get(info.position));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
