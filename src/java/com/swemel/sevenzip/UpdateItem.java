package com.swemel.sevenzip;


public class UpdateItem {

    private int indexInArchive;
    private int indexInClient;

    private long cTime;
    private long aTime;
    private long mTime;

    private long size;
    private String name;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private String fullName;

    private int attrib;

    private boolean newData;
    private boolean newProps;

    private boolean isAnti = false;
    private boolean isDir = false;

    private boolean attribDefined = false;
    private boolean cTimeDefined = false;
    private boolean aTimeDefined = false;
    private boolean mTimeDefined = false;

    public int getIndexInArchive() {
        return indexInArchive;
    }

    public void setIndexInArchive(int indexInArchive) {
        this.indexInArchive = indexInArchive;
    }

    public int getIndexInClient() {
        return indexInClient;
    }

    public void setIndexInClient(int indexInClient) {
        this.indexInClient = indexInClient;
    }

    public long getcTime() {
        return cTime;
    }

    public void setcTime(long cTime) {
        this.cTime = cTime;
    }

    public long getaTime() {
        return aTime;
    }

    public void setaTime(long aTime) {
        this.aTime = aTime;
    }

    public long getmTime() {
        return mTime;
    }

    public void setmTime(long mTime) {
        this.mTime = mTime;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAttrib() {
        return attrib;
    }

    public void setAttrib(int attrib) {
        this.attrib = attrib;
    }

    public boolean isNewData() {
        return newData;
    }

    public void setNewData(boolean newData) {
        this.newData = newData;
    }

    public boolean isNewProps() {
        return newProps;
    }

    public void setNewProps(boolean newProps) {
        this.newProps = newProps;
    }

    public boolean isAnti() {
        return isAnti;
    }

    public void setIsAnti(boolean isAnti) {
        this.isAnti = isAnti;
    }

    public boolean isDir() {
        return isDir;
    }

    public void setIsDir(boolean isDir) {
        this.isDir = isDir;
    }

    public boolean isAttribDefined() {
        return attribDefined;
    }

    public void setAttribDefined(boolean attribDefined) {
        this.attribDefined = attribDefined;
    }

    public boolean iscTimeDefined() {
        return cTimeDefined;
    }

    public void setcTimeDefined(boolean cTimeDefined) {
        this.cTimeDefined = cTimeDefined;
    }

    public boolean isaTimeDefined() {
        return aTimeDefined;
    }

    public void setaTimeDefined(boolean aTimeDefined) {
        this.aTimeDefined = aTimeDefined;
    }

    public boolean ismTimeDefined() {
        return mTimeDefined;
    }

    public void setmTimeDefined(boolean mTimeDefined) {
        this.mTimeDefined = mTimeDefined;
    }

    public boolean hasStream() {
        return !isDir && !isAnti && size != 0;
    }

}
