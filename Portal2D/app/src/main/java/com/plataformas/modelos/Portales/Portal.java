package com.plataformas.modelos.Portales;

import android.content.Context;
import android.graphics.Canvas;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.graficos.Sprite;
import com.plataformas.modelos.Modelo;
import com.plataformas.modelos.Nivel;

/**
 * Created by Pelayo Díaz Soto on 11/12/2017.
 */

public class Portal extends Modelo {

    protected Sprite sprite;

    public Portal(Context context, double x, double y) {
        super(context, x, y, 69, 30);
    }

    public void actualizar (long tiempo) {
        sprite.actualizar(tiempo);
    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y- Nivel.scrollEjeY);
    }
}
