package com.swemel.sevenzip;

import java.util.ArrayList;
import java.util.List;


public class Folder {
    private final List<Long> unpackSizes = new ArrayList<>();
    private final List<CoderInfo> coders = new ArrayList<>();

    public void addUnpackSize(long size) {
        unpackSizes.add(size);
    }

    public List<Long> getUnpackSizes() {
        return unpackSizes;
    }

    public List<CoderInfo> getCoders() {
        return coders;
    }
}
