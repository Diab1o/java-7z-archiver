package com.swemel.common;

import java.io.IOException;
import java.io.OutputStream;

public class OutBuffer {
    private byte[] _buffer;
    private int _pos;
    private int _limitPos;
    private int _streamPos;
    private int _bufferSize;
    private OutputStream _stream;
    private long _processedSize;
    private byte[] _buffer2;
    private int _buffer2Pointer = 0;
    private boolean _overDict;

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

    void flushPart() throws IOException {
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

    void flushWithCheck() throws IOException {
        flush();
    }
}
