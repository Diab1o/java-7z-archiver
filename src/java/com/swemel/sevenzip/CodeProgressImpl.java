package com.swemel.sevenzip;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 04.04.2011
 * Time: 19:38:13
 * To change this template use File | Settings | File Templates.
 */
public class CodeProgressImpl implements ICodeProgress{

    private long totalInSize=0;
    private long outSize=0;
    private long inSize=0;
    private Integer progress;
    private long complexity;

    public CodeProgressImpl(long complexity) {
        this.complexity=complexity;
    }

    public void setProgress(long inSize, long outSize) {
        this.inSize=inSize;
        this.outSize=outSize;
        progress=(int)(((double)(totalInSize+inSize)/(double)complexity)*100.0);
    }

    public long getOutSize() {
        return outSize;
    }

    public long getInSize() {
        return inSize;
    }

    public void setTotalInSize() {
        this.totalInSize += getInSize();
    }

    public int getProgress() {
        return progress;
    }
}