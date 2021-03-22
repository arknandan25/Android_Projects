package com.example.e2eeapp_alpha;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.example.e2eeapp_alpha.Utils.ImageEncrypter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import static android.os.Environment.DIRECTORY_DOCUMENTS;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class MainActivity3 extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        File yourAppDir = new File(getExternalFilesDir(null).getAbsolutePath()+"dd");
        Log.i("CreateDir","App dir:" + yourAppDir);

        if(!yourAppDir.exists() && !yourAppDir.isDirectory())
        {
            // create empty directory
            if (yourAppDir.mkdirs())
            {
                Log.i("CreateDir","App dir created");
            }
            else
            {
                Log.w("CreateDir","Unable to create app dir!");
            }
        }
        else
        {
            Log.i("CreateDir","App dir already exists");
        }


        //
        Uri uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/e2eeapp-alpha-chat-server.appspot.com/o/image_uploads%2F1614191322831.png?alt=media&token=e8718e7d-5a3d-458f-b8fd-5abd4f8f225c");
        File h = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        Uri b = Uri.parse("content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fdata%2Fcom.example.e2eeapp_alpha%2Ffilesdd");
        File x = new File(Environment.DIRECTORY_MOVIES);
        if (!x.exists()){
            x.mkdirs();
            Log.i("CreateDir"," DIRECTORY_MOVIES is created");

        }else{
            Log.i("CreateDir"," DIRECTORY_MOVIES already exists");

        }


//        //download manager
//        DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
//
//        DownloadManager.Request request = new DownloadManager.Request(uri)
//                .setTitle("Chut")
//                .setDescription("MA KA BHOSDA");
//        request.setDestinationInExternalPublicDir(String.valueOf(x), "LUND.bin");
//
//        downloadManager.enqueue(request);
//
//
////        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
////
//////        Uri b = Uri.parse("content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fdata%2Fcom.example.e2eeapp_alpha%2Ffilesdd");
//////        File v = new File(b.getPath());
////
////        request.setDestinationInExternalFilesDir(context,  DIRECTORY_DOWNLOADS, "testImg");
////
////        downloadManager.enqueue(request);
//
//
////        File x = new File(Environment.DIRECTORY_MOVIES);
//        File[] files = x.listFiles();
//        Log.i("CreateDir",files.toString() + " is the length");
//
////        for (int i = 0; i < files.length; i++)
////        {
////            Log.d("Files", "FileName:" + files[i].getName());
////        }
//        if (x.exists()){
//            Log.i("CreateDir","DIRECTORY_MOVIES lund already exists");
//
//        }else{
//            Log.i("CreateDir","DIRECTORY_MOVIES lund does not   exists");
//
//        }
//
//        File o = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS) + "/testImg");
//        if (o.exists()){
//            Log.i("CreateDir","DIRECTORY_DOWNLOADS already exists");
//
//        }else{
//            Log.i("CreateDir","DIRECTORY_DOWNLOADS doesnot  already exists");
//
//        }

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imageRef = storage.getReference()
                .child("image_uploads_enc")
                .child("1616289679846");

//        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public void onSuccess(Uri uri) {
//                Log.d("TAG", "Download URL is:" + uri.toString());
//                //        //download manager
//                DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
//
//                DownloadManager.Request request = new DownloadManager.Request(uri)
//                        .setTitle("Chut")
//                        .setDescription("MA KA BHOSDA");
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//
//                request.setDestinationInExternalPublicDir(DIRECTORY_DOCUMENTS, "LUND");
//
//                downloadManager.enqueue(request);
//            }
//        });
        Log.i("xx", "xxxxxxxxxxxxxx");
        Log.i("xx", "xxxxxxxxxxxxxx");
        Log.i("xx", "xxxxxxxxxxxxxx");
        Log.i("xx", "xxxxxxxxxxxxxx");
        Log.i("xx", "xxxxxxxxxxxxxx");
        Log.i("xx", "xxxxxxxxxxxxxx");
        Log.i("xx", "xxxxxxxxxxxxxx");
        Log.i("xx", "xxxxxxxxxxxxxx");
        Log.i("xx", "xxxxxxxxxxxxxx");
        Log.i("xx", "xxxxxxxxxxxxxx");
        Log.i("xx", "xxxxxxxxxxxxxx");
        Log.i("xx", "xxxxxxxxxxxxxx");
        Log.i("xx", "xxxxxxxxxxxxxx");
        Log.i("xx", "xxxxxxxxxxxxxx");
        Log.i("xx", "xxxxxxxxxxxxxx");
        Log.i("xx", "xxxxxxxxxxxxxx");
        Log.i("xx", "xxxxxxxxxxxxxx");
        Log.i("xx", "xxxxxxxxxxxxxx");
        Log.i("xx", "xxxxxxxxxxxxxx");
        Log.i("xx", "xxxxxxxxxxxxxx");
        Log.i("xx", "xxxxxxxxxxxxxx");
        Log.i("xx", "xxxxxxxxxxxxxx");
        Log.i("xx", "xxxxxxxxxxxxxx");
        Log.i("xx", "xxxxxxxxxxxxxx");




        String my_key = "2421074815010621";
        String my_spec_key = "2421074815010631";
        File p = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File d = new File(p.getAbsolutePath() + "/LUND");

        File do1 = new File(p.getAbsolutePath() + "/LUND_dec.png");

        try {
            ImageEncrypter.decryptToFile(my_key, my_spec_key, new FileInputStream(d), new FileOutputStream(do1));
            Log.i("DECRYPTDOWN", "File decrypted!!");

        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        try {
//            FileOutputStream f = new FileOutputStream(d);
//            Log.i("done", "read");
//        }catch (FileNotFoundException e){
//            Log.e("error", e.getMessage().toString());
//        }


    }
}