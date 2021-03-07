package com.example.e2eeapp_alpha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class tabsAccessorAdapter extends FragmentPagerAdapter {
    public tabsAccessorAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                contactFragment contactFragment = new contactFragment();
                return contactFragment;

            case 1:
                groupChatFragment groupChatFragment = new groupChatFragment();
                return groupChatFragment;
            case 2:
                chatsFragment chatsFragment = new chatsFragment();
                return chatsFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Contacts";
            case 1:
                return "Groups";
            case 2:
                return "Chats ";
            default:
                return null;
        }
    }
}
