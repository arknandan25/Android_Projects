package com.example.e2eeapp_alpha.Chats;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e2eeapp_alpha.Contacts;
import com.example.e2eeapp_alpha.Encryption.TextEncryptor;
import com.example.e2eeapp_alpha.R;
import com.example.e2eeapp_alpha.UserListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{


    ArrayList<MessageObject> ListToDisplay;
    private Context mContext;

    FirebaseAuth mAuth;
    FirebaseDatabase userRef;


    public MessageAdapter( Context context, ArrayList<MessageObject> listToDisplay) {
        this.mContext = context;
        this.ListToDisplay = listToDisplay;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_message_layout,parent,false);

        mAuth = FirebaseAuth.getInstance();
        return new MessageViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        String messageSenderId = mAuth.getCurrentUser().getUid();
        MessageObject messageObject = ListToDisplay.get(position);
        String fromUserID = messageObject.getFrom();
        String MessageType = messageObject.getType();
        String message = messageObject.getMessage();
        String timestamp = messageObject.getTimestamp();
        String ucid = messageObject.getUcid();
        String time= messageObject.getTime();

        if (MessageType.equals("text")){
            holder.receiver.setVisibility(View.INVISIBLE);
            holder.sender.setVisibility(View.VISIBLE);
            if (fromUserID.equals(messageSenderId)){
                //current user
                holder.SenderMessage.setBackgroundResource(R.drawable.sender_messages_layout);
                holder.SenderMessage.setTextColor(Color.BLACK);
                holder.SenderMessage.setText(getDecryptedMessage(message, ucid));
                holder.SenderTime.setText(time);


            }else{
                holder.sender.setVisibility(View.INVISIBLE);
                holder.receiver.setVisibility(View.VISIBLE);

                holder.ReceiverMessage.setBackgroundResource(R.drawable.receiver_messages_layout);
                holder.ReceiverMessage.setTextColor(Color.BLACK);
                holder.ReceiverMessage.setText(getDecryptedMessage(message, ucid));
                holder.ReceiverTime.setText(time);
            }


        }


    }

    @Override
    public int getItemCount() {
        return ListToDisplay.size();
    }


    public static class MessageViewHolder extends RecyclerView.ViewHolder{
        //add all the text views, image views that are in your individual item layout
        TextView SenderMessage, ReceiverMessage, ReceiverTime, SenderTime;
        LinearLayout receiver, sender;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            //Initialize them here
            SenderMessage = (TextView)itemView.findViewById(R.id.sender_message_text);
            ReceiverMessage = (TextView)itemView.findViewById(R.id.receiver_message_text);
            SenderTime =(TextView)itemView.findViewById(R.id.sender_message_time);
            ReceiverTime =(TextView)itemView.findViewById(R.id.receiver_message_time);

            receiver = (LinearLayout)itemView.findViewById(R.id.receiver_side);
            sender = (LinearLayout)itemView.findViewById(R.id.sender_side);
        }
    }


    public  String getDecryptedMessage(String message, String decKey){
        //set the decrypted text here
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    byte[] x = new byte[0];
                    byte[] y = new byte[0];
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        x = Base64.getDecoder().decode(message);
                        y = Base64.getDecoder().decode(decKey);
                        Log.i("MessageAdapter:Message", message);
                    }
                    return  TextEncryptor.DecryptAES(x, mContext, y);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
//        Log.i("Message Adapter:", message);

        return "";
    }



}
