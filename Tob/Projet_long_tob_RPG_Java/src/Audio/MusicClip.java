package Audio;

import javax.sound.sampled.Clip;

public class MusicClip extends AudioClip{

	public MusicClip(Clip clip) {
		super(clip);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	@Override
	protected float getVolume(GameSettings gameSettings) {
		return gameSettings.getMusicVolume();
	}

}
