package com.swemel.sevenzip.common;

import com.swemel.sevenzip.CRC;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 18.04.2011
 * Time: 16:55:02
 * To change this template use File | Settings | File Templates.
 */
public class InStreamWithCRC extends InputStream {

    RandomAccessFile _stream;
    static public final int STREAM_SEEK_SET = 0;
    static public final int STREAM_SEEK_CUR = 1;
    long _size;
    boolean _wasFinished;
    CRC _crc = new CRC();


    public InStreamWithCRC(File file) throws FileNotFoundException {
        _stream = new RandomAccessFile(file, "r");
    }

    public InStreamWithCRC(String fileName) throws FileNotFoundException {
        _stream = new RandomAccessFile(new File(fileName), "r");
    }


    public void setStream(RandomAccessFile stream) {
        this._stream = stream;
    }

    public long seek(int offset, int seekOrigin) throws IOException {
        _size = 0;
        _crc.Init();
        if (seekOrigin == STREAM_SEEK_SET) {
            _stream.seek(offset);
        } else if (seekOrigin == STREAM_SEEK_CUR) {
            _stream.seek(offset + _stream.getFilePointer());
        }
        return _stream.getFilePointer();
    }


    @Override
    public int read() throws IOException {
        int ret = _stream.read();
        _crc.UpdateByte((byte) ret);
        _size++;
        return ret;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int ret = _stream.read(b);
        _crc.Update(b, ret);
        _size += ret;
        return ret;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int ret = _stream.read(b, off, len);
        _crc.Update(b, off, ret);
        _size += ret;
        return ret;
    }

    public void init() {
        _size = 0;
        _wasFinished = false;
        _crc.Init();
    }

    public long getSize() {
        return _size;
    }

    public void releaseStream() throws IOException {
        _stream.close();
    }

    public int getCrc() {
        return _crc.GetDigest();
    }
}
