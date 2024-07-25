package com.example.sokobann;


public class Lobo {
    private int linha;
    private int coluna;

    public Lobo(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void moverCima() {
        linha--;
    }

    public void moverBaixo() {
        linha++;
    }

    public void moverEsquerda() {
        coluna--;
    }

    public void moverDireita() {
        coluna++;
    }
}