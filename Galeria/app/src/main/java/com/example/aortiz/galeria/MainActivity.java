package com.example.aortiz.galeria;

import android.app.usage.UsageEvents;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final int HACER_FOTO=01;
    private Intent intento;
    private File ficheroFoto;
    private Uri fotoactual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     *
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode==HACER_FOTO)
        añadirFotoaGaleria();
    }

    /**
     * Procedimiento que lanzara la actividad de camara nativa,
     * esta diseñado como metodo Click
     *
     * @param boton View desde el que sera lanzado el metodo
     */
    public void abrirCamara(View boton){

        //Intento para abrir la camara de android
        intento = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ficheroFoto=null;

        //Es necesario hacerlo ya que si no se encuentra una app que responda a esrte intent
        //la aplicacion dejara de funcionar

        if(intento.resolveActivity(getPackageManager())!=null){

            //Establecemos un nuevo nombre para cada foto
            establecerFichero();

            if(ficheroFoto!=null){

                //Incluimos la uri del fichero en un extra para que la camara pueda guardar la foto a maxima resolucion
                intento.putExtra(MediaStore.EXTRA_OUTPUT, fotoactual=FileProvider.getUriForFile(this,"com.example.aortiz.galeria",ficheroFoto));

                startActivityForResult(intento,HACER_FOTO);
            }

        }
    }

    /**
     * Procedimiento que lanzara la aplicacion de galeria del sistema
     * diseñado para usarse como metodo Click
     *
     * @param boton
     * */
    public void abrirGaleria(View boton){

        intento= new Intent(Intent.CATEGORY_APP_GALLERY);
        startActivity(intento);
    }

    /**
     * Procedimiento que creara un archivo con nombreprecedifino con el tiempo de creacion
     * se usara para crear diferentes nombres de archivos para las fotos tomadas por la camara
     *
     * */
    public void establecerFichero(){

        try {
            ficheroFoto = File.createTempFile("GaleriaApp-" + System.currentTimeMillis(), ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    /**
     * Procedimiento que lanzara un intento para añadir la foto actual a la galeria de imagenes
     *
     * */

    public void añadirFotoaGaleria(){

        intento= new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        intento.setData(fotoactual);

        this.sendBroadcast(intento);
    }
}
