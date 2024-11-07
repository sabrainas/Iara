package br.edu.fatec.iara;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import br.edu.fatec.iara.model.Planta;

public class PlantaAdapter extends ArrayAdapter<Planta> {

    private boolean exibirBotaoExcluir;
    private List<Planta> plantas;

    public PlantaAdapter(Context context, List<Planta> plantas, boolean exibirBotaoExcluir) {
        super(context, 0, plantas);
        this.plantas = plantas;
        this.exibirBotaoExcluir = exibirBotaoExcluir;
    }

   /* @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());*/

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
        umidadeSolo.setText(planta.getUmidadeSolo() + "");

        ImageView btnExcluir = convertView.findViewById(R.id.btnExcluir);
        btnExcluir.setVisibility(exibirBotaoExcluir ? View.VISIBLE : View.GONE);

        btnExcluir.setOnClickListener(v -> {
            DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();
            dbReference.child("Data").child(planta.getDataRegistro()).removeValue().addOnSuccessListener(aVoid -> {
                plantas.remove(position);
                notifyDataSetChanged();
                Toast.makeText(getContext(), "Registro excluído com sucesso", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Erro ao excluir registro", Toast.LENGTH_SHORT).show();
            });
        });

        dataRegistro.setText(planta.getDataRegistro());
        return convertView;
    }
}