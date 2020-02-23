package org.hellscode.jumpyalarm.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;

import androidx.annotation.NonNull;

import java.io.IOException;

/**
 * Aid in resolving sound resources
 */
public class SoundResolver {
    private Context _context;

    /**
     * Constructor
     * @param context context.  This value can never be null.
     */
    public SoundResolver(@NonNull Context context) {
        _context = context;
    }

    public static BasicSound createSoundFromPath(String fullPath) {
        BasicSound retVal = null;
        String basicSoundPrefix = BasicSound.TYPESTRING + ":";

        if (fullPath == null) {
            fullPath = basicSoundPrefix + "default.mp3";
        }

        if (fullPath.startsWith(basicSoundPrefix)
        ) {
            int afterPos = basicSoundPrefix.length();
            retVal = new BasicSound(fullPath.substring(afterPos));
        }

        return retVal;
    }

    @SuppressWarnings("ManualArrayCopy")
    public BasicSound[] loadBasicSounds()
            throws IOException {

        AssetManager am = _context.getAssets();
        String[] origFileNames = am.list("bells");
        int num = origFileNames != null ? origFileNames.length : 0;

        int defaultIdx = -1;

        for (int i = 0; defaultIdx == -1 && i < num; ++i) {
            if (origFileNames[i] != null && origFileNames[i].toLowerCase().equals("default.mp3")) {
                defaultIdx = i;
            }
        }

        // We want to place the default at the beginning, if it exists.
        // if defaultIdx is 0, we can still do nothing because it's already at the start
        if (defaultIdx > 0) {
            String defaultFileName = origFileNames[defaultIdx];

            // shift everything above the default down one and put the default at the beginning
            for (int i = defaultIdx; i > 0; --i) {
                origFileNames[i] = origFileNames[i - 1];
            }

            origFileNames[0] = defaultFileName;
        }

        BasicSound[] retVal = new BasicSound[num];
        for(int i = 0; i < num; ++i) {
            retVal[i] = new BasicSound(origFileNames[i]);
        }

        return retVal;
    }

}
