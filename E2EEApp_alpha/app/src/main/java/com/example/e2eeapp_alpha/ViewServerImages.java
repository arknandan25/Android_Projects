package com.example.e2eeapp_alpha;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.example.e2eeapp_alpha.Utils.ImageEncrypter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.installations.Utils;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class ViewServerImages extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageAdapter adapter;
    private DatabaseReference databaseReference;
    private List<UploadImage> mUploads = new ArrayList<>();
    private File DirAddress;
    private File download_addr;
    private File apkStorage;

    private String my_key = "2421074815010621";
    private String my_spec_key = "2421074815010631";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_server_images);
        //setup local directory for encrypted and decrypted images

        apkStorage = new File(
                Environment.getExternalStorageDirectory() + "/"
                        + "arkthegreat");

        if (!apkStorage.exists()) {
            apkStorage.mkdir();
            Log.e("TAG", "Directory Created.");
        }

        download_addr = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS);
        Log.i("CreateDir","Download Dir::::::" + download_addr.toURI() + ":::String::" +download_addr.toString());
        if(download_addr.exists() && download_addr.isDirectory()){
            Log.w("CreateDir","Downloads folder exist");

        }else {
            Log.w("CreateDir","Downloads folder dosent exist");

        }

        File testImg =new File(download_addr, "images.jpeg");
        if(testImg.exists()){
            Log.w("CreateDir","Test Image exist");

        }else {
            Log.w("CreateDir","Test Image  dosent exist");

        }


            DirAddress = new File(getExternalFilesDir(null).getAbsolutePath()+"_E2E_DOWN");

        if(!DirAddress.exists() && !DirAddress.isDirectory())
        {
            // create empty directory
            if (DirAddress.mkdirs())
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


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        mUploads = new ArrayList<>();
//        databaseReference = FirebaseDatabase.getInstance().getReference("image_uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("image_uploads_enc");


//        databaseReference.orderByKey().limitToLast(1).addChildEventListener(new ChildEventListener() {
//            @SuppressLint("LongLogTag")
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                UploadImage x = snapshot.getValue(UploadImage.class);
//                Log.i("image added!:", x.getName() + "::::" + x.getURI());
//                mUploads.add(x);
////                Log.i("Size of this betichod mUploads", String.valueOf(mUploads.size()));
//                adapter = new ImageAdapter(ViewServerImages.this, mUploads);
//                recyclerView.setAdapter(adapter);
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                //the loop gets all the data from the image_uploads db parent
                //we can try to get the last child only and update mUploads with one elemnts; might
                //make it easier to decrypt
                for (DataSnapshot s: snapshot.getChildren()){
                    UploadImage u = s.getValue(UploadImage.class);
                    Log.i("item:", u.getName() + "::::" + u.getURI());
                    mUploads.add(u);
                }
                Log.i("muploads:", String.valueOf(mUploads));
                downloadFile(mUploads);
                adapter = new ImageAdapter(ViewServerImages.this, mUploads);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewServerImages.this, "Failed to fetch data",Toast.LENGTH_SHORT).show();

            }
        });

    }


    //on button click view images adapter
    //download file first decrypt it and then show the decrypted image on recycler view
    private void downloadFile( List<UploadImage> img_list){
        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference("image_uploads");
        StorageReference storageRef = storage.getReference("image_uploads_enc");




        //
//        for (int i=0; i< img_list.size();++i){
////            Log.i(">"+i+1, img_list.get(i).getURI() );
//            String curr_img_url = img_list.get(i).getURI().toString();
//            //filename without extension
//            String fname_without_extension = img_list.get(i).getName();
//            int pos = fname_without_extension.lastIndexOf(".");
//            if (pos > 0) {
//                fname_without_extension = fname_without_extension.substring(0, pos);
//            }
//            //get extension
//            String extension = null;
//            int pos1 = img_list.get(i).getName().lastIndexOf(".");
//            if (pos1 > 0) {
//                extension = img_list.get(i).getName().substring(pos1, img_list.get(i).getName().length());
//            }
//            Log.i(">>", fname_without_extension + "::: " + extension);//24591-2-avengers::: .png
//            Log.i(">>>", String.valueOf(DirAddress));///storage/emulated/0/Android/data/com.example.e2eeapp_alpha/files_E2E_DOWNLOADED_IMAGES
//
//            //call the download method
//            download(ViewServerImages.this, fname_without_extension, String.valueOf(DirAddress), curr_img_url);
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            try {
//                decrypt_downloaded(fname_without_extension+".png", fname_without_extension);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        }


        String curr_img_url = img_list.get(0).getURI().toString();
        //filename without extension
        String fname_without_extension = img_list.get(0).getName();
        int pos = fname_without_extension.lastIndexOf(".");
        if (pos > 0) {
            fname_without_extension = fname_without_extension.substring(0, pos);
        }
        //get extension
        String extension = null;
        int pos1 = img_list.get(0).getName().lastIndexOf(".");
        if (pos1 > 0) {
            extension = img_list.get(0).getName().substring(pos1, img_list.get(0).getName().length());
        }
        Log.i(">>", fname_without_extension + "::: " + extension);//24591-2-avengers::: .png
        Log.i(">>>", String.valueOf(DirAddress));///storage/emulated/0/Android/data/com.example.e2eeapp_alpha/files_E2E_DOWNLOADED_IMAGES

        //call the download method
//        download(ViewServerImages.this, fname_without_extension, String.valueOf(DirAddress), curr_img_url);
        Uri x = Uri.parse("content://com.android.providers.downloads.documents/document/downloads");
        File k = new File(x.getPath());
        Log.i("DECRYPTDOWN>>", k.toString());
        download(ViewServerImages.this, fname_without_extension, x.toString() , curr_img_url);

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        try {
            decrypt_downloaded(fname_without_extension+".png", fname_without_extension);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void decrypt_downloaded( String decrypted_file_name, String  encrypted_file_name) throws FileNotFoundException {
        // decrypt image
//        String decrypted_file_name = unenc_fileName;


        File output_dec_file = new File(apkStorage, decrypted_file_name);
        Log.i("DECRYPTDOWN","Decrypted filename:"+ output_dec_file.toString());
//        Decrypted filename:/storage/emulated/0/Android/data/com.example.e2eeapp_alpha/files_E2E_DOWN/images.png
        File pic_addr = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File encFile = new File(apkStorage, encrypted_file_name);
        Log.i("DECRYPTDOWN", "Encrypted file name:"+ encFile.toString());

        if(encFile.exists()){
            Log.i("DECRYPTDOWN", "Encrypted dir exists");

        }else {
            Log.i("DECRYPTDOWN", "Encrypted file does not exists");

        }

        //

        if(apkStorage.exists()){
            Log.i("DECRYPTDOWN", "apk dir exists");

        }else {
            Log.i("DECRYPTDOWN", "apk file does not exists");

        }

//        String l = apkStorage.toString();
//        File directory = new File(l);
//        File[] files = directory.listFiles();
//        Log.d("Files", "Size: "+ files.length);
//        for (int i = 0; i < files.length; i++)
//        {
//            Log.d("Files", "FileName:" + files[i].getName());
//        }


        //Uri
        Uri x = Uri.parse("content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fdata%2Fcom.example.e2eeapp_alpha%2Ffiles%2Fstorage%2Femulated%2F0%2FDownload%2Fimages");
        File k = new File(x.getPath());
        Log.i("DECRYPTDOWN", k.toString());

        if(k.exists()){
            Log.i("DECRYPTDOWN", "Encrypted file!! exists");

        }else {
            Log.i("DECRYPTDOWN", "Encrypted file!! does not exists");
        }

        try {
            ImageEncrypter.decryptToFile(my_key, my_spec_key, new FileInputStream(encFile), new FileOutputStream(output_dec_file));
            Log.i("DECRYPTDOWN", "File decrypted!!");
            Toast.makeText(ViewServerImages.this, "Image decrypted", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    private void download(Context context, String filename, String dest, String url) {
        DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setDestinationInExternalFilesDir(context,  DIRECTORY_DOWNLOADS, filename);

        downloadManager.enqueue(request);
    }

}