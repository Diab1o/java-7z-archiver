package com.swemel.sevenzip.archive;

import com.swemel.sevenzip.Folder;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 24.02.2011
 * Time: 18:16:07
 * To change this template use File | Settings | File Templates.
 */
public class ArchiveDatabaseEx extends ArchiveDatabase {
    InArchiveInfo ArchiveInfo = new InArchiveInfo();
    Vector<Long> PackStreamStartPositions = new Vector<Long>();
    Vector<Integer> FolderStartPackStreamIndex = new Vector<Integer>();

    Vector<Integer> FolderStartFileIndex = new Vector<Integer>();
    Vector<Integer> FileIndexToFolderIndexMap = new Vector<Integer>();

    void Clear() {
        super.Clear();
        ArchiveInfo.Clear();
        PackStreamStartPositions.clear();
        FolderStartPackStreamIndex.clear();
        FolderStartFileIndex.clear();
        FileIndexToFolderIndexMap.clear();
    }

    void FillFolderStartPackStream() {
        FolderStartPackStreamIndex.clear();
        //FolderStartPackStreamIndex.Reserve(Folders.size());
        int startPos = 0;
        for(int i = 0; i < folders.size(); i++) {
            FolderStartPackStreamIndex.add(startPos);
            startPos += folders.get(i).getPackStreams().size();
        }
    }

    void FillStartPos() {
        PackStreamStartPositions.clear();
        //PackStreamStartPositions.Reserve(PackSizes.size());
        long startPos = 0;
        for(int i = 0; i < packSizes.size(); i++) {
            PackStreamStartPositions.add(startPos);
            startPos += packSizes.get(i);
        }
    }

    public void Fill()  throws java.io.IOException {
        FillFolderStartPackStream();
        FillStartPos();
        FillFolderStartFileIndex();
    }

    public long GetFolderFullPackSize(int folderIndex) {
        int packStreamIndex = FolderStartPackStreamIndex.get(folderIndex);
        Folder folder = folders.get(folderIndex);
        long size = 0;
        for (int i = 0; i < folder.getPackStreams().size(); i++)
            size += packSizes.get(packStreamIndex + i);
        return size;
    }


    void FillFolderStartFileIndex() throws java.io.IOException {
        FolderStartFileIndex.clear();
        //FolderStartFileIndex.Reserve(Folders.size());
        FileIndexToFolderIndexMap.clear();
        //FileIndexToFolderIndexMap.Reserve(Files.size());

        int folderIndex = 0;
        int indexInFolder = 0;
        for (int i = 0; i < files.size(); i++) {
            FileItem file = files.get(i);
            boolean emptyStream = !file.hasStream();
            if (emptyStream && indexInFolder == 0) {
                FileIndexToFolderIndexMap.add(0xFFFFFFFF);
                continue;
            }
            if (indexInFolder == 0) {
                // v3.13 incorrectly worked with empty folders
                // v4.07: Loop for skipping empty folders
                for (;;) {
                    if (folderIndex >= folders.size())
                        throw new java.io.IOException("Incorrect Header"); // CInArchiveException(CInArchiveException::kIncorrectHeader);
                    FolderStartFileIndex.add(i); // check it
                    if (numUnPackStreamsVector.get(folderIndex) != 0)
                        break;
                    folderIndex++;
                }
            }
            FileIndexToFolderIndexMap.add(folderIndex);
            if (emptyStream)
                continue;
            indexInFolder++;
            if (indexInFolder >= numUnPackStreamsVector.get(folderIndex)) {
                folderIndex++;
                indexInFolder = 0;
            }
        }
    }

    public long GetFolderStreamPos(int folderIndex, int indexInFolder) {
        return ArchiveInfo.DataStartPosition +
                PackStreamStartPositions.get(FolderStartPackStreamIndex.get(folderIndex) +
                indexInFolder);
    }
}
