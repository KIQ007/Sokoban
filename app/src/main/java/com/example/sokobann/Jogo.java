package com.example.sokobann;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ImageView;
import java.util.Random;

public class Jogo extends AppCompatActivity {

    private Fantasma fantasma;
    private Lobo lobo;

    private int flag;
    MediaPlayer playerFundo;
    private static final long delay = 500;
    private ImageView[][] Matrix = new ImageView[8][8];
    private boolean[][] chamasMatrix = new boolean[8][8];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);

        playerFundo = MediaPlayer.create(this, R.raw.playerfundo);
        playerFundo.setLooping(true);
        playerFundo.start();

        setID();
        levelOne();

        ImageButton btnReiniciar = findViewById(R.id.ResetLevelButton);
        btnReiniciar.setOnClickListener(view -> reiniciarPartida());

        ImageButton btnVoltar = findViewById(R.id.VoltarMenu);
        btnVoltar.setOnClickListener(view -> {
            playerFundo.stop();
            voltarMenu();
        });

        ImageButton btnProximoNivel = findViewById(R.id.NextLevelButton);
        btnProximoNivel.setOnClickListener(view -> {
            if (flag == 1) {
                levelTwo();
            } else if (flag == 2) {
                levelThree();
            } else if (flag == 3) {
                levelFour();
            }else if (flag == 4){
                levelFive();
            }
        });

        ImageButton btnCima = findViewById(R.id.btnCima);
        btnCima.setOnClickListener(v -> moverCima());

        ImageButton btnBaixo = findViewById(R.id.btnBaixo);
        btnBaixo.setOnClickListener(v -> moverBaixo());

        ImageButton btnEsquerda = findViewById(R.id.btnEsquerda);
        btnEsquerda.setOnClickListener(v -> moverEsquerda());

        ImageButton btnDireita = findViewById(R.id.btnDireita);
        btnDireita.setOnClickListener(v -> moverDireita());
    }

    private void reiniciarPartida(){
        if(flag == 1)
            levelOne();
        if(flag == 2)
            levelTwo();
        if(flag == 3)
            levelThree();
        if(flag == 4)
            levelFour();
        if(flag == 5)
            levelFive();
    }


    private boolean movimentoValido(int newRow, int newCol) {
        Drawable parede = ContextCompat.getDrawable(this, R.drawable.parede);

        if (newRow >= 0 && newRow < Matrix.length && newCol >= 0 && newCol < Matrix[0].length) {
            Drawable cellDrawable = Matrix[newRow][newCol].getDrawable();
            return !cellDrawable.getConstantState().equals(parede.getConstantState());
        }

        return false;
    }

    private void moverEsquerda() {
        int linha = fantasma.getLinha();
        int novaColuna = fantasma.getColuna() - 1;
        int proximaColuna = novaColuna - 1;

        Drawable CaixaAberta = ContextCompat.getDrawable(this, R.drawable.caixaaberta);
        Drawable CaixaFechada = ContextCompat.getDrawable(this, R.drawable.caixafechada);


        // Verifica se o movimento para a esquerda é válido (não atinge uma parede)
        if (movimentoValido(linha, novaColuna)) {

            // Verifica se a célula para a esquerda contém uma caixa aberta ou fechada
            if (Matrix[linha][novaColuna].getDrawable().getConstantState().equals(CaixaAberta.getConstantState())
                    || Matrix[linha][novaColuna].getDrawable().getConstantState().equals(CaixaFechada.getConstantState())) {
                // Verifica se a próxima célula à esquerda é válida (não atinge uma parede)
                if (movimentoValido(linha, proximaColuna)) {
                    // Obtém o desenho da próxima célula à esquerda
                    Drawable nextCellDrawable = Matrix[linha][proximaColuna].getDrawable();
                    // Verifica se a próxima célula à esquerda não contém uma caixa aberta ou fechada
                    if (!nextCellDrawable.getConstantState().equals(CaixaAberta.getConstantState())
                            && !nextCellDrawable.getConstantState().equals(CaixaFechada.getConstantState())) {
                        // Verifica se a próxima célula à esquerda contém chamas
                        if (Matrix[linha][proximaColuna].getDrawable().getConstantState().equals(
                                ContextCompat.getDrawable(this, R.drawable.chamas).getConstantState())) {
                            // Substitui as chamas por uma caixa fechada
                            Matrix[linha][proximaColuna].setImageResource(R.drawable.caixafechada);
                        } else {
                            // Substitui a próxima célula à esquerda por uma caixa aberta
                            Matrix[linha][proximaColuna].setImageResource(R.drawable.caixaaberta);
                        }
                    } else {
                        // Se a próxima célula contém uma caixa, o movimento é interrompido
                        return;
                    }
                } else {
                    // Se a próxima célula não é válida, o movimento é interrompido
                    return;
                }
            }

            if(flag == 4){
                if(Matrix[5][0].getDrawable().getConstantState().equals(CaixaAberta.getConstantState())){
                    Matrix[1][5].setImageResource(R.drawable.chao);
                }
            }

            // Verifique se a posição atual do fantasma contém chamas
            if (isChama(fantasma.getLinha(), fantasma.getColuna())) {
                // Se sim, restaure essa posição para conter chamas
                Matrix[fantasma.getLinha()][fantasma.getColuna()].setImageResource(R.drawable.chamas);
            } else {
                // Caso contrário, defina a posição atual para "chão" (sem chamas)
                Matrix[fantasma.getLinha()][fantasma.getColuna()].setImageResource(R.drawable.chao);
            }

            // Mova o fantasma para a esquerda
            fantasma.moverEsquerda();

            // Defina a nova posição do fantasma
            Matrix[fantasma.getLinha()][fantasma.getColuna()].setImageResource(R.drawable.fantasma);

        }
       if(flag == 5) {
            moverLoboAleatoriamente();
            if(lobo.getLinha() ==  fantasma.getLinha() && lobo.getColuna() == fantasma.getColuna())
                levelFive();
        }


        // Verifica se todas as caveiras estão nas bandeiras após o movimento
        if (todasCaveirasNasBandeiras()) {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                if (flag == 1) {
                    levelTwo();
                } else if (flag == 2) {
                    levelThree();
                } else if (flag == 3) {
                    levelFour();
                }else if(flag == 4){
                    levelFive();
                }
            }, delay);

        }
    }

    private void moverDireita() {
        int linha = fantasma.getLinha();
        int novaColuna = fantasma.getColuna() + 1;
        int proximaColuna = novaColuna + 1;

        Drawable CaixaAberta = ContextCompat.getDrawable(this, R.drawable.caixaaberta);
        Drawable CaixaFechada = ContextCompat.getDrawable(this, R.drawable.caixafechada);



        if (movimentoValido(linha, novaColuna)) {

            if (Matrix[linha][novaColuna].getDrawable().getConstantState().equals(CaixaAberta.getConstantState())
                    || Matrix[linha][novaColuna].getDrawable().getConstantState().equals(CaixaFechada.getConstantState())) {
                if (movimentoValido(linha, proximaColuna)) {
                    Drawable nextCellDrawable = Matrix[linha][proximaColuna].getDrawable();
                    if (!nextCellDrawable.getConstantState().equals(CaixaAberta.getConstantState())
                            && !nextCellDrawable.getConstantState().equals(CaixaFechada.getConstantState())) {
                        if (Matrix[linha][proximaColuna].getDrawable().getConstantState().equals(
                                ContextCompat.getDrawable(this, R.drawable.chamas).getConstantState())) {
                            Matrix[linha][proximaColuna].setImageResource(R.drawable.caixafechada);
                        } else {
                            Matrix[linha][proximaColuna].setImageResource(R.drawable.caixaaberta);
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }

            if (isChama(fantasma.getLinha(), fantasma.getColuna())) {
                Matrix[fantasma.getLinha()][fantasma.getColuna()].setImageResource(R.drawable.chamas);
            } else {
                Matrix[fantasma.getLinha()][fantasma.getColuna()].setImageResource(R.drawable.chao);
            }

            fantasma.moverDireita();
            Matrix[fantasma.getLinha()][fantasma.getColuna()].setImageResource(R.drawable.fantasma);

        }

        if(flag == 5) {
            moverLoboAleatoriamente();
            if(lobo.getLinha() ==  fantasma.getLinha() && lobo.getColuna() == fantasma.getColuna())
                levelFive();
        }


        if (todasCaveirasNasBandeiras()) {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                if (flag == 1) {
                    levelTwo();
                } else if (flag == 2) {
                    levelThree();
                } else if (flag == 3) {
                    levelFour();
                } else if (flag == 4) {
                    levelFive();
                }
            }, delay);        }
    }

    private void moverCima() {
        int novaLinha = fantasma.getLinha() - 1;
        int coluna = fantasma.getColuna();
        int proximaLinha = novaLinha - 1;

        Drawable CaixaAberta = ContextCompat.getDrawable(this, R.drawable.caixaaberta);
        Drawable CaixaFechada = ContextCompat.getDrawable(this, R.drawable.caixafechada);



        if (movimentoValido(novaLinha, coluna)) {
            if (Matrix[novaLinha][coluna].getDrawable().getConstantState().equals(CaixaAberta.getConstantState())
                    || Matrix[novaLinha][coluna].getDrawable().getConstantState().equals(CaixaFechada.getConstantState())) {
                if (movimentoValido(proximaLinha, coluna)) {
                    Drawable nextCellDrawable = Matrix[proximaLinha][coluna].getDrawable();
                    if (!nextCellDrawable.getConstantState().equals(CaixaAberta.getConstantState())
                            && !nextCellDrawable.getConstantState().equals(CaixaFechada.getConstantState())) {
                        if (Matrix[proximaLinha][coluna].getDrawable().getConstantState().equals(
                                ContextCompat.getDrawable(this, R.drawable.chamas).getConstantState())) {
                            Matrix[proximaLinha][coluna].setImageResource(R.drawable.caixafechada);
                        } else {
                            Matrix[proximaLinha][coluna].setImageResource(R.drawable.caixaaberta);
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }

            if (isChama(fantasma.getLinha(), coluna)) {
                Matrix[fantasma.getLinha()][coluna].setImageResource(R.drawable.chamas);
            } else {
                Matrix[fantasma.getLinha()][coluna].setImageResource(R.drawable.chao);
            }

            fantasma.moverCima();
            Matrix[fantasma.getLinha()][fantasma.getColuna()].setImageResource(R.drawable.fantasma);

        }

        if(flag == 5) {
            moverLoboAleatoriamente();
            if(lobo.getLinha() ==  fantasma.getLinha() && lobo.getColuna() == fantasma.getColuna())
                levelFive();
        }


        if (todasCaveirasNasBandeiras()) {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                if (flag == 1) {
                    levelTwo();
                } else if (flag == 2) {
                    levelThree();
                } else if (flag == 3) {
                    levelFour();
                } else if (flag == 4) {
                    levelFive();
                }
            }, delay);        }
    }

    private void moverBaixo() {
        int novaLinha = fantasma.getLinha() + 1;
        int coluna = fantasma.getColuna();
        int proximaLinha = novaLinha + 1;

        Drawable crateDrawable = ContextCompat.getDrawable(this, R.drawable.caixaaberta);
        Drawable crateCloseDrawable = ContextCompat.getDrawable(this, R.drawable.caixafechada);



        if (movimentoValido(novaLinha, coluna)) {
            if (Matrix[novaLinha][coluna].getDrawable().getConstantState().equals(crateDrawable.getConstantState())
                    || Matrix[novaLinha][coluna].getDrawable().getConstantState().equals(crateCloseDrawable.getConstantState())) {
                if (movimentoValido(proximaLinha, coluna)) {
                    Drawable nextCellDrawable = Matrix[proximaLinha][coluna].getDrawable();
                    if (!nextCellDrawable.getConstantState().equals(crateDrawable.getConstantState())
                            && !nextCellDrawable.getConstantState().equals(crateCloseDrawable.getConstantState())) {
                        if (Matrix[proximaLinha][coluna].getDrawable().getConstantState().equals(
                                ContextCompat.getDrawable(this, R.drawable.chamas).getConstantState())) {
                            Matrix[proximaLinha][coluna].setImageResource(R.drawable.caixafechada);
                        } else {
                            Matrix[proximaLinha][coluna].setImageResource(R.drawable.caixaaberta);
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }

            if (isChama(fantasma.getLinha(), coluna)) {
                Matrix[fantasma.getLinha()][coluna].setImageResource(R.drawable.chamas);
            } else {
                Matrix[fantasma.getLinha()][coluna].setImageResource(R.drawable.chao);
            }

            fantasma.moverBaixo();
            Matrix[fantasma.getLinha()][coluna].setImageResource(R.drawable.fantasma);
        }

        if(flag == 5) {
            moverLoboAleatoriamente();
            if(lobo.getLinha() ==  fantasma.getLinha() && lobo.getColuna() == fantasma.getColuna())
                levelFive();
        }

        if (todasCaveirasNasBandeiras()) {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                if (flag == 1) {
                    levelTwo();
                } else if (flag == 2) {
                    levelThree();
                } else if (flag == 3) {
                    levelFour();
                } else if (flag == 4) {
                    levelFive();
                }
            }, delay);        }
    }

    private void levelFive(){

        fantasma = new Fantasma(6,1);
        lobo = new Lobo(6,6);

        reiniciarchamas();

        chamasMatrix[1][2] = true;
        chamasMatrix[1][3] = true;
        chamasMatrix[1][4] = true;

        flag = 5;

        Matrix[0][0].setImageResource(R.drawable.parede);
        Matrix[0][1].setImageResource(R.drawable.parede);
        Matrix[0][2].setImageResource(R.drawable.parede);
        Matrix[0][3].setImageResource(R.drawable.parede);
        Matrix[0][4].setImageResource(R.drawable.parede);
        Matrix[0][5].setImageResource(R.drawable.parede);
        Matrix[0][6].setImageResource(R.drawable.parede);
        Matrix[0][7].setImageResource(R.drawable.parede);

        Matrix[1][0].setImageResource(R.drawable.parede);
        Matrix[1][1].setImageResource(R.drawable.chao);
        Matrix[1][2].setImageResource(R.drawable.chamas);
        Matrix[1][3].setImageResource(R.drawable.chamas);
        Matrix[1][4].setImageResource(R.drawable.chamas);
        Matrix[1][5].setImageResource(R.drawable.chao);
        Matrix[1][6].setImageResource(R.drawable.chao);
        Matrix[1][7].setImageResource(R.drawable.parede);

        Matrix[2][0].setImageResource(R.drawable.parede);
        Matrix[2][1].setImageResource(R.drawable.chao);
        Matrix[2][2].setImageResource(R.drawable.caixaaberta);
        Matrix[2][3].setImageResource(R.drawable.chao);
        Matrix[2][4].setImageResource(R.drawable.chao);
        Matrix[2][5].setImageResource(R.drawable.chao);
        Matrix[2][6].setImageResource(R.drawable.chao);
        Matrix[2][7].setImageResource(R.drawable.parede);

        Matrix[3][0].setImageResource(R.drawable.parede);
        Matrix[3][1].setImageResource(R.drawable.chao);
        Matrix[3][2].setImageResource(R.drawable.chao);
        Matrix[3][3].setImageResource(R.drawable.chao);
        Matrix[3][4].setImageResource(R.drawable.chao);
        Matrix[3][5].setImageResource(R.drawable.chao);
        Matrix[3][6].setImageResource(R.drawable.chao);
        Matrix[3][7].setImageResource(R.drawable.parede);

        Matrix[4][0].setImageResource(R.drawable.parede);
        Matrix[4][1].setImageResource(R.drawable.chao);
        Matrix[4][2].setImageResource(R.drawable.chao);
        Matrix[4][3].setImageResource(R.drawable.chao);
        Matrix[4][4].setImageResource(R.drawable.caixaaberta);
        Matrix[4][5].setImageResource(R.drawable.chao);
        Matrix[4][6].setImageResource(R.drawable.chao);
        Matrix[4][7].setImageResource(R.drawable.parede);

        Matrix[5][0].setImageResource(R.drawable.parede);
        Matrix[5][1].setImageResource(R.drawable.chao);
        Matrix[5][2].setImageResource(R.drawable.caixaaberta);
        Matrix[5][3].setImageResource(R.drawable.chao);
        Matrix[5][4].setImageResource(R.drawable.chao);
        Matrix[5][5].setImageResource(R.drawable.chao);
        Matrix[5][6].setImageResource(R.drawable.chao);
        Matrix[5][7].setImageResource(R.drawable.parede);

        Matrix[6][0].setImageResource(R.drawable.parede);
        Matrix[6][1].setImageResource(R.drawable.fantasma);
        Matrix[6][2].setImageResource(R.drawable.chao);
        Matrix[6][3].setImageResource(R.drawable.chao);
        Matrix[6][4].setImageResource(R.drawable.chao);
        Matrix[6][5].setImageResource(R.drawable.chao);
        Matrix[6][6].setImageResource(R.drawable.lobo);
        Matrix[6][7].setImageResource(R.drawable.parede);

        Matrix[7][0].setImageResource(R.drawable.parede);
        Matrix[7][1].setImageResource(R.drawable.parede);
        Matrix[7][2].setImageResource(R.drawable.parede);
        Matrix[7][3].setImageResource(R.drawable.parede);
        Matrix[7][4].setImageResource(R.drawable.parede);
        Matrix[7][5].setImageResource(R.drawable.parede);
        Matrix[7][6].setImageResource(R.drawable.parede);
        Matrix[7][7].setImageResource(R.drawable.parede);


    }

    private void levelFour(){

        fantasma = new Fantasma(5,2);

        reiniciarchamas();

        chamasMatrix[2][2] = true;
        chamasMatrix[3][2] = true;
        chamasMatrix[3][3] = true;

        flag = 4;

        Matrix[0][0].setImageResource(R.drawable.parede);
        Matrix[0][1].setImageResource(R.drawable.parede);
        Matrix[0][2].setImageResource(R.drawable.parede);
        Matrix[0][3].setImageResource(R.drawable.parede);
        Matrix[0][4].setImageResource(R.drawable.parede);
        Matrix[0][5].setImageResource(R.drawable.parede);
        Matrix[0][6].setImageResource(R.drawable.parede);
        Matrix[0][7].setImageResource(R.drawable.parede);

        Matrix[1][0].setImageResource(R.drawable.parede);
        Matrix[1][1].setImageResource(R.drawable.parede);
        Matrix[1][2].setImageResource(R.drawable.chao);
        Matrix[1][3].setImageResource(R.drawable.chao);
        Matrix[1][4].setImageResource(R.drawable.chao);
        Matrix[1][5].setImageResource(R.drawable.parede);
        Matrix[1][6].setImageResource(R.drawable.parede);
        Matrix[1][7].setImageResource(R.drawable.parede);

        Matrix[2][0].setImageResource(R.drawable.parede);
        Matrix[2][1].setImageResource(R.drawable.parede);
        Matrix[2][2].setImageResource(R.drawable.chamas);
        Matrix[2][3].setImageResource(R.drawable.parede);
        Matrix[2][4].setImageResource(R.drawable.parede);
        Matrix[2][5].setImageResource(R.drawable.caixaaberta);
        Matrix[2][6].setImageResource(R.drawable.chao);
        Matrix[2][7].setImageResource(R.drawable.parede);

        Matrix[3][0].setImageResource(R.drawable.parede);
        Matrix[3][1].setImageResource(R.drawable.chao);
        Matrix[3][2].setImageResource(R.drawable.chamas);
        Matrix[3][3].setImageResource(R.drawable.chamas);
        Matrix[3][4].setImageResource(R.drawable.caixaaberta);
        Matrix[3][5].setImageResource(R.drawable.chao);
        Matrix[3][6].setImageResource(R.drawable.chao);
        Matrix[3][7].setImageResource(R.drawable.parede);

        Matrix[4][0].setImageResource(R.drawable.parede);
        Matrix[4][1].setImageResource(R.drawable.chao);
        Matrix[4][2].setImageResource(R.drawable.chao);
        Matrix[4][3].setImageResource(R.drawable.parede);
        Matrix[4][4].setImageResource(R.drawable.caixaaberta);
        Matrix[4][5].setImageResource(R.drawable.chao);
        Matrix[4][6].setImageResource(R.drawable.chao);
        Matrix[4][7].setImageResource(R.drawable.parede);

        Matrix[5][0].setImageResource(R.drawable.paredequebrada);
        Matrix[5][1].setImageResource(R.drawable.caixaaberta);
        Matrix[5][2].setImageResource(R.drawable.fantasma);
        Matrix[5][3].setImageResource(R.drawable.chao);
        Matrix[5][4].setImageResource(R.drawable.chao);
        Matrix[5][5].setImageResource(R.drawable.parede);
        Matrix[5][6].setImageResource(R.drawable.parede);
        Matrix[5][7].setImageResource(R.drawable.parede);

        Matrix[6][0].setImageResource(R.drawable.parede);
        Matrix[6][1].setImageResource(R.drawable.parede);
        Matrix[6][2].setImageResource(R.drawable.parede);
        Matrix[6][3].setImageResource(R.drawable.parede);
        Matrix[6][4].setImageResource(R.drawable.parede);
        Matrix[6][5].setImageResource(R.drawable.parede);
        Matrix[6][6].setImageResource(R.drawable.parede);
        Matrix[6][7].setImageResource(R.drawable.parede);

        Matrix[7][0].setImageResource(R.drawable.parede);
        Matrix[7][1].setImageResource(R.drawable.parede);
        Matrix[7][2].setImageResource(R.drawable.parede);
        Matrix[7][3].setImageResource(R.drawable.parede);
        Matrix[7][4].setImageResource(R.drawable.parede);
        Matrix[7][5].setImageResource(R.drawable.parede);
        Matrix[7][6].setImageResource(R.drawable.parede);
        Matrix[7][7].setImageResource(R.drawable.parede);


    }

    private void levelThree(){

        fantasma = new Fantasma(6,3);

        reiniciarchamas();

        chamasMatrix[1][3] = true;
        chamasMatrix[1][4] = true;
        chamasMatrix[2][4] = true;
        chamasMatrix[3][5] = true;

        flag = 3;

        Matrix[0][0].setImageResource(R.drawable.parede);
        Matrix[0][1].setImageResource(R.drawable.parede);
        Matrix[0][2].setImageResource(R.drawable.parede);
        Matrix[0][3].setImageResource(R.drawable.parede);
        Matrix[0][4].setImageResource(R.drawable.parede);
        Matrix[0][5].setImageResource(R.drawable.parede);
        Matrix[0][6].setImageResource(R.drawable.parede);
        Matrix[0][7].setImageResource(R.drawable.parede);

        Matrix[1][0].setImageResource(R.drawable.parede);
        Matrix[1][1].setImageResource(R.drawable.parede);
        Matrix[1][2].setImageResource(R.drawable.parede);
        Matrix[1][3].setImageResource(R.drawable.chamas);
        Matrix[1][4].setImageResource(R.drawable.chamas);
        Matrix[1][5].setImageResource(R.drawable.parede);
        Matrix[1][6].setImageResource(R.drawable.parede);
        Matrix[1][7].setImageResource(R.drawable.parede);

        Matrix[2][0].setImageResource(R.drawable.parede);
        Matrix[2][1].setImageResource(R.drawable.parede);
        Matrix[2][2].setImageResource(R.drawable.parede);
        Matrix[2][3].setImageResource(R.drawable.chao);
        Matrix[2][4].setImageResource(R.drawable.chamas);
        Matrix[2][5].setImageResource(R.drawable.parede);
        Matrix[2][6].setImageResource(R.drawable.parede);
        Matrix[2][7].setImageResource(R.drawable.parede);

        Matrix[3][0].setImageResource(R.drawable.parede);
        Matrix[3][1].setImageResource(R.drawable.parede);
        Matrix[3][2].setImageResource(R.drawable.chao);
        Matrix[3][3].setImageResource(R.drawable.chao);
        Matrix[3][4].setImageResource(R.drawable.caixaaberta);
        Matrix[3][5].setImageResource(R.drawable.chamas);
        Matrix[3][6].setImageResource(R.drawable.parede);
        Matrix[3][7].setImageResource(R.drawable.parede);

        Matrix[4][0].setImageResource(R.drawable.parede);
        Matrix[4][1].setImageResource(R.drawable.parede);
        Matrix[4][2].setImageResource(R.drawable.chao);
        Matrix[4][3].setImageResource(R.drawable.caixaaberta);
        Matrix[4][4].setImageResource(R.drawable.chao);
        Matrix[4][5].setImageResource(R.drawable.chao);
        Matrix[4][6].setImageResource(R.drawable.parede);
        Matrix[4][7].setImageResource(R.drawable.parede);

        Matrix[5][0].setImageResource(R.drawable.parede);
        Matrix[5][1].setImageResource(R.drawable.chao);
        Matrix[5][2].setImageResource(R.drawable.chao);
        Matrix[5][3].setImageResource(R.drawable.parede);
        Matrix[5][4].setImageResource(R.drawable.caixaaberta);
        Matrix[5][5].setImageResource(R.drawable.caixaaberta);
        Matrix[5][6].setImageResource(R.drawable.chao);
        Matrix[5][7].setImageResource(R.drawable.parede);

        Matrix[6][0].setImageResource(R.drawable.parede);
        Matrix[6][1].setImageResource(R.drawable.chao);
        Matrix[6][2].setImageResource(R.drawable.chao);
        Matrix[6][3].setImageResource(R.drawable.fantasma);
        Matrix[6][4].setImageResource(R.drawable.chao);
        Matrix[6][5].setImageResource(R.drawable.chao);
        Matrix[6][6].setImageResource(R.drawable.chao);
        Matrix[6][7].setImageResource(R.drawable.parede);

        Matrix[7][0].setImageResource(R.drawable.parede);
        Matrix[7][1].setImageResource(R.drawable.parede);
        Matrix[7][2].setImageResource(R.drawable.parede);
        Matrix[7][3].setImageResource(R.drawable.parede);
        Matrix[7][4].setImageResource(R.drawable.parede);
        Matrix[7][5].setImageResource(R.drawable.parede);
        Matrix[7][6].setImageResource(R.drawable.parede);
        Matrix[7][7].setImageResource(R.drawable.parede);


    }

    private void levelTwo() {
        fantasma = new Fantasma(1, 2);

       reiniciarchamas();


        chamasMatrix[4][1] = true;
        chamasMatrix[5][1] = true;
        chamasMatrix[6][1] = true;

        flag = 2;

        Matrix[0][0].setImageResource(R.drawable.parede);
        Matrix[0][1].setImageResource(R.drawable.parede);
        Matrix[0][2].setImageResource(R.drawable.parede);
        Matrix[0][3].setImageResource(R.drawable.parede);
        Matrix[0][4].setImageResource(R.drawable.parede);
        Matrix[0][5].setImageResource(R.drawable.parede);
        Matrix[0][6].setImageResource(R.drawable.parede);
        Matrix[0][7].setImageResource(R.drawable.parede);

        Matrix[1][0].setImageResource(R.drawable.parede);
        Matrix[1][1].setImageResource(R.drawable.parede);
        Matrix[1][2].setImageResource(R.drawable.fantasma);
        Matrix[1][3].setImageResource(R.drawable.chao);
        Matrix[1][4].setImageResource(R.drawable.parede);
        Matrix[1][5].setImageResource(R.drawable.parede);
        Matrix[1][6].setImageResource(R.drawable.parede);
        Matrix[1][7].setImageResource(R.drawable.parede);

        Matrix[2][0].setImageResource(R.drawable.parede);
        Matrix[2][1].setImageResource(R.drawable.parede);
        Matrix[2][2].setImageResource(R.drawable.chao);
        Matrix[2][3].setImageResource(R.drawable.caixaaberta);
        Matrix[2][4].setImageResource(R.drawable.chao);
        Matrix[2][5].setImageResource(R.drawable.chao);
        Matrix[2][6].setImageResource(R.drawable.parede);
        Matrix[2][7].setImageResource(R.drawable.parede);

        Matrix[3][0].setImageResource(R.drawable.parede);
        Matrix[3][1].setImageResource(R.drawable.parede);
        Matrix[3][2].setImageResource(R.drawable.parede);
        Matrix[3][3].setImageResource(R.drawable.chao);
        Matrix[3][4].setImageResource(R.drawable.parede);
        Matrix[3][5].setImageResource(R.drawable.chao);
        Matrix[3][6].setImageResource(R.drawable.parede);
        Matrix[3][7].setImageResource(R.drawable.parede);

        Matrix[4][0].setImageResource(R.drawable.parede);
        Matrix[4][1].setImageResource(R.drawable.chamas);
        Matrix[4][2].setImageResource(R.drawable.parede);
        Matrix[4][3].setImageResource(R.drawable.chao);
        Matrix[4][4].setImageResource(R.drawable.parede);
        Matrix[4][5].setImageResource(R.drawable.chao);
        Matrix[4][6].setImageResource(R.drawable.chao);
        Matrix[4][7].setImageResource(R.drawable.parede);

        Matrix[5][0].setImageResource(R.drawable.parede);
        Matrix[5][1].setImageResource(R.drawable.chamas);
        Matrix[5][2].setImageResource(R.drawable.caixaaberta);
        Matrix[5][3].setImageResource(R.drawable.chao);
        Matrix[5][4].setImageResource(R.drawable.chao);
        Matrix[5][5].setImageResource(R.drawable.parede);
        Matrix[5][6].setImageResource(R.drawable.chao);
        Matrix[5][7].setImageResource(R.drawable.parede);

        Matrix[6][0].setImageResource(R.drawable.parede);
        Matrix[6][1].setImageResource(R.drawable.chamas);
        Matrix[6][2].setImageResource(R.drawable.chao);
        Matrix[6][3].setImageResource(R.drawable.chao);
        Matrix[6][4].setImageResource(R.drawable.chao);
        Matrix[6][5].setImageResource(R.drawable.caixaaberta);
        Matrix[6][6].setImageResource(R.drawable.chao);
        Matrix[6][7].setImageResource(R.drawable.parede);

        Matrix[7][0].setImageResource(R.drawable.parede);
        Matrix[7][1].setImageResource(R.drawable.parede);
        Matrix[7][2].setImageResource(R.drawable.parede);
        Matrix[7][3].setImageResource(R.drawable.parede);
        Matrix[7][4].setImageResource(R.drawable.parede);
        Matrix[7][5].setImageResource(R.drawable.parede);
        Matrix[7][6].setImageResource(R.drawable.parede);
        Matrix[7][7].setImageResource(R.drawable.parede);
    }

    private void levelOne(){

        fantasma = new Fantasma(4,4);

        reiniciarchamas();

        chamasMatrix[1][3] = true;
        chamasMatrix[3][6] = true;
        chamasMatrix[4][1] = true;
        chamasMatrix[6][4] = true;

        flag = 1;

        Matrix[0][0].setImageResource(R.drawable.parede);
        Matrix[0][1].setImageResource(R.drawable.parede);
        Matrix[0][2].setImageResource(R.drawable.parede);
        Matrix[0][3].setImageResource(R.drawable.parede);
        Matrix[0][4].setImageResource(R.drawable.parede);
        Matrix[0][5].setImageResource(R.drawable.parede);
        Matrix[0][6].setImageResource(R.drawable.parede);
        Matrix[0][7].setImageResource(R.drawable.parede);

        Matrix[1][0].setImageResource(R.drawable.parede);
        Matrix[1][1].setImageResource(R.drawable.parede);
        Matrix[1][2].setImageResource(R.drawable.parede);
        Matrix[1][3].setImageResource(R.drawable.chamas);
        Matrix[1][4].setImageResource(R.drawable.parede);
        Matrix[1][5].setImageResource(R.drawable.parede);
        Matrix[1][6].setImageResource(R.drawable.parede);
        Matrix[1][7].setImageResource(R.drawable.parede);

        Matrix[2][0].setImageResource(R.drawable.parede);
        Matrix[2][1].setImageResource(R.drawable.parede);
        Matrix[2][2].setImageResource(R.drawable.parede);
        Matrix[2][3].setImageResource(R.drawable.chao);
        Matrix[2][4].setImageResource(R.drawable.parede);
        Matrix[2][5].setImageResource(R.drawable.parede);
        Matrix[2][6].setImageResource(R.drawable.parede);
        Matrix[2][7].setImageResource(R.drawable.parede);

        Matrix[3][0].setImageResource(R.drawable.parede);
        Matrix[3][1].setImageResource(R.drawable.parede);
        Matrix[3][2].setImageResource(R.drawable.parede);
        Matrix[3][3].setImageResource(R.drawable.caixaaberta);
        Matrix[3][4].setImageResource(R.drawable.chao);
        Matrix[3][5].setImageResource(R.drawable.caixaaberta);
        Matrix[3][6].setImageResource(R.drawable.chamas);
        Matrix[3][7].setImageResource(R.drawable.parede);

        Matrix[4][0].setImageResource(R.drawable.parede);
        Matrix[4][1].setImageResource(R.drawable.chamas);
        Matrix[4][2].setImageResource(R.drawable.chao);
        Matrix[4][3].setImageResource(R.drawable.caixaaberta);
        Matrix[4][4].setImageResource(R.drawable.fantasma);
        Matrix[4][5].setImageResource(R.drawable.parede);
        Matrix[4][6].setImageResource(R.drawable.parede);
        Matrix[4][7].setImageResource(R.drawable.parede);

        Matrix[5][0].setImageResource(R.drawable.parede);
        Matrix[5][1].setImageResource(R.drawable.parede);
        Matrix[5][2].setImageResource(R.drawable.parede);
        Matrix[5][3].setImageResource(R.drawable.parede);
        Matrix[5][4].setImageResource(R.drawable.caixaaberta);
        Matrix[5][5].setImageResource(R.drawable.parede);
        Matrix[5][6].setImageResource(R.drawable.parede);
        Matrix[5][7].setImageResource(R.drawable.parede);

        Matrix[6][0].setImageResource(R.drawable.parede);
        Matrix[6][1].setImageResource(R.drawable.parede);
        Matrix[6][2].setImageResource(R.drawable.parede);
        Matrix[6][3].setImageResource(R.drawable.parede);
        Matrix[6][4].setImageResource(R.drawable.chamas);
        Matrix[6][5].setImageResource(R.drawable.parede);
        Matrix[6][6].setImageResource(R.drawable.parede);
        Matrix[6][7].setImageResource(R.drawable.parede);

        Matrix[7][0].setImageResource(R.drawable.parede);
        Matrix[7][1].setImageResource(R.drawable.parede);
        Matrix[7][2].setImageResource(R.drawable.parede);
        Matrix[7][3].setImageResource(R.drawable.parede);
        Matrix[7][4].setImageResource(R.drawable.parede);
        Matrix[7][5].setImageResource(R.drawable.parede);
        Matrix[7][6].setImageResource(R.drawable.parede);
        Matrix[7][7].setImageResource(R.drawable.parede);


    }

    private void setID(){
        Matrix[0][0] = findViewById(R.id.imageView11);
        Matrix[0][1] = findViewById(R.id.imageView12);
        Matrix[0][2] = findViewById(R.id.imageView13);
        Matrix[0][3] = findViewById(R.id.imageView14);
        Matrix[0][4] = findViewById(R.id.imageView15);
        Matrix[0][5] = findViewById(R.id.imageView16);
        Matrix[0][6] = findViewById(R.id.imageView17);
        Matrix[0][7] = findViewById(R.id.imageView18);

        Matrix[1][0] = findViewById(R.id.imageView21);
        Matrix[1][1] = findViewById(R.id.imageView22);
        Matrix[1][2] = findViewById(R.id.imageView23);
        Matrix[1][3] = findViewById(R.id.imageView24);
        Matrix[1][4] = findViewById(R.id.imageView25);
        Matrix[1][5] = findViewById(R.id.imageView26);
        Matrix[1][6] = findViewById(R.id.imageView27);
        Matrix[1][7] = findViewById(R.id.imageView28);

        Matrix[2][0] = findViewById(R.id.imageView31);
        Matrix[2][1] = findViewById(R.id.imageView32);
        Matrix[2][2] = findViewById(R.id.imageView33);
        Matrix[2][3] = findViewById(R.id.imageView34);
        Matrix[2][4] = findViewById(R.id.imageView35);
        Matrix[2][5] = findViewById(R.id.imageView36);
        Matrix[2][6] = findViewById(R.id.imageView37);
        Matrix[2][7] = findViewById(R.id.imageView38);

        Matrix[3][0] = findViewById(R.id.imageView41);
        Matrix[3][1] = findViewById(R.id.imageView42);
        Matrix[3][2] = findViewById(R.id.imageView43);
        Matrix[3][3] = findViewById(R.id.imageView44);
        Matrix[3][4] = findViewById(R.id.imageView45);
        Matrix[3][5] = findViewById(R.id.imageView46);
        Matrix[3][6] = findViewById(R.id.imageView47);
        Matrix[3][7] = findViewById(R.id.imageView48);

        Matrix[4][0] = findViewById(R.id.imageView51);
        Matrix[4][1] = findViewById(R.id.imageView52);
        Matrix[4][2] = findViewById(R.id.imageView53);
        Matrix[4][3] = findViewById(R.id.imageView54);
        Matrix[4][4] = findViewById(R.id.imageView55);
        Matrix[4][5] = findViewById(R.id.imageView56);
        Matrix[4][6] = findViewById(R.id.imageView57);
        Matrix[4][7] = findViewById(R.id.imageView58);

        Matrix[5][0] = findViewById(R.id.imageView61);
        Matrix[5][1] = findViewById(R.id.imageView62);
        Matrix[5][2] = findViewById(R.id.imageView63);
        Matrix[5][3] = findViewById(R.id.imageView64);
        Matrix[5][4] = findViewById(R.id.imageView65);
        Matrix[5][5] = findViewById(R.id.imageView66);
        Matrix[5][6] = findViewById(R.id.imageView67);
        Matrix[5][7] = findViewById(R.id.imageView68);

        Matrix[6][0] = findViewById(R.id.imageView71);
        Matrix[6][1] = findViewById(R.id.imageView72);
        Matrix[6][2] = findViewById(R.id.imageView73);
        Matrix[6][3] = findViewById(R.id.imageView74);
        Matrix[6][4] = findViewById(R.id.imageView75);
        Matrix[6][5] = findViewById(R.id.imageView76);
        Matrix[6][6] = findViewById(R.id.imageView77);
        Matrix[6][7] = findViewById(R.id.imageView78);

        Matrix[7][0] = findViewById(R.id.imageView81);
        Matrix[7][1] = findViewById(R.id.imageView82);
        Matrix[7][2] = findViewById(R.id.imageView83);
        Matrix[7][3] = findViewById(R.id.imageView84);
        Matrix[7][4] = findViewById(R.id.imageView85);
        Matrix[7][5] = findViewById(R.id.imageView86);
        Matrix[7][6] = findViewById(R.id.imageView87);
        Matrix[7][7] = findViewById(R.id.imageView88);

    }



    public void voltarMenu(){
        Intent intent = new Intent(Jogo.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean todasCaveirasNasBandeiras() {
        for (int i = 0; i < Matrix.length; i++) {
            for (int j = 0; j < Matrix[i].length; j++) {
                Drawable cellDrawable = Matrix[i][j].getDrawable();
                if (cellDrawable.getConstantState().equals(ContextCompat.getDrawable(this, R.drawable.caixaaberta).getConstantState())) {
                    if (!Matrix[i][j].getDrawable().getConstantState().equals(ContextCompat.getDrawable(this, R.drawable.chamas).getConstantState())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isChama(int row, int col) {
        return chamasMatrix[row][col];
    }

    private void reiniciarchamas() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chamasMatrix[i][j] = false;
            }
        }
    }


   private boolean movimentoValidoLobo(int newRow, int newCol) {
        Drawable parede = ContextCompat.getDrawable(this, R.drawable.parede);
        Drawable caixaAberta = ContextCompat.getDrawable(this, R.drawable.caixaaberta);
        Drawable caixaFechada = ContextCompat.getDrawable(this, R.drawable.caixafechada);


        if (newRow >= 0 && newRow < Matrix.length && newCol >= 0 && newCol < Matrix[0].length) {
            Drawable cellDrawable = Matrix[newRow][newCol].getDrawable();
            return !cellDrawable.getConstantState().equals(parede.getConstantState()) &&
                    !cellDrawable.getConstantState().equals(caixaAberta.getConstantState()) &&
                    !cellDrawable.getConstantState().equals(caixaFechada.getConstantState());
        }else {
            return false;
        }
    }


    private void moverEsquerdaLobo() {
        if (isChama(lobo.getLinha(), lobo.getColuna())) {
            Matrix[lobo.getLinha()][lobo.getColuna()].setImageResource(R.drawable.chamas);
        } else {
            Matrix[lobo.getLinha()][lobo.getColuna()].setImageResource(R.drawable.chao);
        }
        lobo.moverEsquerda();
        Matrix[lobo.getLinha()][lobo.getColuna()].setImageResource(R.drawable.lobo);

    }

    private void moverDireitaLobo() {
        if (isChama(lobo.getLinha(), lobo.getColuna())) {
            Matrix[lobo.getLinha()][lobo.getColuna()].setImageResource(R.drawable.chamas);
        } else {
            Matrix[lobo.getLinha()][lobo.getColuna()].setImageResource(R.drawable.chao);
        }
        lobo.moverDireita();
        Matrix[lobo.getLinha()][lobo.getColuna()].setImageResource(R.drawable.lobo);
    }

    private void moverBaixoLobo() {
        if (isChama(lobo.getLinha(), lobo.getColuna())) {
            Matrix[lobo.getLinha()][lobo.getColuna()].setImageResource(R.drawable.chamas);
        } else {
            Matrix[lobo.getLinha()][lobo.getColuna()].setImageResource(R.drawable.chao);
        }
        lobo.moverBaixo();
        Matrix[lobo.getLinha()][lobo.getColuna()].setImageResource(R.drawable.lobo);
    }

    private void moverCimaLobo() {
        if (isChama(lobo.getLinha(), lobo.getColuna())) {
            Matrix[lobo.getLinha()][lobo.getColuna()].setImageResource(R.drawable.chamas);
        } else {
            Matrix[lobo.getLinha()][lobo.getColuna()].setImageResource(R.drawable.chao);
        }
        lobo.moverCima();
        Matrix[lobo.getLinha()][lobo.getColuna()].setImageResource(R.drawable.lobo);
    }

    private void moverLoboAleatoriamente() {
        Random rand = new Random();
        int movimento = rand.nextInt(4);

        switch (movimento) {
            case 0:
                if (movimentoValidoLobo(lobo.getLinha(), lobo.getColuna() - 1)) {
                    moverEsquerdaLobo();
                }
                break;
            case 1:
                if (movimentoValidoLobo(lobo.getLinha(), lobo.getColuna() + 1)) {
                    moverDireitaLobo();
                }
                break;
            case 2:
                if (movimentoValidoLobo(lobo.getLinha() + 1, lobo.getColuna())) {
                    moverBaixoLobo();
                }
                break;
            case 3:
                if (movimentoValidoLobo(lobo.getLinha() - 1, lobo.getColuna())) {
                    moverCimaLobo();
                }
                break;
        }
    }

}




