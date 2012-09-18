package com.swemel.sevenzip;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 24.02.2011
 * Time: 17:20:43
 * To change this template use File | Settings | File Templates.
 */
public class SevenZHandlerOut {

    int _crcSize = 4;

    Vector<OneMethodInfo> _methods;
    boolean _removeSfxBlock = false;

    long _numSolidFiles = -1;
    long _numSolidBytes = -1;
    boolean _numSolidBytesDefined = false;
    boolean _solidExtension = false;

    boolean _compressHeaders = true;
    boolean _encryptHeadersSpecified = false;
    boolean _encryptHeaders = false;

    boolean WriteCTime = false;
    boolean WriteATime = false;
    boolean WriteMTime = true;

    boolean _autoFilter = true;
    int _level = 5;

    boolean _volumeMode = false;

    
}
