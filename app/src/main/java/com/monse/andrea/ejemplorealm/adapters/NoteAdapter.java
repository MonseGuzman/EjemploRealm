package com.monse.andrea.ejemplorealm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.monse.andrea.ejemplorealm.R;
import com.monse.andrea.ejemplorealm.models.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class NoteAdapter extends BaseAdapter
{
    private Context context;
    private List<Note> lista;
    private int layaout;

    public NoteAdapter(Context context, List<Note> lista, int layaout) {
        this.context = context;
        this.lista = lista;
        this.layaout = layaout;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;

        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(layaout, null);
            vh = new ViewHolder();
            vh.descripcion = (TextView)convertView.findViewById(R.id.tvNoteDescripcion);
            vh.fecha = (TextView)convertView.findViewById(R.id.tvFechaNote);

            convertView.setTag(vh);
        }
        else
        {
            vh = (ViewHolder) convertView.getTag();
        }

        Note note = lista.get(position);
        vh.descripcion.setText(note.getDescripcion());

        //formatear una fecha
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String nuevaFecha = df.format(note.getCreado());
        vh.fecha.setText(nuevaFecha);

        return convertView;
    }

    public class ViewHolder
    {
        TextView descripcion, fecha;

    }
}
