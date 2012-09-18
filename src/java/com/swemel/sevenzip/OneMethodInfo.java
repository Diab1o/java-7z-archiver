package com.swemel.sevenzip;

import com.swemel.sevenzip.common.Prop;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 24.02.2011
 * Time: 17:22:21
 * To change this template use File | Settings | File Templates.
 */
public class OneMethodInfo {
    Vector<Prop> props;
    String methodName;

    boolean isLzma() {
        return true;
    }
}
