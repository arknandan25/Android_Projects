package com.example.e2eeapp_alpha;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.e2eeapp_alpha.Utils.ImageEncrypter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

public class MainActivity2 extends AppCompatActivity {

    private static final String FILE_ENC_NAME = "profile_image_enc";
    private static final String  FILE_NAME_DEC = "profile_image_desc.png";
    Button btn_enc, btn_dec;
    ImageView imageView;

    File myDir;
    private String my_key = "2421074815010621";
    private String my_spec_key = "2421074815010621";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_dec  = (Button)findViewById(R.id.btn_decrypt);
        btn_enc = (Button) findViewById(R.id.btn_encrypt);
        imageView = (ImageView) findViewById(R.id.imageView);
        //Init path
        File yourAppDir = new File(getExternalFilesDir(null).getAbsolutePath()+"yourAppDir");
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


        Log.i("this is a tag", Environment.getExternalStorageDirectory().toString());


        //image encryption local takes place here
        Dexter.withActivity(this)
                .withPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                btn_dec.setEnabled(true);
                btn_enc.setEnabled(true);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                Toast.makeText(MainActivity2.this, "Permissions are not given by user", Toast.LENGTH_SHORT).show();
            }
        }).check();

        //encryption
        btn_enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("this is a tag", Environment.getExternalStorageDirectory().toString());

                //convert drawable into bitmap
                Drawable drawable = ContextCompat.getDrawable(MainActivity2.this, R.drawable.user);
                BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                InputStream is = new ByteArrayInputStream(stream.toByteArray());


                //create encrypted file
                File output_enc_file = new File(yourAppDir, FILE_ENC_NAME);
                try {
                    ImageEncrypter.encryptToFile(my_key, my_spec_key, is, new FileOutputStream(output_enc_file));
                    Toast.makeText(MainActivity2.this, "Image encrypted", Toast.LENGTH_SHORT).show();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
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
        });

        btn_dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File output_dec_file = new File(yourAppDir, FILE_NAME_DEC);
                Log.i("DECRYPT", "Name of the o/p decrypted file:" + output_dec_file.toString());
                File encFile = new File(yourAppDir, FILE_ENC_NAME);
                Log.i("DECRYPT", "Name of the i/p encrypted file:" + encFile.toString());

                try {
                    ImageEncrypter.decryptToFile(my_key, my_spec_key, new FileInputStream(encFile), new FileOutputStream(output_dec_file));

                    // set for image view
                    imageView.setImageURI(Uri.fromFile(output_dec_file));

                    Toast.makeText(MainActivity2.this, "Image decrypted", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
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
        });

    }
}