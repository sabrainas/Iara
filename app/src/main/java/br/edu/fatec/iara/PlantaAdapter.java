package br.edu.fatec.iara;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;

import java.time.format.DateTimeFormatter;
import java.util.List;

import br.edu.fatec.iara.model.Planta;

public class PlantaAdapter extends ArrayAdapter<Planta> {

    public PlantaAdapter(Context context, List<Planta> plantas) {
        super(context, 0, plantas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Planta planta = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        ShapeableImageView imgPlanta = convertView.findViewById(R.id.imgPlanta);
        TextView nomePlanta = convertView.findViewById(R.id.nomePlanta);
        TextView tempAr = convertView.findViewById(R.id.tempAr);
        TextView umidadeAr = convertView.findViewById(R.id.umidadeAr);
        TextView qtdSolidosDissolvidos = convertView.findViewById(R.id.qtdSolidosDissolvidos);
        TextView umidadeSolo = convertView.findViewById(R.id.umidadeSolo);
        TextView dataRegistro = convertView.findViewById(R.id.dataRegistro);

        // Preencher os dados
        nomePlanta.setText(planta.getNome());
        tempAr.setText(String.format("%.1f ºC", planta.getTemperaturaAr()));
        umidadeAr.setText(planta.getUmidadeAr() + " %");
        qtdSolidosDissolvidos.setText(planta.getTds() + " ppm");
        umidadeSolo.setText(planta.getUmidadeSolo() + " %");

        dataRegistro.setText(planta.getDataRegistro());

        return convertView;
    }
}