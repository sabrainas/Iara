package br.edu.fatec.iara.model;

import java.time.LocalDateTime;
import java.util.Date;

public class Planta {
        private String nome;
        private Double temperaturaAr;
        private int umidadeAr;
        private int tds;
        private int umidadeSolo;
        private String dataRegistro;

        public Planta() {
        }

        public Planta(String nome) {
            this.nome = nome;
        }

        public Planta(String nome, double temperaturaAr, int umidadeAr, int tds, int umidadeSolo, String dataRegistro) {
            this.nome = nome;
            this.temperaturaAr = temperaturaAr;
            this.umidadeAr = umidadeAr;
            this.tds = tds;
            this.umidadeSolo = umidadeSolo;
            this.dataRegistro = dataRegistro;
        }

        // Getters
        public String getNome() { return nome; }
        public Double getTemperaturaAr() { return temperaturaAr; }
        public int getUmidadeAr() { return umidadeAr; }
        public int getTds() { return tds; }
        public int getUmidadeSolo() { return umidadeSolo; }
        public String getDataRegistro() { return dataRegistro; }
}
