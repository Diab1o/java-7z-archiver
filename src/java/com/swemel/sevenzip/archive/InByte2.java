package com.swemel.sevenzip.archive;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 24.02.2011
 * Time: 18:02:35
 * To change this template use File | Settings | File Templates.
 */
class InByte2 {
    byte [] _buffer;
    int _size;
    int _pos;

    public void Init(byte [] buffer, int size) {
        _buffer = buffer;
        _size = size;
        _pos = 0;
    }
    public int ReadByte() throws IOException {
        if(_pos >= _size)
            throw new IOException("CInByte2 - Can't read stream");
        return (_buffer[_pos++] & 0xFF);
    }

    int ReadBytes2(byte [] data, int size) {
        int processedSize;
        for(processedSize = 0; processedSize < size && _pos < _size; processedSize++)
            data[processedSize] = _buffer[_pos++];
        return processedSize;
    }

    boolean ReadBytes(byte [] data, int size) {
        int processedSize = ReadBytes2(data, size);
        return (processedSize == size);
    }

    int GetProcessedSize() { return _pos; }

    InByte2() {
    }
}
