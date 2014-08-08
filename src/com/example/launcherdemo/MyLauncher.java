package com.example.launcherdemo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LauncherActivity;
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
import android.view.DragEvent;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;
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
import android.widget.SlidingDrawer.OnDrawerScrollListener;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class MyLauncher extends Activity {
	//private GridView mGrid;
	private RelativeLayout mainLiner;
	private List<RResolveInfo> mApps=Collections.synchronizedList(new ArrayList<RResolveInfo>());
	//private List<List<RResolveInfo>> everyPageDataList=Collections.synchronizedList(new ArrayList<List<RResolveInfo>>());
	//private List<AppsAdapter> everyPageDataAdapter=Collections.synchronizedList(new ArrayList<MyLauncher.AppsAdapter>());
	List<View> views=Collections.synchronizedList(new ArrayList<View>());
	@SuppressWarnings("deprecation")
	private SlidingDrawer slidingDrawer;
	private Button handle;
	static final String TAG="TAG";
	protected static final int CUT_HANDLE_HEIGHT = 0x112;
	AppWidgetHost mAppWidgetHost ;
	private AppWidgetManager mAppWidgetManager; 
	private Context context;
	private ViewPager viewPager;
	private ViewPagerAdapter viewPagerAdapter=null;
	private AppsAdapter gridViewAdapter;
	private MyInstalledReceiver installedReceiver=null;
	int intPageNumber=0;
    int lastPageNumber=0;
    int currentPage=0;
	
    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadApps();
        context = getApplicationContext();     
        mAppWidgetManager = AppWidgetManager.getInstance(context); 
        setContentView(R.layout.activity_my_launcher);
        
        slidingDrawer=(SlidingDrawer) findViewById(R.id.slidingdrawer1);
        handle=(Button) findViewById(R.id.handle);
        viewPager=(ViewPager) findViewById(R.id.vPager);
        //mGrid = (GridView) findViewById(R.id.apps_list); 
        mainLiner=(RelativeLayout) findViewById(R.id.mainLinear);
        //mGrid.setAdapter(new AppsAdapter()); 
        
        
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
				shakeAnimation(slidingDrawer);//������
				handle.setHeight(0);
				handle.setBackgroundColor(Color.TRANSPARENT);
				
			}
		});  
        //mainLiner.setOnLongClickListener(longClickListener);
        
        reLoadApp();//����app
        
        
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				currentPage=arg0;
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

    //����app����
	private void reLoadApp() {
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
        
	}     

    //�����汳��������¼�
	private OnLongClickListener longClickListener=new OnLongClickListener() {
		
		@Override
		public boolean onLongClick(View arg0) {
			Log.e(TAG, "����-------");
			addWidget();
			return true;
		}
	};
	
	
	//���˰�ť�¼�
	@SuppressWarnings("deprecation")
	@Override
	public void onBackPressed() {
		isNeed=false;
		if(slidingDrawer.isOpened())
		{
			slidingDrawer.animateClose();
		}
		//ȡ���������
		new Thread(){
			@Override
			public void run() {
				for(RResolveInfo rr:mApps)
				{
					if(rr.getLongclicked())
					{
						rr.setLongclicked(false);
					}
				}
				
				gridViewAdapter.notifyDataSetChanged();
				super.run();
			}}.start();
		
		//super.onBackPressed();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}

	//��widget
	void addWidget() {           
		//mAppWidgetManager = AppWidgetManager.getInstance(context);  
		mAppWidgetHost = new AppWidgetHost(MyLauncher.this, 0x100);
		int appWidgetId = mAppWidgetHost.allocateAppWidgetId(); 
		//int appWidgetId=AppWidgetManager.INVALID_APPWIDGET_ID;
	    Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);           
	    pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);           
	    // start the pick activity           
	    startActivityForResult(pickIntent,0);       
	} 
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		  if (resultCode == RESULT_OK) {  
	            switch (requestCode) {  
	            case 0:  
	                addAppWidget(data);  
	                break;  
	            case 1:  
	                completeAddAppWidget(data);  
	                break;  
	            }  
	        } else if (requestCode == 0  
	                && resultCode == RESULT_CANCELED && data != null) {  
	            int appWidgetId = data.getIntExtra(  
	                    AppWidgetManager.EXTRA_APPWIDGET_ID, -1);  
	            if (appWidgetId != -1) {  
	                mAppWidgetHost.deleteAppWidgetId(appWidgetId);  
	            }  
	        }
	}
    
    private void addAppWidget(Intent data) {  
        int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,-1);  
        String customWidget = data.getStringExtra("custom_widget");  
        if ("search_widget".equals(customWidget)) {  
            mAppWidgetHost.deleteAppWidgetId(appWidgetId);  
        } else {  
            AppWidgetProviderInfo appWidget = mAppWidgetManager  
                    .getAppWidgetInfo(appWidgetId);  
  
            Log.d("addAppWidget", "configure:" + appWidget.configure);  
            if (appWidget.configure != null) {  
                // �������ý���   
                Intent intent = new Intent(  
                        AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);  
                intent.setComponent(appWidget.configure);  
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,  
                        appWidgetId);  
  
                startActivityForResult(intent, 1);  
            } else {  
                // ֱ�����ӵ�����   
                onActivityResult(1, Activity.RESULT_OK,  
                        data);  
            }  
        }  
    }
    
    /** 
     * ����widget 
     *  
     * @param data 
     */  
    private void completeAddAppWidget(Intent data) {  
        Bundle extras = data.getExtras();  
        int appWidgetId = extras  
                .getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);  
  
        AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager  
                .getAppWidgetInfo(appWidgetId);  
  
        View hostView = mAppWidgetHost.createView(this, appWidgetId,  
                appWidgetInfo);  
        mainLiner.addView(hostView, appWidgetInfo.minWidth, appWidgetInfo.minHeight);
        /*mainLiner.addInScreen(hostView, appWidgetInfo.minWidth,  
                appWidgetInfo.minHeight); */ 
    } 
    
    //��ȡ�����Ѿ���װ��app��Ϣ
    private void loadApps() {  
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
    } 

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_my_launcher, menu);
        return true;
    }
    
    //ViewPager Adapter
    public class ViewPagerAdapter extends PagerAdapter
    {
    	 private List<View> mListViews;  
         
         public ViewPagerAdapter(List<View> mListViews) {  
             this.mListViews = mListViews;  
         } 
		@Override
		public int getCount() {
			 return  mListViews.size(); 
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		
		@Override  
        public void destroyItem(ViewGroup container, int position, Object object)   {     
            container.removeView(mListViews.get(position));  
        }  
  
  
        @Override  
        public Object instantiateItem(ViewGroup container, int position) {            
             container.addView(mListViews.get(position), 0);  
             return mListViews.get(position);  
        }          	
    }
    
    //GridView Adapter
    public class AppsAdapter extends BaseAdapter   
    {   
    	private List<RResolveInfo> apps;       
        public List<RResolveInfo> getApps() {
			return apps;
		}
		public void setApps(List<RResolveInfo> mApps) {
			this.apps = mApps;
		}
		
		public AppsAdapter(List<RResolveInfo> mApps) {
        	this.apps=mApps;
        } 
        @Override   
        public View getView(int position, View convertView, ViewGroup parent) {               
        	final ViewHolder holder;                                
            final RResolveInfo info=apps.get(position);
            if(info!=null)
            {
            	if (convertView == null) { 
                	convertView = getLayoutInflater().inflate(R.layout.gridlayout_item, null);
                	holder = new ViewHolder();
                    holder.tv = (TextView)convertView.findViewById(R.id.tv);
                    holder.iv = (ImageView)convertView.findViewById(R.id.iv);
                    holder.deletev=(ImageView)convertView.findViewById(R.id.imageViewDelete);
                    holder.picAndText=(LinearLayout) convertView.findViewById(R.id.picAndText);
                    convertView.setTag(holder);                
                } else {                   
                	holder = (ViewHolder) convertView.getTag();               
                }
            	holder.tv.setText(info.getResolveInfo().loadLabel(getPackageManager()));
                holder.iv.setImageDrawable(info.getResolveInfo().activityInfo.loadIcon(getPackageManager()));
                if(info.getLongclicked())
                {
                	holder.deletev.setVisibility(View.VISIBLE);
                }else
                {
                	holder.deletev.setVisibility(View.GONE);
                }
                //ͼ�곤����¼�
                holder.picAndText.setOnLongClickListener(new OnLongClickListener() {
    				
    				@Override
    				public boolean onLongClick(View arg0) {
    					info.setLongclicked(true);
    					holder.deletev.setVisibility(View.VISIBLE);
    					return false;
    				}
    			});
                //ͼ�����¼�
                holder.picAndText.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View arg0) {
                      if(!info.getLongclicked())
                      {
                    	//��Ӧ�õİ���  
     	                String pkg = info.getResolveInfo().activityInfo.packageName;  
     	                //Ӧ�õ���activity��  
     	                String cls = info.getResolveInfo().activityInfo.name;  
     	                  
     	                ComponentName componet = new ComponentName(pkg, cls);  
     	                  
     	                Intent i = new Intent();  
     	                i.setComponent(componet);  
     	                startActivity(i); 
                      }	                 
    				}
    			});
                //ж�ذ�ť����¼�
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
    					startActivity(intent);
    				}
    			});
            }
            
            return convertView;           
        }                   
        public  int getCount() {              
            return apps.size();           
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
    
    private static final int ANIMATION_DURATION = 2000;
    private boolean isNeed=false;
    float hscale=0.85f; 
    @SuppressLint("NewApi")
    //�ص�Ч������
	private void shakeAnimation(final View v) {  
		ValueAnimator bounceAnim1 = ObjectAnimator.ofFloat(v, "y",
                0f, 500f);
        bounceAnim1.setDuration(1000);
        //bounceAnim1.setRepeatCount(1);
        bounceAnim1.setInterpolator(new DecelerateInterpolator());
    	
    	ValueAnimator bounceAnim = ObjectAnimator.ofFloat(v, "y",
                500.0f, 0f).setDuration(3000);
        bounceAnim.setInterpolator(new BounceInterpolator());
        
        AnimatorSet bouncer = new AnimatorSet();
        bouncer.play(bounceAnim1).before(bounceAnim);
        
        bouncer.start();   	
    }
    //Ӧ�õİ�װж�ؼ����㲥
    public class MyInstalledReceiver extends BroadcastReceiver {  
        @Override  
        public void onReceive(Context context, Intent intent) {  
      
            if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {     // install  
                //String packageName = intent.getDataString(); 
                //Log.e("launcher===", "��װ�� :" + packageName);
               /* Intent ii = new Intent(packageName.replace("package:", ""));  
                PackageManager pm = getPackageManager();  
                List<ResolveInfo> apps = pm.queryIntentActivities(ii, PackageManager.MATCH_DEFAULT_ONLY);
                if(apps!=null&&apps.size()>0)
                {
                	RResolveInfo arg0=new RResolveInfo();
                    arg0.setLongclicked(false);
                    ResolveInfo resolveInfo=apps.get(0);
                    if(resolveInfo!=null)
                    {
                    	arg0.setResolveInfo(resolveInfo);
        				mApps.add(arg0);
                        reLoadApp();
                    }
    					
                }*/
                loadApps();
                reLoadApp();
            }  
      
            if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {   // uninstall  
                String packageName = intent.getDataString();  
                for(RResolveInfo rr:mApps)
				{
					if(packageName.equalsIgnoreCase("package:"+rr.getResolveInfo().activityInfo.packageName))
					{
						mApps.remove(rr);
						//gridViewAdapter.getApps().remove(rr);
						//gridViewAdapter.notifyDataSetChanged();
						break;
					}
				}
                reLoadApp();
                //viewPagerAdapter.notifyDataSetChanged();
                Log.e("launcher===", "ж���� :" + packageName);  
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
    
}