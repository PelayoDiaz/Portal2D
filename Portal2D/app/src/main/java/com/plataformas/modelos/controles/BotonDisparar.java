package com.plataformas.modelos.controles;

import android.content.Context;

import com.plataformas.GameView;
import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.modelos.Modelo;

/**
 * Created by jordansoy on 09/10/2017.
 */

public class BotonDisparar extends Modelo {

    public final static int NORTE = 0;
    public final static int ESTE = 1;
    public final static int OESTE = 2;
    public final static int NORESTE = 3;
    public final static int NOROESTE = 4;
    public final static int SURESTE = 5;
    public final static int SUROESTE = 6;
    public final static int SUR = 7;

    public BotonDisparar(Context context) {
        super(context, GameView.pantallaAncho*0.85 , GameView.pantallaAlto*0.6,
                100,100);

        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.pad_direccion);
    }

    public boolean estaPulsado(float clickX, float clickY) {
        boolean estaPulsado = false;

        if (clickX <= (x + ancho / 2) && clickX >= (x - ancho / 2)
                && clickY <= (y + altura / 2) && clickY >= (y - altura / 2)) {
            estaPulsado = true;
        }
        return estaPulsado;
    }

    public int getDireccion(float clickX, float clickY){
        double pX =x;
        double pY = y;
        double pa = altura;
        if ((clickX <= x+10) && (clickX >= x-10) && (clickY < y))
            return NORTE;
        else if((clickX <= x+10) && (clickX >= x-10) && (clickY >= y))
            return SUR;
        else if (clickX <= (x + ancho / 2) && (clickX > x) && (clickY >= y-altura/6) && (clickY < y+10))
            return ESTE;
        else if (clickX <= (x + ancho / 2) && (clickX > x) && (clickY <= y-altura/6) && (clickY >= y-altura-20))
            return NORESTE;
        else if (clickX >= (x - ancho / 2) && (clickX < x) && (clickY >= y-altura/6) && (clickY < y+10))
            return OESTE;
        else if (clickX >= (x - ancho / 2) && (clickX < x) && (clickY <= y-altura/6) && (clickY >= y-altura-20))
            return NOROESTE;
        else if (clickX <= (x + ancho / 2) && (clickX > x) && (clickY <= y+altura/2) && (clickY <= y+altura-20))
            return SURESTE;
        else if (clickX >= (x - ancho / 2) && (clickX < x) && (clickY <= y+altura/2) && (clickY <= y+altura-20))
            return SUROESTE;
        return ESTE;
    }
}

