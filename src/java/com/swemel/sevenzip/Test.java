package com.swemel.sevenzip;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 26.04.2011
 * Time: 17:19:39
 */
public class Test {
    public Test() throws IOException {
        SevenZip sz = new SevenZip("D:\\Big.7z", "D:\\Sun");
        Thread t =new Thread(sz);
        t.start();

    }

    public static void main(String[] args) {
        try {
            new Test();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
