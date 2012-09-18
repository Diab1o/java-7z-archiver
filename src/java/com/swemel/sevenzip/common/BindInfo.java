package com.swemel.sevenzip.common;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 03.02.2011
 * Time: 17:00:39
 * To change this template use File | Settings | File Templates.
 */
public class BindInfo {
    private Vector<CoderStreamsInfo> coders;
    private Vector<BindPair> bindPairs;
    private Vector<Integer> inStreams;
    private Vector<Integer> outStreams;

    public Vector<CoderStreamsInfo> getCoders() {
        return coders;
    }

    public void setCoders(Vector<CoderStreamsInfo> coders) {
        this.coders = coders;
    }

    public Vector<BindPair> getBindPairs() {
        return bindPairs;
    }

    public void setBindPairs(Vector<BindPair> bindPairs) {
        this.bindPairs = bindPairs;
    }

    public Vector<Integer> getInStreams() {
        return inStreams;
    }

    public void setInStreams(Vector<Integer> inStreams) {
        this.inStreams = inStreams;
    }

    public Vector<Integer> getOutStreams() {
        return outStreams;
    }

    public void setOutStreams(Vector<Integer> outStreams) {
        this.outStreams = outStreams;
    }

    void Clear() {
        coders.clear();
        bindPairs.clear();
        inStreams.clear();
        outStreams.clear();
    }


    int getNumInStreams() {
        int numInStreams = 0;
        for (int i = 0; i < coders.size(); i++) {
            CoderStreamsInfo coderStreamsInfo = coders.get(i);
            numInStreams += coderStreamsInfo.getNumInStreams();
        }
        return numInStreams;
    }

    int getNumOutStreams() {
        int getNumOutStreams = 0;
        for (int i = 0; i < coders.size(); i++) {
            CoderStreamsInfo coderStreamsInfo = coders.get(i);
            getNumOutStreams += coderStreamsInfo.getNumOutStreams();
        }
        return getNumOutStreams;
    }

    int findBinderForInStream(int inStream) {
        for (int i = 0; i < bindPairs.size(); i++)
            if (bindPairs.get(i).getInIndex() == inStream)
                return i;
        return -1;
    }

    int findBinderForOutStream(int outStream) {
        for (int i = 0; i < bindPairs.size(); i++)
            if (bindPairs.get(i).getOutIndex() == outStream)
                return i;
        return -1;
    }

    int getCoderInStreamIndex(int coderIndex) {
        int streamIndex = 0;
        for (Integer i = 0; i < coderIndex; i++)
            streamIndex += coders.get(i).getNumInStreams();
        return streamIndex;
    }

    int getCoderOutStreamIndex(int coderIndex) {
        int streamIndex = 0;
        for (Integer i = 0; i < coderIndex; i++)
            streamIndex += coders.get(i).getNumOutStreams();
        return streamIndex;
    }


    int findInStream(int streamIndex, int coderIndex) {
        for (coderIndex = 0; coderIndex <  coders.size(); coderIndex++) {
            Integer curSize = coders.get(coderIndex).getNumInStreams();
            if (streamIndex < curSize) {
                return streamIndex;
            }
            streamIndex -= curSize;
        }
        throw new UnknownError("1");
    }

    int findOutStream(int streamIndex, int coderIndex) {
        for (coderIndex = 0; coderIndex <  coders.size(); coderIndex++) {
            Integer curSize = coders.get(coderIndex).getNumOutStreams();
            if (streamIndex < curSize) {
                return streamIndex;
            }
            streamIndex -= curSize;
        }
        throw new UnknownError("1");
    }
}
