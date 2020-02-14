package org.hellscode.util.ui;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.hellscode.util.SimpleMethod;
import org.hellscode.util.SimpleVoidMethod;

/**
 * Utility class for dialogs
 */
public class DialogUtil<T,R> {

    /**
     * show an uncancellable wait dialog while a process completes.  This method needs to be called
     * from the UI thread.
     * @param activity hosting activity
     * @param title dialog title.  This may be null.
     * @param message dialog message.  This may be null.
     * @param icon dialog icon.  This may be null.
     * @param backgroundRunner Method to run in background while the wait dialog is displayed. Type
     *                         T of the class is the argument type to pass in to this method. Type
     *                         R is the return type of this method.
     * @param arg argument to pass in to backgroundRunner method
     * @param postUIRunner Method to run in UI thread after background processing completes
     *                     but before giving control back to user. This method will take the result
     *                     of backgroundRunner (of type R) as an argument.
     */
    public void runWaitDialog(
            @NonNull final Activity activity,
            @Nullable String title,
            @Nullable String message,
            @Nullable Drawable icon,
            @NonNull final SimpleMethod<T,R> backgroundRunner,
            @Nullable final T arg,
            @Nullable final SimpleVoidMethod<R> postUIRunner) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);

        if (title != null) {
            builder.setTitle(title);
        }

        if (message != null) {
            builder.setMessage(message);
        }

        if (icon != null) {
            builder.setIcon(icon);
        }

        final AlertDialog dlg = builder.setCancelable(false).create();
        dlg.show();
        Thread t = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        final R result = backgroundRunner.run(arg);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (postUIRunner != null)
                                {
                                    postUIRunner.run(result);
                                }
                                dlg.dismiss();
                            }
                        });
                    }

                }
        );
        t.start();
    }


}
