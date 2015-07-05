package com.swemel.sevenzip;

import com.swemel.common.RandomAccessOutputStream;
import com.swemel.sevenzip.archive.ArchiveDatabase;
import com.swemel.sevenzip.archive.FileItem;
import com.swemel.sevenzip.archive.OutArchive;
import com.swemel.sevenzip.archive.SevenZipFolderInStream;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is rough java implementation of 7z archive algorithm. Translated into Java from C++.
 * It is simplified, and uses lzma compression algorithm provided by http://www.7-zip.org/
 */

public class SevenZip {
    private final List<UpdateItem> updateItems = new ArrayList<>();
    private int currentIndex = 0;
    private RandomAccessOutputStream outStream;

    private void collectFiles(File... files) {
        for (File file : files) {
            if (!file.canRead()) {
                System.err.println("Can't read from file:" + file.getAbsolutePath());
                continue;
            }

            int indexInArchive = -1;
            UpdateItem ui = new UpdateItem();
            ui.setNewProps(true);
            ui.setNewData(true);
            ui.setIndexInArchive(indexInArchive);
            ui.setIndexInClient(currentIndex++);
            ui.setIsAnti(false);
            ui.setSize(0);
            ui.setaTimeDefined(false);
            ui.setmTime(file.lastModified());
            ui.setmTimeDefined(file.lastModified() != 0L);
            ui.setmTimeDefined(true);
            ui.setaTimeDefined(false);
            ui.setcTimeDefined(false);
            ui.setName(file.getName());
            ui.setFullName(file.getAbsolutePath());
            ui.setIsDir(file.isDirectory());
            ui.setIsAnti(false);
            ui.setSize(file.length());
            if (file.isDirectory())
                ui.setAttrib(16);
            else
                ui.setAttrib(32);
            ui.setAttribDefined(true);
            updateItems.add(ui);
            if (file.isDirectory()) {
                collectFiles(file.listFiles());
            }
        }
    }

    public SevenZip(String archiveName, File... files) throws IOException {
        collectFiles(files);
        outStream = new RandomAccessOutputStream(new File(archiveName), "rw");
    }

    private static void setMethodProperties(com.swemel.sevenzip.compression.lzma.Encoder encoder, long inSizeForReduce, LZMACoderInfo info) {
        boolean tryReduce = false;
        int dictionarySize = 1 << 24;
        int reducedDictionarySize = 1 << 10;
        if (inSizeForReduce != 0) {
            for (; ; ) {
                int step = (reducedDictionarySize >> 1);
                if (reducedDictionarySize >= inSizeForReduce) {
                    tryReduce = true;
                    break;
                }
                reducedDictionarySize += step;
                if (reducedDictionarySize >= inSizeForReduce) {
                    tryReduce = true;
                    break;
                }
                if (reducedDictionarySize >= (3 << 30))
                    break;
                reducedDictionarySize += step;
            }
        }
        if (tryReduce)
            if (reducedDictionarySize < dictionarySize)
                dictionarySize = reducedDictionarySize;
        info.setDictionarySize(dictionarySize);
        encoder.setDictionarySize(dictionarySize);
        encoder.setNumFastBytes(32);
        encoder.setMatchFinder(1);
        encoder.setLcLpPb(3, 0, 2);
    }

    private static boolean isExeExt(String s) {
        if (s.equalsIgnoreCase("exe")) return true;
        if (s.equalsIgnoreCase("dll")) return true;
        if (s.equalsIgnoreCase("ocx")) return true;
        if (s.equalsIgnoreCase("sfx")) return true;
        if (s.equalsIgnoreCase("sys")) return true;
        return false;

    }

    public void createArchive() throws IOException {
        OutArchive archive = new OutArchive();
        ArchiveDatabase newDatabase = new ArchiveDatabase();
        long kLzmaDicSizeX5 = 1 << 24;
        long numSolidFiles = Long.MAX_VALUE;
        long numSolidBytes = kLzmaDicSizeX5 << 7;
        long inSizeForReduce = 0;

        for (UpdateItem updateItem : updateItems) {
            if (updateItem.isNewData()) {
                inSizeForReduce += updateItem.getSize();
            }
        }

        if (inSizeForReduce < 0) {
            inSizeForReduce = 0;
        }
        long kMinReduceSize = 1 << 16;
        if (inSizeForReduce < kMinReduceSize) {
            inSizeForReduce = kMinReduceSize;
        }

        archive.create(outStream);
        archive.SkipPrefixArchiveHeader();
        com.swemel.sevenzip.compression.lzma.Encoder encoder = new com.swemel.sevenzip.compression.lzma.Encoder();
        int numSubFiles;
        LZMACoderInfo info = new LZMACoderInfo();
        setMethodProperties(encoder, inSizeForReduce, info);

        for (int i = 0; i < updateItems.size(); ) {
            long totalSize = 0;
            for (numSubFiles = 0; i + numSubFiles < updateItems.size() &&
                    numSubFiles < numSolidFiles; numSubFiles++) {
                if (!updateItems.get(i + numSubFiles).isNewData() || !updateItems.get(i + numSubFiles).hasStream()) {
                    continue;
                }
                totalSize += updateItems.get(i + numSubFiles).getSize();
                if (totalSize > numSolidBytes) {
                    break;
                }
            }
            if (numSubFiles < 1) {
                numSubFiles = 1;
            }
            Folder folder = new Folder();
            folder.getCoders().clear();
            folder.getPackStreams().clear();
            folder.getCoders().add(info);
            folder.getPackStreams().add(0);
            int numUnpackStreams = 0;

            SevenZipFolderInStream inStream = new SevenZipFolderInStream();
            inStream.init(updateItems, i, numSubFiles);
            encoder.code(inStream, outStream);

            folder.addUnpackSize(inStream.getFullSize());
            for (int j = i; j < i + numSubFiles; j++) {
                FileItem file = new FileItem();
                UpdateItem ui = updateItems.get(j);
                file.setName(ui.getName());

                if (ui.isAttribDefined()) {
                    file.setAttributes(ui.getAttrib());
                    file.setAttributesDefined(true);
                }
                file.setLastAccessTime(ui.getaTime());
                file.setAnti(ui.isAnti());
                file.setIsStartPosDefined(false);

                file.setSize(ui.getSize());
                file.setDirectory(ui.isDir());
                file.setHasStream(ui.hasStream());

                if (file.getSize() != 0) {
                    file.setCrcDefined(true);
                    file.setFileCRC(inStream.getCrc(j - i));
                    numUnpackStreams++;
                } else {
                    file.setCrcDefined(false);
                    file.setHasStream(false);
                }
                newDatabase.addMTimeDefined(ui.ismTimeDefined());
                if (ui.ismTimeDefined()) {
                    newDatabase.addMTime(ui.getmTime());
                }
                newDatabase.addFile(file);
            }
            newDatabase.addFolder(folder);
            newDatabase.addPackSize(outStream.getSize());
            outStream.setSize(0);
            newDatabase.getNumUnPackStreamsVector().add(numUnpackStreams);
            i += numSubFiles;
        }

        fillEmptyRefs(newDatabase);
        archive.writeDatabase(newDatabase);
        outStream.close();
    }

    private void fillEmptyRefs(ArchiveDatabase archiveDatabase) {
        List<UpdateItem> emptyRefs = new ArrayList<>();
        for (UpdateItem ui : updateItems) {
            if (!ui.isNewData() || !ui.hasStream()) {
                emptyRefs.add(ui);
            }
        }
        for (UpdateItem emptyRef : emptyRefs) {
            FileItem file = new FileItem();
            file.setName(emptyRef.getName());
            if (emptyRef.isAttribDefined()) {
                file.setAttributes(emptyRef.getAttrib());
                file.setAttributesDefined(true);
            }
            file.setLastAccessTime(emptyRef.getaTime());
            file.setAnti(emptyRef.isAnti());
            file.setIsStartPosDefined(false);

            file.setSize(emptyRef.getSize());
            file.setDirectory(emptyRef.isDir());
            file.setHasStream(emptyRef.hasStream());
            archiveDatabase.addMTimeDefined(emptyRef.ismTimeDefined());
            if (emptyRef.ismTimeDefined()) {
                archiveDatabase.addMTime(emptyRef.getmTime());
            }
            archiveDatabase.addFile(file);
        }
    }
}
