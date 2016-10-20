package com.bssy.customui.gif.movie;

import android.graphics.Bitmap;

public class XSYGifFrame
{
  public Bitmap image;
  public int delay;
  public XSYGifFrame nextFrame = null;

  public XSYGifFrame(Bitmap im, int del)
  {
    this.image = im;
    this.delay = del;
  }
}
