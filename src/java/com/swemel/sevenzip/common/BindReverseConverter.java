package com.swemel.sevenzip.common;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 15.02.2011
 * Time: 15:06:13
 * To change this template use File | Settings | File Templates.
 */
public class BindReverseConverter {
    private int numSrcOutStreams;
    private BindInfo srcBindInfo;
    private Vector<Integer> srcInToDestOutMap;
    private Vector<Integer> srcOutToDestInMap;
    private Vector<Integer> destInToSrcOutMap;

    private int numSrcInStreams;
    Vector<Integer> destOutToSrcInMap;

    public Vector<Integer> getDestOutToSrcInMap() {
        return destOutToSrcInMap;
    }

    public void setDestOutToSrcInMap(Vector<Integer> destOutToSrcInMap) {
        this.destOutToSrcInMap = destOutToSrcInMap;
    }

    public int getNumSrcInStreams() {
        return numSrcInStreams;
    }

    public void setNumSrcInStreams(int numSrcInStreams) {
        numSrcInStreams = numSrcInStreams;
    }

    public BindReverseConverter(BindInfo srcBindInfo) {
        numSrcInStreams = srcBindInfo.getNumInStreams();
        numSrcOutStreams = srcBindInfo.getNumOutStreams();
        for (int j = 0; j < numSrcInStreams; j++) {
            srcInToDestOutMap.add(0);
            destOutToSrcInMap.add(0);
        }
        for (int j = 0; j < numSrcOutStreams; j++) {
            srcOutToDestInMap.add(0);
            destInToSrcOutMap.add(0);
        }

        int destInOffset = 0;
        int destOutOffset = 0;
        int srcInOffset = numSrcInStreams;
        int srcOutOffset = numSrcOutStreams;

        for (int i = srcBindInfo.getCoders().size() - 1; i >= 0; i--) {
            CoderStreamsInfo srcCoderInfo = srcBindInfo.getCoders().get(i);

            srcInOffset -= srcCoderInfo.getNumInStreams();
            srcOutOffset -= srcCoderInfo.getNumOutStreams();

            for (int j = 0; j < srcCoderInfo.getNumInStreams(); j++, destOutOffset++) {
                int index = srcInOffset + j;
                srcInToDestOutMap.set(index, destOutOffset);
                destOutToSrcInMap.set(destOutOffset, index);
            }
            for (int j = 0; j < srcCoderInfo.getNumOutStreams(); j++, destInOffset++) {
                int index = srcOutOffset + j;
                srcOutToDestInMap.set(index, destInOffset);
                destInToSrcOutMap.set(destInOffset, index);
            }
        }
    }

    public void createReverseBindInfo(BindInfo destBindInfo) {
        destBindInfo.getCoders().clear();
        destBindInfo.getBindPairs().clear();
        destBindInfo.getInStreams().clear();
        destBindInfo.getOutStreams().clear();

        for (int i = srcBindInfo.getCoders().size() - 1; i >= 0; i--) {
            CoderStreamsInfo srcCoderInfo = srcBindInfo.getCoders().get(i);
            CoderStreamsInfo destCoderInfo=new CoderStreamsInfo();
            destCoderInfo.setNumInStreams(srcCoderInfo.getNumOutStreams());
            destCoderInfo.setNumOutStreams(srcCoderInfo.getNumInStreams());
            destBindInfo.getCoders().add(destCoderInfo);
        }
        for (int i = srcBindInfo.getBindPairs().size() - 1; i >= 0; i--) {
            BindPair srcBindPair = srcBindInfo.getBindPairs().get(i);
            BindPair destBindPair=new BindPair();
            destBindPair.setInIndex(srcOutToDestInMap.get(srcBindPair.getOutIndex()));
            destBindPair.setOutIndex(srcInToDestOutMap.get(srcBindPair.getInIndex()));
            destBindInfo.getBindPairs().add(destBindPair);
        }
        for (int i = 0; i < srcBindInfo.getInStreams().size(); i++)
            destBindInfo.getOutStreams().add(srcInToDestOutMap.get(srcBindInfo.getInStreams().get(i)));
        for (int i = 0; i < srcBindInfo.getOutStreams().size(); i++)
            destBindInfo.getInStreams().add(srcOutToDestInMap.get(srcBindInfo.getOutStreams().get(i)));

    }
}
