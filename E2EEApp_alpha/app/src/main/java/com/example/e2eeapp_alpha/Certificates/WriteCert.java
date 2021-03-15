//package com.example.e2eeapp_alpha.Certificates;
//
//import android.content.Context;
//import android.util.Log;
//import android.widget.Toast;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//public class WriteCert {
//
//    public void writeToFile(String data, Context context) {
//        try {
//            File yourAppDir = new File(getExternalFilesDir(null).getAbsolutePath()+"_CSR");
//
//            if(!yourAppDir.exists() && !yourAppDir.isDirectory())
//            {
//                // create empty directory
//                if (yourAppDir.mkdirs())
//                {
//                    Log.i("CreateDir","App dir created");
//                }
//                else
//                {
//                    Log.w("CreateDir","Unable to create app dir!");
//                }
//            }
//            else
//            {
//                Log.i("CreateDir","App dir already exists");
//            }
//            File csr = new File(yourAppDir, "CSR_Ark.txt");
//            FileOutputStream stream = new FileOutputStream(csr);
//            try {
//                stream.write(data.getBytes());
//            } finally {
//                stream.close();
//            }
//
//            Log.i("csr file","created");
//            Toast.makeText(context, "CSR Generated", Toast.LENGTH_LONG ).show();
//
//        }
//        catch (IOException e) {
//            Log.e("Exception", "File write failed: " + e.toString());
//        }
//    }
//}
