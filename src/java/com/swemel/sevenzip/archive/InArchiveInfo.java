package com.swemel.sevenzip.archive;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 24.02.2011
 * Time: 18:17:29
 * To change this template use File | Settings | File Templates.
 */
class InArchiveInfo {
    public byte ArchiveVersion_Major;
    public byte ArchiveVersion_Minor;

    public long StartPosition;
    public long StartPositionAfterHeader;
    public long DataStartPosition;
    public long DataStartPosition2;
    private final Vector<Long> FileInfoPopIDs = new Vector<Long>();

    void Clear() {
        FileInfoPopIDs.clear();
    }
}
