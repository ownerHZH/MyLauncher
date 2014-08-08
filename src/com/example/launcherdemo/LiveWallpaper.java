package com.example.launcherdemo;
import android.graphics.Canvas;  
import android.graphics.Paint;  
import android.os.Handler;  
import android.service.wallpaper.WallpaperService;  
import android.view.MotionEvent;  
import android.view.SurfaceHolder;  
/** 
 *  
 * @author Tian 
 * 
 */  
public class LiveWallpaper extends WallpaperService  
{  
    // ʵ��WallpaperService����ʵ�ֵĳ��󷽷�  
    public Engine onCreateEngine()  
    {  
        // �����Զ����Engine  
        return new MyEngine();  
    }  
  
    class MyEngine extends Engine  
    {  
        // ��¼��������Ƿ�ɼ�  
        private boolean mVisible;  
        // ��¼��ǰ��ǰ�û������¼��ķ���λ��  
        private float mTouchX = -1;  
        private float mTouchY = -1;  
        // ��¼��ǰԲȦ�Ļ���λ��  
          
        //���Ͻ�����  
        private float cx1 = 15;  
        private float cy1 = 20;  
          
        //���½�����  
        private float cx2 = 300;  
        private float cy2 = 380;  
          
        //���Ͻ�����  
        private float cx3 = 300;  
        private float cy3 = 20;  
          
        //���½�����  
        private float cx4 = 15;  
        private float cy4 = 380;  
          
        // ���廭��  
        private Paint mPaint = new Paint();  
        // ����һ��Handler  
        Handler mHandler = new Handler();  
        // ����һ��������ִ�е�����  
        private final Runnable drawTarget = new Runnable()  
        {  
            public void run()  
            {  
                // ��̬�ػ���ͼ��  
                drawFrame();  
            }  
        };  
  
        @Override  
        public void onCreate(SurfaceHolder surfaceHolder)  
        {  
            super.onCreate(surfaceHolder);  
            // ��ʼ������  
            mPaint.setColor(0xffffffff);  
            mPaint.setAntiAlias(true);  
            mPaint.setStrokeWidth(2);  
            mPaint.setStrokeCap(Paint.Cap.ROUND);  
            mPaint.setStyle(Paint.Style.STROKE);  
            // ���ô������¼�  
            setTouchEventsEnabled(true);  
        }  
  
        @Override  
        public void onDestroy()  
        {  
            super.onDestroy();  
            // ɾ���ص�  
            mHandler.removeCallbacks(drawTarget);  
        }  
  
        @Override  
        public void onVisibilityChanged(boolean visible)  
        {  
            mVisible = visible;  
            // ������ɼ�ʱ��ִ��drawFrame()������  
            if (visible)  
            {  
                // ��̬�ػ���ͼ��  
                drawFrame();  
            }  
            else  
            {  
                // ������治�ɼ���ɾ���ص�  
                mHandler.removeCallbacks(drawTarget);  
            }  
        }  
  
        public void onOffsetsChanged(float xOffset, float yOffset, float xStep,  
            float yStep, int xPixels, int yPixels)  
        {  
            drawFrame();  
        }  
  
  
        public void onTouchEvent(MotionEvent event)  
        {  
            // �����⵽��������  
            if (event.getAction() == MotionEvent.ACTION_MOVE)  
            {  
                mTouchX = event.getX();  
                mTouchY = event.getY();  
            }  
            else  
            {  
                mTouchX = -1;  
                mTouchY = -1;  
            }  
            super.onTouchEvent(event);  
        }  
  
        // �������ͼ�εĹ��߷���  
        private void drawFrame()  
        {  
            // ��ȡ�ñ�ֽ��SurfaceHolder  
            final SurfaceHolder holder = getSurfaceHolder();  
            Canvas c = null;  
            try  
            {  
                // �Ի�������  
                c = holder.lockCanvas();  
                if (c != null)  
                {  
                    c.save();  
                    // ���Ʊ���ɫ  
                    c.drawColor(0xff000000);  
                    // �ڴ��������ԲȦ  
                    drawTouchPoint(c);  
                      
                    // ����ԲȦ  
                    c.drawCircle(cx1, cy1, 80, mPaint);  
                    c.drawCircle(cx2, cy2, 40, mPaint);  
                    c.drawCircle(cx3, cy3, 50, mPaint);  
                    c.drawCircle(cx4, cy4, 60, mPaint);  
                    c.restore();  
                }  
            }  
            finally  
            {  
                if (c != null)  
                    holder.unlockCanvasAndPost(c);  
            }  
            mHandler.removeCallbacks(drawTarget);  
            // ������һ���ػ�  
            if (mVisible)  
            {  
                cx1 += 6;  
                cy1 += 8;  
                // ���cx1��cy1�Ƴ���Ļ������Ͻ����¿�ʼ  
                if (cx1 > 320)  
                    cx1 = 15;  
                if (cy1 > 400)  
                    cy1 = 20;  
                  
                  
                cx2 -= 6;  
                cy2 -= 8;  
                // ���cx2��cy2�Ƴ���Ļ������½����¿�ʼ  
                if (cx2 <15)  
                    cx2 = 300;  
                if (cy2 <20)  
                    cy2 = 380;  
                  
                  
                cx3 -= 6;  
                cy3 += 8;  
                // ���cx3��cy3�Ƴ���Ļ������Ͻ����¿�ʼ  
                if (cx3 <0)  
                    cx3 = 300;  
                if (cy3 >400)  
                    cy3 = 20;  
                  
                  
                cx4 += 6;  
                cy4 -= 8;  
                // ���cx4��cy4�Ƴ���Ļ������½����¿�ʼ  
                if (cx4 >320)  
                    cx4 = 15;  
                if (cy4 <0)  
                    cy4 = 380;  
                  
                // ָ��0.1�������ִ��mDrawCubeһ��  
                mHandler.postDelayed(drawTarget, 100);  
            }  
        }  
  
        // ����Ļ���������ԲȦ  
        private void drawTouchPoint(Canvas c)  
        {  
            if (mTouchX >= 0 && mTouchY >= 0)  
            {  
                c.drawCircle(mTouchX, mTouchY, 40, mPaint);  
            }  
        }  
    }  
} 
