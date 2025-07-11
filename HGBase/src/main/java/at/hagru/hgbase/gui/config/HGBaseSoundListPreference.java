package at.hagru.hgbase.gui.config;

import android.content.Context;
import android.preference.ListPreference;
import android.view.View;

import at.hagru.hgbase.R;
import at.hagru.hgbase.lib.HGBaseTools;
import at.hagru.hgbase.lib.SoundPlayer;

/**
 * A preference to select a sound with has a button to play the selected sound.
 */
public class HGBaseSoundListPreference extends ListPreference {
    /**
     * The player which is used to play the sound.
     */
    private final SoundPlayer soundPlayer;

    /**
     * Constructs a new instance.
     *
     * @param context The context for the preference.
     */
    public HGBaseSoundListPreference(Context context, SoundPlayer soundPlayer) {
        super(context);
        this.soundPlayer = soundPlayer;
        setWidgetLayoutResource(R.layout.sound_list_preference_play_button);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        view.findViewById(R.id.play_button).setOnClickListener(v -> playSound(getValue()));
    }

    /**
     * Plays the sound with the set sound player.
     *
     * @param soundName The name of the sound to play.
     */
    private void playSound(String soundName) {
        if (soundPlayer == null || !HGBaseTools.hasContent(soundName)) {
            return;
        }
        soundPlayer.play(soundName);
    }
}
