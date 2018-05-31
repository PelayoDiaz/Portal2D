package com.plataformas.modelos.controles;

import android.content.Context;

import com.plataformas.GameView;
import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.modelos.Modelo;

/**
 * Created by Pelayo Díaz Soto on 12/12/2017.
 */

public class BotonPortalAzul extends Modelo{

    public BotonPortalAzul(Context context) {
        super(context, GameView.pantallaAncho*0.50 , GameView.pantallaAlto*0.85,
                55,24);
        setImagen(false);
    }

    public void setImagen(boolean pulsado){
        if (pulsado)
            imagen = CargadorGraficos.cargarDrawable(context, R.drawable.blue_portal_selected);
        else
            imagen = CargadorGraficos.cargarDrawable(context, R.drawable.blue_portal_button);
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
