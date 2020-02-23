package org.hellscode.jumpyalarm.ui.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import org.hellscode.jumpyalarm.R;
import org.hellscode.jumpyalarm.databinding.ActivitySoundSelectionBinding;
import org.hellscode.jumpyalarm.util.BasicSound;
import org.hellscode.jumpyalarm.util.SoundResolver;
import org.hellscode.util.StringUtil;
import org.hellscode.util.ui.ErrorDialog;

import java.io.IOException;

/**
 * Where the user selects a sound for a particular alarm to play
 */
public class SoundSelectionActivity extends AppCompatActivity {

    static class IntentConstants {
        static String ID = "ID";
        static String SOUND = "Sound";
    }

    ActivitySoundSelectionBinding _binding;

    private long _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_sound_selection);

        String initialSound = null;
        Intent intent = getIntent();
        if (intent != null) {
            _id = intent.getLongExtra(IntentConstants.ID, -1);
            initialSound = intent.getStringExtra(IntentConstants.SOUND);
        }

        SoundResolver resolver = new SoundResolver(this);

        BasicSound[] soundColl = new BasicSound[0];

        try {
            soundColl = resolver.loadBasicSounds();
        } catch(IOException ex) {
            ErrorDialog.handleUnrecoverableError(
                    this,
                    "Couldn't load sound list.",
                    ex);

        }

        int defaultPosition = 0;
        boolean found = false;
        for (int i = 0; !found && i < soundColl.length; ++i) {
            if (StringUtil.areEqual(initialSound, soundColl[i].getSoundPathForStorage())) {
                defaultPosition = i;
                found = true;
            }
        }

        ArrayAdapter<BasicSound> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                soundColl);

        Spinner spinner = _binding.basicSoundSpinner;

        if (spinner != null) {
            spinner.setAdapter(adapter);
            spinner.setSelection(defaultPosition);
        }

    }

    /**
     * Called when confirmation button is pressed
     * @param v view
     */
    public void onSubmit(View v) {

        String soundPath = null;
        if (_binding != null && _binding.basicSoundSpinner != null) {
            int position = _binding.basicSoundSpinner.getSelectedItemPosition();
            BasicSound selectedSound = (BasicSound)_binding.basicSoundSpinner.getItemAtPosition(position);
            if (selectedSound != null) {
                soundPath = selectedSound.getSoundPathForStorage();
            }
        }

        Intent intent = new Intent();
        intent.putExtra(IntentConstants.ID, _id);
        intent.putExtra(IntentConstants.SOUND, soundPath);

        setResult(1, intent);
        finish();
    }

    /**
     * Called when confirmation button is pressed
     * @param v view
     */
    public void onCancel(View v) {
        onBackPressed();
    }
}
