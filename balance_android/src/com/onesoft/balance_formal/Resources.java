package com.onesoft.balance_formal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.graphics.Texture;

public class Resources {
	public static Simulation m_theSimulation = null;
	public static UI m_theUI = null;

	public static AndroidApplication m_theApp = null;

	public static Texture m_texBoard = null;

	public static Texture m_texPingpang = null;
	public static Texture m_texMetal = null;
	public static Texture m_texRabber = null;
	public static Texture m_texFulcrum = null;

	public static void load() {
		m_theSimulation = new Simulation();
		m_theUI = new UI();

		m_texBoard = loadTexture("data/board.png");

		m_texPingpang = loadTexture("data/1.png");
		m_texMetal = loadTexture("data/2.png");
		m_texRabber = loadTexture("data/3.png");
		m_texFulcrum = loadTexture("data/4.png");
	}

	public static void dispose() {
		m_theSimulation.dispose();
		m_theUI.dispose();

		m_texBoard.dispose();
		m_texMetal.dispose();
		m_texPingpang.dispose();
		m_texRabber.dispose();
		m_texFulcrum.dispose();
	}

	public static void vibrate(int time) {
		if (Settings.bVibrateEnable
				&& Gdx.app.getType() == ApplicationType.Android) {
			Gdx.input.vibrate(time);
		}
	}

	public static void playButtonClick() {
		vibrate(60);
	}

	public static void playSound(Sound s) {
		if (Settings.bSoundEnabled)
			s.play();
	}

	public static Texture loadTexture(String fileName) {
		return new Texture(Gdx.files.internal(fileName));
	}

}
