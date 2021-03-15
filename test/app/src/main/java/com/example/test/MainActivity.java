package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

//import org.spongycastle.asn1.x500.X500Name;
//import org.spongycastle.asn1.x500.X500NameBuilder;
//import org.spongycastle.asn1.x500.style.BCStyle;
//import org.spongycastle.asn1.x509.SubjectPublicKeyInfo;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.spongycastle.operator.OperatorCreationException;
import org.spongycastle.pkcs.PKCS10CertificationRequest;
import org.spongycastle.util.io.pem.PemObject;
import org.spongycastle.util.io.pem.PemWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import chilkatsoft.CkCrypt2;
import chilkatsoft.CkCsr;
import chilkatsoft.CkFileAccess;
import chilkatsoft.CkPrivateKey;
import chilkatsoft.CkRsa;
import de.frank_durr.ecdh_curve25519.ECDHCurve25519;

public class MainActivity extends AppCompatActivity {

//    static {
//        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
//    }

    static {
        try {
            System.loadLibrary("ecdhcurve25519");
            Log.i("TAG", "Loaded ecdhcurve25519 library.");
        } catch (UnsatisfiedLinkError e) {
            Log.e("TAG", "Error loading ecdhcurve25519 library: " + e.getMessage());
        }

//        addBCProvider();
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

    }

//    public static void addBCProvider() {
//        // java.security.NoSuchProviderException: no such provider: BC
//        if (Security.getProvider(org.bouncycastle.jce.provider.BouncyCastleProvider.PROVIDER_NAME) == null) {
//            System.out.println("JVM Installing BouncyCastle Security Providers to the Runtime");
//            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//        } else {
//            System.out.println("JVM Installed with BouncyCastle Security Providers");
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ecdh curver 25519
        //generating test keys
        SecureRandom random = new SecureRandom();
        SecureRandom random1 = new SecureRandom();

        // Create Alice's secret key from a big random number.
        byte[] alice_secret_key = ECDHCurve25519.generate_secret_key(random);
        // Create Alice's public key.
        byte[] alice_public_key = ECDHCurve25519.generate_public_key(alice_secret_key);
        //

        ///
//        ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec("secp192r1");
//
//        ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(new BigInteger(1, alice_secret_key), spec);
//
//        ECNamedCurveSpec params = new ECNamedCurveSpec("secp192r1", spec.getCurve(), spec.getG(), spec.getN());
//        java.security.spec.ECPoint w = new java.security.spec.ECPoint(new BigInteger(1, Arrays.copyOfRange(alice_public_key, 0, 24)), new BigInteger(1, Arrays.copyOfRange(alice_public_key, 24, 48)));
//        PublicKey publicKey = factory.generatePublic(new java.security.spec.ECPublicKeySpec(w, params));



        ///




        //convert byte[] keys to public/private key or keypair

//        KeyFactory kf = null; // or "EC" or whatever
//        PrivateKey privateKey = null;
//        PublicKey publicKey = null;
//        try {
//            kf = KeyFactory.getInstance("DH");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
////        try {
////             privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(alice_secret_key));
////        } catch (InvalidKeySpecException e) {
////            e.printStackTrace();
////        }
//        try {
//                publicKey = (org.bouncycastle.jce.interfaces.ECPublicKey)kf.generatePublic(new X509EncodedKeySpec(alice_public_key));
////             publicKey = kf.generatePublic(new X509EncodedKeySpec(alice_public_key));
//        } catch (InvalidKeySpecException e) {
//            e.printStackTrace();
//        }
//
//        KeyPair keyPair = new KeyPair(publicKey, privateKey);
//        Log.i("KeyPairManual:", keyPair.getPrivate() + ":::pub;ic::" + keyPair.getPublic());

    .    //
        //Generate KeyPair
        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance("EC");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyGen.initialize(256, new SecureRandom());
        KeyPair x = keyGen.generateKeyPair();
        Log.i("KeyPair:", x.getPrivate() + ":::pub;ic::" + x.getPublic());


//Generate CSR in PKCS#10 format encoded in DER
        PKCS10CertificationRequest csr = null;
        try {
            csr = CsrHelper.generateCSR(x, "commonname");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        }
        byte  CSRder[] = new byte[0];
        try {
            CSRder = csr.getEncoded();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringWriter writer = new StringWriter();
        PemWriter pemWriter = new PemWriter(writer);
        try {
            pemWriter.writeObject(new PemObject("CERTIFICATE REQUEST", CSRder));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pemWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pemWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String csrPEM = writer.toString();
        writeToFile(csrPEM, this);
        Log.i("fsf:", csrPEM);

    }

    private void writeToFile(String data, Context context) {
        try {
            File yourAppDir = new File(getExternalFilesDir(null).getAbsolutePath()+"_CSR");

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
            File csr = new File(yourAppDir, "CSR_Ark.txt");
            FileOutputStream stream = new FileOutputStream(csr);
            try {
                stream.write(data.getBytes());
            } finally {
                stream.close();
            }

            Log.i("csr file","created");

//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.csr", Context.MODE_PRIVATE));
//            outputStreamWriter.write(data);
//            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


}