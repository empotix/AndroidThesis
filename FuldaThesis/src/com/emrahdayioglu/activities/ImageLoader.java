package com.emrahdayioglu.activities;
/**
* Load images on the background asyncronously for better performance to load lists
* @author Emrah Dayioglu
* @since 01.05.2012
* @version 1.0.0.1
*/

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Stack;

import com.emrahdayioglu.R;
import com.emrahdayioglu.Util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class ImageLoader {
    
    private HashMap<String, Bitmap> cache = new HashMap<String, Bitmap>();
    final int drawableId=R.drawable.ic_launcher;
    private File cacheDir;
    ImagesQueue imagesQueue=new ImagesQueue();
    ImagesLoader imageLoaderThread=new ImagesLoader();
    
    // this decrease the priority of threat
    public ImageLoader(Context context){
    	imageLoaderThread.setPriority(Thread.NORM_PRIORITY-1);
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
        	cacheDir=new File(context.getCacheDir(),"CouponImageList");
        } else { 
            cacheDir=context.getCacheDir();
        }
        if(!cacheDir.exists()){
            cacheDir.mkdirs();
        }
    }
    
    public void DisplayImage(String url, Context context, ImageView imageView)
    {
        if(cache.containsKey(url)){
            imageView.setImageBitmap(cache.get(url));
        } else {
        	queueImage(url, context, imageView);
            imageView.setImageResource(drawableId);
        }    
    }
        
    //scale image
    private Bitmap decodeFile(File f){
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,options);
            final int REQUIRED_SIZE = 70;
            int tempWidth = options.outWidth; 
            int tempHeight = options.outHeight;
            int scale=1;
            while(true){
                if(tempWidth/2<REQUIRED_SIZE || tempHeight/2<REQUIRED_SIZE){
                    break;
                }
                tempWidth/=2;
                tempHeight/=2;
                scale++;
            }
            BitmapFactory.Options optionSample = new BitmapFactory.Options();
            optionSample.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, optionSample);
        } catch (FileNotFoundException e) {}
        return null;
    }
    
    //Task for the queue
    private class ImageToLoad
    {
        public String url;
        public ImageView imageView;
        public ImageToLoad(String u, ImageView i){
            url=u; 
            imageView=i;
        }
    }
    
    public void stopThread(){
    	imageLoaderThread.interrupt();
    }
    
    //stores list of photos to download
    public class ImagesQueue {
        private Stack<ImageToLoad> imagesToLoad=new Stack<ImageToLoad>();
        public void Clean(ImageView image){
            for(int i=0 ;i<imagesToLoad.size();){
                if(imagesToLoad.get(i).imageView==image){
                	imagesToLoad.remove(i);
                } else {
                    ++i;
                }
            }
        }
    }
    
    public class ImagesLoader extends Thread {
        public void run() {
            try {
                while(true)
                {
                    if(imagesQueue.imagesToLoad.size()==0){
                        synchronized(imagesQueue.imagesToLoad){
                        	imagesQueue.imagesToLoad.wait();
                        }
                    }
                    if(imagesQueue.imagesToLoad.size()!=0){
                    	ImageToLoad imageToLoad;
                        synchronized(imagesQueue.imagesToLoad){
                        	imageToLoad=imagesQueue.imagesToLoad.pop();
                        }
                        Bitmap bmp=getBitmap(imageToLoad.url);
                        cache.put(imageToLoad.url, bmp);
                        if(((String)imageToLoad.imageView.getTag()).equals(imageToLoad.url)){
                        	ImageOnView bd=new ImageOnView(bmp, imageToLoad.imageView);
                            Activity a=(Activity)imageToLoad.imageView.getContext();
                            a.runOnUiThread(bd);
                        }
                    }
                    if(Thread.interrupted()){
                        break;
                    }
                }
            } catch (InterruptedException e) {
            }
        }
    }
    
    //put image to the view
    class ImageOnView implements Runnable
    {
        Bitmap bitmap;
        ImageView imageView;
        public ImageOnView(Bitmap b, ImageView i){bitmap=b;imageView=i;}
        public void run(){
            if(bitmap!=null){
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(drawableId);
            }
        }
    }

    // clear cache
    public void clearCache() {
        cache.clear();
        File[] files=cacheDir.listFiles();
        for(File f:files){
            f.delete();
        }
    }
    
    // may the image is on the background load process so pass them, if it is not start thread 
    private void queueImage(String url, Context context, ImageView imageView)
    {
    	imagesQueue.Clean(imageView);
        ImageToLoad p=new ImageToLoad(url, imageView);
        synchronized(imagesQueue.imagesToLoad){
        	imagesQueue.imagesToLoad.push(p);
        	imagesQueue.imagesToLoad.notifyAll();
        }
        if(imageLoaderThread.getState()==Thread.State.NEW)
        	imageLoaderThread.start();
    }
    
    //gets images according to its hashcode
    private Bitmap getBitmap(String url) 
    {
        String filename=String.valueOf(url.hashCode());
        File file = new File(cacheDir, filename);
        Bitmap bitmapFile = decodeFile(file);
        if(bitmapFile!=null){
            return bitmapFile;
        }
        try {
            Bitmap bitmap=null;
            InputStream inputStream=new URL(url).openStream();
            OutputStream outputSream = new FileOutputStream(file);
            Util.copyStream(inputStream, outputSream);
            outputSream.close();
            bitmap = decodeFile(file);
            return bitmap;
        } catch (Exception ex){
           ex.printStackTrace();
           return null;
        }
    }


}
