package com.plataformas.modelos.puntosControl;

import android.content.Context;
import android.graphics.Canvas;

import com.plataformas.modelos.Modelo;
import com.plataformas.modelos.Nivel;

/**
 * Created by Pelayo Díaz Soto on 26/11/2017.
 */

public class Control extends Modelo {

    public Control(Context context, double x, double y, int altura, int ancho) {
        super(context, x, y, altura, ancho);
        this.y =  y - altura/2;
    }

    public void dibujar(Canvas canvas){
        int yArriva = (int)  y - altura / 2 - Nivel.scrollEjeY;
        int xIzquierda = (int) x - ancho / 2 - Nivel.scrollEjeX;

        imagen.setBounds(xIzquierda, yArriva, xIzquierda
                + ancho, yArriva + altura);
        imagen.draw(canvas);

    }
}
