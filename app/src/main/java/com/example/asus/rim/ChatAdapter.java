package com.example.asus.rim;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater = null;

    private ArrayList<ChatData> chatMessageList;
    private Context myContext;

    public ChatAdapter(Activity activity, ArrayList<ChatData> chatList) {
        myContext = activity;
        chatMessageList = chatList;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void add(ChatData object) {
        chatMessageList.add(object);
    }

    @Override
    public int getCount() {
        return chatMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
