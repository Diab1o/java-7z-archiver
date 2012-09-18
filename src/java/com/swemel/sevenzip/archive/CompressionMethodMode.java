package com.swemel.sevenzip.archive;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 09.02.2011
 * Time: 18:28:18
 * To change this template use File | Settings | File Templates.
 */
public class CompressionMethodMode {
    private Vector<MethodFull> methods;
    private Vector<Bind> binds;
    private int numThreads;
    private boolean passwordIsDefined;
    private String password;

    boolean IsEmpty() {
        return (methods.isEmpty() && !passwordIsDefined);
    }

    CompressionMethodMode() {
        numThreads = 1;
        passwordIsDefined = false;
    }

    public Vector<MethodFull> getMethods() {
        return methods;
    }

    public void setMethods(Vector<MethodFull> methods) {
        this.methods = methods;
    }

    public Vector<Bind> getBinds() {
        return binds;
    }

    public void setBinds(Vector<Bind> binds) {
        this.binds = binds;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }

    public boolean isPasswordIsDefined() {
        return passwordIsDefined;
    }

    public void setPasswordIsDefined(boolean passwordIsDefined) {
        this.passwordIsDefined = passwordIsDefined;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
