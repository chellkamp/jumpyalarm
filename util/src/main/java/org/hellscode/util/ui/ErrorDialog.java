package org.hellscode.util.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.hellscode.util.R;

public class ErrorDialog {
    /**
     * We can't recover from the error.  Just log it and close the application.
     * Try to pop up an alert dialog for the user.
     * @param msg message
     * @param ex exception
     */
    public static void handleUnrecoverableError(Context context, String msg, Exception ex) {
        Log.e("ERRDLG", msg, ex);

        if (context != null) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context)
                    .setTitle(R.string.error)
                    .setMessage(msg)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    System.exit(1);
                                }
                            });
            builder.show();
        } else {
            System.exit(1);
        }
    }
}
