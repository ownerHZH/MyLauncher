package com.example.launcherdemo;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


//GridView Adapter
public class AppsAdapter extends BaseAdapter   
{   
	private List<RResolveInfo> apps;
	private Context context;
	
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	public List<RResolveInfo> getApps() {
		return apps;
	}
	public void setApps(List<RResolveInfo> mApps) {		
		this.apps = mApps;
	}
	
	public AppsAdapter(Context context,List<RResolveInfo> mApps) {
		this.apps=mApps;
		this.context=context;
    } 
    @Override   
    public View getView(int position, View convertView, ViewGroup parent) {               
    	final ViewHolder holder;                                
        final RResolveInfo info=apps.get(position);
        if(info!=null)
        {
        	if (convertView == null) {
        		
            	convertView = LayoutInflater.from(context).inflate(R.layout.gridlayout_item, null);
            	holder = new ViewHolder();
                holder.tv = (TextView)convertView.findViewById(R.id.tv);
                holder.iv = (ImageView)convertView.findViewById(R.id.iv);
                holder.deletev=(ImageView)convertView.findViewById(R.id.imageViewDelete);
                holder.picAndText=(LinearLayout) convertView.findViewById(R.id.picAndText);
                convertView.setTag(holder);                
            } else {                   
            	holder = (ViewHolder) convertView.getTag();               
            }
        	holder.tv.setText(info.getResolveInfo().loadLabel(context.getPackageManager()));
            holder.iv.setImageDrawable(info.getResolveInfo().activityInfo.loadIcon(context.getPackageManager()));
            if(info.getLongclicked())
            {
            	holder.deletev.setVisibility(View.VISIBLE);
            }else
            {
            	holder.deletev.setVisibility(View.GONE);
            }

            /*//ж�ذ�ť����¼�
            holder.deletev.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					//��Ӧ�õİ���  
 	                String pkg = info.getResolveInfo().activityInfo.packageName; 
					//ͨ������İ�������URI 
					Uri packageURI = Uri.parse("package:"+pkg); 
					//����Intent��ͼ 
					Intent intent = new Intent(Intent.ACTION_DELETE,packageURI); 
					//ִ��ж�س��� 
					context.startActivity(intent);
				}
			});*/
        }
        
        return convertView;           
    }                   
    public  int getCount() {              
        return apps.size()>0?apps.size():0;           
    }               
    public  Object getItem(int position) {               
        return apps.get(position);           
    }               
    public  long getItemId(int position) {               
        return position;           
    }
    
    class ViewHolder{
        private TextView tv;
        private ImageView iv;
        private ImageView deletev;
        private LinearLayout picAndText;
    }      	   
}
