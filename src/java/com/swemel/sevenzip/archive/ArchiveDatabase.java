package com.swemel.sevenzip.archive;

import com.swemel.sevenzip.Folder;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public final class ArchiveDatabase {
    private final List<Long> packSizes = new ArrayList<Long>();
    private List<Boolean> packCRCsDefined = new ArrayList<Boolean>();
    private List<Integer> packCRCs = new ArrayList<Integer>();
    private List<Folder> folders = new ArrayList<Folder>();
    private List<Integer> numUnPackStreamsVector = new ArrayList<Integer>();
    private List<FileItem> files = new ArrayList<FileItem>();
    private final List<Boolean> isAnti = new ArrayList<Boolean>();
    private final List<Long> mTimes = new ArrayList<Long>();
    private final List<Boolean> mTimesDefined = new ArrayList<Boolean>();

    public void clear() {
        packSizes.clear();
        packCRCsDefined.clear();
        packCRCs.clear();
        folders.clear();
        numUnPackStreamsVector.clear();
        files.clear();
    }


    public List<Long> getMTimes() {
        return mTimes;
    }

    public List<Boolean> getMTimesDefined() {
        return mTimesDefined;
    }

    public void addMTime(long mTime) {
        mTimes.add(mTime);
    }

    public void addMTimeDefined(boolean mTimeDefined) {
        mTimesDefined.add(mTimeDefined);
    }


    boolean isEmpty() {
        return (packSizes.isEmpty() &&
                packCRCsDefined.isEmpty() &&
                packCRCs.isEmpty() &&
                folders.isEmpty() &&
                numUnPackStreamsVector.isEmpty() &&
                files.isEmpty());
    }

    public void addFolder(Folder f) {
        folders.add(f);
    }

    public List<Long> getPackSizes() {
        return packSizes;
    }

    public void addPackSize(long packSize) {
        packSizes.add(packSize);
    }

    public List<Boolean> getPackCRCsDefined() {
        return packCRCsDefined;
    }

    public void setPackCRCsDefined(Vector<Boolean> packCRCsDefined) {
        this.packCRCsDefined = packCRCsDefined;
    }

    public List<Integer> getPackCRCs() {
        return packCRCs;
    }

    public void setPackCRCs(Vector<Integer> packCRCs) {
        this.packCRCs = packCRCs;
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public void setFolders(Vector<Folder> folders) {
        this.folders = folders;
    }

    public List<Integer> getNumUnPackStreamsVector() {
        return numUnPackStreamsVector;
    }

    public void setNumUnPackStreamsVector(Vector<Integer> numUnPackStreamsVector) {
        this.numUnPackStreamsVector = numUnPackStreamsVector;
    }

    public List<FileItem> getFiles() {
        return files;
    }

    public void setFiles(Vector<FileItem> files) {
        this.files = files;
    }

    public void addFile(FileItem file) {
        files.add(file);
    }

    public boolean isItemAnti(int i) {
        return (i < isAnti.size() && isAnti.get(i));
    }
}
