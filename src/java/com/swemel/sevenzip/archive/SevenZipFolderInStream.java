package com.swemel.sevenzip.archive;

import com.swemel.sevenzip.UpdateItem;
import com.swemel.sevenzip.common.InStreamWithCRC;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SevenZipFolderInStream extends InputStream {
    private InStreamWithCRC stream;

    private boolean _fileIsOpen;
    private long _filePos;
    private List<UpdateItem> updateItems;
    private int _numFiles;
    private int _fileIndex;
    private int off;

    private final List<Integer> CRCs = new ArrayList<>();
    private final List<Long> sizes = new ArrayList<>();


    public void init(List<UpdateItem> updateItems, int off, int numFiles) {
        this.updateItems = updateItems;
        _numFiles = numFiles;
        _fileIndex = 0;
        this.off = off;
        _fileIsOpen = false;
    }

    private void openStream() throws FileNotFoundException {
        _filePos = 0;
        while (_fileIndex < _numFiles) {
            stream = new InStreamWithCRC(updateItems.get(off + _fileIndex).getFullName());
            _fileIndex++;
            stream.init();
            if (stream != null) {
                _fileIsOpen = true;
                return;
            }
            sizes.add(0L);
            addDigest();
        }
    }

    private void addDigest() {
        CRCs.add(stream.getCrc());
    }

    private void closeStream() throws IOException {
        stream.releaseStream();
        _fileIsOpen = false;
        sizes.add(_filePos);
        addDigest();
    }


    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }


    public int getCrc(int i) {
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
        for (Long i : sizes) {
            size += i;
        }
        return size;
    }

    @Override
    public int read() throws IOException {
        byte[] ret = new byte[1];
        if (read(ret) < 0) return -1;
        return ret[0];
    }
}
