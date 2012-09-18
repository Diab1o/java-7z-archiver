package com.swemel.sevenzip.archive;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 12.04.2011
 * Time: 14:53:49
 * To change this template use File | Settings | File Templates.
 */
public class StartHeader {
    private long nextHeaderOffset;
    private long nextHeaderSize;
    private int nextHeaderCRC;

    public long getNextHeaderOffset() {
        return nextHeaderOffset;
    }

    public void setNextHeaderOffset(long nextHeaderOffset) {
        this.nextHeaderOffset = nextHeaderOffset;
    }

    public long getNextHeaderSize() {
        return nextHeaderSize;
    }

    public void setNextHeaderSize(long nextHeaderSize) {
        this.nextHeaderSize = nextHeaderSize;
    }

    public int getNextHeaderCRC() {
        return nextHeaderCRC;
    }

    public void setNextHeaderCRC(int nextHeaderCRC) {
        this.nextHeaderCRC = nextHeaderCRC;
    }
}
