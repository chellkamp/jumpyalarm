package org.hellscode.util.ui;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/**
 * Utility class for dialogs
 */
public class DialogUtil {

    /**
     * show an uncancellable wait dialog while a process completes.  This method needs to be called
     * from the UI thread.
     * @param activity hosting activity
     * @param title dialog title.  This may be null.
     * @param message dialog message.  This may be null.
     * @param icon dialog icon.  This may be null.
     * @param backgroundRunner Method to run in background while the wait dialog is displayed.
     * @param postUIRunner Method to run in UI thread after background processing completes
     *                     but before giving control back to user.
     */
    public static void runWaitDialog(
            @NonNull final Activity activity,
            @Nullable String title,
            @Nullable String message,
            @Nullable Drawable icon,
            @NonNull final Runnable backgroundRunner,
            @Nullable final Runnable postUIRunner) {
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
                        backgroundRunner.run();
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (postUIRunner != null)
                                {
                                    postUIRunner.run();
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
