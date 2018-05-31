package com.plataformas.modelos.controles;

import android.content.Context;

import com.plataformas.GameView;
import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.modelos.Modelo;

/**
 * Created by Pelayo Díaz Soto on 13/12/2017.
 */

public class BotonBorrarPortales extends Modelo{

    public BotonBorrarPortales(Context context) {
        super(context, GameView.pantallaAncho*0.42 , GameView.pantallaAlto*0.85,
                55,24);

        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.remove_portal);
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
