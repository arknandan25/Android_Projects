package com.example.e2eeapp_alpha;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link contactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class contactFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //my code goes here
    private View ContactsView;
    private RecyclerView MyContactList;
    private  UserListAdapter mUserListAdapter;
//    private  RecyclerView.Adapter mUserListAdapter;


    private DatabaseReference databaseReference;

    ArrayList<Contacts> contactList = new ArrayList<>();

    // variables end here



    public contactFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment contactFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static contactFragment newInstance(String param1, String param2) {
        contactFragment fragment = new contactFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        //ContactView is the access point for all items in the contactFragmentActivity
        ContactsView =  inflater.inflate(R.layout.fragment_contact, container, false);

        //Note MyContactList is the recycler view
        //the adapter is set defined below in another function
        //MyContactList will be the list that will show the people that are your local phone contact
        // and also app users
        MyContactList = (RecyclerView)ContactsView.findViewById(R.id.userList);
//        MyContactList.setNestedScrollingEnabled(false);
        MyContactList.setLayoutManager(new LinearLayoutManager(getContext()));
//         mUserListAdapter= new UserListAdapter(contactList);
//         databaseReference = FirebaseDatabase.getInstance().getReference().child("User_Data_Temp");
        return ContactsView;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onStart() {
        super.onStart();

        getContactList();

//        FirebaseRecyclerOptions<Contacts> options = new FirebaseRecyclerOptions.Builder<Contacts>()
//                .setQuery(databaseReference, Contacts.class)
//                .build();

//        FirebaseRecyclerAdapter<Contacts, contactFragment.FindContactsViewHolder> adapter =
//                new FirebaseRecyclerAdapter<Contacts, contactFragment.FindContactsViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull contactFragment.FindContactsViewHolder holder, int position, @NonNull Contacts model) {
//                        //your code for getting data from DB and displaying ti front-end goes here
//                        holder.UserName.setText(model.getName());
//                        holder.UserPhone.setText(model.getPhone());
//                        holder.UserStatus.setText(model.getStatus());
//                        Picasso.get().load(model.getImage()).into(holder.UserProfile);
//                    }
//
//                    @NonNull
//                    @Override
//                    public contactFragment.FindContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                        //connect your each item display here
//                        // for this case it will be the friends_display_layout
//                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_display_layout,
//                                parent, false);
//
//                        contactFragment.FindContactsViewHolder viewHolder = new contactFragment.FindContactsViewHolder(view);
//                        return viewHolder;
//                    }
//                };
//
//        //Fire up the recycler view
//        MyContactList.setAdapter(adapter);
//        adapter.startListening();



    }


    public static class FindContactsViewHolder extends RecyclerView.ViewHolder{

        //add all the textviews, image views that are in your individual item layout
        TextView UserName, UserPhone, UserStatus;
        CircleImageView UserProfile;

        public FindContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            //Initialize them here
            UserName = itemView.findViewById(R.id.friend_name);
            UserPhone = itemView.findViewById(R.id.friend_phone);
            UserProfile = itemView.findViewById(R.id.friend_profile_image);
            UserStatus = itemView.findViewById(R.id.friend_status);
        }
    }


    //checking /working with the iso code for the contact in our phone as firebase always stores with a prefix like +353
    @RequiresApi(api = Build.VERSION_CODES.M)
    private String getCountryISO(){
        String iso = null;
        TelephonyManager telephonyManager =  (TelephonyManager) getActivity().getApplicationContext().getSystemService(getActivity().getApplicationContext().TELEPHONY_SERVICE);
        if (telephonyManager.getNetworkCountryIso()!=null){
            if (!telephonyManager.getNetworkCountryIso().toString().equals("")){
                iso= telephonyManager.getNetworkCountryIso().toString();
            }
        }
        return CountryToPhonePrefix.getPhone(iso);
    }


    //GETTING LOCAL PHONE CONTACT LIST ->firstly goes through all the contacts in my phone
    //then finds which users of those are app users
    //in this function we loop through all your phone's contacts and add them one by one to a list
    //this list is then checked against all the users using our app
    //those users will be shown as contacts in your contactFragment
    @RequiresApi(api = Build.VERSION_CODES.M)
    private  void getContactList(){

        String isoPrefix = getCountryISO();

        Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,null,null,null);
        contactList.clear();

        while (phones.moveToNext()){
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            phone = phone.replace(" ","");
            phone = phone.replace("-","");
            phone = phone.replace("(","");
            phone = phone.replace(")","");

            if(!String.valueOf(phone.charAt(0)).equals("+")){
                phone = isoPrefix + phone;
            }

            //Add this user entry to your contact list
            Contacts mContact = new Contacts(name,phone);
//            contactList.add(mContact);
            Log.i("getContactList", mContact.getName()+ ":::" + mContact.getPhone());
            //calling to find the users on ur phone that are app users to chat further
            CheckUserIsCustomer(mContact);

        }
    }

    private void CheckUserIsCustomer(Contacts mContact) {
        DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("User_Data_Temp");
        //checking which user is on our db i.e app user
        Query query = mUserDB.orderByChild("phone").equalTo(mContact.getPhone());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){//user exists

                    String phone= null, name = null,
                            status =null, image = null;

                    //ideally there shouls be only one user corresponding to a phone number
                    //but the above query might return multiple entries with same number
                    //so this loop is implemented just to be careful
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        if (childSnapshot.child("phone").getValue() != null)
                            phone = childSnapshot.child("phone").getValue().toString();
                        if (childSnapshot.child("name").getValue() != null)
                            name = childSnapshot.child("name").getValue().toString();
                        if (childSnapshot.child("status").getValue() != null)
                            status = childSnapshot.child("status").getValue().toString();
                        if (childSnapshot.child("image").getValue() != null)
                            image = childSnapshot.child("image").getValue().toString();


                        Contacts mUser = new Contacts(name,phone,status,image, childSnapshot.getKey());
                        contactList.add(mUser);
//                        mUserListAdapter.notifyDataSetChanged();

                    }

                    mUserListAdapter = new UserListAdapter(getContext(), contactList);
                    MyContactList.setAdapter(mUserListAdapter);




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        contactList.clear();
//    }
}

