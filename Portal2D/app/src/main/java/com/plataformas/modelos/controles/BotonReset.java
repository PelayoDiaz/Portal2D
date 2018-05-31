package com.plataformas.modelos.controles;

import android.content.Context;

import com.plataformas.GameView;
import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.modelos.Modelo;

/**
 * Created by jordansoy on 09/10/2017.
 */

public class BotonReset extends Modelo {

    public BotonReset(Context context) {
        super(context, GameView.pantallaAncho*0.20 , GameView.pantallaAlto*0.1,
                GameView.pantallaAlto, GameView.pantallaAncho);

        altura = 30;
        ancho = 30;
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.reset);
    }

    public boolean estaPulsado(float clickX, float clickY) {
        boolean estaPulsado = false;

        if (clickX <= (x + ancho / 2) && clickX >= (x - ancho / 2)
                && clickY <= (y + altura / 2) && clickY >= (y - altura / 2)) {
            estaPulsado = true;
        }
        return estaPulsado;
    }

}

