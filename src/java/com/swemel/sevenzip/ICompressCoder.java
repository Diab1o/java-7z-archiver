package com.swemel.sevenzip;


/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 03.02.2011
 * Time: 17:52:23
 * To change this template use File | Settings | File Templates.
 */
public interface ICompressCoder {
    void Code(
            java.io.InputStream inStream, java.io.OutputStream outStream,
            long inSize, long outSize, ICodeProgress progress) throws java.io.IOException;
}
