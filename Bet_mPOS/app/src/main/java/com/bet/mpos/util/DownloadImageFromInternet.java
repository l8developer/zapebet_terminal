package com.bet.mpos.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import com.caverock.androidsvg.RenderOptions;
import com.caverock.androidsvg.SVG;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadImageFromInternet /*extends AsyncTask<String, Void, Bitmap>*/ {



    //    @SuppressLint("StaticFieldLeak")
//    ImageView imageView;
//
//    public DownloadImageFromInternet(ImageView imageView) {
//        this.imageView=imageView;
//        Toast.makeText(ArtizApp.getAppContext(), "Please wait, it may take a few minute...",Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected Bitmap doInBackground(String... urls) {
//        String imageURL=urls[0];
//        Bitmap bimage=null;
//        try {
//            InputStream in=new java.net.URL(imageURL).openStream();
//            bimage= BitmapFactory.decodeStream(in);
//        } catch (Exception e) {
//            Log.e("Error Message", e.getMessage());
//            e.printStackTrace();
//        }
//        return bimage;
//    }
//
//    protected void onPostExecute(Bitmap result) {
//        imageView.setImageBitmap(result);
//    }
    private Bitmap bitmap;
    private final ImageView imageView;
    public DownloadImageFromInternet(ImageView imageView)
    {
        //this.bitmap = null;
        this.imageView = imageView;
    }

    //SVG

    public Bitmap downloadSVGImage(String url)
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {

                //Background work here
                //String imageURL=urls[0];
                Bitmap bimage = null;
                try {
                    // baixa imagem
                    InputStream in = new java.net.URL(url).openStream();

                    // Converter o InputStream para SVG
                    SVG svg = SVG.getFromInputStream(in);

                    int width = 420;
                    int height = 420;

                    Picture picture = svg.renderToPicture(width, height);
                    bimage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bimage);
                    canvas.drawPicture(picture);

                } catch (Exception e) {
                    Log.e("Error Message", e.getMessage());
                    e.printStackTrace();
                }
                bitmap = bimage;

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here

                        // transforma a escala do bitmap para os valores passados por paramento
                        if(imageView != null && bitmap != null)
                            imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 220, 220, false));
                    }
                });
            }
        }); // fim execute

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
            }
        }, 2000);

        return bitmap;
    }

    //JPG, PNG
    public Bitmap downloadImage(String url)
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {

                //Background work here
                //String imageURL=urls[0];
                Bitmap bimage = null;
                try {
                    // baixa imagem
                    InputStream in = new java.net.URL(url).openStream();

                    // transforma imagem em bitmap
                    bimage= BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error Message", e.getMessage());
                    e.printStackTrace();
                }
                bitmap = bimage;

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here

                        // transforma a escala do bitmap para os valores passados por paramento
                        if(imageView != null && bitmap != null)
                            imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 220, 220, false));
                    }
                });
            }
        }); // fim execute

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
            }
        }, 2000);

        return bitmap;
    }



    public Bitmap getBitmap() {
        return bitmap;
    }


}
