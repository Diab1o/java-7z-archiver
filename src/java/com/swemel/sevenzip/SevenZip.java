package com.swemel.sevenzip;

import com.swemel.common.OutStream_;
import com.swemel.sevenzip.archive.*;

import java.io.*;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 16.02.2011
 * Time: 17:14:34
 *
 * This is rough java implementation of 7z archive algorithm. Translated into Java from C++.
 * It is simplified, and uses lzma compression algorithm provided by http://www.7-zip.org/
 */
public class SevenZip implements Runnable {
    private Vector<UpdateItem> updateItems = new Vector<UpdateItem>();
    int currentIndex = 0;
    private OutStream_ outStream;
    private CodeProgressImpl cp;

    public int getProgress() {
        if (cp != null)
            return cp.getProgress();
        else return 0;
    }


    private void generateUpdateItems(File... files) {
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
            ui.setATimeDefined(false);
            ui.setMTime(file.lastModified());
            ui.setMTimeDefined(file.lastModified() != 0L);
            ui.setMTimeDefined(true);
            ui.setATimeDefined(false);
            ui.setCTimeDefined(false);
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
                collectFiles(file, file.getName());
            }
        }

    }

    private void generateUpdateItems(String... fileNames) {
        for (int i = 0; i < fileNames.length; i++) {
            File file = new File(fileNames[i]);
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
            ui.setATimeDefined(false);
            ui.setMTime(file.lastModified());
            ui.setMTimeDefined(file.lastModified() != 0L);
            ui.setMTimeDefined(true);
            ui.setATimeDefined(false);
            ui.setCTimeDefined(false);
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
            if (file.isDirectory()) {
                collectFiles(file, file.getName());
                updateItems.add(ui);
            }
        }

    }

    private void collectFiles(File file, String path) {
        for (File child : file.listFiles()) {
            if (!child.canRead()) {
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
            ui.setATimeDefined(false);
            ui.setMTime(child.lastModified());
            ui.setMTimeDefined(true);
            ui.setATimeDefined(false);
            ui.setCTimeDefined(false);
            ui.setName(path + "/" + child.getName());
            ui.setFullName(child.getAbsolutePath());
            ui.setIsDir(child.isDirectory());
            ui.setIsAnti(false);
            ui.setSize(child.length());
            if (child.isDirectory())
                ui.setAttrib(16);
            else
                ui.setAttrib(32);
            ui.setAttribDefined(true);
            updateItems.add(ui);
            if (child.isDirectory()) {
                collectFiles(child, path + "/" + child.getName());
            }
        }
    }


    public SevenZip(String archiveName, File... files) throws IOException {
        generateUpdateItems(files);
        outStream = new OutStream_(new File(archiveName), "rw");
    }

    public SevenZip(String archiveName, String... fileNames) throws IOException {
        generateUpdateItems(fileNames);
        outStream = new OutStream_(new File(archiveName), "rw");
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

    public static boolean isExeExt(String s) {
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
        long complexity = 0;
        long inSizeForReduce2 = 0;
        long inSizeForReduce = 0;

        for (UpdateItem updateItem : updateItems) {
            if (updateItem.isNewData()) {
                complexity += updateItem.getSize();
                if (numSolidFiles != 1) {
                    inSizeForReduce += updateItem.getSize();
                } else if (updateItem.getSize() > inSizeForReduce) {
                    inSizeForReduce = updateItem.getSize();
                }
            }
        }


        if (inSizeForReduce2 > inSizeForReduce) {
            inSizeForReduce = inSizeForReduce2;
        }
        long kMinReduceSize = (1 << 16);
        if (inSizeForReduce < kMinReduceSize)
            inSizeForReduce = kMinReduceSize;
        Vector<Vector<Integer>> groups = new Vector<Vector<Integer>>();
        for (int i = 0; i < 4; i++) {
            groups.add(new Vector<Integer>());
        }
        boolean useFilters = true;

        for (int i = 0, updateItemsSize = updateItems.size(); i < updateItemsSize; i++) {
            UpdateItem ui = updateItems.get(i);
            if (!ui.isNewData() || !ui.hasStream()) {
                continue;
            }
            boolean filteredGroup = false;
            if (useFilters) {
                int dotPos = ui.getName().lastIndexOf('.');
                if (dotPos >= 0) {
                    filteredGroup = isExeExt(ui.getName().substring(dotPos + 1));
                }
                if (filteredGroup) {
                    groups.get(1).add(i);
                } else {
                    groups.get(0).add(i);
                }

            }
        }


        archive.create(outStream, false);
        archive.SkipPrefixArchiveHeader();
        for (Vector<Integer> group : groups) {
            com.swemel.sevenzip.compression.lzma.Encoder encoder = new com.swemel.sevenzip.compression.lzma.Encoder();
            int numFiles = group.size();
            if (numFiles == 0)
                continue;
            Vector<RefItem> refItems = new Vector<RefItem>();
            boolean sortByType = (false);
            for (int i = 0; i < numFiles; i++)
                refItems.add(new RefItem(group.get(i), updateItems.get(group.get(i)), sortByType));

            Vector<Integer> indices = new Vector<Integer>();

            for (int i = 0; i < numFiles; i++) {
                int index = refItems.get(i).getIndex();
                indices.add(index);
            }
            int numSubFiles;
            LZMACoderInfo info = new LZMACoderInfo();
            setMethodProperties(encoder, inSizeForReduce, info);
            cp = new CodeProgressImpl(complexity);

            for (int i = 0; i < numFiles; ) {
                long totalSize = 0;

                for (numSubFiles = 0; i + numSubFiles < numFiles &&
                        numSubFiles < numSolidFiles; numSubFiles++) {
                    totalSize += updateItems.get(indices.get(i + numSubFiles)).getSize();
                    if (totalSize > numSolidBytes)
                        break;
                }
                if (numSubFiles < 1)
                    numSubFiles = 1;
                Folder folder = new Folder();
                folder.getCoders().clear();
                folder.getPackStreams().clear();
                folder.getCoders().add(info);
                folder.getPackStreams().add(0);
                int numUnpackStreams = 0;


                SevenZipFolderInStream inStream = new SevenZipFolderInStream();
                inStream.init(updateItems, indices, i, numSubFiles);
                encoder.code(inStream, outStream, -1, -1, cp);

                cp.setTotalInSize();

                folder.addUnpackSize(inStream.getFullSize());
                for (int j = i; j < i + numSubFiles; j++) {


                    FileItem file = new FileItem();
                    UpdateItem ui = updateItems.get(indices.get(j));
                    file.setName(ui.getName());


                    if (ui.isAttribDefined()) {
                        file.setAttributes(ui.getAttrib());
                        file.setAttributesDefined(true);
                    }
                    file.setLastAccessTime(ui.getATime());
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
                    newDatabase.addMTimeDefined(ui.isMTimeDefined());
                    if (ui.isMTimeDefined()) newDatabase.addMTime(ui.getMTime());
                    newDatabase.addFile(file);
                }
                newDatabase.addFolder(folder);
                newDatabase.addPackSize(outStream.getSize());
                outStream.setSize(0);
                newDatabase.getNumUnPackStreamsVector().add(numUnpackStreams);
                i += numSubFiles;


            }

        }
        Vector<Integer> emptyRefs = new Vector<Integer>();
        for (int i = 0; i < updateItems.size(); i++) {
            UpdateItem ui = updateItems.get(i);
            if (ui.isNewData()) {
                if (ui.hasStream())
                    continue;
            }
            emptyRefs.add(i);
        }
        for (int i = 0; i < emptyRefs.size(); i++) {
            UpdateItem ui = updateItems.get(emptyRefs.get(i));
            FileItem file = new FileItem();
            file.setName(ui.getName());
            if (ui.isAttribDefined()) {
                file.setAttributes(ui.getAttrib());
                file.setAttributesDefined(true);
            }
            file.setLastAccessTime(ui.getATime());
            file.setAnti(ui.isAnti());
            file.setIsStartPosDefined(false);

            file.setSize(ui.getSize());
            file.setDirectory(ui.isDir());
            file.setHasStream(ui.hasStream());
            newDatabase.addMTimeDefined(ui.isMTimeDefined());
            if (ui.isMTimeDefined()) newDatabase.addMTime(ui.getMTime());
            newDatabase.addFile(file);
        }


        archive.writeDatabase(newDatabase);
        outStream.close();
    }


    public void run() {
        OutArchive archive = new OutArchive();
        ArchiveDatabase newDatabase = new ArchiveDatabase();
        long kLzmaDicSizeX5 = 1 << 24;
        long numSolidFiles = Long.MAX_VALUE;
        long numSolidBytes = kLzmaDicSizeX5 << 7;
        long complexity = 0;
        long inSizeForReduce2 = 0;
        long inSizeForReduce = 0;

        for (UpdateItem updateItem : updateItems) {
            if (updateItem.isNewData()) {
                complexity += updateItem.getSize();
                if (numSolidFiles != 1) {
                    inSizeForReduce += updateItem.getSize();
                } else if (updateItem.getSize() > inSizeForReduce) {
                    inSizeForReduce = updateItem.getSize();
                }
            }
        }


        if (inSizeForReduce2 > inSizeForReduce) {
            inSizeForReduce = inSizeForReduce2;
        }
        long kMinReduceSize = (1 << 16);
        if (inSizeForReduce < kMinReduceSize)
            inSizeForReduce = kMinReduceSize;
        Vector<Vector<Integer>> groups = new Vector<Vector<Integer>>();
        for (int i = 0; i < 4; i++) {
            groups.add(new Vector<Integer>());
        }
        boolean useFilters = true;

        for (int i = 0, updateItemsSize = updateItems.size(); i < updateItemsSize; i++) {
            UpdateItem ui = updateItems.get(i);
            if (!ui.isNewData() || !ui.hasStream()) {
                continue;
            }
            boolean filteredGroup = false;
            if (useFilters) {
                int dotPos = ui.getName().lastIndexOf('.');
                if (dotPos >= 0) {
                    filteredGroup = isExeExt(ui.getName().substring(dotPos + 1));
                }
                if (filteredGroup) {
                    groups.get(1).add(i);
                } else {
                    groups.get(0).add(i);
                }

            }
        }

        try {
            archive.create(outStream, false);
            archive.SkipPrefixArchiveHeader();
        } catch (IOException e) {
            System.err.println("Couldn't create archive file.");
            e.printStackTrace();
            return;
        }
        for (Vector<Integer> group : groups) {
            com.swemel.sevenzip.compression.lzma.Encoder encoder = new com.swemel.sevenzip.compression.lzma.Encoder();
            int numFiles = group.size();
            if (numFiles == 0)
                continue;
            Vector<RefItem> refItems = new Vector<RefItem>();
            boolean sortByType = (false);
            for (int i = 0; i < numFiles; i++)
                refItems.add(new RefItem(group.get(i), updateItems.get(group.get(i)), sortByType));

            Vector<Integer> indices = new Vector<Integer>();

            for (int i = 0; i < numFiles; i++) {
                int index = refItems.get(i).getIndex();
                indices.add(index);
            }
            int numSubFiles = 0;
            LZMACoderInfo info = new LZMACoderInfo();
            setMethodProperties(encoder, inSizeForReduce, info);
            cp = new CodeProgressImpl(complexity);

            for (int i = 0; i < numFiles; ) {
                long totalSize = 0;

                for (numSubFiles = 0; i + numSubFiles < numFiles &&
                        numSubFiles < numSolidFiles; numSubFiles++) {
                    totalSize += updateItems.get(indices.get(i + numSubFiles)).getSize();
                    if (totalSize > numSolidBytes)
                        break;
                }
                if (numSubFiles < 1)
                    numSubFiles = 1;
                Folder folder = new Folder();
                folder.getCoders().clear();
                folder.getPackStreams().clear();
                folder.getCoders().add(info);
                folder.getPackStreams().add(0);
                int numUnpackStreams = 0;


                SevenZipFolderInStream inStream = new SevenZipFolderInStream();
                inStream.init(updateItems, indices, i, numSubFiles);
                try {
                    encoder.code(inStream, outStream, -1, -1, cp);
                } catch (IOException e) {
                    System.err.println("IOException while coding data.");
                    e.printStackTrace();
                    return;
                }
                cp.setTotalInSize();

                folder.addUnpackSize(inStream.getFullSize());
                for (int j = i; j < i + numSubFiles; j++) {


                    FileItem file = new FileItem();
                    UpdateItem ui = updateItems.get(indices.get(j));
                    file.setName(ui.getName());


                    if (ui.isAttribDefined()) {
                        file.setAttributes(ui.getAttrib());
                        file.setAttributesDefined(true);
                    }
                    file.setLastAccessTime(ui.getATime());
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
                    newDatabase.addMTimeDefined(ui.isMTimeDefined());
                    if (ui.isMTimeDefined()) newDatabase.addMTime(ui.getMTime());
                    newDatabase.addFile(file);
                }
                newDatabase.addFolder(folder);
                newDatabase.addPackSize(outStream.getSize());
                outStream.setSize(0);
                newDatabase.getNumUnPackStreamsVector().add(numUnpackStreams);
                i += numSubFiles;


            }

        }
        Vector<Integer> emptyRefs = new Vector<Integer>();
        for (int i = 0; i < updateItems.size(); i++) {
            UpdateItem ui = updateItems.get(i);
            if (ui.isNewData()) {
                if (ui.hasStream())
                    continue;
            }
            emptyRefs.add(i);
        }
        for (int i = 0; i < emptyRefs.size(); i++) {
            UpdateItem ui = updateItems.get(emptyRefs.get(i));
            FileItem file = new FileItem();
            file.setName(ui.getName());
            if (ui.isAttribDefined()) {
                file.setAttributes(ui.getAttrib());
                file.setAttributesDefined(true);
            }
            file.setLastAccessTime(ui.getATime());
            file.setAnti(ui.isAnti());
            file.setIsStartPosDefined(false);

            file.setSize(ui.getSize());
            file.setDirectory(ui.isDir());
            file.setHasStream(ui.hasStream());
            newDatabase.addMTimeDefined(ui.isMTimeDefined());
            if (ui.isMTimeDefined()) newDatabase.addMTime(ui.getMTime());
            newDatabase.addFile(file);
        }


        try {
            archive.writeDatabase(newDatabase);
            outStream.close();
        } catch (IOException e) {
            System.err.println("IOException while writing header.");
            e.printStackTrace();
        }


    }
}
