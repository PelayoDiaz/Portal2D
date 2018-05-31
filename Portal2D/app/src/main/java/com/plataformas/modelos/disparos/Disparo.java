package com.plataformas.modelos.disparos;

import android.content.Context;
import android.graphics.Canvas;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.graficos.Sprite;
import com.plataformas.modelos.Jugador;
import com.plataformas.modelos.Modelo;
import com.plataformas.modelos.Nivel;
import com.plataformas.modelos.controles.BotonDisparar;

/**
 * Created by Pelayo Díaz Soto on 21/11/2017.
 */

public abstract class Disparo extends Modelo {
    protected Sprite sprite;
    public double velocidadX = 20;
    public double velocidadY=0;

    public Disparo(Context context, double xInicial, double yInicial, int orientacion) {
        super(context, xInicial, yInicial, 35, 35);

        cDerecha = 6;
        cIzquierda = 6;
        cArriba = 6;
        cAbajo = 6;
    }


    public void actualizar (long tiempo) {
        sprite.actualizar(tiempo);
    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y- Nivel.scrollEjeY);
    }

    public void setDireccion(int direccion){
        switch (direccion){
            case BotonDisparar.NORTE:
                this.velocidadX = 0;
                this.velocidadY = -20;
                break;
            case BotonDisparar.ESTE:
                this.velocidadX = 20;
                this.velocidadY = 0;
                break;
            case BotonDisparar.NORESTE:
                this.velocidadX = 15;
                this.velocidadY = -15;
                break;
            case BotonDisparar.OESTE:
                this.velocidadX = -20;
                this.velocidadY = 0;
                break;
            case BotonDisparar.NOROESTE:
                this.velocidadX = -15;
                this.velocidadY = -15;
                break;
            case BotonDisparar.SUROESTE:
                this.velocidadX = -15;
                this.velocidadY = 15;
                break;
            case BotonDisparar.SURESTE:
                this.velocidadX = 15;
                this.velocidadY = 15;
                break;
            case BotonDisparar.SUR:
                this.velocidadX = 0;
                this.velocidadY = 20;
                break;
        }
    }

    public void teletransportar(double x, double y, int direccionSalida) {
        this.x = x+(35*direccionSalida);
        this.y = y-velocidadY;
    }
}