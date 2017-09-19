
package com.bs.bsims.interfaces;

public class DataChangedListenerImp implements DataChangedListener {
    private UpdateDataChangedListener updateDataChangedListener;

    @Override
    public void dataChanged() {
        updateDataChangedListener.updateData();
    }

    public UpdateDataChangedListener getUpdateDataChangedListener() {
        return updateDataChangedListener;
    }

    public void setUpdateDataChangedListener(UpdateDataChangedListener updateDataChangedListener) {
        this.updateDataChangedListener = updateDataChangedListener;
    }

}
