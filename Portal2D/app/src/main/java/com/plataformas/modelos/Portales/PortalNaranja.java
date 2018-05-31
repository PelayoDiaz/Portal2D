package com.plataformas.modelos.Portales;

import android.content.Context;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.graficos.Sprite;

/**
 * Created by Pelayo Díaz Soto on 12/12/2017.
 */

public class PortalNaranja extends Portal{

    private PortalAzul portalAzul;
    private int direccionSalida=0;

    public PortalNaranja(Context context, double x, double y) {
        super(context, x, y);
        this.inicializar();
    }


    public void inicializar (){
        sprite= new Sprite(
                CargadorGraficos.cargarDrawable(context,
                        R.drawable.orange_portal),
                ancho, altura,
                8, 4, true);
    }

    public void setPortalAzul(PortalAzul portalAzul){
        if (portalAzul!=null)
            this.portalAzul = portalAzul;
    }

    public PortalAzul getPortalAzul(){
        return this.portalAzul;
    }

    public int getDireccionSalida() {
        return direccionSalida;
    }

    public void setDireccionSalida(int direccionSalida) {
        this.direccionSalida = direccionSalida;
    }
}
