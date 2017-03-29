package com.pottda.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.pottda.game.MyGame;

public class AndroidLauncher extends AndroidApplication {
<<<<<<< HEAD:android/src/com/pottda/game/AndroidLauncher.java
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new MyGame(), config);
	}
=======
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;
        initialize(new Shooter(), config);
    }
>>>>>>> 3ad64f2cf269b5f587aaa9148cadf0e27f6a873c:android/src/com/panicontdancefloor/shooter/AndroidLauncher.java
}
