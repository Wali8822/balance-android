package com.onesoft.balance_formal;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class Render {
	public static SpriteBatch m_batch = null;

	public static Random m_rand = null;
	public static BitmapFont m_font = null;

	public static void InitRender() {
		m_batch = new SpriteBatch();

		m_rand = new Random();

		m_font = new BitmapFont(Gdx.files.internal("data/font.fnt"),
				Gdx.files.internal("data/font.png"), false);
	}

	public static void dispose() {
		m_batch.dispose();
	}

	public static void reset() {
		Matrix4 p = new Matrix4();
		p.setToOrtho2D(0, 0, Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);

		m_batch.setProjectionMatrix(p);
	}

	public static void reset_font() {
		Matrix4 p = new Matrix4();
		p.setToOrtho2D(0, 0, Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);

		m_batch.setProjectionMatrix(p);
	}

	public static void draw(TextureRegion t, float x, float y, float width,
			float height, float angle) {
		reset();
		m_batch.begin();
		m_batch.draw(t, x, y, width / 2, height / 2, width, height, 1.0f, 1.0f,
				(float) (Math.toDegrees(angle)));
		m_batch.end();
	}

	public static void draw(TextureRegion t, float x, float y, float width,
			float height, float orignX, float orignY, float angle,
			float scaleX, float scaleY) {
		reset();
		m_batch.begin();
		m_batch.draw(t, x, y, orignX, orignY, width, height, scaleX, scaleY,
				(float) (Math.toDegrees(angle)));
		m_batch.end();
	}

	public static void draw(TextureRegion t, float x, float y, float width,
			float height) {
		reset();
		m_batch.begin();
		m_batch.draw(t, x, y, width, height);
		m_batch.end();
	}

	public static void draw(Texture t, float x, float y) {
		reset();
		m_batch.begin();
		m_batch.draw(t, x, y);
		m_batch.end();
	}

	public static BitmapFont.TextBounds draw(String text, float x, float y) {
		BitmapFont.TextBounds b = new BitmapFont.TextBounds();

		reset_font();
		m_batch.begin();
		BitmapFont.TextBounds tmp = m_font.draw(m_batch, text, x, y);
		m_batch.end();

		b.width = tmp.width;
		b.height = tmp.height;

		return b;
	}

	public static BitmapFont.TextBounds getTextBounds(String text) {
		BitmapFont.TextBounds ret = new BitmapFont.TextBounds();

		BitmapFont.TextBounds temp = m_font.getBounds(text);

		ret.width = temp.width;
		ret.height = temp.height;
		return ret;
	}

	public static int randInt() {
		return m_rand.nextInt();
	}

	public static int randInt(int n) {
		return m_rand.nextInt(n);
	}

	public static float randFloat() {
		return m_rand.nextFloat();
	}

	public static boolean proprably(int p) {
		return (Render.randInt() % p == 0);
	}

	public static float getAngle(Vector2 v1, Vector2 v2) {
		Vector2 temp = v2.cpy();

		temp.sub(v1);

		return (float) Math.atan2(temp.y, temp.x);
	}
}
