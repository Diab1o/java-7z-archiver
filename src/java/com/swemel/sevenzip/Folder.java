package com.swemel.sevenzip;

import com.swemel.sevenzip.common.BindPair;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 15.02.2011
 * Time: 16:23:40
 * To change this template use File | Settings | File Templates.
 */

public class Folder {
    private Vector<CoderInfo> coders=new Vector<CoderInfo>();
    private Vector<BindPair> bindPairs=new Vector<BindPair>();
    private Vector<Integer> packStreams=new Vector<Integer>();
    private Vector<Long> unpackSizes=new Vector<Long>();
    int unpackCRC;
    boolean unpackCRCDefined=false;

    public int getUnpackCRC() {
        return unpackCRC;
    }

    public void setUnpackCRC(int unpackCRC) {
        this.unpackCRC = unpackCRC;
    }

    public boolean isUnpackCRCDefined() {
        return unpackCRCDefined;
    }

    public void setUnpackCRCDefined(boolean unpackCRCDefined) {
        this.unpackCRCDefined = unpackCRCDefined;
    }

    public void addUnpackSize(long size)
    {
        unpackSizes.add(size);
    }


    public Vector<CoderInfo> getCoders() {
        return coders;
    }

    public void setCoders(Vector<CoderInfo> coders) {
        this.coders = coders;
    }

    public Vector<BindPair> getBindPairs() {
        return bindPairs;
    }

    public void setBindPairs(Vector<BindPair> bindPairs) {
        this.bindPairs = bindPairs;
    }

    public Vector<Integer> getPackStreams() {
        return packStreams;
    }

    public void setPackStreams(Vector<Integer> packStreams) {
        this.packStreams = packStreams;
    }

    public Vector<Long> getUnpackSizes() {
        return unpackSizes;
    }

    public void setUnpackSizes(Vector<Long> unpackSizes) {
        this.unpackSizes = unpackSizes;
    }

    Folder(){
        coders=new Vector<CoderInfo>();
        bindPairs=new Vector<BindPair>();
        packStreams=new Vector<Integer>();
        unpackSizes=new Vector<Long>();
    }

    public long getUnpackSize(){
        if (unpackSizes.isEmpty())
            return 0;
        for (int i = unpackSizes.size() - 1; i >= 0; i--)
            if (findBindPairForOutStream(i) < 0)
                return unpackSizes.get(i);
        throw new UnknownError("1");
    }

    public int getNumOutStreams(){
        int result = 0;
        for (int i = 0; i < coders.size(); i++)
            result += coders.get(i).getNumOutStreams();
        return result;
    }

    public int findBindPairForInStream(int inStreamIndex)
    {
        for (int i = 0; i < bindPairs.size(); i++)
            if (bindPairs.get(i).getInIndex() == inStreamIndex)
                return i;
        return -1;
    }

    public int findBindPairForOutStream(int outStreamIndex)
    {
        for (int i = 0; i < bindPairs.size(); i++)
            if (bindPairs.get(i).getOutIndex() == outStreamIndex)
                return i;
        return -1;
    }

    public int findPackStreamArrayIndex(int inStreamIndex)
    {
        for (int i = 0; i < packStreams.size(); i++)
            if (packStreams.get(i) == inStreamIndex)
                return i;
        return -1;
    }

    public boolean isEncrypted()
    {
        for (int i = coders.size() - 1; i >= 0; i--)
            if (coders.get(i).getMethodID() == 0x06F10701)
                return true;
        return false;
    }

    //boolean CheckStructure();   todo ?
}
