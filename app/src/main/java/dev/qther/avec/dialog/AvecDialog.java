package dev.qther.avec.dialog;

import androidx.appcompat.app.AlertDialog;

public abstract class AvecDialog {
    AlertDialog dialog;
    Runnable successCallback = null;
    
    abstract public void show();

    public void succeed() {
        if (successCallback != null) {
            successCallback.run();
        }
        dialog.dismiss();
    }

    // Sets the success callback to be called when the dialog is dismissed with succeed()
    public void setSuccessCallback(Runnable successCallback) {
        this.successCallback = successCallback;
    }
}
