package com.monse.andrea.ejemplorealm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.monse.andrea.ejemplorealm.R;
import com.monse.andrea.ejemplorealm.models.Board;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class BoardAdapter extends BaseAdapter
{
    private Context context;
    private List<Board> lista;
    private int layaout;

    public BoardAdapter(Context context, List<Board> lista, int layaout) {
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
            vh.titulo = (TextView)convertView.findViewById(R.id.tvBoardTitulo);
            vh.notas = (TextView)convertView.findViewById(R.id.tvBoardNotas);
            vh.fecha = (TextView)convertView.findViewById(R.id.tvBoardFecha);

            convertView.setTag(vh);
        }
        else
        {
            vh = (ViewHolder) convertView.getTag();
        }

        Board board = lista.get(position);
        vh.titulo.setText(board.getTitulo());

        //formatear una fecha
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String nuevaFecha = df.format(board.getCreado());
        vh.fecha.setText(nuevaFecha);

        int numeroDeNotas = board.getNotas().size();
        String textForNote = (numeroDeNotas == 1) ? numeroDeNotas + " Nota" : numeroDeNotas + " Notas";

        vh.notas.setText(textForNote);
        return convertView;
    }

    public class ViewHolder
    {
        TextView titulo, notas, fecha;

    }
}
