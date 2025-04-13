package at.hagru.hgbase.lib;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import at.hagru.hgbase.android.HGBaseAppTools;
import at.hagru.hgbase.android.HGBaseResources;

/**
 * For playing sound using Android library.
 *
 * @author hagru
 */
public final class HGBaseSound {

    @SuppressWarnings("deprecation")
    private static final SoundPool soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
    private static final Map<Integer, Integer> soundPoolMap = new HashMap<>();
    private static final Map<String, Integer> soundPoolAssetsMap = new HashMap<>();

    /**
     * Prevent instantiation.
     */
    private HGBaseSound() {
        super();
    }

    /**
     * Plays an audio file by specifying the name.
     *
     * @param audioFile the name of the audio file (raw resource)
     */
    public static void playAudio(String audioFile) {
        int resId = HGBaseResources.getResourceIdByName(audioFile, HGBaseResources.RAW);
        if (resId != 0) {
            playAudio(resId);
        } else {
            Integer soundId = soundPoolAssetsMap.get(audioFile);
            if (soundId == null) {
                AssetManager am = HGBaseAppTools.getContext().getResources().getAssets();
                AssetFileDescriptor afd;
                try {
                    afd = am.openFd(HGBaseFileTools.correctAssetsPath(audioFile));
                    soundId = soundPool.load(afd, 1);
                    if (soundId > 0) {
                        HGBaseTools.delay(100);
                        soundPoolAssetsMap.put(audioFile, soundId);
                    }
                } catch (IOException e) {
                    HGBaseLog.logError("Could not open file in assets directory: " + audioFile);
                }

            }
            if (soundId != null && soundId > 0) {
                soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
            }
        }
    }

    /**
     * Plays an audio file by passing the raw resource id.
     *
     * @param resId the raw resource id of the audio file
     */
    public static void playAudio(int resId) {
        AudioManager am = (AudioManager) HGBaseAppTools.getContext().getSystemService(Context.AUDIO_SERVICE);
        if (am.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
            Integer soundId = soundPoolMap.get(resId);
            if (soundId == null) {
                soundId = soundPool.load(HGBaseAppTools.getContext(), resId, 1);
                HGBaseTools.delay(100);
                soundPoolMap.put(resId, soundId);
            }
            soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
        }
    }

    /**
     * Returns if the given audio file exists in the assets folder.
     *
     * @param audioFile the file path to check
     * @return true if the file exists in the assets folder
     */
    public static boolean existsSoundFile(String audioFile) {
        Integer soundId = soundPoolAssetsMap.get(audioFile);
        return (soundId != null && soundId > 0);
    }
}
