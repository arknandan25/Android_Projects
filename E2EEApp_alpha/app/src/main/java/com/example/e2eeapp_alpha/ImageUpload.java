package com.example.e2eeapp_alpha;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e2eeapp_alpha.Utils.ImageEncrypter;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

public class ImageUpload extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageButton mButtonChooseImage;
    private ImageView mImageView;
    private Button mButtonUpload;
    private Button mShowImages;
    private Uri mImageUri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private File yourAppDir;
    private String my_key = "2421074815010621";
    private String my_spec_key = "2421074815010631";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        mButtonChooseImage = (ImageButton) findViewById(R.id.attach_file);
        mButtonUpload= findViewById(R.id.upload_image);
        mShowImages = findViewById(R.id.show_image);
        mImageView = findViewById(R.id.image_view);


        storageReference = FirebaseStorage.getInstance().getReference("image_uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("image_uploads");

        //setup local directoy for encrypted and decrypted images
        yourAppDir = new File(getExternalFilesDir(null).getAbsolutePath()+"_E2E_IMAGES");

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

        //Onclicks
        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ImageUpload.this, "Button clicked", Toast.LENGTH_SHORT).show();
                openFileChooser();
            }
        });


        mShowImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageUpload.this, ViewServerImages.class);
                ImageUpload.this.startActivity(intent);
            }
        });



        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });

    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //this is function called when user picks an image
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Log.i("imageLog", String.valueOf(data));
            Log.i("imageLog", String.valueOf(data.getData()));
            Log.i("imageLog", String.valueOf(mImageUri));
            Log.i("imageLog", queryName(getContentResolver(), mImageUri));
//            Picasso.with(this).load(mImageUri).into(mImageView);

            mImageView.setImageURI(mImageUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    private String getFileExt(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @SuppressLint("LongLogTag")
    private String EncryptUploads(Uri uri, String unenc_fileName){
        //convert the uri into a bitmap
        Bitmap bitmap = null;
//        try {
//            ParcelFileDescriptor parcelFileDescriptor =
//                    getContentResolver().openFileDescriptor(uri, "r");
//            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
//            bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
//
//            parcelFileDescriptor.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //
        InputStream input = null;
        try {
            input = getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap = BitmapFactory.decodeStream(input);
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        InputStream is = new ByteArrayInputStream(stream.toByteArray());

        //create encrypted file
        //unecnrypted file name like abc.png
        String fname_without_extension = unenc_fileName;
        int pos = fname_without_extension.lastIndexOf(".");
        if (pos > 0) {
            fname_without_extension = fname_without_extension.substring(0, pos);
        }
        String encrypted_file_name = fname_without_extension+"_enc";
        Log.i("Un-ENC Names:", unenc_fileName);
        Log.i("ENC Names:", fname_without_extension + "///:" + encrypted_file_name);
        File output_enc_file = new File(yourAppDir, encrypted_file_name);
        try {
            ImageEncrypter.encryptToFile(my_key, my_spec_key, is, new FileOutputStream(output_enc_file));

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
        Log.i("Encrypted before upload", output_enc_file.getAbsolutePath());
        Log.i("Encrypted before : File to URI", String.valueOf(output_enc_file.toURI()));
        return output_enc_file.getAbsolutePath();

        //decrypt image
//        String decrypted_file_name = unenc_fileName;
//        File output_dec_file = new File(yourAppDir, decrypted_file_name);
//        File encFile = new File(yourAppDir, encrypted_file_name);
//        try {
//            ImageEncrypter.decryptToFile(my_key, my_spec_key, new FileInputStream(encFile), new FileOutputStream(output_dec_file));
//            Toast.makeText(ImageUpload.this, "Image decrypted", Toast.LENGTH_SHORT).show();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (InvalidAlgorithmParameterException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        }
    }

//    THIS uploadfile sends unencrypted images successfully ot the firebase storage and database
    private void uploadFile(){
        //remember all files need to have a unique name; else it start to overwrite
        if (mImageUri != null) {
            StorageReference fileRef = storageReference.child(
                    System.currentTimeMillis()+ "." + getFileExt(mImageUri));

            fileRef.putFile(mImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        Log.i("Upload Image URI:", downloadUri.toString());
                        Toast.makeText(ImageUpload.this, "File upload successful!",Toast.LENGTH_SHORT).show();
                        String fileName = queryName(getContentResolver(), mImageUri).trim();
                        UploadImage uploadImage = new UploadImage(fileName,downloadUri.toString());
//                        String uploadId = databaseReference.push().getKey();
//                        Log.i("UploadID:", uploadId);
                        databaseReference.push().setValue(uploadImage);

                    }else{
                        Toast.makeText(ImageUpload.this, "File upload failed!"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else{
            Toast.makeText(ImageUpload.this, "No file selected!", Toast.LENGTH_SHORT).show();

        }
    }


    //This upload File uploads the encrypted version of the images and cretes it metadata entry in the realtimeDB
//    private void uploadFile(){
//        //remember all files need to have a unique name; else it start to overwrite
//        if (mImageUri != null) {
//            String encrypted_file_Addr;
//            String fileName = queryName(getContentResolver(), mImageUri).trim();
//            encrypted_file_Addr = EncryptUploads(mImageUri, fileName);
//            Log.i("Image URI Sttring", String.valueOf(mImageUri));
//
//            StorageReference fileRef = storageReference.child(
//                    String.valueOf(System.currentTimeMillis()));
//
//            //upload
//            Uri file = Uri.fromFile(new File(encrypted_file_Addr));
//
//            Task<Uri> urlTask = fileRef.putFile(file).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                @Override
//                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                    if (!task.isSuccessful()) {
//                        throw task.getException();
//                    }
//
//                    // Continue with the task to get the download URL
//                    return fileRef.getDownloadUrl();
//                }
//            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    if (task.isSuccessful()) {
//                        Uri downloadUri = task.getResult();
//                        Toast.makeText(ImageUpload.this, "Encrypted File upload successful!",Toast.LENGTH_SHORT).show();
//                        UploadImage uploadImage = new UploadImage(fileName,downloadUri.toString());
//                        databaseReference.push().setValue(uploadImage);
//                    } else {
//                        // Handle failures
//                        Toast.makeText(ImageUpload.this, "EncryptedFile upload failed!",Toast.LENGTH_SHORT).show();
//
//                    }
//                }
//            });
//
//        }else{
//            Toast.makeText(ImageUpload.this, "No file selected!", Toast.LENGTH_SHORT).show();
//
//        }
//    }
}