package br.edu.fatec.iara.model;

import java.time.LocalDateTime;
import java.util.Date;

public class Planta {
        private String nome;
        private double temperaturaAr;
        private int umidadeAr;
        private int tds;
        private int umidadeSolo;
        private Date dataRegistro;

        public Planta() {
        }

        public Planta(String nome, double temperaturaAr, int umidadeAr, int tds, int umidadeSolo, Date dataRegistro) {
            this.nome = nome;
            this.temperaturaAr = temperaturaAr;
            this.umidadeAr = umidadeAr;
            this.tds = tds;
            this.umidadeSolo = umidadeSolo;
            this.dataRegistro = dataRegistro;
        }

        // Getters
        public String getNome() { return nome; }
        public double getTemperaturaAr() { return temperaturaAr; }
        public int getUmidadeAr() { return umidadeAr; }
        public int getTds() { return tds; }
        public int getUmidadeSolo() { return umidadeSolo; }
        public Date getDataRegistro() { return dataRegistro; }
}
