package com.swemel.sevenzip;

import com.swemel.sevenzip.common.BindPair;

import java.util.ArrayList;
import java.util.List;


public class Folder {
    private List<CoderInfo> coders = new ArrayList<CoderInfo>();
    private List<BindPair> bindPairs = new ArrayList<BindPair>();
    private List<Integer> packStreams = new ArrayList<Integer>();
    private List<Long> unpackSizes = new ArrayList<Long>();
    private int unpackCRC;
    private boolean unpackCRCDefined = false;

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

    public void addUnpackSize(long size) {
        unpackSizes.add(size);
    }


    public List<CoderInfo> getCoders() {
        return coders;
    }

    public void setCoders(List<CoderInfo> coders) {
        this.coders = coders;
    }

    public List<BindPair> getBindPairs() {
        return bindPairs;
    }

    public void setBindPairs(List<BindPair> bindPairs) {
        this.bindPairs = bindPairs;
    }

    public List<Integer> getPackStreams() {
        return packStreams;
    }

    public void setPackStreams(List<Integer> packStreams) {
        this.packStreams = packStreams;
    }

    public List<Long> getUnpackSizes() {
        return unpackSizes;
    }

    public void setUnpackSizes(List<Long> unpackSizes) {
        this.unpackSizes = unpackSizes;
    }

    public long getUnpackSize() {
        if (unpackSizes.isEmpty())
            return 0;
        for (int i = unpackSizes.size() - 1; i >= 0; i--)
            if (findBindPairForOutStream(i) < 0)
                return unpackSizes.get(i);
        throw new UnknownError("1");
    }

    public int getNumOutStreams() {
        int result = 0;
        for (CoderInfo coder : coders) {
            result += coder.getNumOutStreams();
        }
        return result;
    }

    public int findBindPairForInStream(int inStreamIndex) {
        for (int i = 0; i < bindPairs.size(); i++)
            if (bindPairs.get(i).getInIndex() == inStreamIndex)
                return i;
        return -1;
    }

    int findBindPairForOutStream(int outStreamIndex) {
        for (int i = 0; i < bindPairs.size(); i++)
            if (bindPairs.get(i).getOutIndex() == outStreamIndex)
                return i;
        return -1;
    }

    public int findPackStreamArrayIndex(int inStreamIndex) {
        for (int i = 0; i < packStreams.size(); i++)
            if (packStreams.get(i) == inStreamIndex)
                return i;
        return -1;
    }

    public boolean isEncrypted() {
        for (int i = coders.size() - 1; i >= 0; i--)
            if (coders.get(i).getMethodID() == 0x06F10701)
                return true;
        return false;
    }
}
