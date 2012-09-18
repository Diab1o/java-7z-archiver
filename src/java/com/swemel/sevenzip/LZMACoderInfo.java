package com.swemel.sevenzip;

import com.swemel.common.ByteBuffer;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 12.04.2011
 * Time: 15:40:58
 * To change this template use File | Settings | File Templates.
 */
public class LZMACoderInfo extends CoderInfo{
    private long dictionarySize=1 << 16;

    @Override
    public ByteBuffer getProps() {
        ByteBuffer buf = new ByteBuffer();
        buf.setCapacity(5);
        byte[] properties=buf.data();
        properties[0] = (byte) (93);
        for (int i = 0; i < 4; i++)
            properties[1 + i] = (byte) (dictionarySize >> (8 * i));
        return buf;
    }

    public void setDictionarySize(long size)
    {
        dictionarySize=size;
    }

    @Override
    public boolean isSimpleCoder() {
        return true; 
    }

    @Override
    public long getMethodID() {
        return 0x030101;
    }

    @Override
    public int getNumInStreams() {
        return 1;
    }

    @Override
    public int getNumOutStreams() {
        return 1;
    }
}
