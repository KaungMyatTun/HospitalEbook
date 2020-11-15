package com.anzer.hospitalebook.models.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.anzer.hospitalebook.R;
import com.anzer.hospitalebook.models.views.ExpandableHeightGridView;
import com.anzer.hospitalebook.models.views.SquareImageView;

import java.util.ArrayList;


/**
 * Created by David on 5/23/2018.
 */

public class ImageGridAdapter extends BaseAdapter {
    ArrayList<Item> data = new ArrayList<>();
    LayoutInflater inflater;
    Context c;

    public ImageGridAdapter(Context c, ArrayList<Item> data, ExpandableHeightGridView mGridView){
        this.c = c;
        this.data = data;
        this.inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AttachmentHolder holder = null;

//        Log.e("position", String.valueOf(position) );

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) c).getLayoutInflater();
            convertView = inflater.inflate(R.layout.gridview_item, parent, false);

            holder = new AttachmentHolder();
            holder.imageItem = (SquareImageView) convertView.findViewById(R.id.gridview_item_picture);
            convertView.setTag(holder);
        } else {
            holder = (AttachmentHolder) convertView.getTag();
        }

        Item item = (Item) data.get(position);
//        Log.e("$$$$$$$$$$$$$$$$$$", item.getCaption());
        holder.imageItem.setImageBitmap(item.getImage());

        return convertView;
    }

    static class AttachmentHolder {
        TextView txtTitle;
        SquareImageView imageItem;
    }
}

