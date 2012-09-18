package com.swemel.sevenzip;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 03.02.2011
 * Time: 17:53:06
 * To change this template use File | Settings | File Templates.
 */
public interface ICompressProgressInfo {
    public static final long INVALID = -1;
    int SetRatioInfo(long inSize, long outSize);
}
