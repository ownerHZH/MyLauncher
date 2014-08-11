package com.example.launcherdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class OwnAnimationUtils {
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public static void shakeAnimation(final View v,float scale,int time,final boolean isShake) { 
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
						bouncer.cancel();
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
						bouncer.cancel();
					}
				}
			});
		}
		   	
    }
	
	//回弹效果函数
		public static void reboundAnimation(final View v) { 
	    	if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
	    	     //TODO:如果当前版本小于HONEYCOMB版本，即3.0版本
	    		com.nineoldandroids.animation.ObjectAnimator bounceAnim1 = com.nineoldandroids.animation.ObjectAnimator.ofFloat(v, "y",
	                    0f, 500f);
	            bounceAnim1.setDuration(1000);
	            //bounceAnim1.setRepeatCount(1);
	            bounceAnim1.setInterpolator(new DecelerateInterpolator());
	        	
	        	com.nineoldandroids.animation.ObjectAnimator bounceAnim = com.nineoldandroids.animation.ObjectAnimator.ofFloat(v, "y",
	                    500.0f, 0f).setDuration(3000);
	            bounceAnim.setInterpolator(new BounceInterpolator());
	            
	            com.nineoldandroids.animation.AnimatorSet bouncer = new com.nineoldandroids.animation.AnimatorSet();
	            bouncer.play(bounceAnim1).before(bounceAnim);
	            
	            bouncer.start();
	    	}else {
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
			   	
	    }
}
