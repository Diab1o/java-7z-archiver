package com.swemel.sevenzip.archive;

import com.swemel.common.ByteBuffer;
import com.swemel.common.OutStream_;
import com.swemel.common.OutBuffer;
import com.swemel.sevenzip.CRC;
import com.swemel.sevenzip.CoderInfo;
import com.swemel.sevenzip.Folder;
import com.swemel.sevenzip.common.BindPair;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 24.02.2011
 * Time: 18:58:46
 * To change this template use File | Settings | File Templates.
 */
public class OutArchive {

    private OutStream_ stream;
    private OutBuffer outByte;
    private CRC crc;
    private long headerOffset;

    public OutArchive() {
        outByte = new OutBuffer();
        outByte.create(1 << 16);
    }

    public void writeBytes(OutputStream stream, byte[] data, int size) throws IOException {
        int processedSize = 0;
        while (size > 0) {
            int curSize = (int) (size < ((int) 0x0FFFFFFF) ? size : (int) 0x0FFFFFFF);

            stream.write(data, processedSize, curSize);
            processedSize += curSize;
            size -= curSize;
        }
    }


    public void writeDirect(byte[] data, int size) throws IOException {
        writeBytes(stream, data, size);
    }

    static void SetUInt32(byte[] p, int offset, int d) {
        for (int i = 0; i < 4; i++, d >>= 8)
            p[i + offset] = (byte) d;
    }

    static void SetUInt64(byte[] p, int offset, long d) {
        for (int i = 0; i < 8; i++, d >>= 8)
            p[i + offset] = (byte) d;
    }


    public void writeStartHeader(StartHeader h) throws IOException {
        byte[] buf = new byte[24];
        SetUInt64(buf, 4, h.getNextHeaderOffset());
        SetUInt64(buf, 12, h.getNextHeaderSize());
        SetUInt32(buf, 20, h.getNextHeaderCRC());
        SetUInt32(buf, 0, CRC.CalculateDigest(buf, 4, 20));
        writeDirect(buf, 24);
    }

    void skipPrefixArchiveHeader() throws IOException {
        stream.seek(24, OutStream_.STREAM_SEEK_CUR);
    }


    public long getPos() {
        return outByte.getProcessedSize();
    }

    public void writeBytes(byte[] data, int size) throws IOException {

        outByte.writeBytes(data, size);
        crc.Update(data, size);

    }

    public void writeByte(Byte b) throws IOException {

        outByte.writeByte(b);
        crc.UpdateByte(b);

    }

    void writeUInt32(int value) throws IOException {
        for (int i = 0; i < 4; i++) {
            writeByte((byte) value);
            value >>= 8;
        }
    }

    public void writeUInt64(long value) throws IOException {
        for (int i = 0; i < 8; i++) {
            writeByte((byte) value);
            value >>= 8;
        }
    }

    void writeNumber(long value) throws IOException {
        byte firstByte = 0;
        byte mask = (byte) 0x80;
        int i;
        for (i = 0; i < 8; i++) {
            if (value < ((((long) (1)) << (7 * (i + 1))))) {
                firstByte |= ((byte) (value >> (8 * i)));
                break;
            }
            firstByte |= mask;
            mask >>= 1;
        }
        writeByte(firstByte);
        for (; i > 0; i--) {
            writeByte((byte) value);
            value >>= 8;
        }

    }

    static int getBigNumberSize(long value) {
        int i;
        for (i = 1; i < 9; i++)
            if (value < ((((long) 1) << (i * 7))))
                break;
        return i;
    }

    void writeFolder(Folder folder) throws IOException {
        writeNumber(folder.getCoders().size());
        int i;
        for (i = 0; i < folder.getCoders().size(); i++) {
            CoderInfo coder = folder.getCoders().get(i);
            {
                int propsSize = coder.getProps().GetCapacity();

                long id = coder.getMethodID();
                int idSize;
                for (idSize = 1; idSize < 8; idSize++)
                    if ((id >> (8 * idSize)) == 0)
                        break;
                byte[] longID = new byte[15];
                for (int t = idSize - 1; t >= 0; t--, id >>= 8)
                    longID[t] = (byte) (id & 0xFF);
                byte b;
                b = (byte) (idSize & 0xF);
                boolean isComplex = !coder.isSimpleCoder();
                b |= (isComplex ? 0x10 : 0);
                b |= ((propsSize != 0) ? 0x20 : 0);
                writeByte(b);
                writeBytes(longID, idSize);
                if (isComplex) {
                    writeNumber(coder.getNumInStreams());
                    writeNumber(coder.getNumOutStreams());
                }
                if (propsSize == 0)
                    continue;
                writeNumber(propsSize);
                writeBytes(coder.getProps(), propsSize);
            }
        }
        for (i = 0; i < folder.getBindPairs().size(); i++) {
            BindPair bindPair = folder.getBindPairs().get(i);
            writeNumber(bindPair.getInIndex());
            writeNumber(bindPair.getOutIndex());
        }
        if (folder.getPackStreams().size() > 1)
            for (i = 0; i < folder.getPackStreams().size(); i++) {
                writeNumber(folder.getPackStreams().get(i));
            }
    }

    private void writeBytes(ByteBuffer props, int propsSize) throws IOException {

        writeBytes(props.data(),propsSize);
    }

    void writeBoolVector(Vector<Boolean> boolVector) throws IOException {
        byte b = 0;
        int mask = 0x80;
        for (int i = 0; i < boolVector.size(); i++) {
            if (boolVector.get(i))
                b |= mask;
            mask >>= 1;
            if (mask == 0) {
                writeByte(b);
                mask = 0x80;
                b = 0;
            }
        }
        if (mask != 0x80)
            writeByte(b);
    }


    void writeHashDigests(
            Vector<Boolean> digestsDefined,
            Vector<Integer> digests) throws IOException {


        int numDefined = 0;
        int i;
        for (i = 0; i < digestsDefined.size(); i++)
            if (digestsDefined.get(i))
                numDefined++;
        if (numDefined == 0)
            return;

        writeByte((byte) Header.NID.kCRC);
        if (numDefined == digestsDefined.size())
            writeByte((byte) 1);
        else {
            writeByte((byte) 0);
            writeBoolVector(digestsDefined);
        }
        for (i = 0; i < digests.size(); i++)
            if (digestsDefined.get(i))
                writeUInt32(digests.get(i));
    }

    void writePackInfo(
            long dataOffset,
            Vector<Long> packSizes,
            Vector<Boolean> packCRCsDefined,
            Vector<Integer> packCRCs) throws IOException {
        if (packSizes.isEmpty())
            return;
        writeByte((byte) Header.NID.kPackInfo);
        writeNumber(dataOffset);
        writeNumber(packSizes.size());
        writeByte((byte) Header.NID.kSize);
        for (int i = 0; i < packSizes.size(); i++)
            writeNumber(packSizes.get(i));

        writeHashDigests(packCRCsDefined, packCRCs);

        writeByte((byte) Header.NID.kEnd);
    }

    void writeUnpackInfo(Vector<Folder> folders) throws IOException {
        if (folders.isEmpty())
            return;

        writeByte((byte) Header.NID.kUnPackInfo);

        writeByte((byte) Header.NID.kFolder);
        writeNumber(folders.size());
        {
            writeByte((byte) 0);
            for (int i = 0; i < folders.size(); i++)
                writeFolder(folders.get(i));
        }

        writeByte((byte) Header.NID.kCodersUnPackSize);
        int i;
        for (i = 0; i < folders.size(); i++) {
            Folder folder = folders.get(i);
            for (int j = 0; j < folder.getUnpackSizes().size(); j++)
                writeNumber(folder.getUnpackSizes().get(j));
        }

        Vector<Boolean> unpackCRCsDefined = new Vector<Boolean>();
        Vector<Integer> unpackCRCs = new Vector<Integer>();
        for (i = 0; i < folders.size(); i++) {
            Folder folder = folders.get(i);
            unpackCRCsDefined.add(folder.isUnpackCRCDefined());
            unpackCRCs.add(folder.getUnpackCRC());
        }
        writeHashDigests(unpackCRCsDefined, unpackCRCs);

        writeByte((byte) Header.NID.kEnd);
    }

    void writeSubStreamsInfo(
            Vector<Folder> folders,
            Vector<Integer> numUnpackStreamsInFolders,
            Vector<Long> unpackSizes,
            Vector<Boolean> digestsDefined,
            Vector<Integer> digests) throws IOException {
        writeByte((byte) Header.NID.kSubStreamsInfo);

        int i;
        for (i = 0; i < numUnpackStreamsInFolders.size(); i++) {
            if (numUnpackStreamsInFolders.get(i) != 1) {
                writeByte((byte) Header.NID.kNumUnPackStream);
                for (i = 0; i < numUnpackStreamsInFolders.size(); i++)
                    writeNumber(numUnpackStreamsInFolders.get(i));
                break;
            }
        }


        boolean needFlag = true;
        int index = 0;
        for (i = 0; i < numUnpackStreamsInFolders.size(); i++)
            for (int j = 0; j < numUnpackStreamsInFolders.get(i); j++) {
                if (j + 1 != numUnpackStreamsInFolders.get(i)) {
                    if (needFlag)
                        writeByte((byte) Header.NID.kSize);
                    needFlag = false;
                    writeNumber(unpackSizes.get(index));
                }
                index++;
            }

        Vector<Boolean> digestsDefined2 = new Vector<Boolean>();
        Vector<Integer> digests2 = new Vector<Integer>();

        int digestIndex = 0;
        for (i = 0; i < folders.size(); i++) {
            int numSubStreams = (int) numUnpackStreamsInFolders.get(i);
            if (numSubStreams == 1 && folders.get(i).isUnpackCRCDefined())
                digestIndex++;
            else
                for (int j = 0; j < numSubStreams; j++, digestIndex++) {
                    digestsDefined2.add(digestsDefined.get(digestIndex));
                    digests2.add(digests.get(digestIndex));
                }
        }
        writeHashDigests(digestsDefined2, digests2);
        writeByte((byte) Header.NID.kEnd);
    }


    static int Bv_GetSizeInBytes(Vector v) {
        return ((int) v.size() + 7) / 8;
    }

    void writeAlignedBoolHeader(Vector v, int numDefined, int type, int itemSize) throws IOException {
        int bvSize = (numDefined == v.size()) ? 0 : Bv_GetSizeInBytes(v);
        long dataSize = (long) numDefined * itemSize + bvSize + 2;

        writeByte((byte) type);
        writeNumber(dataSize);
        if (numDefined == v.size())
            writeByte((byte) 1);
        else {
            writeByte((byte) 0);
            writeBoolVector(v);
        }
        writeByte((byte) 0);
    }

    void writeUInt64DefVector(Vector<Boolean> defined, Vector<Long> values, int type) throws IOException {
        int numDefined = 0;

        int i;
        for (i = 0; i < defined.size(); i++)
            if (defined.get(i))
                numDefined++;

        if (numDefined == 0)
            return;

        writeAlignedBoolHeader(defined, numDefined, type, 8);

        for (i = 0; i < defined.size(); i++)
            if (defined.get(i))
                writeUInt64(values.get(i)*10000+116444736000002500L);
    }

    void writeHeader(
            ArchiveDatabase db) throws IOException {

        long packedSize = 0;
        for (int i = 0; i < db.getPackSizes().size(); i++)
            packedSize += db.getPackSizes().get(i);

        headerOffset = packedSize;

        writeByte((byte) Header.NID.kHeader);

        // Archive Properties

        if (db.getFolders().size() > 0) {
            writeByte((byte) Header.NID.kMainStreamsInfo);
            writePackInfo(0, db.getPackSizes(),
                    db.getPackCRCsDefined(),
                    db.getPackCRCs());

            writeUnpackInfo(db.getFolders());

            Vector<Long> unpackSizes = new Vector<Long>();
            Vector<Boolean> digestsDefined = new Vector<Boolean>();
            Vector<Integer> digests = new Vector<Integer>();
            for (int i = 0; i < db.getFiles().size(); i++) {
                FileItem file = db.getFiles().get(i);
                if (!file.hasStream())
                    continue;
                unpackSizes.add(file.getSize());
                digestsDefined.add(file.isCrcDefined());
                digests.add(file.getFileCRC());
            }

            writeSubStreamsInfo(
                    db.getFolders(),
                    db.getNumUnPackStreamsVector(),
                    unpackSizes,
                    digestsDefined,
                    digests);
            writeByte((byte) Header.NID.kEnd);
        }

        if (db.getFiles().isEmpty()) {
            writeByte((byte) Header.NID.kEnd);
            return;
        }

        writeByte((byte) Header.NID.kFilesInfo);
        writeNumber(db.getFiles().size());

        {
            /* ---------- Empty Streams ---------- */
            Vector<Boolean> emptyStreamVector = new Vector<Boolean>();
            int numEmptyStreams = 0;
            for (int i = 0; i < db.getFiles().size(); i++)
                if (db.getFiles().get(i).hasStream())
                    emptyStreamVector.add(false);
                else {
                    emptyStreamVector.add(true);
                    numEmptyStreams++;
                }
            if (numEmptyStreams > 0) {
                writeByte((byte) Header.NID.kEmptyStream);
                writeNumber(Bv_GetSizeInBytes(emptyStreamVector));
                writeBoolVector(emptyStreamVector);

                Vector<Boolean> emptyFileVector = new Vector<Boolean>();
                Vector<Boolean> antiVector = new Vector<Boolean>();
                int numEmptyFiles = 0, numAntiItems = 0;
                for (int i = 0; i < db.getFiles().size(); i++) {
                    FileItem file = db.getFiles().get(i);
                    if (!file.hasStream()) {
                        emptyFileVector.add(!file.isDirectory());
                        if (!file.isDirectory())
                            numEmptyFiles++;
                        boolean isAnti = db.isItemAnti(i);
                        antiVector.add(isAnti);
                        if (isAnti)
                            numAntiItems++;
                    }
                }

                if (numEmptyFiles > 0) {
                    writeByte((byte) Header.NID.kEmptyFile);
                    writeNumber(Bv_GetSizeInBytes(emptyFileVector));
                    writeBoolVector(emptyFileVector);
                }

                if (numAntiItems > 0) {
                    writeByte((byte) Header.NID.kAnti);
                    writeNumber(Bv_GetSizeInBytes(antiVector));
                    writeBoolVector(antiVector);
                }
            }
        }


        {
            /* ---------- Names ---------- */

            int numDefined = 0;
            int namesDataSize = 0;
            for (int i = 0; i < db.getFiles().size(); i++) {
                String name = db.getFiles().get(i).getName();         
                if (!name.isEmpty())
                    numDefined++;
                namesDataSize += (name.length() + 1) * 2;
            }

            if (numDefined > 0) {
                namesDataSize++;
                //skipAlign(2 + getBigNumberSize(namesDataSize), 2);

                writeByte((byte) Header.NID.kName);
                writeNumber(namesDataSize);
                writeByte((byte) 0);
                for (int i = 0; i < db.getFiles().size(); i++) {
                    String name = db.getFiles().get(i).getName();
                    for (int t = 0; t < name.length(); t++) {
                        char c = name.charAt(t);
                        writeByte((byte) c);
                        writeByte((byte) (c >> 8));
                    }
                    writeByte((byte) 0);
                    writeByte((byte) 0);
                }
            }
        }

        //if (headerOptions.writeCTime) WriteUInt64DefVector(db.CTime, Header.NID.kCTime);
        //if (headerOptions.writeATime) WriteUInt64DefVector(db.ATime, Header.NID.kATime);
        writeUInt64DefVector(db.getMTimesDefined(), db.getMTimes(), Header.NID.kLastWriteTime);
        //writeUInt64DefVector(db.getStartPos(), Header.NID.kStartPos);

        {
            /* ---------- Write Attrib ---------- */
            Vector<Boolean> boolVector = new Vector<Boolean>();
            int numDefined = 0;
            for (int i = 0; i < db.getFiles().size(); i++) {
                boolean defined = db.getFiles().get(i).isAttributesDefined();
                boolVector.add(defined);
                if (defined)
                    numDefined++;
            }
            if (numDefined > 0) {
                writeAlignedBoolHeader(boolVector, numDefined, (byte) Header.NID.kWinAttributes, 4);
                for (int i = 0; i < db.getFiles().size(); i++) {
                    FileItem file = db.getFiles().get(i);
                    if (file.isAttributesDefined())
                        writeUInt32(file.getAttributes());
                }
            }
        }

        writeByte((byte) Header.NID.kEnd); // for files
        writeByte((byte) Header.NID.kEnd); // for headers
    }


    public void writeDatabase(ArchiveDatabase db) throws IOException {
        int headerCRC;
        long headerSize;
        if (db.isEmpty()) {
            headerSize = 0;
            headerOffset = 0;
            headerCRC = CRC.CalculateDigest(null, 0);
        } else {
            boolean encodeHeaders = false;


            outByte.setStream(stream);
            outByte.init();
            crc = new CRC();
            //countMode = encodeHeaders;
            //writeToStream = true;
            //countSize = 0;
            writeHeader(db);

            /*if (encodeHeaders)
            {
              CByteBuffer buf;
              buf.SetCapacity(_countSize);
              _outByte2.Init((Byte *)buf, _countSize);

              _countMode = false;
              _writeToStream = false;
              WriteHeader(db, headerOptions, headerOffset);

              if (_countSize != _outByte2.GetPos())
                return E_FAIL;

              CCompressionMethodMode encryptOptions;
              encryptOptions.PasswordIsDefined = options->PasswordIsDefined;
              encryptOptions.Password = options-> Password;
              CEncoder encoder(headerOptions.CompressMainHeader ? *options : encryptOptions);
              CRecordVector<UInt64> packSizes;
              CObjectVector<CFolder> folders;
              RINOK(EncodeStream(
                  EXTERNAL_CODECS_LOC_VARS
                  encoder, buf,
                  packSizes, folders));

              _writeToStream = true;

              if (folders.Size() == 0)
                throw 1;

              WriteID(NID::kEncodedHeader);
              WritePackInfo(headerOffset, packSizes,
                CRecordVector<bool>(), CRecordVector<UInt32>());
              WriteUnpackInfo(folders);
              WriteByte(NID::kEnd);
              for (int i = 0; i < packSizes.Size(); i++)
                headerOffset += packSizes[i];
            }*/
            outByte.flush();
            headerCRC = crc.GetDigest();
            headerSize = outByte.getProcessedSize();
        }
        {
            StartHeader h = new StartHeader();
            h.setNextHeaderSize(headerSize);
            h.setNextHeaderCRC(headerCRC);
            h.setNextHeaderOffset(headerOffset);
            stream.seek(8, OutStream_.STREAM_SEEK_SET);
            writeStartHeader(h);
        }
    }

    public void create(OutStream_ stream, boolean endMarker) throws IOException {
        this.stream = stream;
        writeSignature();
    }

    private void writeSignature() throws IOException {
        byte[] buff = {'7', 'z', (byte) 0xBC, (byte) 0xAF, 0x27, 0x1C, 0, 3};
        stream.write(buff);

    }

    public void SkipPrefixArchiveHeader() {
        try {
            stream.seek(24, OutStream_.STREAM_SEEK_CUR);
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }


}
