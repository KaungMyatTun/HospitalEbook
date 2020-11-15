package com.anzer.hospitalebook.LabAndDI.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.anzer.hospitalebook.R;
import com.anzer.hospitalebook.vo.DIGroupTitleModel;

import java.util.List;

public class DIExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ExpandableListView mExpandableListView;
    private List<DIGroupTitleModel> mGroupCollection;
    private int[] groupStatus;
    ImageView gp_icon_iv;
    Boolean isActive=false;

    public DIExpandableListViewAdapter(Context pContext, ExpandableListView pExpandableListView,
                                     List<DIGroupTitleModel> pGroupCollection) {
        mContext = pContext;
        mGroupCollection = pGroupCollection;
        mExpandableListView = pExpandableListView;
        groupStatus = new int[mGroupCollection.size()];
        Log.e("Size", String.valueOf(mGroupCollection.size()));
        setListEvent();
    }

    private void setListEvent() {
        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int arg0) {
               groupStatus[arg0] = 1;
            }
        });
        mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int arg0) {
                groupStatus[arg0] = 0;
            }
        });
    }


    @Override
    public int getGroupCount() {
        return mGroupCollection.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mGroupCollection.get(i).sub_item.size();
    }

    @Override
    public Object getGroup(int i) {
        return mGroupCollection.get(i);
    }

    @Override
    public Object getChild(int arg0, int arg1) {
        return mGroupCollection.get(arg0).sub_item.get(arg1).itemName;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {
        GroupHolder groupHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.di_group_layout,null);
            groupHolder = new GroupHolder();
            groupHolder.title = (TextView) view.findViewById(R.id.di_group_name);
            groupHolder.img = view.findViewById(R.id.di_group_prefix_icon);
            if(b){
                groupHolder.img.setImageResource(R.drawable.minus_icon);
            }else{
                groupHolder.img.setImageResource(R.drawable.plus_icon);
            }
            view.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) view.getTag();
            if(b){
                groupHolder.img.setImageResource(R.drawable.minus_icon);
            }else{
                groupHolder.img.setImageResource(R.drawable.plus_icon);
            }
        }
        groupHolder.title.setText(mGroupCollection.get(groupPosition).groupTitleName);
        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean arg2, View convertView,ViewGroup parent) {
        final ChildHolder childHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.di_procedure_child_row, null);
            childHolder = new ChildHolder();
            childHolder.checkBox = (ImageView) convertView.findViewById(R.id.checkbox);
            childHolder.name=(TextView)convertView.findViewById(R.id.childname);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        childHolder.name.setText(mGroupCollection.get(groupPosition).sub_item.get(childPosition).itemName);

        if(mGroupCollection.get(groupPosition).sub_item.get(childPosition).selected) {
            // check box selected
            childHolder.checkBox.setImageResource(R.drawable.checkbox_selected);
        } else {
            childHolder.checkBox.setImageResource(R.drawable.checkbox_normal2);
        }
        return convertView;
    }

    class GroupHolder {
        ImageView img;
        TextView title;
    }
    class ChildHolder {
        ImageView checkBox;
        TextView name;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
