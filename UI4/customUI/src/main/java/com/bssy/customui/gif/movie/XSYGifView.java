package com.bssy.customui.gif.movie;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import java.io.InputStream;

public class XSYGifView extends View implements XSYGifAction
{
    private XSYGifDecoder gifDecoder = null;
    private Bitmap currentImage = null;
    private DrawThread drawThread = null;
    private GifImageType animationType = GifImageType.SYNC_DECODER;
    private boolean isRun = true;
    private boolean pause = false;
    private int showWidth = -1;
    private int showHeight = -1;
    private Rect rect = null;


    private Handler redrawHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {          
            XSYGifView.this.invalidate();//���½���handler
        }
    };

    
    public XSYGifView(Context context)
    {
        super(context);
    }

    public XSYGifView(Context context, AttributeSet attrs)
    {
        this(context,attrs,0);
    }

    public XSYGifView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context,attrs,defStyle);
    }

    /**
     * ����GIF����
     * @param gif �ֽ�������
     */
    private void setGifDecoderImage(byte[] gif)
    {
        if (this.gifDecoder != null)
        {
            this.gifDecoder.free();
            this.gifDecoder = null;
        }
        this.gifDecoder = new XSYGifDecoder(gif, this);
        this.gifDecoder.start();
    }

    
    /**
     * ����GIF����
     * @param is �ļ���
     */
    private void setGifDecoderImage(InputStream is)
    {
        if (this.gifDecoder != null)
        {
            this.gifDecoder.free();
            this.gifDecoder = null;
        }
        this.gifDecoder = new XSYGifDecoder(is, this);
        this.gifDecoder.start();
    }

    /**
     * ����GIFͼƬ
     * 
     * @param gif �ֽ�������
     */
    public void setGifImage(byte[] gif)
    {
        setGifDecoderImage(gif);
    }

    /**
     * ����GIFͼƬ
     * 
     * @param is �ļ���
     */
    public void setGifImage(InputStream is)
    {
        setGifDecoderImage(is);
    }

    /**
     * ����GIFͼƬ
     * 
     * @param resId ��ԴID
     */
    public void setGifImage(int resId)
    {
        Resources r = getResources();
        InputStream is = r.openRawResource(resId);
        setGifDecoderImage(is);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
                
        if (this.gifDecoder == null)
        {
            return;
        }
        if (this.currentImage == null)
        {
            this.currentImage = this.gifDecoder.getImage();
        }
        
        if (this.currentImage == null)
        {
            return;
        }
        int saveCount = canvas.getSaveCount();
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());

        if (this.showWidth == -1 && this.showHeight == -1)
        {
            canvas.drawBitmap(this.currentImage, 0.0F, 0.0F, null);//Ĭ�ϳߴ�           
        }
        else
        {
            canvas.drawBitmap(this.currentImage, null, this.rect, null);//�����趨�ߴ�
        }
        
        canvas.restoreToCount(saveCount);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int pleft = getPaddingLeft();
        int pright = getPaddingRight();
        int ptop = getPaddingTop();
        int pbottom = getPaddingBottom();
        int h;
        int w;
        
        if (this.gifDecoder == null)
        {
            w = 1;
            h = 1;
        }
        else
        {
            w = this.gifDecoder.width;
            h = this.gifDecoder.height;
        }

        
        float widthHeight=(float)w/(float)h;//ͼƬ��߱�
        float heightWidth=(float)h/(float)w;//ͼƬ�߿��
        
        w += pleft + pright;
        h += ptop + pbottom;
        

        w = Math.max(w, getSuggestedMinimumWidth());
        h = Math.max(h, getSuggestedMinimumHeight());
        
       

        int widthSize = resolveSize(w, widthMeasureSpec);
        int heightSize = resolveSize(h, heightMeasureSpec);
        
        //�����Ļ��С�����»�ȡ���ָ߿�ȵ�View��С
        if(widthSize<w)
        {
            heightSize=(int) (widthSize*heightWidth);
        }
        else if(heightSize<h)
        {
            widthSize=(int)(heightSize*widthHeight);
        }
        
        setShowDimension(widthSize,heightSize);
        //�����Ļ��С�����»�ȡ���ָ߿�ȵ�View��С
        
        
        setMeasuredDimension(widthSize, heightSize);
    }

    
    
    public void showCover()
    {
        if (this.gifDecoder == null)
            return;
        this.pause = true;
        this.currentImage = this.gifDecoder.getImage();
        invalidate();
    }

    /**
     * ��������
     */
    public void startAnimation()
    {
        if (this.pause)
            this.pause = false;
    }

    /**
     * ���ö�������
     * @param type
     */
    public void setGifImageType(GifImageType type)
    {
        if (this.gifDecoder == null)
            this.animationType = type;
    }

    /**
     * ����GIFͼ����ʾ�ߴ�
     * @param width
     * @param height
     */
    public void setShowDimension(int width, int height)
    {
        if ((width > 0) && (height > 0))
        {
            this.showWidth = width;
            this.showHeight = height;
            this.rect = new Rect();
            this.rect.left = 0;
            this.rect.top = 0;
            this.rect.right = width;
            this.rect.bottom = height;
        }
    }

    
    /**
     * ����GIFͼƬ�߳�
     */
    public void parseOk(boolean parseStatus, int frameIndex)
    {

        if (parseStatus)
        {
            if (this.gifDecoder != null)
            {

                switch (this.animationType.ordinal())
                {

                    case 1:

                        if (frameIndex != -1)
                            break;
                        if (this.gifDecoder.getFrameCount() > 1)
                        {
                            if (this.drawThread == null)
                            {
                                this.drawThread = new DrawThread();
                                this.drawThread.start();
                            }
                        }
                        else
                        {
                            reDraw();
                        }

                        break;
                    case 2:
                        
                        if (frameIndex == 1)
                        {
                            this.currentImage = this.gifDecoder.getImage();
                            reDraw();
                        }
                        else if (frameIndex == -1)
                        {
                            if (this.gifDecoder.getFrameCount() > 1)
                            {
                                if (this.drawThread == null)
                                {
                                    this.drawThread = new DrawThread();
                                    this.drawThread.start();
                                }
                            }
                            else
                                reDraw();
                        }
                        
                        break;
                    case 3:

                        if (frameIndex == 1)
                        {
                            this.currentImage = this.gifDecoder.getImage();
                            reDraw();
                        }
                        else if (frameIndex == -1)
                        {
                            if (this.gifDecoder.getFrameCount() > 1)
                            {
                                if (this.drawThread == null)
                                {
                                    this.drawThread = new DrawThread();
                                    this.drawThread.start();
                                }
                            }
                            else
                                reDraw();
                        }

                        break;
                    default:
                        break;
                }
            }
            else
            {
                Log.e("gif", "parse error");
            }
        }
    }

    
    /**
     * �ػ����
     */
    private void reDraw()
    {
        if (this.redrawHandler != null)
        {
            Message msg = this.redrawHandler.obtainMessage();
            this.redrawHandler.sendMessage(msg);
        }
    }

    
    /**
     * �����߳�
     * @author czh
     *
     */
    private class DrawThread extends Thread
    {

        public void run()
        {
            if (XSYGifView.this.gifDecoder == null)
            {
                return;
            }
            
            while (XSYGifView.this.isRun)
            {
                if (!XSYGifView.this.pause)
                {
                    XSYGifFrame frame = XSYGifView.this.gifDecoder.next();//��ȡGIF֡
                    if(frame==null) break;
                    XSYGifView.this.currentImage = frame.image;//֡ͼ��
                    long sp = frame.delay;//��ȡ�������ʱ��
                    if (XSYGifView.this.redrawHandler == null) break;
                    Message msg = XSYGifView.this.redrawHandler.obtainMessage();
                    XSYGifView.this.redrawHandler.sendMessage(msg);//ˢ�¶���
                    SystemClock.sleep(sp);//�ȴ���ʱ��
                }
                else
                {
                    SystemClock.sleep(10L);//������ͣ
                }
            }
        }
    }

    
    /**
     * ����ͼ������
     * @author czh
     *
     */
    public static enum GifImageType
    {
        WAIT_FINISH(0),

        SYNC_DECODER(1),

        COVER(2);

        final int nativeInt;

        private GifImageType(int i)
        {
            this.nativeInt = i;
        }

    }

    /**
     * �ͷŻ��涯������
     */
    public void clean()
    {

        if(this.drawThread!=null&&this.drawThread.isAlive())
        {
            this.drawThread.interrupt();
        }
        
        if(this.gifDecoder!=null)
        {
            for(int i=0;i<gifDecoder.getFrameCount();i++)
            {
                Bitmap bmp=gifDecoder.getFrameImage(i);
                
                if(bmp!=null&&!bmp.isRecycled())
                {
                    bmp.recycle();
                    System.gc();
                }
            }
            
            this.gifDecoder.free();
        }
        
        isRun=false;
        this.drawThread=null;
        this.gifDecoder=null;

    }
}
