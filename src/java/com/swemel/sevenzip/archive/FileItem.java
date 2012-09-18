package com.swemel.sevenzip.archive;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 24.02.2011
 * Time: 18:15:30
 * To change this template use File | Settings | File Templates.
 */
public class FileItem {
    private long creationTime;
    private long lastWriteTime;
    private long lastAccessTime;

    private long unPackSize;
    private long startPos;
    private int attributes;
    private int fileCRC;

    private boolean directory;
    private boolean anti;
    private boolean fileCRCDefined;
    private boolean attributesDefined;
    private boolean hasStream;
    private long size;

    private boolean crcDefined;

    public boolean isCrcDefined() {
        return crcDefined;
    }

    public void setCrcDefined(boolean crcDefined) {
        this.crcDefined = crcDefined;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
    // public boolean IsCreationTimeDefined; replace by (CreationTime != 0)
    // public boolean IsLastWriteTimeDefined; replace by (LastWriteTime != 0)
    // public boolean IsLastAccessTimeDefined; replace by (LastAccessTime != 0)
    private boolean IsStartPosDefined;

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public long getLastWriteTime() {
        return lastWriteTime;
    }

    public void setLastWriteTime(long lastWriteTime) {
        this.lastWriteTime = lastWriteTime;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public long getUnPackSize() {
        return unPackSize;
    }

    public void setUnPackSize(long unPackSize) {
        this.unPackSize = unPackSize;
    }

    public long getStartPos() {
        return startPos;
    }

    public void setStartPos(long startPos) {
        this.startPos = startPos;
    }

    public int getAttributes() {
        return attributes;
    }

    public void setAttributes(int attributes) {
        this.attributes = attributes;
    }

    public int getFileCRC() {
        return fileCRC;
    }

    public void setFileCRC(int fileCRC) {
        this.fileCRC = fileCRC;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public boolean isAnti() {
        return anti;
    }

    public void setAnti(boolean isAnti) {
        this.anti = isAnti;
    }

    public boolean isFileCRCDefined() {
        return fileCRCDefined;
    }

    public void setFileCRCDefined(boolean isFileCRCDefined) {
        this.fileCRCDefined = isFileCRCDefined;
    }

    public boolean isAttributesDefined() {
        return attributesDefined;
    }

    public void setAttributesDefined(boolean areAttributesDefined) {
        this.attributesDefined = areAttributesDefined;
    }

    public boolean hasStream() {
        return hasStream;
    }

    public void setHasStream(boolean hasStream) {
        this.hasStream = hasStream;
    }

    public boolean isIsStartPosDefined() {
        return IsStartPosDefined;
    }

    public void setIsStartPosDefined(boolean isStartPosDefined) {
        IsStartPosDefined = isStartPosDefined;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public FileItem() {
        hasStream = true;
        directory = false;
        anti = false;
        fileCRCDefined = false;
        attributesDefined = false;
        creationTime = 0; // IsCreationTimeDefined = false;
        lastWriteTime = 0; // IsLastWriteTimeDefined = false;
        lastAccessTime = 0; // IsLastAccessTimeDefined = false;
        IsStartPosDefined = false;
    }
}
