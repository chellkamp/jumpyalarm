package org.hellscode.util.ui.view;

import androidx.appcompat.app.AlertDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import org.hellscode.util.R;

public class TextEditFragment extends DialogFragment {

    /**
     * Provides an entry point for the dialog to return its info to
     */
    public interface TextEditCallback {
        void onTextSet(long lookupId, String text);
    }

    public static final String PARENT_TAG = "tag";
    public static final String LOOKUP_ID = "lookupid";
    public static final String TEXT = "text";

    private TextInputEditText _textInput;

    @SuppressLint("InflateParams")
    @Override
    public @NonNull
    Dialog onCreateDialog(Bundle savedInstanceState) {
        String parentTag = null;
        long lookupId = -1;
        String text = null;

        Bundle args = getArguments();

        if(args != null) {
            parentTag = args.getString(PARENT_TAG);
            lookupId = args.getLong(LOOKUP_ID, lookupId);
            text = args.getString(TEXT, text);
        }

        PosListener posListener = new PosListener(this, parentTag, lookupId);

        // To reference the text input, you can't use findViewById()
        // right after creating the dialog.
        // You'll need to inflate the view first before passing it to the builder.
        // Simply calling getLayoutInflater() from the fragment won't work.
        // You'll need to use getActivity().getLayoutInflater().  Good news:
        // you won't need to specify a root.

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View innerView = inflater.inflate(R.layout.text_input_dialog, null, false);
        TextInputEditText inputEditText = innerView.findViewById(R.id.textInput);
        if (inputEditText != null) {
            inputEditText.setText(text);
        }

        MaterialAlertDialogBuilder builder =
                new MaterialAlertDialogBuilder(getActivity())
                .setView(innerView)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, posListener)
                .setNegativeButton(android.R.string.cancel, null);

        return builder.create();
    }

    private static class PosListener implements DialogInterface.OnClickListener {

        private Fragment _owner;
        private String _parentTag;
        private long _lookupId;

        PosListener(Fragment owner, String parentTag, long lookupId) {
            _owner = owner;
            _parentTag = parentTag;
            _lookupId = lookupId;
        }

        public void onClick(DialogInterface dialog, int result) {
            FragmentManager fm  = _owner != null ? _owner.getFragmentManager() : null;
            Fragment f = fm != null ? fm.findFragmentByTag(_parentTag) : null;
            TextInputEditText textInput = null;
            if (dialog instanceof AlertDialog) {
                AlertDialog castDialog = (AlertDialog)dialog;
                textInput = castDialog.findViewById(R.id.textInput);
            }

            String text = "";
            if (textInput != null && textInput.getText() != null) {
                text = textInput.getText().toString();
            }

            if (f instanceof TextEditCallback) {
                TextEditCallback tec;
                tec = (TextEditCallback)f;
                tec.onTextSet(_lookupId, text);
            }

        }
    }
}
