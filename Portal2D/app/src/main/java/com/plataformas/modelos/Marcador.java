package com.plataformas.modelos;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Pelayo Díaz Soto on 26/11/2017.
 */

public class Marcador extends Modelo{
    private int puntos;

    public Marcador(Context context, double x, double y) {
        super(context, x, y, 20, 20);
    }

    public void dibujar(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(20);
        canvas.drawText(String.valueOf(puntos), (int)x, (int)y, paint);
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }
}
