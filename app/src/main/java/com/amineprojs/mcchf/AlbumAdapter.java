package com.amineprojs.mcchf;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AlbumAdapter extends BaseAdapter {

    private List<Album> albums;
    private LayoutInflater lf;

    public AlbumAdapter(Activity a, List<Album> albums) {
        lf = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.albums = albums;
    }

    @Override
    public int getCount() {
        return albums.size();
    }

    @Override
    public Object getItem(int position) {
        return albums.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = lf.inflate(R.layout.item, null);
        }
        TextView id = (TextView) convertView.findViewById(R.id.albumId);
        TextView name = (TextView) convertView.findViewById(R.id.albumName);

        id.setText(albums.get(position).getId());
        name.setText(albums.get(position).getName());
        return convertView;
    }
}
