package com.plataformas.modelos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.plataformas.GameView;
import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.gestores.Utilidades;
import com.plataformas.modelos.Portales.PortalAzul;
import com.plataformas.modelos.Portales.PortalNaranja;
import com.plataformas.modelos.Tiles.Tile;
import com.plataformas.modelos.coleccionables.Coleccionable;
import com.plataformas.modelos.disparos.Disparo;
import com.plataformas.modelos.disparos.DisparoJugador;
import com.plataformas.modelos.disparos.DisparoJugadorAzul;
import com.plataformas.modelos.disparos.DisparoJugadorNaranja;
import com.plataformas.modelos.disparos.DisparoCore;
import com.plataformas.modelos.enemigos.CorePasivo;
import com.plataformas.modelos.enemigos.Enemigo;
import com.plataformas.modelos.enemigos.Core;
import com.plataformas.modelos.puntosControl.Caja;
import com.plataformas.modelos.puntosControl.CajaExplosiva;
import com.plataformas.modelos.puntosControl.CajaMuro;
import com.plataformas.modelos.puntosControl.Interruptor;
import com.plataformas.modelos.puntosControl.Meta;
import com.plataformas.modelos.puntosControl.PuntoSalvado;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Nivel {

    public static final boolean NARANJA = false;
    public static final boolean AZUL = true;


    public static int scrollEjeX = 0;
    public static int scrollEjeY=0;
    public float orientacionPad = 0;
    public boolean botonSaltarPulsado = false;
    public boolean botonDispararPulsado = false;

    private Context context = null;
    private int numeroNivel;
    private Fondo[] fondos;
    private Tile[][] mapaTiles;
    public Jugador jugador;
    public boolean inicializado;
    private float velocidadGravedad = 0.8f;
    private float velocidadMaximaCaida = 10 ;
    private List<Enemigo> enemigos;
    private List<Coleccionable> coleccionables;
    private List<DisparoJugador> disparosJugador;
    private List<DisparoCore> disparosCore;
    private List<CajaMuro> cajaMuros;
    private List<CajaExplosiva> cajaExplosivas;
    private List<Interruptor> interruptores;
    private Meta meta;
    private List<PuntoSalvado> banderas;
    private List<Caja> cajas;
    private PortalNaranja naranja;
    private PortalAzul azul;
    public GameView gameView;

    public Bitmap mensaje;
    public boolean nivelPausado;
    public boolean colorPortal;
    public boolean borrarPortales;
    public int direccionDisparo;

    public Nivel(Context context, int numeroNivel) throws Exception {
        inicializado = false;

        this.context = context;
        this.numeroNivel = numeroNivel;
        inicializar();

        inicializado = true;
    }

    public void inicializar()throws Exception {
        scrollEjeX = 0;

        mensaje = CargadorGraficos.cargarBitmap(context, R.drawable.description);
        nivelPausado = true;
        borrarPortales = false;

        disparosJugador = new LinkedList<DisparoJugador>();
        disparosCore = new LinkedList<DisparoCore>();
        enemigos = new LinkedList<Enemigo>();
        coleccionables = new LinkedList<Coleccionable>();
        cajaMuros = new LinkedList<CajaMuro>();
        cajaExplosivas = new LinkedList<CajaExplosiva>();
        cajas = new LinkedList<Caja>();
        interruptores = new LinkedList<Interruptor>();
        banderas = new LinkedList<PuntoSalvado>();
        fondos = new Fondo[2];
        fondos[0] = new Fondo(context,CargadorGraficos.cargarBitmap(context,
                R.drawable.fondo_1), 0);
        fondos[1] = new Fondo(context,CargadorGraficos.cargarBitmap(context,
                R.drawable.medium_tres), 1f);
        inicializarMapaTiles();
        scrollEjeY= (altoMapaTiles()* Tile.altura - GameView.pantallaAlto);
    }

    private void inicializarMapaTiles() throws Exception {
        InputStream is = context.getAssets().open(numeroNivel+".txt");
        int anchoLinea;

        List<String> lineas = new LinkedList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        {
            String linea = reader.readLine();
            anchoLinea = linea.length();
            while (linea != null)
            {
                lineas.add(linea);
                if (linea.length() != anchoLinea)
                {
                    Log.e("ERROR", "Dimensiones incorrectas en la línea");
                    throw new Exception("Dimensiones incorrectas en la línea.");
                }
                linea = reader.readLine();
            }
        }

        // Inicializar la matriz
        mapaTiles = new Tile[anchoLinea][lineas.size()];
        // Iterar y completar todas las posiciones
        for (int y = 0; y < altoMapaTiles(); ++y) {
            for (int x = 0; x < anchoMapaTiles(); ++x) {
                char tipoDeTile = lineas.get(y).charAt(x);//lines[y][x];
                mapaTiles[x][y] = inicializarTile(tipoDeTile,x,y);
            }
        }
        enlazarInterruptores();
    }

    private Tile inicializarTile(char codigoTile,int x, int y) {
        switch (codigoTile) {
            case 'C':
                // Core
                int xCentroAbajoTileZ = x * Tile.ancho + Tile.ancho/2;
                int yCentroAbajoTileZ = y * Tile.altura + Tile.altura;
                enemigos.add(new Core(context,xCentroAbajoTileZ,yCentroAbajoTileZ));
                return new Tile(null, Tile.PASABLE);
            case 'Z':
                int xCentroAbajoTileX = x * Tile.ancho + Tile.ancho/2;
                int yCentroAbajoTileX = y * Tile.altura + Tile.altura;
                enemigos.add(new CorePasivo(context,xCentroAbajoTileX,yCentroAbajoTileX));
                return new Tile(null, Tile.PASABLE);

            case 'S':
                // Interruptor
                int xCentroAbajoTileS = x * Tile.ancho + Tile.ancho/2;
                int yCentroAbajoTileS = y * Tile.altura + Tile.altura;
                interruptores.add(new Interruptor(context,xCentroAbajoTileS,yCentroAbajoTileS));

                return new Tile(null, Tile.PASABLE);

            case '1':
                // Jugador
                // Posicion centro abajo
                int xCentroAbajoTile = x * Tile.ancho + Tile.ancho/2;
                int yCentroAbajoTile = y * Tile.altura + Tile.altura;
                jugador = new Jugador(context,xCentroAbajoTile,yCentroAbajoTile);

                return new Tile(null, Tile.PASABLE);

            case '.':
                // en blanco, sin textura
                return new Tile(null, Tile.PASABLE);
            case '#':
                // bloque de musgo, no se puede pasar
                return new Tile(
                        CargadorGraficos.cargarDrawable(context, R.drawable.platform_dos)
                        , Tile.SOLIDO);
            case '^':
                // bloque de musgo, no se puede pasar
                return new Tile(
                        CargadorGraficos.cargarDrawable(context, R.drawable.suelo_3)
                        , Tile.SOLIDO, true);
            case 'O':
                crearCajaExplosiva(x, y);
                return new Tile(null, Tile.SOLIDO);
            case 'M':
                int xCentroAbajoTileM = x * Tile.ancho + Tile.ancho/2;
                int yCentroAbajoTileM = y * Tile.altura + Tile.altura;
                meta = new Meta(context,xCentroAbajoTileM,yCentroAbajoTileM);

                return new Tile(null, Tile.PASABLE);
            case 'K':
                //Coleccionable
                int xCentroAbajoTileC = x * Tile.ancho + Tile.ancho/2;;
                int yCentroAbajoTileC = y * Tile.altura + Tile.altura/2;
                coleccionables.add(new Coleccionable(context,xCentroAbajoTileC,yCentroAbajoTileC));
                return new Tile(null, Tile.PASABLE);
            case 'B':
                //Caja
                int xCentroAbajoTileB = x * Tile.ancho + Tile.ancho/2;
                int yCentroAbajoTileB = y * Tile.altura + Tile.altura;
                cajas.add(new Caja(context,xCentroAbajoTileB,yCentroAbajoTileB));
                return new Tile(null, Tile.PASABLE);
            case '2':
                crearCajaMuro(x, y, 2);
                return new Tile(null, Tile.SOLIDO);
            case '3':
                crearCajaMuro(x, y, 3);
                return new Tile(null, Tile.SOLIDO);
            case '4':
                crearCajaMuro(x, y, 4);
                return new Tile(null, Tile.SOLIDO);
            case '5':
                crearCajaMuro(x, y, 5);
                return new Tile(null, Tile.SOLIDO);
            case '6':
                crearCajaMuro(x, y, 6);
                return new Tile(null, Tile.SOLIDO);
            case '7':
                crearCajaMuro(x, y, 7);
                return new Tile(null, Tile.SOLIDO);
            case '8':
                crearCajaMuro(x, y, 8);
                return new Tile(null, Tile.SOLIDO);
            case '9':
                crearCajaMuro(x, y, 9);
                return new Tile(null, Tile.SOLIDO);

            case 'A':
                int xCentroAbajoTileA = x * Tile.ancho + Tile.ancho/2;
                int yCentroAbajoTileA = y * Tile.altura + Tile.altura;
                banderas.add(new PuntoSalvado(context,xCentroAbajoTileA,yCentroAbajoTileA));
                return new Tile(null, Tile.PASABLE);
            default:
                //cualquier otro caso
                return new Tile(null, Tile.PASABLE);
        }
    }

    public void crearCajaMuro(int x, int y, int enlace){
        int xCentroAbajoTileA = x * Tile.ancho + Tile.ancho/2;
        int yCentroAbajoTileA = y * Tile.altura + Tile.altura;
        cajaMuros.add(new CajaMuro(context,xCentroAbajoTileA,yCentroAbajoTileA, x, y, enlace));
    }

    public void crearCajaExplosiva(int x, int y){
        int xCentroAbajoTileA = x * Tile.ancho + Tile.ancho/2;
        int yCentroAbajoTileA = y * Tile.altura + Tile.altura;
        cajaExplosivas.add(new CajaExplosiva(context,xCentroAbajoTileA,yCentroAbajoTileA, x, y));
    }

    public Tile crearPortal(int x, int y, int suma, int direccion){
        int xCentroAbajoTileP = x * Tile.ancho +suma;
        int yCentroAbajoTileP = y * Tile.altura;
        if (!colorPortal) {
            this.naranja = new PortalNaranja(context, xCentroAbajoTileP, yCentroAbajoTileP);
            this.naranja.setDireccionSalida(direccion);
            enlazarPortales();
        } else {
            this.azul = new PortalAzul(context, xCentroAbajoTileP, yCentroAbajoTileP);
            this.azul.setDireccionSalida(direccion);
            enlazarPortales();
        }

        return new Tile(null, Tile.PASABLE);
    }

    public void actualizar (long tiempo) throws Exception {
        if (inicializado) {
            if (naranja!=null){
                naranja.actualizar(tiempo);
            }
            if(azul!=null){
                azul.actualizar(tiempo);
            }

            for(DisparoJugador disparoJugador: disparosJugador){
                disparoJugador.actualizar(tiempo);
            }
            for(DisparoCore disparoCore : disparosCore){
                disparoCore.actualizar(tiempo);
            }

            for(Enemigo enemigo: enemigos){
                enemigo.actualizar(tiempo);
            }

            for (Coleccionable coleccionable : coleccionables){
                coleccionable.actualizar(tiempo);
            }

            jugador.procesarOrdenes(orientacionPad, botonSaltarPulsado,botonDispararPulsado);
            if (botonSaltarPulsado)
                botonSaltarPulsado = false;

            if (botonDispararPulsado) {
                if (colorPortal) {
                    DisparoJugadorAzul disparo = new DisparoJugadorAzul(context,jugador.x,jugador.y, jugador.orientacion);
                    disparo.setDireccion(this.direccionDisparo);
                    disparosJugador.add(disparo);
                }
                else{
                    DisparoJugadorNaranja disparo = new DisparoJugadorNaranja(context,jugador.x,jugador.y, jugador.orientacion);
                    disparo.setDireccion(this.direccionDisparo);
                    disparosJugador.add(disparo);
                }
                botonDispararPulsado = false;
            }

            if (borrarPortales){
                this.naranja=null;
                this.azul=null;
                this.borrarPortales=false;
            }
            crearDisparos();

            jugador.actualizar(tiempo);
            aplicarReglasMovimiento();
        }
    }
    private void crearDisparos() {

        long tiempo = System.currentTimeMillis();
        for (Enemigo enemigo : enemigos) {
            if (enemigo instanceof Core) {
                DisparoCore disparo = (DisparoCore) ((Core)enemigo).disparar(tiempo, jugador.x);
                if (disparo != null) {
                    disparosCore.add(disparo);
                }
            }
        }
    }

    private void aplicarReglasMovimiento() throws Exception {
        if( jugador.colisiona(meta) && meta.isAbierta() ) {
            gameView.nivelCompleto();
            return;
        }
        for (PuntoSalvado bandera: banderas){
            if( jugador.colisiona(bandera) ) {
                jugador.guardarPosicion(bandera.x, bandera.y);
            }
        }

        reglasCajas();

        int tileXJugadorIzquierda
                = (int) (jugador.x - (jugador.ancho / 2 - 1)) / Tile.ancho;
        int tileXJugadorDerecha
                = (int) (jugador.x + (jugador.ancho / 2 - 1)) / Tile.ancho;

        int tileYJugadorInferior
                = (int) (jugador.y + (jugador.altura / 2 - 1)) / Tile.altura;
        int tileYJugadorCentro
                = (int) jugador.y / Tile.altura;
        int tileYJugadorSuperior
                = (int) (jugador.y - (jugador.altura / 2 - 1)) / Tile.altura;



        for (Iterator<Enemigo> iterator = enemigos.iterator(); iterator.hasNext();) {
            Enemigo enemigo = iterator.next();

            if (enemigo.estado == Enemigo.ELIMINAR) {
                iterator.remove();
                continue;
            }

            if (enemigo.estado != Enemigo.ACTIVO)
                continue;

            int tileXEnemigoIzquierda =
                    (int) (enemigo.x - (enemigo.ancho / 2 - 1)) / Tile.ancho;
            int tileXEnemigoDerecha =
                    (int) (enemigo.x + (enemigo.ancho / 2 - 1)) / Tile.ancho;

            int tileYEnemigoInferior =
                    (int) (enemigo.y + (enemigo.altura / 2 - 1)) / Tile.altura;
            int tileYEnemigoCentro =
                    (int) enemigo.y / Tile.altura;
            int tileYEnemigoSuperior =
                    (int) (enemigo.y - (enemigo.altura / 2 - 1)) / Tile.altura;

            int rango = 4;
            if (tileXJugadorIzquierda - rango < tileXEnemigoIzquierda &&
                    tileXJugadorIzquierda + rango > tileXEnemigoIzquierda) {

                if (jugador.colisiona(enemigo)) {
                    if (jugador.golpeado() <= 0) {
                        jugador.restablecerPosicionInicial();
                        scrollEjeX = 0;
                        nivelPausado = true;
                        mensaje = CargadorGraficos.cargarBitmap(context, R.drawable.you_lose);
                        return;
                    }
                }
            }
                if (enemigo.velocidadX > 0) {
                    //  Solo una condicion para pasar:  Tile delante libre, el de abajo solido
                    if (tileXEnemigoDerecha + 1 <= anchoMapaTiles() - 1 &&
                            mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoInferior].tipoDeColision ==
                                    Tile.PASABLE &&
                            mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoCentro].tipoDeColision ==
                                    Tile.PASABLE &&
                            mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoSuperior].tipoDeColision ==
                                    Tile.PASABLE &&
                            mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoInferior + 1].tipoDeColision ==
                                    Tile.SOLIDO) {

                        enemigo.x += enemigo.velocidadX;

                        // Sino, me acerco al borde del que estoy
                    } else if (tileXEnemigoDerecha + 1 <= anchoMapaTiles() - 1) {

                        int TileEnemigoDerecho = tileXEnemigoDerecha * Tile.ancho + Tile.ancho;
                        double distanciaX = TileEnemigoDerecho - (enemigo.x + enemigo.ancho / 2);

                        if (distanciaX > 0) {
                            double velocidadNecesaria = Math.min(distanciaX, enemigo.velocidadX);
                            enemigo.x += velocidadNecesaria;
                        } else {
                            enemigo.girar();
                        }

                        // No hay Tile, o es el final del mapa
                    } else {
                        enemigo.girar();
                    }
                }

                if (enemigo.velocidadX < 0) {
                    // Solo una condición para pasar: Tile izquierda pasable y suelo solido.
                    if (tileXEnemigoIzquierda - 1 >= 0 &&
                            mapaTiles[tileXEnemigoIzquierda - 1][tileYEnemigoInferior].tipoDeColision ==
                                    Tile.PASABLE &&
                            mapaTiles[tileXEnemigoIzquierda - 1][tileYEnemigoCentro].tipoDeColision ==
                                    Tile.PASABLE &&
                            mapaTiles[tileXEnemigoIzquierda - 1][tileYEnemigoSuperior].tipoDeColision ==
                                    Tile.PASABLE &&
                            mapaTiles[tileXEnemigoIzquierda - 1][tileYEnemigoInferior + 1].tipoDeColision
                                    == Tile.SOLIDO) {

                        enemigo.x += enemigo.velocidadX;

                        // Solido / borde del tile acercarse.
                    } else if (tileXEnemigoIzquierda - 1 >= 0) {

                        int TileEnemigoIzquierdo = tileXEnemigoIzquierda * Tile.ancho;
                        double distanciaX = (enemigo.x - enemigo.ancho / 2) - TileEnemigoIzquierdo;

                        if (distanciaX > 0) {
                            double velocidadNecesaria =
                                    Utilidades.proximoACero(-distanciaX, enemigo.velocidadX);
                            enemigo.x += velocidadNecesaria;
                        } else {
                            enemigo.girar();
                        }
                    } else {
                        enemigo.girar();
                    }
            }
        }


        // Gravedad Jugador
        if(jugador.enElAire){
            // Recordar los ejes:
            // - es para arriba       + es para abajo.
            jugador.velocidadY += velocidadGravedad;
            if(jugador.velocidadY > velocidadMaximaCaida){
                jugador.velocidadY = velocidadMaximaCaida;
            }
        }

        // Hacia arriba
        if(jugador.velocidadY < 0){
            // Tile superior PASABLE
            // Podemos seguir moviendo hacia arriba
            if (tileYJugadorSuperior-1 >= 0 &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorSuperior-1].tipoDeColision
                            == Tile.PASABLE
                    && mapaTiles[tileXJugadorDerecha][tileYJugadorSuperior-1].tipoDeColision
                    == Tile.PASABLE){

                jugador.y +=  jugador.velocidadY;

                // Tile superior != de PASABLE
                // O es un tile SOLIDO, o es el TECHO del mapa
            } else {

                // Si en el propio tile del jugador queda espacio para
                // subir más, subo
                int TileJugadorBordeSuperior = (tileYJugadorSuperior)*Tile.altura;
                double distanciaY =  (jugador.y - jugador.altura/2) - TileJugadorBordeSuperior;

                if( distanciaY  > 0) {
                    jugador.y += Utilidades.proximoACero(-distanciaY, jugador.velocidadY);

                } else {
                    // Efecto Rebote -> empieza a bajar;
                    jugador.velocidadY = velocidadGravedad;
                    jugador.y +=  jugador.velocidadY;
                }

            }
        }

        // Hacia abajo
        if (jugador.velocidadY >= 0) {
            // Tile inferior PASABLE
            // Podemos seguir moviendo hacia abajo
            // NOTA - El ultimo tile es especial (caer al vacío )
            if (tileYJugadorInferior + 1 <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior + 1].tipoDeColision
                            == Tile.PASABLE
                    && mapaTiles[tileXJugadorDerecha][tileYJugadorInferior + 1].tipoDeColision
                    == Tile.PASABLE) {
                // si los dos están libres cae

                jugador.y += jugador.velocidadY;
                jugador.enElAire = true; // Sigue en el aire o se cae
                // Tile inferior SOLIDO
                // El ULTIMO, es un caso especial

            } else if (tileYJugadorInferior + 1 <= altoMapaTiles() - 1 &&
                    (mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior + 1].tipoDeColision
                            == Tile.SOLIDO ||
                            mapaTiles[tileXJugadorDerecha][tileYJugadorInferior + 1].tipoDeColision ==
                                    Tile.SOLIDO)) {

                // Con que uno de los dos sea solido ya no puede caer
                // Si en el propio tile del jugador queda espacio para bajar más, bajo
                int TileJugadorBordeInferior =
                        tileYJugadorInferior * Tile.altura + Tile.altura;

                double distanciaY =
                        TileJugadorBordeInferior - (jugador.y + jugador.altura / 2);

                jugador.enElAire = true; // Sigue en el aire o se cae
                if (distanciaY > 0) {
                    jugador.y += Math.min(distanciaY, jugador.velocidadY);

                } else {
                    // Toca suelo, nos aseguramos de que está bien
                    jugador.y = TileJugadorBordeInferior - jugador.altura / 2;
                    jugador.velocidadY = 0;
                    jugador.enElAire = false;
                }

                // Esta cayendo por debajo del ULTIMO
                // va a desaparecer y perder.
            } else {

                jugador.y += jugador.velocidadY;
                jugador.enElAire = true;

                if (jugador.y + jugador.altura / 2 > GameView.pantallaAlto) {
                    // ha perdido
                    scrollEjeX = 0;
                    jugador.restablecerPosicionInicial();
                    nivelPausado = true;
                    mensaje = CargadorGraficos.cargarBitmap(context, R.drawable.you_lose);
                }

            }
        }


        // derecha o parado
        if (jugador.velocidadX > 0) {
            // Tengo un tile delante y es PASABLE
            // El tile de delante está dentro del Nivel
            if (tileXJugadorDerecha + 1 <= anchoMapaTiles() - 1 &&
                    tileYJugadorInferior <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorDerecha + 1][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha + 1][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha + 1][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE) {

                jugador.x += jugador.velocidadX;

                // No tengo un tile PASABLE delante
                // o es el FINAL del nivel o es uno SOLIDO
            } else if (tileXJugadorDerecha <= anchoMapaTiles() - 1 &&
                    tileYJugadorInferior <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE) {

                // Si en el propio tile del jugador queda espacio para
                // avanzar más, avanzo
                int TileJugadorBordeDerecho = tileXJugadorDerecha * Tile.ancho + Tile.ancho;
                double distanciaX = TileJugadorBordeDerecho - (jugador.x + jugador.ancho / 2);

                if (distanciaX > 0) {
                    double velocidadNecesaria = Math.min(distanciaX, jugador.velocidadX);
                    jugador.x += velocidadNecesaria;
                } else {
                    // Opcional, corregir posición
                    jugador.x = TileJugadorBordeDerecho - jugador.ancho / 2;
                }
            }
        }

        // izquierda
        if (jugador.velocidadX <= 0) {
            // Tengo un tile detrás y es PASABLE
            // El tile de delante está dentro del Nivel
            if (tileXJugadorIzquierda - 1 >= 0 &&
                    tileYJugadorInferior < altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorIzquierda - 1][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda - 1][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda - 1][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE) {

                jugador.x += jugador.velocidadX;

                // No tengo un tile PASABLE detrás
                // o es el INICIO del nivel o es uno SOLIDO
            } else if (tileXJugadorIzquierda >= 0 && tileYJugadorInferior <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior].tipoDeColision
                            == Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorCentro].tipoDeColision
                            == Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorSuperior].tipoDeColision
                            == Tile.PASABLE) {

                // Si en el propio tile del jugador queda espacio para
                // avanzar más, avanzo
                int TileJugadorBordeIzquierdo = tileXJugadorIzquierda * Tile.ancho;
                double distanciaX = (jugador.x - jugador.ancho / 2) - TileJugadorBordeIzquierdo;

                if (distanciaX > 0) {
                    double velocidadNecesaria = Utilidades.proximoACero(-distanciaX, jugador.velocidadX);
                    jugador.x += velocidadNecesaria;
                } else {
                    // Opcional, corregir posición
                    jugador.x = TileJugadorBordeIzquierdo + jugador.ancho / 2;
                }
            }
        }

        reglasMovimientoDisparos(disparosJugador.iterator(), 0);
        reglasMovimientoDisparos(disparosCore.iterator(), 1);
        reglasColeccionable();
        reglasPortales();
    }

    private void reglasCajasExplosivas(Disparo disparo){
        for (Iterator<CajaExplosiva> cajaExplosivaIterator = cajaExplosivas.iterator(); cajaExplosivaIterator.hasNext(); ) {
            CajaExplosiva caja = cajaExplosivaIterator.next();
            double bordeDerecho = caja.x+caja.ancho/2 +15;
            double bordeIzquierdo = caja.x-caja.ancho/2-15;
            double bordeSuperior = caja.y-caja.altura/2;
            double bordeInferior = caja.y+caja.altura/2;
            if (disparo.x>=bordeIzquierdo && disparo.x<=bordeDerecho &&
                    disparo.y>=bordeSuperior && disparo.y<=bordeInferior){
                mapaTiles[caja.getPosX()][caja.getPosY()].tipoDeColision = Tile.PASABLE;
                cajaExplosivaIterator.remove();
            }
        }
    }

    private void reglasInterruptores(Caja caja){
        for (Iterator<Interruptor> interruptorIterator = interruptores.iterator(); interruptorIterator.hasNext(); ) {
            Interruptor interruptor = interruptorIterator.next();
            if (jugador.colisiona(interruptor)){
                ocultarMuros(interruptor);
                interruptor.setPressedBy(jugador);
            } else if(caja.colisiona(interruptor)){
                ocultarMuros(interruptor);
                interruptor.setPressedBy(caja);
            }
            else if (interruptor.getPressedBy()!=null &&
                    (interruptor.getPressedBy().equals(jugador) ||
                            interruptor.getPressedBy().equals(caja))){
                mostrarMuros(interruptor);
                interruptor.setPressedBy(null);
            }
        }
    }

    private void ocultarMuros(Interruptor interruptor){
        for (CajaMuro muro : interruptor.getMuros()){
            muro.removeImagen();
            mapaTiles[muro.getPosX()][muro.getPosY()].tipoDeColision=Tile.PASABLE;
        }
    }

    private void mostrarMuros(Interruptor interruptor){
        for (CajaMuro muro : interruptor.getMuros()){
            muro.setImagen();
            mapaTiles[muro.getPosX()][muro.getPosY()].tipoDeColision=Tile.SOLIDO;
        }
    }

    private void reglasPortales(){
        if (naranja!=null && jugador.colisiona(naranja) && naranja.getPortalAzul()!=null){
            jugador.teletransportar(naranja.getPortalAzul().x, naranja.getPortalAzul().y, naranja.getPortalAzul().getDireccionSalida());
            moverScrollY();
            moverScrollX();
        } else if(azul!=null && jugador.colisiona(azul) && azul.getPortalNaranja()!=null){
            jugador.teletransportar(azul.getPortalNaranja().x, azul.getPortalNaranja().y, azul.getPortalNaranja().getDireccionSalida());
            moverScrollY();
            moverScrollX();
        }
    }

    private void reglasCajas() {
        for (Iterator<Caja> cajaIterator = cajas.iterator(); cajaIterator.hasNext(); ) {
            Caja caja = cajaIterator.next();
            int TileX = (int) (caja.x) /Tile.ancho;
            int tileXCajaIzquierda
                    = (int) (caja.x - (caja.ancho - 1)) / Tile.ancho;
            int tileXCajaDerecha
                    = (int) (caja.x + (caja.ancho - 1)) / Tile.ancho;
            int tileYCajaInferior =
                    (int) (caja.y + (caja.altura - 1)) / Tile.altura;
            int tileYCajaCentro =
                    (int) caja.y / Tile.altura;
            int tileYCajaSuperior =
                    (int) (caja.y - (caja.altura - 1)) / Tile.altura;

            // /Izquierda
            if (jugador.velocidadX < 0 && jugador.x > caja.x) {
                if (jugador.colisiona(caja)
                        && mapaTiles[tileXCajaIzquierda][tileYCajaCentro].tipoDeColision == Tile.PASABLE) {
                    caja.mover(jugador.velocidadX);

                } else if (jugador.colisiona(caja) && jugador.velocidadX < 0) {
                    int TileCajaBordeDerecho =
                            tileXCajaIzquierda * Tile.ancho + Tile.ancho;
                    double distanciaX =
                            TileCajaBordeDerecho - (caja.x - caja.ancho / 2);

                    if (distanciaX < 0) {
                        caja.x += distanciaX;
                    } else {
                        jugador.velocidadX = 0;
                        jugador.velocidadY = 0;
                    }
                }
                //Derecha
            } else if (jugador.velocidadX > 0 && jugador.x < caja.x) {
                if (jugador.colisiona(caja)
                        && mapaTiles[tileXCajaDerecha][tileYCajaCentro].tipoDeColision == Tile.PASABLE) {
                    caja.mover(jugador.velocidadX);
                } else if (jugador.colisiona(caja) && jugador.velocidadX > 0) {
                    int TileCajaBordeIzquierdo =
                            tileXCajaDerecha * Tile.ancho - Tile.ancho;
                    double distanciaX =
                            TileCajaBordeIzquierdo - (caja.x - caja.ancho / 2);

                    if (distanciaX > 0) {
                        caja.x += distanciaX;
                    } else {
                        jugador.velocidadX = 0;
                        jugador.velocidadY = 0;
                    }
                }
            }

            if (mapaTiles[TileX][tileYCajaInferior].tipoDeColision
                    == Tile.PASABLE){
                gravedadCaja(caja, cajaIterator, TileX, tileYCajaInferior);
            }

            if (naranja != null && caja.colisiona(naranja) && naranja.getPortalAzul() != null) {
                caja.teletransportar(naranja.getPortalAzul().x, naranja.getPortalAzul().y, naranja.getPortalAzul().getDireccionSalida());
            } else if (azul != null && caja.colisiona(azul) && azul.getPortalNaranja() != null) {
                caja.teletransportar(azul.getPortalNaranja().x, azul.getPortalNaranja().y, azul.getPortalNaranja().getDireccionSalida());
            }
            reglasInterruptores(caja);
        }
    }

    private void gravedadCaja(Caja caja,Iterator<Caja> cajaIterator, int tileX, int tileYCajaInferior){
        int velocidad = 10;
        // Tile inferior PASABLE
        // Podemos seguir moviendo hacia abajo
        // NOTA - El ultimo tile es especial (caer al vacío )
        if (tileYCajaInferior+1 <= altoMapaTiles() - 1 &&
                mapaTiles[tileX][tileYCajaInferior+1].tipoDeColision
                        == Tile.PASABLE
                /*&& mapaTiles[TileX][tileYCajaInferior+1].tipoDeColision
                == Tile.PASABLE*/) {
            // si los dos están libres cae

            caja.y += velocidad;
            // Tile inferior SOLIDO
            // El ULTIMO, es un caso especial

        } else if (tileYCajaInferior +1 <= altoMapaTiles() - 1 &&
                mapaTiles[tileX][tileYCajaInferior+1].tipoDeColision
                        == Tile.SOLIDO) {

            // Con que uno de los dos sea solido ya no puede caer
            // Si en el propio tile del jugador queda espacio para bajar más, bajo
            int TileCajaBordeInferior =
                    tileYCajaInferior * Tile.altura + Tile.altura;

            double distanciaY =
                    TileCajaBordeInferior - (caja.y + caja.altura / 2);

            if (distanciaY > 0) {
                caja.y = TileCajaBordeInferior - caja.altura / 2;
                //caja.y += Math.min(distanciaY, velocidad);

            }
            // Esta cayendo por debajo del ULTIMO, desaparece
        } else {
            if (caja.y + caja.altura / 2 > GameView.pantallaAlto) {
                cajaIterator.remove();
            }

        }
    }

    private void reglasColeccionable(){
        for (Iterator<Coleccionable> coleccionableIterator = coleccionables.iterator(); coleccionableIterator.hasNext();) {
            Coleccionable coleccionable = coleccionableIterator.next();
            if (jugador.colisiona(coleccionable)){
                if (coleccionable.estado == Coleccionable.ACTIVO)
                    coleccionable.execute(this.gameView, meta);
                coleccionable.atrapado();
            }
            if (coleccionable.estado == Coleccionable.ELIMINAR) {
                coleccionableIterator.remove();
                continue;
            }
        }
    }


    private void reglasMovimientoDisparos(Iterator iterator, int ruta){
        while (iterator.hasNext()) {

            Disparo disparo = (Disparo)iterator.next();

            int tileXDisparo = (int)disparo.x / Tile.ancho ;
            int tileYDisparoInferior =
                    (int) (disparo.y  + disparo.cAbajo) / Tile.altura;

            int tileYDisparoSuperior =
                    (int) (disparo.y  - disparo.cArriba)  / Tile.altura;

            if (ruta==1) {
                if (disparo.colisiona(jugador)) {
                    if (jugador.golpeado() <= 0) {
                        jugador.restablecerPosicionInicial();
                        scrollEjeX = 0;
                        nivelPausado = true;
                        mensaje = CargadorGraficos.cargarBitmap(context, R.drawable.you_lose);
                        return;
                    }
                    iterator.remove();
                    break;
                }
                for (Enemigo enemigo : enemigos) {
                    if (!((DisparoCore)disparo).getOrigen().equals(enemigo) &&
                            disparo.colisiona(enemigo)) {
                        enemigo.destruir();
                        iterator.remove();
                        break;
                    }
                }
            }


            if(disparo.velocidadX > 0){
                // Tiene delante un tile pasable, puede avanzar.
                if (tileXDisparo+1 <= anchoMapaTiles()-1 &&
                        mapaTiles[tileXDisparo+1][tileYDisparoInferior].tipoDeColision
                                == Tile.PASABLE &&
                        mapaTiles[tileXDisparo+1][tileYDisparoSuperior].tipoDeColision
                                == Tile.PASABLE ){

                    disparo.x +=  disparo.velocidadX;
                    disparo.y += disparo.velocidadY;
                //Tile X Portable
                } else if(disparo instanceof DisparoJugador && tileXDisparo+1 <= anchoMapaTiles()-1 &&
                        mapaTiles[tileXDisparo+1][tileYDisparoInferior].isPortable() &&
                        mapaTiles[tileXDisparo+1][tileYDisparoSuperior].isPortable() ){
                        crearPortal(tileXDisparo+1, tileYDisparoInferior, -5, -1);
                        iterator.remove();
                    //Tile Y portable
                } else if(disparo instanceof DisparoJugador &&
                        mapaTiles[tileXDisparo][tileYDisparoInferior].isPortable() &&
                                mapaTiles[tileXDisparo][tileYDisparoSuperior+1].isPortable() ){
                    crearPortal(tileXDisparo, tileYDisparoInferior, 5, 1);
                    iterator.remove();
                } //El superior o inferior es solido, eliminar!
                else if (mapaTiles[tileXDisparo][tileYDisparoInferior].tipoDeColision
                        == Tile.SOLIDO ||
                        mapaTiles[tileXDisparo][tileYDisparoSuperior].tipoDeColision
                                == Tile.SOLIDO ){
                    iterator.remove();

                    // No tengo un tile PASABLE detras
                    // o es el INICIO del nivel o es uno SOLIDO
                }
                else if (tileXDisparo <= anchoMapaTiles() - 1){

                    int TileDisparoBordeDerecho = tileXDisparo*Tile.ancho + Tile.ancho ;
                    double distanciaX =
                            TileDisparoBordeDerecho - (disparo.x +  disparo.cDerecha);

                    if( distanciaX  > 0) {
                        double velocidadNecesaria =
                                Math.min(distanciaX, disparo.velocidadX);
                        disparo.x += velocidadNecesaria;
                    } else {
                        if (ruta==1) {
                            reglasCajasExplosivas(disparo);
                        }
                        iterator.remove();
                        continue;
                    }
                }
            }

            // izquierda
            if (disparo.velocidadX <= 0){
                if (tileXDisparo-1 >= 0 &&
                        tileYDisparoSuperior < altoMapaTiles()-1 &&
                        mapaTiles[tileXDisparo-1][tileYDisparoSuperior].tipoDeColision ==
                                Tile.PASABLE &&
                        mapaTiles[tileXDisparo-1][tileYDisparoInferior].tipoDeColision ==
                                Tile.PASABLE){

                    disparo.x +=  disparo.velocidadX;
                    disparo.y += disparo.velocidadY;

                    //El tile x es portable
                } else if(disparo instanceof DisparoJugador && tileXDisparo-1 <= anchoMapaTiles()-1 &&
                        /*tileYDisparoSuperior < altoMapaTiles()-1 &&*/
                        mapaTiles[tileXDisparo-1][tileYDisparoInferior].isPortable() &&
                        mapaTiles[tileXDisparo-1][tileYDisparoSuperior].isPortable() ){
                    crearPortal(tileXDisparo, tileYDisparoInferior, 5, 1);
                    iterator.remove();
                    //Tile Y portable
                } else if(disparo instanceof DisparoJugador &&
                        mapaTiles[tileXDisparo][tileYDisparoInferior].isPortable() &&
                        mapaTiles[tileXDisparo][tileYDisparoSuperior+1].isPortable() ){
                    crearPortal(tileXDisparo, tileYDisparoInferior, 5, 1);
                    iterator.remove();
                }
                //El superior o inferior es solido, eliminar!
                else if (mapaTiles[tileXDisparo][tileYDisparoInferior].tipoDeColision
                                == Tile.SOLIDO ||
                        mapaTiles[tileXDisparo][tileYDisparoSuperior].tipoDeColision
                                == Tile.SOLIDO ){
                        iterator.remove();

                    // No tengo un tile PASABLE detras
                    // o es el INICIO del nivel o es uno SOLIDO
                } else if(tileXDisparo >= 0 ){
                    // Si en el propio tile del jugador queda espacio para
                    // avanzar más, avanzo
                    int TileDisparoBordeIzquierdo = tileXDisparo*Tile.ancho ;
                    double distanciaX =
                            (disparo.x - disparo.cIzquierda) - TileDisparoBordeIzquierdo ;

                    if( distanciaX  > 0) {
                        double velocidadNecesaria =
                                Utilidades.proximoACero(-distanciaX, disparo.velocidadX);
                        disparo.x += velocidadNecesaria;
                    } else {
                        if (ruta==1) {
                            reglasCajasExplosivas(disparo);
                        }
                        iterator.remove();
                        continue;
                    }
                }
            }
            //Disparos Enemigos
            if (ruta==1){
                if (naranja!=null && disparo.colisiona(naranja) && naranja.getPortalAzul()!=null){
                    disparo.teletransportar(naranja.getPortalAzul().x, naranja.getPortalAzul().y, naranja.getPortalAzul().getDireccionSalida());
                    moverScrollY();
                    moverScrollX();
                } else if(azul!=null && disparo.colisiona(azul) && azul.getPortalNaranja()!=null) {
                    disparo.teletransportar(azul.getPortalNaranja().x, azul.getPortalNaranja().y, azul.getPortalNaranja().getDireccionSalida());
                    moverScrollY();
                    moverScrollX();
                }
            }
        }
    }

    public void dibujar (Canvas canvas) {
        if(inicializado) {
            fondos[0].dibujar(canvas);
            fondos[1].dibujar(canvas);
            dibujarTiles(canvas);
            if (naranja!=null) {
                naranja.dibujar(canvas);
            }
            if (azul!=null){
                azul.dibujar(canvas);
            }

            meta.dibujar(canvas);

            for (PuntoSalvado bandera: banderas) {
                bandera.dibujar(canvas);
            }

            for (Interruptor interruptor : interruptores){
                interruptor.dibujar(canvas);
            }

            for (Caja caja : cajas){
                caja.dibujar(canvas);
            }

            for (CajaMuro caja : cajaMuros){
                caja.dibujar(canvas);
            }
            for (CajaExplosiva caja : cajaExplosivas){
                caja.dibujar(canvas);
            }

            for(DisparoJugador disparoJugador: disparosJugador){
                disparoJugador.dibujar(canvas);
            }
            for(DisparoCore disparoCore : disparosCore){
                disparoCore.dibujar(canvas);
            }


            jugador.dibujar(canvas);

            for(Enemigo enemigo: enemigos){
                enemigo.dibujar(canvas);
            }
            for (Coleccionable coleccionable: coleccionables){
                coleccionable.dibujar(canvas);
            }

            if (nivelPausado){
                // la foto mide 480x320
                Rect orgigen = new Rect(0,0 ,
                        480,320);

                Paint efectoTransparente = new Paint();
                efectoTransparente.setAntiAlias(true);

                Rect destino = new Rect((int)(GameView.pantallaAncho/2 - 480/2),
                        (int)(GameView.pantallaAlto/2 - 320/2),
                        (int)(GameView.pantallaAncho/2 + 480/2),
                        (int)(GameView.pantallaAlto/2 + 320/2));
                canvas.drawBitmap(mensaje,orgigen,destino, null);
            }


        }
    }


    private void dibujarTiles(Canvas canvas){
        // Calcular que tiles serán visibles en la pantalla
        // La matriz de tiles es más grande que la pantalla

        int tileXJugador = (int) jugador.x / Tile.ancho;
        int izquierda = (int) (tileXJugador - tilesEnDistanciaX(jugador.x - scrollEjeX));
        izquierda = Math.max(0,izquierda); // Que nunca sea < 0, ej -1

        //ScrollY
        int tileYJugador = (int) jugador.y / Tile.altura;
        int abajo = (int) (tileYJugador - tilesEnDistanciaY(jugador.y - scrollEjeY));
        abajo = Math.max(0,abajo);

        moverScrollY();
        moverScrollX();

        int derecha = izquierda +
                GameView.pantallaAncho / Tile.ancho + 1;

        // el ultimo tile visible, no puede superar el tamaño del mapa
        derecha = Math.min(derecha, anchoMapaTiles() - 1);

        int arriba = abajo +
                GameView.pantallaAlto / Tile.altura + 1;
        arriba = Math.min(arriba, altoMapaTiles() - 1);

        for (int y = abajo; y <= arriba; ++y) {
            for (int x = izquierda; x <= derecha; ++x) {
                if (mapaTiles[x][y].imagen != null) {
                    // Calcular la posición en pantalla correspondiente
                    // izquierda, arriba, derecha , abajo
                    mapaTiles[x][y].imagen.setBounds(
                            (x  * Tile.ancho) - scrollEjeX,
                            (y * Tile.altura) - scrollEjeY,
                            (x * Tile.ancho) + Tile.ancho - scrollEjeX,
                            (y * Tile.altura + Tile.altura)-scrollEjeY);

                    mapaTiles[x][y].imagen.draw(canvas);
                }
            }
        }
    }

    private void moverScrollY() {
        if ( jugador .y < altoMapaTiles()* Tile.altura - GameView.pantallaAlto*0.3 )
            if( jugador .y - scrollEjeY > GameView.pantallaAlto * 0.7 ){
                scrollEjeY = (int) ((jugador .y ) - GameView.pantallaAlto* 0.7);
            }
        if ( jugador .y > GameView.pantallaAlto*0.3 )
            if( jugador .y - scrollEjeY < GameView.pantallaAlto *0.3 ){
                scrollEjeY = (int)(jugador .y - GameView.pantallaAlto*0.3);
            }
    }

    private void moverScrollX() {
        if (jugador.x < anchoMapaTiles() * Tile.ancho - GameView.pantallaAncho * 0.3)
            if (jugador.x - scrollEjeX > GameView.pantallaAncho * 0.7) {
                fondos[0].mover((int) jugador.x - scrollEjeX - GameView.pantallaAncho * 0.7);
                fondos[1].mover((int) jugador.x - scrollEjeX - GameView.pantallaAncho * 0.7);
                scrollEjeX = (int) ((jugador.x) - GameView.pantallaAncho * 0.7);
            }


        if (jugador.x > GameView.pantallaAncho * 0.3)
            if (jugador.x - scrollEjeX < GameView.pantallaAncho * 0.3) {
                fondos[0].mover((int) jugador.x - scrollEjeX - GameView.pantallaAncho * 0.3);
                fondos[1].mover((int) jugador.x - scrollEjeX - GameView.pantallaAncho * 0.3);
                scrollEjeX = (int) (jugador.x - GameView.pantallaAncho * 0.3);
            }


    }

    private float tilesEnDistanciaX(double distanciaX){
        return (float) distanciaX/Tile.ancho;
    }
    private float tilesEnDistanciaY(double distanciaY){
        return (float) distanciaY/Tile.ancho;
    }


    public int anchoMapaTiles(){
        return mapaTiles.length;
    }

    public int altoMapaTiles(){
        return mapaTiles[0].length;
    }

    private void enlazarPortales(){
        if (this.naranja!=null)
            this.naranja.setPortalAzul(this.azul);
        if (this.azul!=null)
            this.azul.setPortalNaranja(this.naranja);
    }


    private void enlazarInterruptores(){
        int enlace = 2;
        for (Interruptor interruptor : interruptores){
            for (CajaMuro muro : cajaMuros){
                if (muro.getEnlace()==enlace) {
                    interruptor.addMuro(muro);
                }
            }
            enlace++;
        }
    }

}

