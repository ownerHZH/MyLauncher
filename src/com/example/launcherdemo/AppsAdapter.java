package com.example.launcherdemo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


//GridView Adapter
public class AppsAdapter extends BaseAdapter implements DragGridBaseAdapter
{   
	private List<RResolveInfo> apps;
	private Context context;
	private int currentPage;
	private int mHidePosition = -1;
	//private int itemWidth,itemHeight;
	//private boolean drag=false;
	
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
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
		//apps.addAll(mApps);
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
    		convertView.setVisibility(View.VISIBLE);
            /*convertView.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					int action = event.getAction();
					switch (action & MotionEvent.ACTION_MASK) {
					case MotionEvent.ACTION_DOWN:
						
						touchDown(event);
						break;
					case MotionEvent.ACTION_MOVE:
						drag=true;
						if (info.getLongclicked()&&drag)
						{
							touchMove(event,v);
						}
						
						break;
					case MotionEvent.ACTION_UP:
						//touchUp(event);
						drag=false;
						break;
					}
					if (info.getLongclicked()&&drag)
						return true;
					return false;
				}
			});*/
        }
        
        //itemWidth=convertView.getMeasuredWidth();
        //itemHeight=convertView.getMeasuredHeight();
        
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
    
    public void removeItem(int index)
    {
    	apps.remove(index);
    }
    public void removeItem(RResolveInfo item)
    {
    	apps.remove(item);
    }
   /* private int initialX;
	private int initialY;
	
	private int lastTouchX;
	private int lastTouchY;
    private void touchDown(MotionEvent event) {
		initialX = (int)event.getRawX();
		initialY = (int)event.getRawY();
	}
    
    private void touchMove(MotionEvent event,View v) {
			lastTouchX = (int) event.getRawX();
			lastTouchY = (int) event.getRawY();
			
			moveDraggedView(lastTouchX, lastTouchY,v);
			manageSwapPosition(lastTouchX, lastTouchY);
			//manageEdgeCoordinates(lastTouchX);
	}
    private void moveDraggedView(int x, int y,View v) {		
		
		int width = v.getMeasuredWidth();
		int height = v.getMeasuredHeight();

		int l = x - (1 * width / 2);
		int t = y - (1 * height / 2);

		v.layout(l, t, l + width, t + height);
	}
    private void manageSwapPosition(int x, int y) {
		int target = getTargetAtCoor(x, y);
		
		animateGap(target);
	}
    
    private int getTargetAtCoor(int x, int y)
    {
    	int hangshu=apps.size()/4+1;
    	int lieshu=apps.size()>4?4:apps.size();
    	int row = 0;//第几行
    	int col=0;//第几列
    	for (int i = 1; i <= lieshu; i++) {
			if (x < i * itemWidth) {
				break;
			}	
			col++;
		}
		for (int i = 1; i <= hangshu; i++) {
			if (y < i * itemHeight) {
				break;
			}
			row++;
		}
		int position=col+row*4;
		Log.e("itemWidth----", itemWidth+"");
		Log.e("itemHeight----", itemHeight+"");
		Log.e("col----", col+"");
		Log.e("row----", row+"");
    	return position;
    }
    private void animateGap(int targetLocationInGrid) {
    	GridView gridview=(GridView) MyLauncher.pages.get(currentPage).getView();
		View targetView=gridview.getChildAt(targetLocationInGrid);
		float ofx=targetView.getLeft()-(initialX-itemWidth/2);
		float ofy=targetView.getTop()-(initialY-itemHeight/2);
		animateMoveToNewPosition(targetView,targetView.getLeft(),targetView.getTop(),initialX-itemWidth/2,initialY-itemHeight/2);
	}
    
    private void animateMoveToNewPosition(View targetView,float oldOffsetX,float oldOffsetY, float f, float g) {
		AnimationSet set = new AnimationSet(true);

		Animation rotate = createFastRotateAnimation();
		Animation translate = createTranslateAnimation(oldOffsetX,oldOffsetY,f,g);

		set.addAnimation(rotate);
		set.addAnimation(translate);

		targetView.clearAnimation();
		targetView.startAnimation(set);
	}

	private TranslateAnimation createTranslateAnimation(float oldOffsetX,float oldOffsetY, float newOffsetX, float newOffsetY) {
		TranslateAnimation translate = new TranslateAnimation(Animation.ABSOLUTE, oldOffsetX,
															  Animation.ABSOLUTE, newOffsetX,
															  Animation.ABSOLUTE, oldOffsetY,
															  Animation.ABSOLUTE, newOffsetY);
		translate.setDuration(250);
		translate.setFillEnabled(true);
		translate.setFillAfter(true);
		translate.setInterpolator(new AccelerateDecelerateInterpolator());
		return translate;
	}

	private Animation createFastRotateAnimation() {
		Animation rotate = new RotateAnimation(-2.0f,
										  2.0f,
										  Animation.RELATIVE_TO_SELF,
										  0.5f,
										  Animation.RELATIVE_TO_SELF,
										  0.5f);

	 	rotate.setRepeatMode(Animation.REVERSE);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setDuration(60);
        rotate.setInterpolator(new AccelerateDecelerateInterpolator());

		return rotate;
	}*/
	@Override
	public void reorderItems(int oldPosition, int newPosition) {
		RResolveInfo temp = apps.get(oldPosition);
		if(oldPosition < newPosition){
			for(int i=oldPosition; i<newPosition; i++){
				Collections.swap(apps, i, i+1);
			}
		}else if(oldPosition > newPosition){
			for(int i=oldPosition; i>newPosition; i--){
				Collections.swap(apps, i, i-1);
			}
		}
		
		apps.set(newPosition, temp);
	}
	@Override
	public void setHideItem(int hidePosition) {
		this.mHidePosition = hidePosition; 
		notifyDataSetChanged();
	}
}
