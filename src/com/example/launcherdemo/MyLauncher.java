package com.example.launcherdemo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;

@SuppressWarnings("deprecation")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class MyLauncher extends Activity {
	//private GridView mGrid;
	private RelativeLayout mainLiner;
	
	public static List<Page> pages=Collections.synchronizedList(new ArrayList<Page>());
	//private List<List<RResolveInfo>> everyPageDataList=Collections.synchronizedList(new ArrayList<List<RResolveInfo>>());
	//private List<AppsAdapter> everyPageDataAdapter=Collections.synchronizedList(new ArrayList<MyLauncher.AppsAdapter>());
	//List<View> views=Collections.synchronizedList(new ArrayList<View>());
	@SuppressWarnings("deprecation")
	private SlidingDrawer slidingDrawer;
	private Button handle;
	static final String TAG="TAG";
	protected static final int CUT_HANDLE_HEIGHT = 0x112;
	AppWidgetHost mAppWidgetHost ;
	private Context context;
	private ViewPager viewPager;
	private ViewPagerAdapter viewPagerAdapter=null;
	//private AppsAdapter gridViewAdapter;
	private MyInstalledReceiver installedReceiver=null;
    int currentPage=0; //在哪一页
    int pagePosition=0;//点击的是哪一页的第几个
    private StopItemAnimator stopItemAnimatorCallBack;
	
    public void setStopItemAnimatorCallBack(
			StopItemAnimator stopItemAnimatorCallBack) {
		this.stopItemAnimatorCallBack = stopItemAnimatorCallBack;
	}

	@SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = MyLauncher.this;
        setContentView(R.layout.activity_my_launcher);
        loadApps();            
              
        slidingDrawer=(SlidingDrawer) findViewById(R.id.slidingdrawer1);
        handle=(Button) findViewById(R.id.handle);
        viewPager=(ViewPager) findViewById(R.id.vPager);
        //mGrid = (GridView) findViewById(R.id.apps_list); 
        mainLiner=(RelativeLayout) findViewById(R.id.mainLinear);
        //mGrid.setAdapter(new AppsAdapter()); 
        viewPagerAdapter=new ViewPagerAdapter(context);
        viewPager.setAdapter(viewPagerAdapter);
        
        //mGrid.setOnItemClickListener(listener);
       slidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			
			@Override
			public void onDrawerClosed() {
				handle.setBackgroundResource(R.drawable.ic_launcher);
				handle.setAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_enter));
			}
		});
        slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			
			@Override
			public void onDrawerOpened() {
				OwnAnimationUtils.reboundAnimation(slidingDrawer);//来回震荡
				handle.setHeight(0);
				handle.setBackgroundColor(Color.TRANSPARENT);
				
			}
		});  
        //mainLiner.setOnLongClickListener(longClickListener);
        
        //reLoadApp();//加载app
        
        
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				currentPage=arg0;
				//clearGridViewItemAnimaion();
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
              
    }
    
    //清除item的动画效果
    /*private void clearGridViewItemAnimaion() {
		for(Page page:pages)
		{
			for(RResolveInfo rr:page.getItems())
			{
				rr.setLongclicked(false);
			}
			GridView gv=((GridView)(page.getView()));
			int c=gv.getChildCount();
			for(int i=0;i<c;i++)
			{
				gv.getChildAt(i).clearAnimation();
			}
		}		
	}*/

    //加载app函数
	/*private void reLoadApp() {
		intPageNumber=mApps.size()/16;
        lastPageNumber=mApps.size()%16;
        gridViewAdapter=null;
        viewPagerAdapter=null;
        views.clear();
        for(int i=0;i<=intPageNumber;i++)
        {
        	GridView gridview=new GridView(context);
        	gridview.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        	gridview.setNumColumns(4);
        	gridview.setGravity(Gravity.CENTER); 	
        	gridview.setHorizontalFadingEdgeEnabled(false);
        	gridview.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        	gridview.setVerticalSpacing(20);
        	List<RResolveInfo> datalist=null;
        	if(i==intPageNumber)
        	{
    			datalist=mApps.subList(mApps.size()-lastPageNumber, mApps.size());
        		gridViewAdapter=new AppsAdapter(datalist);            		    			         		
        	}else
        	{
        		datalist=mApps.subList(i*16, i*16+16);
        		gridViewAdapter=new AppsAdapter(datalist);  
        	} 
        	gridview.setAdapter(gridViewAdapter);
            views.add(gridview);
        	
        }
        viewPagerAdapter=new ViewPagerAdapter(views);
        viewPager.setAdapter(viewPagerAdapter);  
        if(lastPageNumber>0)
        {
        	viewPager.setCurrentItem(currentPage);	
        }else
        {
        	viewPager.setCurrentItem(--currentPage);
        }
        
	}*/     

	
	
	//后退按钮事件
	@SuppressWarnings("deprecation")
	@Override
	public void onBackPressed() {
		//停止抖动效果
		//stopItemAnimatorCallBack.onStopAnimator();
		viewPagerAdapter.notifyDataSetChanged();
	}

	//获取所有已经安装的app信息
    private void loadApps() {
    	System.gc();
    	List<RResolveInfo> mApps=new ArrayList<RResolveInfo>();
    	mApps.clear();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);           
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);              
        List<ResolveInfo>  apps = getPackageManager().queryIntentActivities(mainIntent, 0);
        
        for(ResolveInfo r:apps)
        {
        	RResolveInfo arg0=new RResolveInfo();
        	arg0.setResolveInfo(r);
        	arg0.setLongclicked(false);
        	mApps.add(arg0);
        }
        
        if(mApps!=null&&mApps.size()>0)
        {
        	pages.clear();        	        	       	
        	for(int i=0;i<=mApps.size()/16;i++)
        	{
        		Page page=new Page();            	       						
				if(i==apps.size()/16)
        		{
        			List<RResolveInfo> lastList=mApps.subList(i*16, mApps.size());
        			if(lastList!=null&&lastList.size()>0)
        			{
        				page.setItems(lastList);     				
    					pages.add(page);
        			}        			
        		}else
        		{
        			page.setItems(mApps.subList(i*16, i*16+16));
					pages.add(page);
        		}
        	}
        }
    }        
	   
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_my_launcher, menu);
        return false;
    }
    
   
    
    

    @SuppressLint("NewApi")
    
    
    
    //应用的安装卸载监听广播
    public class MyInstalledReceiver extends BroadcastReceiver {  
        @Override  
        public void onReceive(Context context, Intent intent) {  
      
            if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {     // install  
                String packageName = intent.getDataString(); 
                //loadApps();
            }  
      
            if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {   // uninstall  
                String packageName = intent.getDataString(); 
                /*List<RResolveInfo> deLi=new ArrayList<RResolveInfo>();
                int index=-1;
                for(RResolveInfo rr:pages.get(currentPage).getItems())
				{
                	index++;
					if(packageName.equalsIgnoreCase("package:"+rr.getResolveInfo().activityInfo.packageName))
					{					
						deLi.add(rr);						
						break;
					}
				}
                pages.get(currentPage).getItems().removeAll(deLi);
                Log.e("launcher===", "卸载了 :" + packageName); */ 
                //loadApps();
                //viewPagerAdapter.notifyDataSetChanged();
            }  
        }  
    }
    
    @Override  
    public void onStart(){  
        super.onStart();  
        if(installedReceiver==null)
        {
        	installedReceiver = new MyInstalledReceiver();  
            IntentFilter filter = new IntentFilter();  
              
            filter.addAction("android.intent.action.PACKAGE_ADDED");  
            filter.addAction("android.intent.action.PACKAGE_REMOVED");  
            filter.addDataScheme("package");  
              
            this.registerReceiver(installedReceiver, filter);
        }     
    }  
    
    
      
    @Override  
    public void onDestroy(){  
        if(installedReceiver != null) {  
            this.unregisterReceiver(installedReceiver);  
        }  
          
        super.onDestroy();  
    } 
    
 
    
    /*public void shakeAnimation(final View v,float scale,int time) { 
    	if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
    	     //TODO:如果当前版本小于HONEYCOMB版本，即3.0版本
    		com.nineoldandroids.animation.ObjectAnimator bounceAnim1 = com.nineoldandroids.animation.ObjectAnimator
    				.ofFloat(v, "rotation",
                    0f, scale);
            bounceAnim1.setDuration(time);
            //bounceAnim1.setRepeatCount(1);
            bounceAnim1.setInterpolator(new LinearInterpolator());
        	
        	com.nineoldandroids.animation.ObjectAnimator bounceAnim = com.nineoldandroids.animation.ObjectAnimator
        			.ofFloat(v, "rotation",
        					scale, 0f).setDuration(time);
            bounceAnim.setInterpolator(new LinearInterpolator());
            
            com.nineoldandroids.animation.ValueAnimator bounceAnim2 = com.nineoldandroids.animation.ObjectAnimator
            		.ofFloat(v, "rotation",
                    0f, -scale).setDuration(time);
            bounceAnim2.setInterpolator(new LinearInterpolator());
            
            com.nineoldandroids.animation.ValueAnimator bounceAnim3 = com.nineoldandroids.animation.ObjectAnimator
            		.ofFloat(v, "rotation",
            				-scale, 0f).setDuration(time);
            bounceAnim3.setInterpolator(new LinearInterpolator());
            
            final com.nineoldandroids.animation.AnimatorSet bouncer = new com.nineoldandroids.animation.AnimatorSet();
            bouncer.play(bounceAnim1).before(bounceAnim);
            bouncer.play(bounceAnim).before(bounceAnim2);
            bouncer.play(bounceAnim2).before(bounceAnim3);
            
            bouncer.start();
            bouncer.addListener(new com.nineoldandroids.animation.AnimatorListenerAdapter(){

				@Override
				public void onAnimationEnd(
						com.nineoldandroids.animation.Animator animation) {
					if(isShake)
					{
						bouncer.start();
					}else {
						bouncer.removeAllListeners();
						bouncer.cancel();
						bouncer.end();
					}
				}
            	
            });
            
    	}else {
    		ValueAnimator bounceAnim1 = ObjectAnimator.ofFloat(v, "rotation",
                    0f, scale);
            bounceAnim1.setDuration(time);
            //bounceAnim1.setRepeatCount(1);
            bounceAnim1.setInterpolator(new LinearInterpolator());
        	
        	ValueAnimator bounceAnim = ObjectAnimator.ofFloat(v, "rotation",
        			scale, 0f).setDuration(time);
            bounceAnim.setInterpolator(new LinearInterpolator());
            
            ValueAnimator bounceAnim2 = ObjectAnimator.ofFloat(v, "rotation",
                    0f, -scale).setDuration(time);
            bounceAnim2.setInterpolator(new LinearInterpolator());
            
            ValueAnimator bounceAnim3 = ObjectAnimator.ofFloat(v, "rotation",
                    -scale, 0f).setDuration(time);
            bounceAnim3.setInterpolator(new LinearInterpolator());
            
            final AnimatorSet bouncer = new AnimatorSet();
            bouncer.play(bounceAnim1).before(bounceAnim);
            bouncer.play(bounceAnim).before(bounceAnim2);
            bouncer.play(bounceAnim2).before(bounceAnim3);
           
            bouncer.start();
            bouncer.addListener(new AnimatorListenerAdapter() {
            	@Override
				public void onAnimationEnd(Animator animation) {
            		if(isShake)
					{
						bouncer.start();
					}else {
						bouncer.removeAllListeners();
						bouncer.cancel();
						bouncer.end();
					}
				}
			});
		}
		   	
    }*/
    
    public interface StopItemAnimator
    {
    	public void onStopAnimator();
    }
    
}
