package com.example.e2eeapp_alpha;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.e2eeapp_alpha.GroupChat.GroupChatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link groupChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class groupChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    define your variables here---

    private View groupfragmentView;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups = new ArrayList<>();

    private DatabaseReference databaseReference;





    public groupChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment groupChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static groupChatFragment newInstance(String param1, String param2) {
        groupChatFragment fragment = new groupChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
        //no code
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //no code
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        groupfragmentView = inflater.inflate(R.layout.fragment_group_chat, container, false);
        Initializefields();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("GroupMessages");
        RetriveAndDisplay();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentgrpName = parent.getItemAtPosition(position).toString();
                Intent groupIntent = new Intent(getContext(), GroupChatActivity.class);
                groupIntent.putExtra("groupName", currentgrpName);
                startActivity(groupIntent);

            }
        });

        return groupfragmentView;
    }

    private void RetriveAndDisplay() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_of_groups.clear();
                Iterator iterator = snapshot.getChildren().iterator();
                while (iterator.hasNext()){
//                    set.add(((DataSnapshot) iterator.next()).getKey());
                    String x = ((DataSnapshot) iterator.next()).getKey();
                    list_of_groups.add(x);
                    Log.i("GroupChat:", x);
                }

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Initializefields() {
        listView = (ListView) groupfragmentView.findViewById(R.id.group_list_view);
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list_of_groups);
        listView.setAdapter(arrayAdapter);
    }


}