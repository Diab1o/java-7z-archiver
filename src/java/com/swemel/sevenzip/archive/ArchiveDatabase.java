package com.swemel.sevenzip.archive;

import com.swemel.sevenzip.Folder;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 24.02.2011
 * Time: 18:13:46
 * To change this template use File | Settings | File Templates.
 */
public class ArchiveDatabase {
    protected Vector<Long> packSizes = new Vector<Long>();
    protected Vector<Boolean> packCRCsDefined = new Vector<Boolean>();
    protected Vector<Integer> packCRCs = new Vector<Integer>();
    protected Vector<Folder> folders = new Vector<Folder>();
    protected Vector<Integer> numUnPackStreamsVector = new Vector<Integer>();
    protected Vector<FileItem> files = new Vector<FileItem>();
    protected Vector<Boolean> isAnti = new Vector<Boolean>();
    protected Vector<Long> mTimes = new Vector<Long>();
    protected Vector<Boolean> mTimesDefined = new Vector<Boolean>();

    void Clear() {
        packSizes.clear();
        packCRCsDefined.clear();
        packCRCs.clear();
        folders.clear();
        numUnPackStreamsVector.clear();
        files.clear();
    }


    public Vector<Long> getMTimes() {
        return mTimes;
    }

    public Vector<Boolean> getMTimesDefined() {
        return mTimesDefined;
    }

    public void addMTime(long mTime)
    {
        mTimes.add(mTime);
    }

    public void addMTimeDefined(boolean mTimeDefined)
    {
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

    public Vector<Long> getPackSizes() {
        return packSizes;
    }

    public void addPackSize(long packSize) {
        packSizes.add(packSize);
    }

    public Vector<Boolean> getPackCRCsDefined() {
        return packCRCsDefined;
    }

    public void setPackCRCsDefined(Vector<Boolean> packCRCsDefined) {
        this.packCRCsDefined = packCRCsDefined;
    }

    public Vector<Integer> getPackCRCs() {
        return packCRCs;
    }

    public void setPackCRCs(Vector<Integer> packCRCs) {
        this.packCRCs = packCRCs;
    }

    public Vector<Folder> getFolders() {
        return folders;
    }

    public void setFolders(Vector<Folder> folders) {
        this.folders = folders;
    }

    public Vector<Integer> getNumUnPackStreamsVector() {
        return numUnPackStreamsVector;
    }

    public void setNumUnPackStreamsVector(Vector<Integer> numUnPackStreamsVector) {
        this.numUnPackStreamsVector = numUnPackStreamsVector;
    }

    public Vector<FileItem> getFiles() {
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
