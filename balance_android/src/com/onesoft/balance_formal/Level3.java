package com.onesoft.balance_formal;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Level3 extends AbstractLevel {
	private Body m_theFulcrum = null;
	private Body m_theBall = null;
	private Body m_theBoard = null;

	public Level3(GameLoopScreen s) {
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
		bdBall.position.set(Settings.SCENE_WIDTH / 2,
				Settings.SCENE_HEIGHT - 0.20f);
		m_theBall = Resources.m_theSimulation.addBody(bdBall, fdBall);

		m_theBall.setUserData("BALL");
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
	}

	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);

		Resources.m_theSimulation.render();

		// float angle = m_theBoard.getAngle();
		//
		// float x = m_theBoard.getPosition().x;
		// float y = m_theBoard.getPosition().y;
		//
		// TextureRegion texBoard = new TextureRegion(Resources.m_texBoard);
		//
		// Render.draw(texBoard, x-200, y-20, 200*2, 20*2,angle);
		//
		// angle = m_theBall.getAngle();
		// x = m_theBall.getPosition().x;
		// y = m_theBall.getPosition().y;
		//
		// TextureRegion texBall = null;
		// if(Settings.LEVEL == Settings.LEVEL_METAL_BALL)
		// {
		// texBall = new TextureRegion(Resources.m_texMetal);
		// }
		// else if(Settings.LEVEL == Settings.LEVEL_PINGPANG_BALL)
		// {
		// texBall = new TextureRegion(Resources.m_texPingpang);
		// }
		// else if(Settings.LEVEL == Settings.LEVEL_RUBBER_BALL)
		// {
		// texBall = new TextureRegion(Resources.m_texRabber);
		// }
		//
		// Render.draw(texBall, x - 20, y - 20, 40, 40, angle);
		//
		// TextureRegion texF = new TextureRegion(Resources.m_texFulcrum);
		// x = m_theFulcrum.getPosition().x;
		// y = m_theFulcrum.getPosition().y;
		// Render.draw(texF, x-15, y - 30f, 30, 30);
	}

	@Override
	public void dispose() {
		super.dispose();

		Resources.m_theSimulation.removeBody(m_theBall);
		Resources.m_theSimulation.removeBody(m_theBoard);
		Resources.m_theSimulation.removeBody(m_theFulcrum);
	}

	@Override
	public void restart() {
		super.restart();

		Resources.m_theSimulation.removeBody(m_theBall);
		Resources.m_theSimulation.removeBody(m_theBoard);
		Resources.m_theSimulation.removeBody(m_theFulcrum);

		buildScene();
	}

}
