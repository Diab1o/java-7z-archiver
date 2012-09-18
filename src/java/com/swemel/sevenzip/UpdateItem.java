package com.swemel.sevenzip;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 17.02.2011
 * Time: 17:42:54
 * To change this template use File | Settings | File Templates.
 */
public class UpdateItem {

    private int IndexInArchive;
    private int IndexInClient;

    private long CTime;
    private long ATime;
    private long MTime;

    private long Size;
    private String Name;

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    private String FullName;

    private int Attrib;

    private boolean NewData;
    private boolean NewProps;

    private boolean IsAnti = false;
    private boolean IsDir = false;

    private boolean AttribDefined = false;
    private boolean CTimeDefined = false;
    private boolean ATimeDefined = false;
    private boolean MTimeDefined = false;

    public int getIndexInArchive() {
        return IndexInArchive;
    }

    public void setIndexInArchive(int indexInArchive) {
        IndexInArchive = indexInArchive;
    }

    public int getIndexInClient() {
        return IndexInClient;
    }

    public void setIndexInClient(int indexInClient) {
        IndexInClient = indexInClient;
    }

    public long getCTime() {
        return CTime;
    }

    public void setCTime(long CTime) {
        this.CTime = CTime;
    }

    public long getATime() {
        return ATime;
    }

    public void setATime(long ATime) {
        this.ATime = ATime;
    }

    public long getMTime() {
        return MTime;
    }

    public void setMTime(long MTime) {
        this.MTime = MTime;
    }

    public long getSize() {
        return Size;
    }

    public void setSize(long size) {
        Size = size;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getAttrib() {
        return Attrib;
    }

    public void setAttrib(int attrib) {
        Attrib = attrib;
    }

    public boolean isNewData() {
        return NewData;
    }

    public void setNewData(boolean newData) {
        NewData = newData;
    }

    public boolean isNewProps() {
        return NewProps;
    }

    public void setNewProps(boolean newProps) {
        NewProps = newProps;
    }

    public boolean isAnti() {
        return IsAnti;
    }

    public void setIsAnti(boolean isAnti) {
        IsAnti = isAnti;
    }

    public boolean isDir() {
        return IsDir;
    }

    public void setIsDir(boolean isDir) {
        IsDir = isDir;
    }

    public boolean isAttribDefined() {
        return AttribDefined;
    }

    public void setAttribDefined(boolean attribDefined) {
        AttribDefined = attribDefined;
    }

    public boolean isCTimeDefined() {
        return CTimeDefined;
    }

    public void setCTimeDefined(boolean CTimeDefined) {
        this.CTimeDefined = CTimeDefined;
    }

    public boolean isATimeDefined() {
        return ATimeDefined;
    }

    public void setATimeDefined(boolean ATimeDefined) {
        this.ATimeDefined = ATimeDefined;
    }

    public boolean isMTimeDefined() {
        return MTimeDefined;
    }

    public void setMTimeDefined(boolean MTimeDefined) {
        this.MTimeDefined = MTimeDefined;
    }

    public boolean hasStream() {
        return !IsDir && !IsAnti && Size != 0;
    }

    UpdateItem() {

    }



}
