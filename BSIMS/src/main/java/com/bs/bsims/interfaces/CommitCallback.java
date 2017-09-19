
package com.bs.bsims.interfaces;

public interface CommitCallback {
    public abstract boolean commit();

    public abstract void commitSuccess();

    public abstract void commitFailure();
}
