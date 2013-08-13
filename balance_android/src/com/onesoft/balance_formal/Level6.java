package com.onesoft.balance_formal;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Level6 extends AbstractLevel {
	private Body m_theFulcrum = null;
	private Body m_theBoard = null;

	private ArrayList<Ball> m_theBalls = null;

	protected Random m_random = null;

	protected static int INTERVAL = 8;

	public Level6(GameLoopScreen s) {
		super("data/game_background.png", s);

		m_theBalls = new ArrayList<Ball>();
		m_random = new Random();

		buildScene();
	}

	@Override
	protected void buildScene() {
		// define the Fulcrum
		BodyDef bdFulcrum = new BodyDef();
		bdFulcrum.type = BodyType.StaticBody;
		bdFulcrum.position.set(Settings.SCENE_WIDTH / 2,
				Settings.SCENE_HEIGHT / 2 - 0.5f);

		PolygonShape shape = new PolygonShape();

		Vector2[] points = new Vector2[3];
		for (int i = 0; i < 3; ++i) {
			points[i] = new Vector2();
		}
		points[0].set(0, 0);
		points[1].set(-0.15f, -0.3f);
		points[2].set(0.15f, -0.3f);
		shape.set(points);

		FixtureDef fdFulcrum = new FixtureDef();
		fdFulcrum.shape = shape;
		fdFulcrum.friction = 1.0f;
		fdFulcrum.density = 1.0f;
		m_theFulcrum = Resources.m_theSimulation.addBody(bdFulcrum, fdFulcrum);
		m_theFulcrum.setUserData("F");

		// //////////////////////////////////////////////////////////////////////////////
		// define the board
		BodyDef bdBoard = new BodyDef();

		bdBoard.type = BodyType.DynamicBody;
		bdBoard.position.set(Settings.SCENE_WIDTH / 2,
				Settings.SCENE_HEIGHT / 2 - 0.35f);

		PolygonShape psBoard = new PolygonShape();

		Vector2 [] pointsBoard = new Vector2[5];
		
		for(int i = 0;i < 5;++i)
		{
			pointsBoard[i] = new Vector2();
		}
		
		pointsBoard[0].x = Settings.BOARD_WIDTH;
		pointsBoard[0].y = 0;
		pointsBoard[1].x = 0;
		pointsBoard[1].y = Settings.BOARD_HEIGHT/3*2;
		pointsBoard[2].x = -Settings.BOARD_WIDTH;
		pointsBoard[2].y = 0;
		pointsBoard[3].x = -Settings.BOARD_WIDTH;
		pointsBoard[3].y = -Settings.BOARD_HEIGHT;
		pointsBoard[4].x = Settings.BOARD_WIDTH;
		pointsBoard[4].y = -Settings.BOARD_HEIGHT;
		
		psBoard.set(pointsBoard);
		
		FixtureDef fdBoard = new FixtureDef();
		fdBoard.shape = psBoard;
		fdBoard.density = 2.0f;
		fdBoard.friction = 1.0f;
		m_theBoard = Resources.m_theSimulation.addBody(bdBoard, fdBoard);
		m_theBoard.setUserData("BOARD");
	}

	protected void addBall() {
		Ball b = new Ball();

		CircleShape cs = new CircleShape();
		cs.setPosition(new Vector2(0, 0));
		cs.setRadius(Settings.BALL_RADIUS);

		FixtureDef fdBall = new FixtureDef();
		fdBall.shape = cs;

		byte l = (byte) m_random.nextInt(3);

		b.m_ballType = l;

		if (l == Settings.LEVEL_METAL_BALL) {
			fdBall.density = Settings.DENSITY_METAL;
			fdBall.restitution = Settings.RESTITUTION_METAL;
		} else if (l == Settings.LEVEL_PINGPANG_BALL) {
			fdBall.density = Settings.DENSITY_PINGPANG;
			fdBall.restitution = Settings.RESTITUTION_PINGPANG;
		} else if (l == Settings.LEVEL_RUBBER_BALL) {
			fdBall.density = Settings.DENSITY_RUBBER;
			fdBall.restitution = Settings.RESTITUTION_RUBBER;
		}

		BodyDef bdBall = new BodyDef();
		bdBall.type = BodyType.DynamicBody;
		bdBall.position.set(Settings.SCENE_WIDTH / 2 + 0.8f,
				Settings.SCENE_HEIGHT - 0.2f);
		Body ball = Resources.m_theSimulation.addBody(bdBall, fdBall);

		ball.setUserData("BALL");
		ball.setBullet(true);
		
		b.m_theBall = ball;

		m_theBalls.add(b);
	}

	protected void removeAllBall() {
		for (int i = 0; i < m_theBalls.size(); ++i) {
			Resources.m_theSimulation.removeBody(m_theBalls.get(i).m_theBall);
		}

		m_theBalls.clear();
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		int currentBallCount = m_theBalls.size() - 1;

		if ((m_timeEllapse / INTERVAL) > currentBallCount) {
			addBall();
		}
	}

	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);
		
		Resources.m_theSimulation.render();

		float angle = m_theBoard.getAngle();

		float x = m_theBoard.getPosition().x;
		float y = m_theBoard.getPosition().y;

		TextureRegion texBoard = new TextureRegion(Resources.m_texBoard);

		Render.draw(texBoard, x - Settings.BOARD_WIDTH, y
				- Settings.BOARD_HEIGHT, Settings.BOARD_WIDTH * 2,
				Settings.BOARD_HEIGHT * 2, angle);

		for (int i = 0; i < m_theBalls.size(); ++i) {
			Ball b = m_theBalls.get(i);

			angle = b.m_theBall.getAngle();
			x = b.m_theBall.getPosition().x;
			y = b.m_theBall.getPosition().y;

			TextureRegion texBall = null;
			if (b.m_ballType == Settings.LEVEL_METAL_BALL) {
				texBall = new TextureRegion(Resources.m_texMetal);
			} else if (b.m_ballType == Settings.LEVEL_PINGPANG_BALL) {
				texBall = new TextureRegion(Resources.m_texPingpang);
			} else if (b.m_ballType == Settings.LEVEL_RUBBER_BALL) {
				texBall = new TextureRegion(Resources.m_texRabber);
			}

			Render.draw(texBall, x - Settings.BALL_RADIUS, y
					- Settings.BALL_RADIUS, Settings.BALL_RADIUS * 2,
					Settings.BALL_RADIUS * 2, angle);
		}

		TextureRegion texF = new TextureRegion(Resources.m_texFulcrum);
		x = m_theFulcrum.getPosition().x;
		y = m_theFulcrum.getPosition().y;
		Render.draw(texF, x - 0.15f, y - 0.3f, 0.3f, 0.3f);
	}

	@Override
	public void dispose() {
		super.dispose();

		Resources.m_theSimulation.removeBody(m_theBoard);
		Resources.m_theSimulation.removeBody(m_theFulcrum);

		removeAllBall();

		m_theBalls.clear();
	}

	@Override
	public void restart() {
		super.restart();

		Resources.m_theSimulation.removeBody(m_theBoard);
		Resources.m_theSimulation.removeBody(m_theFulcrum);

		removeAllBall();
		m_theBalls.clear();

		buildScene();
	}

	private static class Ball {
		public Body m_theBall = null;
		public byte m_ballType = 0;
	}
}
