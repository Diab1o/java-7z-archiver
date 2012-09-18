package com.swemel.sevenzip;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 24.02.2011
 * Time: 18:01:30
 * To change this template use File | Settings | File Templates.
 */
public abstract class IInStream extends java.io.InputStream
{
  static public final int STREAM_SEEK_SET	= 0;
  static public final int STREAM_SEEK_CUR	= 1;
  // static public final int STREAM_SEEK_END	= 2;
  public abstract long Seek(long offset, int seekOrigin)  throws java.io.IOException ;
}