package com.science_o_matic.imedjelly.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.science_o_matic.imedjelly.R;
import com.science_o_matic.imedjelly.activity.DrawerItem;

public class NewNavigationAdapter extends BaseExpandableListAdapter {
    private Activity activity;  
	ArrayList<DrawerItem> itemarray;
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	} 

//	public NewNavigationAdapter(Activity activity, ArrayList<DrawerItem> itemarray) {  
//		super();  
//		this.activity = activity;  
//		this.itemarray = itemarray;
//	}     
//
//	@Override
//	public Object getItem(int position) {       
//		return itemarray.get(position);
//	}
//
//	public int getCount() {    
//        return itemarray.size();  
//    }
//
//    @Override
//    public long getItemId(int position) {
//    	return position;
//    }
//
//    public static class Row  
//    {  
//    	TextView title;
//    	ImageView icon;
//    }
//
//    public View getView(int position, View convertView, ViewGroup parent) {
//    	Row view;  
//    	LayoutInflater inflator = activity.getLayoutInflater();  
//    	if(convertView==null)  
//    	{  
//           view = new Row();
//           DrawerItem item = itemarray.get(position);
//           convertView = inflator.inflate(R.layout.drawer_item, null);
//           view.title = (TextView) convertView.findViewById(R.id.title_item);
//           view.title.setText(item.getTitle());     
//           view.icon = (ImageView) convertView.findViewById(R.id.icon);
//           view.icon.setImageResource(item.getIcon());           
//           convertView.setTag(view);  
//        }  
//        else  
//        {  
//           view = (Row) convertView.getTag();  
//        }  
//        return convertView;  
//    }
}