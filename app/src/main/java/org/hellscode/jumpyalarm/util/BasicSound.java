package org.hellscode.jumpyalarm.util;

import androidx.annotation.NonNull;

import org.hellscode.util.StringUtil;

/**
 * Represents a basic sound to play in the assets folder
 */
public class BasicSound {
    static final String TYPESTRING = "basic";

    private String _displayName;
    private String _path;

    /**
     * Constructor
     * @param path path
     */
    BasicSound(String path) {
        _path = path;
        String displayName = null;
        if (path != null) {
            int lastDotIndex = path.lastIndexOf('.');
            if (lastDotIndex >= 0) {
                displayName = path.substring(0, lastDotIndex);
            } else {
                displayName = path;
            }
        }

        _displayName = displayName;
    }

    /**
     * Get display name
     * @return display name
     */
    public String getDisplayName() { return _displayName; }

    /**
     * Get the path of the sound file
     */
    public String getPath() { return _path; }

    public String getSoundPathForStorage() {
        return String.format("%s:%s",TYPESTRING, _path);
    }

    @Override
    public boolean equals(Object o) {
        boolean retVal = false;

        if (o instanceof BasicSound) {
            BasicSound castObj = (BasicSound)o;
            retVal = StringUtil.areEqual(_path, castObj._path);
        }

        return retVal;
    }

    /**
     * Convert to string
     * @return string
     */
    @Override
    public @NonNull
    String toString() {
        return _displayName != null ? _displayName : "";
    }
}
