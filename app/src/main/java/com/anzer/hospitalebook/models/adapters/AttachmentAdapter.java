package com.anzer.hospitalebook.models.adapters;//
//package anzer.com.handwritingorder.models.adapters;
//
//import android.app.Activity;
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//
//import java.util.Collections;
//import java.util.List;
//
//import anzer.com.handwritingorder.R;
//import anzer.com.handwritingorder.models.Attachment;
//import anzer.com.handwritingorder.models.views.SquareImageView;
//
//
//public class AttachmentAdapter extends BaseAdapter {
//
//    private Activity mActivity;
//    private List<Attachment> attachmentsList;
//    private LayoutInflater inflater;
//
//
//    public AttachmentAdapter(Activity mActivity, List<Attachment> attachmentsList) {
//        this.mActivity = mActivity;
//		if (attachmentsList == null) {
//			attachmentsList = Collections.emptyList();
//		}
//		this.attachmentsList = attachmentsList;
//        this.inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    }
//
//
//    public int getCount() {
//        return attachmentsList.size();
//    }
//
//
//    public Attachment getItem(int position) {
//        return attachmentsList.get(position);
//    }
//
//
//    public long getItemId(int position) {
//        return 0;
//    }
//
//
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        Log.v("gridview called", "GridView called for position " + position);
//
//        Attachment mAttachment = attachmentsList.get(position);
//
//        AttachmentHolder holder;
//        if (convertView == null) {
//            convertView = inflater.inflate(R.layout.gridview_item, parent, false);
//
//            holder = new AttachmentHolder();
//            holder.image = (SquareImageView) convertView.findViewById(R.id.gridview_item_picture);
//            holder.text = (TextView) convertView.findViewById(R.id.gridview_item_text);
//            convertView.setTag(holder);
//        } else {
//            holder = (AttachmentHolder) convertView.getTag();
//        }
//
//        return convertView;
//    }
//
//
//	public List<Attachment> getAttachmentsList() {
//		return this.attachmentsList;
//	}
//
//
//
//
//    public class AttachmentHolder {
//
//        TextView text;
//        SquareImageView image;
//    }
//
//
//}
