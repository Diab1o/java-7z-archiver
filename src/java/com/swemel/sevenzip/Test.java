package com.swemel.sevenzip;

import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 26.04.2011
 * Time: 17:19:39
 */
class Test {
    public static void main(String[] args) {
        try {
            SevenZip sevenZip = new SevenZip("c:\\test.7z", new File("c:\\Fraps"));
            sevenZip.createArchive();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

