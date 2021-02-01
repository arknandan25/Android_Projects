package com.example.xmppapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private Button registerButton;
    private static final String logtagR = "YYYY";
    private static final String logtag = "XXXX";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = (Button) findViewById(R.id.login);
        registerButton = (Button) findViewById(R.id.register);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(logtag, "User clicked Login here!!!!");
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        //connect to the server backgroung thread code here
                        XMPPTCPConnectionConfiguration connectionConfiguration = null;
                        try {
                            connectionConfiguration = XMPPTCPConnectionConfiguration.builder()
                                    .setUsernameAndPassword("varunMx3131", "1234")
                                    .setXmppDomain("chinwag.im")
                                    .setKeystoreType(null)
                                    .build();

                        } catch (XmppStringprepException e){
                            e.printStackTrace();
                        }

                        AbstractXMPPConnection connection = null;
                        connection = new XMPPTCPConnection(connectionConfiguration);

                        connection.addConnectionListener(new ConnectionListener() {
                            @Override
                            public void connected(XMPPConnection connection) {
                                Log.d(logtag, "Connected to  Chinwag Server!");
                            }

                            @Override
                            public void authenticated(XMPPConnection connection, boolean resumed) {
                                Log.d(logtag, "Authenticated to  Chinwag Server!");

                            }

                            @Override
                            public void connectionClosed() {
                                Log.d(logtag, "Connection Closed to  Chinwag Server!");

                            }

                            @Override
                            public void connectionClosedOnError(Exception e) {
                                Log.d(logtag, "Connected to  Chinwag Server!");

                            }

                            @Override
                            public void reconnectionSuccessful() {
                                Log.d(logtag, "Re-Connected success to  Chinwag Server!");

                            }

                            @Override
                            public void reconnectingIn(int seconds) {
                                Log.d(logtag, "Re-Connecting to  Chinwag Server!");

                            }

                            @Override
                            public void reconnectionFailed(Exception e) {
                                Log.d(logtag, "Re-Connecting to  Chinwag Server Failed!");

                            }
                        });


                        //Receive messages
                        ChatManager chatManager = ChatManager.getInstanceFor(connection);
                        chatManager.addIncomingListener(new IncomingChatMessageListener() {
                            @Override
                            public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                                Log.d(logtag,"Incoming message->>" + message.getBody());
                            }
                        });

                        try {
                            connection.connect().login();
                        } catch (XMPPException e) {
                            e.printStackTrace();
                        } catch (SmackException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                // Runnable needs to be attached to a thread to run in background
                Thread connection_thread = new Thread(runnable);
                connection_thread.start();


            }
        });
        //-------end of login code to server and receive messages--------------


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(logtag, "User clicked Register here!!!!");
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        XMPPTCPConnectionConfiguration config = null;
                        try {
                            config = XMPPTCPConnectionConfiguration.builder()
                                    .setXmppDomain("creep.im")
                                    .setHost("creep.im")
                                    .build();
                        } catch (XmppStringprepException e){
                            e.printStackTrace();
                        }

                        AbstractXMPPConnection connection = null;
                        connection = new XMPPTCPConnection(config);
//                        connection.addConnectionListener(new ConnectionListener() {
//                            @Override
//                            public void connected(XMPPConnection connection) {
//                                Log.d(logtagR, "Connected");
//                            }
//
//                            @Override
//                            public void authenticated(XMPPConnection connection, boolean resumed) {
//                                Log.d(logtagR, "User created");
//
//                            }
//
//                            @Override
//                            public void connectionClosed() {
//                                Log.d(logtagR, "Connection Closed");
//
//                            }
//
//                            @Override
//                            public void connectionClosedOnError(Exception e) {
//                                Log.d(logtagR, "connectionClosedOnError");
//
//                            }
//
//                            @Override
//                            public void reconnectionSuccessful() {
//                                Log.d(logtagR, "reconnectionSuccessful");
//
//                            }
//
//                            @Override
//                            public void reconnectingIn(int seconds) {
//                                Log.d(logtagR, "hvjhv"+seconds);
//                            }
//
//                            @Override
//                            public void reconnectionFailed(Exception e) {
//                                Log.d(logtagR, "reconnectionFailed");
//                            }
//                        });
                        String uname = "dudezz1212";
                        String pass = "54321";
                        try {
                            //connection.connect().login();
                            connection.connect();
                            Log.d(logtagR, "Connected");
                            AccountManager accountManager = AccountManager.getInstance(connection);
                            if (accountManager.supportsAccountCreation()){
                                Log.d(logtagR, "Inside if");
                                accountManager.sensitiveOperationOverInsecureConnection(true);
                                Log.d(logtagR, "create account executed here");

//                                Map<String, String> map = new HashMap<String, String>();
//                                map.put("username","vinay");
//                                map.put("name", "vinay");
//                                map.put("password", "vinay");
//                                map.put("emial", "vinay@gmail.com");
//                                accountManager.createAccount(Localpart.from("vinay"), "vinay", map);

                                accountManager.createAccount(Localpart.from(uname), pass);
                                Log.d(logtagR, "Creation success");
                            }else{
                                Log.d(logtagR, "No support for account creation :( ");
                            }
//                            ((org.jivesoftware.smackx.iqregister.AccountManager) accountManager).sensitiveOperationOverInsecureConnection(true);
//                            accountManager.createAccount(Localpart.from(uname), pass);


                        } catch (XMPPException e) {
                            e.printStackTrace();
                        } catch (SmackException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                };
                Thread connection_thread = new Thread(runnable);
                connection_thread.start();

            }
        });

    }// end OnCreate
}//end MainActivity