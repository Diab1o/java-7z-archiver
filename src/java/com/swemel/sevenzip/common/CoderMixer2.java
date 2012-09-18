package com.swemel.sevenzip.common;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 03.02.2011
 * Time: 16:53:26
 * To change this template use File | Settings | File Templates.
 */
public interface CoderMixer2 {
    public long SetBindInfo(BindInfo bindInfo);

    public void ReInit();

    public void SetCoderInfo(int coderIndex, Vector<Long> inSizes, Vector<Long> outSizes);
}
