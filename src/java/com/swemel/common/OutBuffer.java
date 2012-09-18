package com.swemel.common;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Diablo
 * Date: 21.03.2011
 * Time: 17:09:08
 * To change this template use File | Settings | File Templates.
 */
public class OutBuffer {
    byte[] _buffer;
    int _pos;
    int _limitPos;
    int _streamPos;
    int _bufferSize;
    OutputStream _stream;
    long _processedSize;
    byte[] _buffer2;
    int _buffer2Pointer = 0;
    boolean _overDict;

    public void setStream(OutputStream _stream) {
        this._stream = _stream;
    }

    public void create(int bufferSize) {
        int kMinBlockSize = 1;
        if (bufferSize < kMinBlockSize)
            bufferSize = kMinBlockSize;
        if (_buffer != null && _bufferSize == bufferSize)
            return;
        _bufferSize = bufferSize;
        _buffer = new byte[bufferSize];
    }

    public void init() {
        _streamPos = 0;
        _limitPos = _bufferSize;
        _pos = 0;
        _processedSize = 0;
        _overDict = false;
    }

    public long getProcessedSize() {
        long res = _processedSize + _pos - _streamPos;
        if (_streamPos > _pos)
            res += _bufferSize;
        return res;
    }

    public void writeByte(Byte b) throws IOException {
        _buffer[_pos++] = b;
        if (_pos == _limitPos)
            flushWithCheck();
    }

    public void writeBytes(byte[] data, int size) throws IOException {
        for (int i = 0; i < size; i++)
            writeByte(data[i]);
    }

    public void flushPart() throws IOException {
        // _streamPos < _bufferSize
        int size = (_streamPos >= _pos) ? (_bufferSize - _streamPos) : (_pos - _streamPos);
        boolean result = true;
        if (_buffer2 != null) {
            System.arraycopy(_buffer, _streamPos, _buffer2, _buffer2Pointer, size);
            _buffer2Pointer += size;
        }

        if (_stream != null) {
            _stream.write(_buffer, _streamPos, size);
        }
        _streamPos += size;
        if (_streamPos == _bufferSize)
            _streamPos = 0;
        if (_pos == _bufferSize) {
            _overDict = true;
            _pos = 0;
        }
        _limitPos = (_streamPos > _pos) ? _streamPos : _bufferSize;
        _processedSize += size;
    }

    public void flush() throws IOException {
        while (_streamPos != _pos) {
            flushPart();
        }
    }

    public void flushWithCheck() throws IOException {
        flush();
    }
}
