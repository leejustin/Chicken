package com.sinkwater.chicken;

/**
 * Created by dongjin on 12/9/2014.
 */
public class WrapperObject <T> {
    private T innerVal;

    public WrapperObject() {
    }

    public WrapperObject(T val) {
        innerVal = val;
    }

    public void setVal(T val) {
        innerVal = val;
    }

    public T getVal() {
        return innerVal;
    }
}

