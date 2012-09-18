package com.swemel.sevenzip.common;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 09.02.2011
 * Time: 18:30:34
 * To change this template use File | Settings | File Templates.
 */
public class Method {
    long id;
    Vector<Prop> props;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Vector<Prop> getProps() {
        return props;
    }

    public void setProps(Vector<Prop> props) {
        this.props = props;
    }
}
