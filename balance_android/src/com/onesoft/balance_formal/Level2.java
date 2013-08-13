package com.onesoft.balance_formal;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Level2 extends AbstractLevel {
	private Body m_theFulcrum = null;
	private Body m_theBall1 = null;
	private Body m_theBall2 = null;
	private Body m_theBoard = null;

	private boolean m_bIsBomb2 = false;
	
	public Level2(GameLoopScreen s) {
		super("data/game_background.png", s);

		buildScene();
	}

	@Override
	protected void buildScene() {
		// define the Fulcrum
		BodyDef bdFulcrum = new BodyDef();
		bdFulcrum.type = BodyType.StaticBody;
		bdFulcrum.position.set(Settings.SCENE_WIDTH / 2,
				Settings.SCENE_HEIGHT / 2);

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

		// define the board
		BodyDef bdBoard = new BodyDef();

		bdBoard.type = BodyType.DynamicBody;
		bdBoard.position.set(Settings.SCENE_WIDTH / 2,
				Settings.SCENE_HEIGHT / 2 + 0.15f);

		PolygonShape psBoard = new PolygonShape();

		psBoard.setAsBox(Settings.BOARD_WIDTH, Settings.BOARD_HEIGHT);

		FixtureDef fdBoard = new FixtureDef();
		fdBoard.shape = psBoard;
		fdBoard.density = 2.0f;
		fdBoard.friction = 1.0f;
		m_theBoard = Resources.m_theSimulation.addBody(bdBoard, fdBoard);
		m_theBoard.setUserData("BOARD");

		// define the ball
		CircleShape cs = new CircleShape();
		// cs.setPosition(new
		// Vector2(Settings.SCREEN_WIDTH/2+80,Settings.SCREEN_HEIGHT-20.0f));
		cs.setPosition(new Vector2(0, 0));
		cs.setRadius(Settings.BALL_RADIUS);

		FixtureDef fdBall = new FixtureDef();
		fdBall.shape = cs;

		if (Settings.LEVEL == Settings.LEVEL_METAL_BALL) {
			fdBall.density = Settings.DENSITY_METAL;
			fdBall.restitution = Settings.RESTITUTION_METAL;
		} else if (Settings.LEVEL == Settings.LEVEL_PINGPANG_BALL) {
			fdBall.density = Settings.DENSITY_PINGPANG;
			fdBall.restitution = Settings.RESTITUTION_PINGPANG;
		} else if (Settings.LEVEL == Settings.LEVEL_RUBBER_BALL) {
			fdBall.density = Settings.DENSITY_RUBBER;
			fdBall.restitution = Settings.RESTITUTION_RUBBER;
		}

		BodyDef bdBall = new BodyDef();
		bdBall.type = BodyType.DynamicBody;
		bdBall.position.set(Settings.SCENE_WIDTH / 2 + 0.8f,
				Settings.SCENE_HEIGHT - 0.2f);
		m_theBall1 = Resources.m_theSimulation.addBody(bdBall, fdBall);

		bdBall.position.set(Settings.SCENE_WIDTH / 2 - 0.15f,
				Settings.SCENE_HEIGHT - 0.2f);

		m_theBall2 = Resources.m_theSimulation.addBody(bdBall, fdBall);
		// m_theBall.setBullet(true);
		m_theBall1.setUserData("BALL");
		m_theBall2.setUserData("BALL");
	}

	@Override
	public void update(float deltaTime){
		super.update(deltaTime);
		
		if (!m_bIsBombed) {
			float x = m_theBall1.getPosition().x;
			float y = m_theBall1.getPosition().y;

			if (m_fireArea.contains(x, y)) {
				m_bIsBombed = true;

				Vector2 p = m_theBoard.getPosition();
				
				m_theBoard.applyForce(new Vector2((x - p.x) * 1000,
						(y - p.y) * 1000), m_theBoard.getWorldPoint(p));
			}
		}
		
		if(!m_bIsBomb2)
		{
			float x = m_theBall2.getPosition().x;
			float y = m_theBall2.getPosition().y;

			if (m_fireArea.contains(x, y)) {
				m_bIsBomb2 = true;

				Vector2 p = m_theBoard.getPosition();
				
				m_theBoard.applyForce(new Vector2((x - p.x) * 1000,
						(y - p.y) * 1000), m_theBoard.getWorldPoint(p));
			}
		}
	}
	
	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);

		float angle = m_theBoard.getAngle();

		float x = m_theBoard.getPosition().x;
		float y = m_theBoard.getPosition().y;

		TextureRegion texBoard = new TextureRegion(Resources.m_texBoard);

		Render.draw(texBoard, x - Settings.BOARD_WIDTH, y
				- Settings.BOARD_HEIGHT, Settings.BOARD_WIDTH * 2,
				Settings.BOARD_HEIGHT * 2, angle);

		TextureRegion texBall = null;
		float width = Settings.BALL_RADIUS * 2;
		float height = Settings.BALL_RADIUS * 2;
		// draw the first ball1
		angle = m_theBall1.getAngle();
		x = m_theBall1.getPosition().x;
		y = m_theBall1.getPosition().y;

		if (m_bIsBombed) {
			texBall = m_animaBomb.getKeyFrame(m_bombFrameDuration, true);
			width = 0.6f;
			height = 0.6f;
		} else {
			if (Settings.LEVEL == Settings.LEVEL_METAL_BALL) {
				texBall = new TextureRegion(Resources.m_texMetal);
			} else if (Settings.LEVEL == Settings.LEVEL_PINGPANG_BALL) {
				texBall = new TextureRegion(Resources.m_texPingpang);
			} else if (Settings.LEVEL == Settings.LEVEL_RUBBER_BALL) {
				texBall = new TextureRegion(Resources.m_texRabber);
			}
		}

		Render.draw(texBall, x - Settings.BALL_RADIUS,
				y - Settings.BALL_RADIUS, width,
				height, angle);

		// draw the second ball ball2
		angle = m_theBall2.getAngle();
		x = m_theBall2.getPosition().x;
		y = m_theBall2.getPosition().y;

		width = Settings.BALL_RADIUS * 2;
		height = Settings.BALL_RADIUS * 2;
		
		if (m_bIsBomb2) {
			texBall = m_animaBomb.getKeyFrame(m_bombFrameDuration, true);
			width = 0.6f;
			height = 0.6f;
		} else {
			if (Settings.LEVEL == Settings.LEVEL_METAL_BALL) {
				texBall = new TextureRegion(Resources.m_texMetal);
			} else if (Settings.LEVEL == Settings.LEVEL_PINGPANG_BALL) {
				texBall = new TextureRegion(Resources.m_texPingpang);
			} else if (Settings.LEVEL == Settings.LEVEL_RUBBER_BALL) {
				texBall = new TextureRegion(Resources.m_texRabber);
			}
		}

		Render.draw(texBall, x - Settings.BALL_RADIUS,
				y - Settings.BALL_RADIUS, width,
				height, angle);

		TextureRegion texF = new TextureRegion(Resources.m_texFulcrum);
		x = m_theFulcrum.getPosition().x;
		y = m_theFulcrum.getPosition().y;
		Render.draw(texF, x - 0.15f, y - 0.3f, 0.3f, 0.3f);
	}

	@Override
	public void dispose() {
		super.dispose();

		Resources.m_theSimulation.removeBody(m_theBall2);
		Resources.m_theSimulation.removeBody(m_theBall1);
		Resources.m_theSimulation.removeBody(m_theBoard);
		Resources.m_theSimulation.removeBody(m_theFulcrum);
	}

	@Override
	public void restart() {
		super.restart();

		m_bIsBomb2 = false;
		m_bIsBombed = false;
		
		Resources.m_theSimulation.removeBody(m_theBall2);
		Resources.m_theSimulation.removeBody(m_theBall1);
		Resources.m_theSimulation.removeBody(m_theBoard);
		Resources.m_theSimulation.removeBody(m_theFulcrum);

		buildScene();
	}
}
