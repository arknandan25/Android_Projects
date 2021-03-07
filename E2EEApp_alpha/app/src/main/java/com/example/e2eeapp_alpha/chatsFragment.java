package com.example.e2eeapp_alpha;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.e2eeapp_alpha.Chats.ChatObject;
import com.example.e2eeapp_alpha.Chats.ChatUsersAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link chatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class chatsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //declare variables here
    private View ChatsView;
    private RecyclerView ChatUsersRecyclerView;
    private  ChatUsersAdapter mChatUsersAdapter;

    ArrayList<Contacts> ChatUserList = new ArrayList<>();






    //variables end here

    public chatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment chatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static chatsFragment newInstance(String param1, String param2) {
        chatsFragment fragment = new chatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
        //no code goes here
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //no code goes here
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Access point fort this fragment
        if (ChatsView == null){
            ChatsView =  inflater.inflate(R.layout.fragment_chats, container, false);
            ChatUsersRecyclerView = (RecyclerView)ChatsView.findViewById(R.id.chatuserList);
            ChatUsersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            Log.i("chatsFragment:", "OnCreateView called");
            mChatUsersAdapter = new ChatUsersAdapter(getContext(),ChatUserList);
            ChatUsersRecyclerView.setAdapter(mChatUsersAdapter);
            displayChatUsers();


        }


//        ChatsView =  inflater.inflate(R.layout.fragment_chats, container, false);
//        ChatUsersRecyclerView = (RecyclerView)ChatsView.findViewById(R.id.chatuserList);
//        ChatUsersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return ChatsView;
    }

    @Override
    public void onStart() {
        super.onStart();
//        Intent intent = new Intent(ChatsView.getContext(), contactFragment.class);
//        ChatsView.getContext().startActivity(intent);

        Log.i("chatsFragment:", "Onstart called");
//        displayChatUsers();
    }//end of onStart

        @Override
    public void onResume() {
        super.onResume();
//        ChatUsersRecyclerView.setAdapter(mChatUsersAdapter);
//        ChatUserList.clear();
            Log.i("chatsFragment:", "OnResume called");



        }

    private void displayChatUsers() {
        Log.i("chatsFragment:", "In displayChats");

        String current_user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference query1 = FirebaseDatabase.getInstance().getReference()
                .child("User_ONO_Chat")
                .child(current_user);

        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ChatUserList.clear();

                    for (DataSnapshot childSnapshot : snapshot.getChildren()){
                        ChatObject mChatObj = new ChatObject(childSnapshot.getKey().toString(), childSnapshot.getValue().toString());
                        //with mChatObj you can access the unique session key and the user id associated with it

                        DatabaseReference query2 = FirebaseDatabase.getInstance().getReference()
                                .child("User_Data_Temp")
                                .child(mChatObj.getUserId());

                        query2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    String phone= null, name = null, status =null, image = null;
                                    phone = snapshot.child("phone").getValue().toString();
                                    name = snapshot.child("name").getValue().toString();
                                    status = snapshot.child("status").getValue().toString();
                                    image = snapshot.child("image").getValue().toString();
                                    Contacts mUser = new Contacts(name,phone,status,image, snapshot.getKey());
                                    ChatUserList.add(mUser);
                                    Log.i("List created:", "successfully from user_Data_temp db!");

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.i("Some error in query 2:" , error.getMessage().toString());
                            }
                        });
                        //query2 ends here

                    }// end of for
                    //call the adapter once the list is formed
                    mChatUsersAdapter = new ChatUsersAdapter(getContext(),ChatUserList);
                    ChatUsersRecyclerView.setAdapter(mChatUsersAdapter);
//                    mChatUsersAdapter.notifyDataSetChanged();
                    Log.i("chatsFragment:", "In displayChats at end");




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Some error in query1:" , error.getMessage().toString());

            }
        });
    }//end of displayChatUsers

//    @Override
//    public void onResume() {
//        super.onResume();
//        ChatUsersRecyclerView.setAdapter(mChatUsersAdapter);
//        ChatUserList.clear();
//
//
//    }


    @Override
    public void onStop() {
        super.onStop();
//        ChatUserList.clear();
        Log.i("chatsFragment:", "OnStop called");


    }

    @Override
    public void onPause() {
        super.onPause();
//        ChatUserList.clear();
        Log.i("chatsFragment:", "OnPause called");


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        ChatUserList.clear();
        Log.i("chatsFragment:", "OnDestroy called");

    }
}