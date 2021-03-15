package com.example.e2eeapp_alpha.Certificates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.e2eeapp_alpha.MainActivity;
import com.example.e2eeapp_alpha.R;

import org.spongycastle.operator.OperatorCreationException;
import org.spongycastle.pkcs.PKCS10CertificationRequest;
import org.spongycastle.util.io.pem.PemObject;
import org.spongycastle.util.io.pem.PemWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

public class CERTCaller extends AppCompatActivity {
    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_e_r_t_caller);

    }



    public static String CertificateCaller(Context context) {
        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance("EC");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        keyGen.initialize(256, new SecureRandom());
        KeyPair x = keyGen.generateKeyPair();
        Log.i("KeyPair:", x.getPrivate() + ":::public::" + x.getPublic());

        //Generate CSR in PKCS#10 format encoded in DER
        PKCS10CertificationRequest csr = null;
        try {
            csr = CSRHelper.generateCSR(x, "commonname");
        } catch (
                IOException e) {
            e.printStackTrace();
        } catch (
                OperatorCreationException e) {
            e.printStackTrace();
        }
        byte  CSRder[] = new byte[0];
        try {
            CSRder = csr.getEncoded();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //convert the cert to string
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

        return csrPEM;

    }


}