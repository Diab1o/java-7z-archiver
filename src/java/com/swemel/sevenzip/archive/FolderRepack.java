package com.swemel.sevenzip.archive;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 02.03.2011
 * Time: 15:50:21
 * To change this template use File | Settings | File Templates.
 */
public class FolderRepack {
    private int FolderIndex;
    private int Group;
    private int NumCopyFiles;

    public FolderRepack(int folderIndex, int group, int numCopyFiles) {
        FolderIndex = folderIndex;
        Group = group;
        NumCopyFiles = numCopyFiles;
    }

    public int getFolderIndex() {
        return FolderIndex;
    }

    public void setFolderIndex(int folderIndex) {
        FolderIndex = folderIndex;
    }

    public int getGroup() {
        return Group;
    }

    public void setGroup(int group) {
        Group = group;
    }

    public int getNumCopyFiles() {
        return NumCopyFiles;
    }

    public void setNumCopyFiles(int numCopyFiles) {
        NumCopyFiles = numCopyFiles;
    }
}
