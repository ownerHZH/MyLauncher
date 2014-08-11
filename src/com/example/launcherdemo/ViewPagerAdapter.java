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
    
    private Page getPage(int pageIndex) {
		return pages.get(pageIndex);
	}

	public void swapItems(int pageIndex, int itemIndexA, int itemIndexB) {
		getPage(pageIndex).swapItems(itemIndexA, itemIndexB);
	}

	public void moveItemToPreviousPage(int pageIndex, int itemIndex) {
		int leftPageIndex = pageIndex-1;
		if (leftPageIndex >= 0) {
			Page startpage = getPage(pageIndex);
			Page landingPage = getPage(leftPageIndex);
			
			RResolveInfo item = startpage.removeItem(itemIndex);
			landingPage.addItem(item);	
		}	
	}

	public void moveItemToNextPage(int pageIndex, int itemIndex) {
		int rightPageIndex = pageIndex+1;
		if (rightPageIndex < getCount()) {
			Page startpage = getPage(pageIndex);
			Page landingPage = getPage(rightPageIndex);
			
			RResolveInfo item = startpage.removeItem(itemIndex);
			landingPage.addItem(item);			
		}	
	}

	public void deleteItem(int pageIndex, int itemIndex) {
		getPage(pageIndex).deleteItem(itemIndex);
	}
}
