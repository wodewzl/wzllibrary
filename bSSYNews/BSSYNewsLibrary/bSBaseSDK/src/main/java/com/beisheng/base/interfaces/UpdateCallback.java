
package com.beisheng.base.interfaces;

public interface UpdateCallback {

    public abstract boolean execute();

    public abstract void executeSuccess();

    public abstract void executeFailure();
}
