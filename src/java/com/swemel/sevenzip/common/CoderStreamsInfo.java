package com.swemel.sevenzip.common;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 03.02.2011
 * Time: 17:24:01
 * To change this template use File | Settings | File Templates.
 */
public class CoderStreamsInfo {
    private int NumInStreams;
    private int NumOutStreams;

    public int getNumInStreams() {
        return NumInStreams;
    }

    public void setNumInStreams(int numInStreams) {
        NumInStreams = numInStreams;
    }

    public int getNumOutStreams() {
        return NumOutStreams;
    }

    public void setNumOutStreams(int numOutStreams) {
        NumOutStreams = numOutStreams;
    }
}
