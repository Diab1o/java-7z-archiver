package com.swemel.sevenzip.archive;

import com.swemel.sevenzip.common.Method;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 10.02.2011
 * Time: 18:27:36
 * To change this template use File | Settings | File Templates.
 */
public class MethodFull extends Method {
    private int numInStreams;
    private int numOutStreams;

    boolean IsSimpleCoder() {
        return (numInStreams == 1) && (numOutStreams == 1);
    }

    public int getNumInStreams() {
        return numInStreams;
    }

    public void setNumInStreams(int numInStreams) {
        this.numInStreams = numInStreams;
    }

    public int getNumOutStreams() {
        return numOutStreams;
    }

    public void setNumOutStreams(int numOutStreams) {
        this.numOutStreams = numOutStreams;
    }
}
