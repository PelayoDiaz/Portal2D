package com.plataformas.modelos.Portales;

import android.content.Context;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.graficos.Sprite;

/**
 * Created by Pelayo Díaz Soto on 12/12/2017.
 */

public class PortalAzul extends Portal{

    private PortalNaranja portalNaranja;
    private int direccionSalida;

    public PortalAzul(Context context, double x, double y) {
        super(context, x, y);
        this.inicializar();
    }


    public void inicializar (){
        sprite= new Sprite(
                CargadorGraficos.cargarDrawable(context,
                        R.drawable.blue_portal),
                ancho, altura,
                8, 4, true);
    }

    public void setPortalNaranja(PortalNaranja portalNaranja){
        if (portalNaranja!=null)
            this.portalNaranja = portalNaranja;
    }

    public PortalNaranja getPortalNaranja(){
        return this.portalNaranja;
    }

    public int getDireccionSalida() {
        return direccionSalida;
    }

    public void setDireccionSalida(int direccionSalida) {
        this.direccionSalida = direccionSalida;
    }
}
