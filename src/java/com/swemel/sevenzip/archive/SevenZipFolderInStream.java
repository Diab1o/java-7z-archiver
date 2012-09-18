package com.swemel.sevenzip.archive;

import com.swemel.sevenzip.UpdateItem;
import com.swemel.sevenzip.common.InStreamWithCRC;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 20.04.2011
 * Time: 19:09:08
 * To change this template use File | Settings | File Templates.
 */
public class SevenZipFolderInStream extends InputStream {
    InStreamWithCRC stream;

    boolean _currentSizeIsDefined;
    boolean _fileIsOpen;
    long _currentSize;
    long _filePos;
    Vector<UpdateItem> updateItems;
    Vector<Integer> indices;
    int _numFiles;
    int _fileIndex;
    int off;
    long size=0;

    Vector<Boolean> processed = new Vector<Boolean>();
    Vector<Integer> CRCs = new Vector<Integer>();
    Vector<Long> sizes = new Vector<Long>();


    public void init(Vector<UpdateItem> updateItems, Vector<Integer> indices, int off, int numFiles) {
        this.updateItems = updateItems;
        _numFiles = numFiles;
        _fileIndex = 0;
        this.off=off;
        processed.clear();
        CRCs.clear();
        sizes.clear();
        _fileIsOpen = false;
        this.indices=indices;
        _currentSizeIsDefined = false;
        size=0;
    }

    void openStream() throws FileNotFoundException {
        _filePos = 0;
        while (_fileIndex < _numFiles) {
            stream = new InStreamWithCRC(updateItems.get(indices.get(off+_fileIndex)).getFullName());

            _fileIndex++;
            stream.init();
            if (stream != null) {
                _fileIsOpen = true;

                _currentSize = stream.getSize();
                _currentSizeIsDefined = true;
                return;
            }
            sizes.add(0L);
            processed.add(true);
            addDigest();
        }
    }

    public void addDigest() {
        CRCs.add(stream.getCrc());
    }

    public void closeStream() throws IOException {
        stream.releaseStream();
        _fileIsOpen = false;
        _currentSizeIsDefined = false;
        processed.add(true);
        sizes.add(_filePos);
        addDigest();
    }


    @Override
    public int read(byte[] b) throws IOException {
        return read(b,0,b.length);
    }


    public int getCrc(int i)
    {
        return CRCs.get(i);
    }

    @Override
    public int read(byte[] data, int off, int len) throws IOException {
        int processedSize = -1;
        while (len > 0) {
            if (_fileIsOpen) {

                int processed2 = stream.read(data, off, len);
                if (!(processed2 > 0)) {
                    closeStream();
                    continue;
                }
                processedSize = processed2;
                size+=processed2;
                _filePos += processed2;
                break;
            }
            if (_fileIndex >= _numFiles)
                break;
            openStream();
        }
        return processedSize;
    }

    public long getFullSize() {
        long size = 0;
        for (int i = 0; i < sizes.size(); i++)
            size += sizes.get(i);
        return size;
    }

    @Override
    public int read() throws IOException {
        byte[] ret = new byte[1];
        if(read(ret)<0) return -1;
        return ret[0];
    }
}
