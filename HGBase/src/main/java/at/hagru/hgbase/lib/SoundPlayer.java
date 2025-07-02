package at.hagru.hgbase.lib;

/**
 * An interface to play a sound.
 */
@FunctionalInterface
public interface SoundPlayer {
    /**
     * Plays the specified sound.
     *
     * @param sound The sound to play.
     */
    void play(String sound);
}
