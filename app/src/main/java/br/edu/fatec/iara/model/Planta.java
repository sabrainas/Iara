package br.edu.fatec.iara.model;

public class Planta {
        private String nome;
        private double temperaturaAr;
        private int umidadeAr;
        private int tds;
        private int umidadeSolo;

        public Planta() {
        }

        public Planta(String nome, double temperaturaAr, int umidadeAr, int tds, int umidadeSolo) {
            this.nome = nome;
            this.temperaturaAr = temperaturaAr;
            this.umidadeAr = umidadeAr;
            this.tds = tds;
            this.umidadeSolo = umidadeSolo;
        }

        // Getters
        public String getNome() { return nome; }
        public double getTemperaturaAr() { return temperaturaAr; }
        public int getUmidadeAr() { return umidadeAr; }
        public int getTds() { return tds; }
        public int getUmidadeSolo() { return umidadeSolo; }
}
