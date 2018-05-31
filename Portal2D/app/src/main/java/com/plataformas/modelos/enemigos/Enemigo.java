package com.plataformas.modelos.enemigos;

import android.content.Context;
import android.graphics.Canvas;

import com.plataformas.graficos.Sprite;
import com.plataformas.modelos.Modelo;
import com.plataformas.modelos.Nivel;

import java.util.HashMap;

/**
 * Created by jordansoy on 09/10/2017.
 */


public abstract class Enemigo extends Modelo {
    public int estado = ACTIVO;
    public static final int ACTIVO = 1;
    public static final int INACTIVO = 0;
    public static final int ELIMINAR = -1;


    public static final String DISPARANDO = "disparando";
    public static final String CAMINANDO = "caminando";
    public static final String MUERTE = "muerte";
    private double velocidadPrevia;

    protected Sprite sprite;
    protected HashMap<String,Sprite> sprites = new HashMap<String,Sprite> ();

    public double velocidadX = 1.2;

    public Enemigo(Context context, double xInicial, double yInicial) {
        super(context, 0, 0, 40, 40);

        this.x = xInicial;
        this.y = yInicial - altura/2;

        cDerecha = 15;
        cIzquierda = 15;
        cArriba = 20;
        cAbajo = 20;
    }

    public void destruir (){
        velocidadPrevia = velocidadX;
        velocidadX = 0;
        estado = INACTIVO;
    }


    public void actualizar (long tiempo) {
        boolean finSprite = sprite.actualizar(tiempo);

        if ( estado == INACTIVO && finSprite == true){
            estado = ELIMINAR;
        }

        if (estado == INACTIVO){
            sprite = sprites.get(MUERTE);

        } else {
            sprite = sprites.get(CAMINANDO);
        }
    }

    public void girar(){
        velocidadX = velocidadX*-1;
    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y- Nivel.scrollEjeY);
    }

}
