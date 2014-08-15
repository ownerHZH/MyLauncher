package com.example.launcherdemo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.Inflater;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

//ViewPager Adapter
public class ViewPagerAdapter extends PagerAdapter
{
	 private Context context;  
     
     public ViewPagerAdapter(Context context) {  
    	 this.context = context;
     } 
	@Override
	public int getCount() {
		 return  MyLauncher.pages.size(); 
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}
	
	@Override  
    public void destroyItem(ViewGroup container, int position, Object object)   {     
        container.removeView((View) object);  
    }  


    @Override  
    public Object instantiateItem(ViewGroup container, int position) { 
    	DragGridView v=(DragGridView) LayoutInflater.from(context).inflate(R.layout.gridview_layout, null);
        v.setContainer((ViewPager) container);
        v.setAdapter(new AppsAdapter(context, MyLauncher.pages.get(position).getItems()));
        v.setContainerAdapter(this);
    	container.addView(v);            
        return v;  
    } 
    
  //����ҳ����������Page����
  	private Page getPage(int index) {
  	     return MyLauncher.pages.get(index);
      }
  	
  	//����ĳ��ҳ����������ݵ�˳��
  	public void swapItems(int pageIndex,int itemA, int itemB) {
  		getPage(pageIndex).swapItems(itemA, itemB);
  	}
  	
  	//��һ��ҳ���Item�Ƶ�ǰ��һ������
  	public void moveItemToPreviousPage(int pageIndex, int itemIndex) {
  		int leftPageIndex = pageIndex-1;
  		if (leftPageIndex >= 0) {
  			Page startpage = getPage(pageIndex);
  			Page landingPage = getPage(leftPageIndex);
  			
  			RResolveInfo item = startpage.removeItem(itemIndex);
  			landingPage.addItem(item);	
  		}	
  	}
  	
  	//��һ��ҳ���Item�Ƶ���һ��ҳ����ȥ
  	public void moveItemToNextPage(int pageIndex, int itemIndex) {
  		int rightPageIndex = pageIndex+1;
  		if (rightPageIndex < getCount()) {
  			Page startpage = getPage(pageIndex);
  			Page landingPage = getPage(rightPageIndex);
  			
  			RResolveInfo item = startpage.removeItem(itemIndex);
  			landingPage.addItem(item);			
  		}	
  	}
  	
  	//ɾ��ĳ��ҳ���е�ĳ��
  	public void deleteItem(int pageIndex, int itemIndex) {
  		getPage(pageIndex).deleteItem(itemIndex);
  	}
}
