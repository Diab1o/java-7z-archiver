package com.swemel.sevenzip.common;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 03.02.2011
 * Time: 16:27:26
 * To change this template use File | Settings | File Templates.
 */
public class StreamUtils {
    static public int readStream(java.io.InputStream stream, byte [] data,int off, int size) throws IOException
    {
        int processedSize = 0;

        while(size != 0)
        {
             int processedSizeLoc = stream.read(data,off + processedSize,size);
             if (processedSizeLoc > 0)
             {
                processedSize += processedSizeLoc;
                size -= processedSizeLoc;
             }
             if (processedSizeLoc == -1) {
                 if (processedSize > 0) return processedSize;
                 return -1; // EOF
             }
        }
        return processedSize;
    }
}
