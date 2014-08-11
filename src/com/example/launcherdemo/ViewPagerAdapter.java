package com.example.launcherdemo;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

//ViewPager Adapter
public class ViewPagerAdapter extends PagerAdapter
{
	 private List<Page> pages;  
     
     public ViewPagerAdapter(List<Page> pages) {  
         this.pages = pages;  
     } 
	@Override
	public int getCount() {
		 return  pages.size(); 
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}
	
	@Override  
    public void destroyItem(ViewGroup container, int position, Object object)   {     
        container.removeView(pages.get(position).getView());  
    }  


    @Override  
    public Object instantiateItem(ViewGroup container, int position) {               
        View v=pages.get(position).getView();
        container.addView(v);            
        return v;  
    }          	
}
