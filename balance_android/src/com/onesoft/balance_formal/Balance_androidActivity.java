package com.onesoft.balance_formal;

import com.badlogic.gdx.backends.android.AndroidApplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;

public class Balance_androidActivity extends AndroidApplication {
	protected GameBalance game = null;

	protected PowerManager m_PowerManager = null;
	protected PowerManager.WakeLock m_wakeLock = null;

	protected String m_strPrefName = "BEST_SCORE";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		m_PowerManager = (PowerManager) getSystemService(POWER_SERVICE);
		m_wakeLock = m_PowerManager.newWakeLock(
				PowerManager.SCREEN_DIM_WAKE_LOCK, "Balance");

		m_wakeLock.acquire();

		game = new GameBalance(this);
		initialize(game, false);
	}

	@Override
	public void onDestroy() {
		m_wakeLock.release();

		super.onDestroy();
	}

	@Override
	public void onPause() {
		m_wakeLock.release();

		super.onPause();
	}

	@Override
	public void onResume() {
		m_wakeLock.acquire();

		super.onResume();
	}

	@Override
	public void onBackPressed() {
		if (game.getCurrentScreen() instanceof GameLoopScreen) {
			GameLoopScreen s = (GameLoopScreen) game.getCurrentScreen();

			s.stop();
		} else {
			super.onBackPressed();
		}
	}

	public long getValue(String key) {
		SharedPreferences sp = getSharedPreferences(m_strPrefName, 0);

		return sp.getLong(key, 0);
	}

	public void putValue(String key, long value) {
		SharedPreferences sp = getSharedPreferences(m_strPrefName, 0);

		SharedPreferences.Editor e = sp.edit();

		e.putLong(key, value);

		e.commit();
	}
}