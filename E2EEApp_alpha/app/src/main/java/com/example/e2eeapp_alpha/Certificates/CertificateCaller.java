//package com.example.e2eeapp_alpha.Certificates;
//
//import android.content.Context;
//import android.util.Log;
//
//import org.spongycastle.operator.OperatorCreationException;
//import org.spongycastle.pkcs.PKCS10CertificationRequest;
//import org.spongycastle.util.io.pem.PemObject;
//import org.spongycastle.util.io.pem.PemWriter;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.StringWriter;
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.NoSuchAlgorithmException;
//import java.security.Security;
//
//
//public class CertificateCaller {
//
//
//
//    //Generate KeyPair
//    KeyPairGenerator keyGen = null;
//
//    keyGen = KeyPairGenerator.getInstance("EC");
//
//    keyGen.initialize(256, new SecureRandom());
//    KeyPair x = keyGen.generateKeyPair();
//        Log.i("KeyPair:", x.getPrivate() + ":::pub;ic::" + x.getPublic());
//
//
//    //Generate CSR in PKCS#10 format encoded in DER
//    PKCS10CertificationRequest csr = null;
//        try {
//        csr = CsrHelper.generateCSR(x, "commonname");
//    } catch (
//    IOException e) {
//        e.printStackTrace();
//    } catch (
//    OperatorCreationException e) {
//        e.printStackTrace();
//    }
//    byte  CSRder[] = new byte[0];
//        try {
//        CSRder = csr.getEncoded();
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
//
//    StringWriter writer = new StringWriter();
//    PemWriter pemWriter = new PemWriter(writer);
//        try {
//        pemWriter.writeObject(new PemObject("CERTIFICATE REQUEST", CSRder));
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
//        try {
//        pemWriter.flush();
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
//        try {
//        pemWriter.close();
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
//    String csrPEM = writer.toString();
//    writeToFile(csrPEM, this);
//        Log.i("fsf:", csrPEM);
//
//}
//
//    private void writeToFile(String data, Context context) {
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
//
//        }
//        catch (IOException e) {
//            Log.e("Exception", "File write failed: " + e.toString());
//        }
//    }
//}
