package com.game.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.game.Game;
import com.game.entity.Bullet;
import com.game.entity.GameObject;
import com.game.entity.Light;
import com.game.entity.Tank;
import com.game.entity.player.Player;
import com.game.level.Level;
import com.game.states.State;

public class Play extends State {

	private World world;
	private Box2DDebugRenderer renderer;
	public static boolean GameOver = false;

	private SpriteBatch batch;
	private Sprite GameOverTex;
	private Level level;

	public Play() {
		GameOverTex = new Sprite(new Texture("res/GameOver.png"));
		GameOverTex.setPosition(Game.width/2-GameOverTex.getWidth()/2, Game.height/2);
		world = new World(new Vector2(0, 0), false);
		renderer = new Box2DDebugRenderer();
		batch = new SpriteBatch();

		new Player(world);
		level = new Level(world, null);

		new Tank(world);
		new Tank(world, 0.3f, 0.6f);
		new Tank(world, 0.9f, 0.4f);
		createCollisionListener();

	}

	private void createCollisionListener() {
		world.setContactListener(new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();
				if (fixtureA.getBody().getUserData() instanceof Bullet
						&& fixtureB.getBody().getUserData() instanceof Tank) {
					((Bullet) fixtureA.getBody().getUserData()).destroy();
					((Tank) fixtureB.getBody().getUserData()).hit();
				} else if (fixtureB.getBody().getUserData() instanceof Bullet
						&& fixtureA.getBody().getUserData() instanceof Tank) {
					((Bullet) fixtureB.getBody().getUserData()).destroy();
					((Tank) fixtureA.getBody().getUserData()).hit();

				}
			}

			public void endContact(Contact contact) {
			}

			public void preSolve(Contact contact, Manifold oldManifold) {
			}

			public void postSolve(Contact contact, ContactImpulse impulse) {
			}

		});
	}

	float x = 0, y = 0;
	int att = 10000;

	public void render(OrthographicCamera camera) {
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl20.glEnable(GL20.GL_TEXTURE_2D);
		level.render();
		// renderer.render(world, camera.combined);
		world.step(1 / 60f, 60, 20);
		if(GameOver) {
		
		batch.begin();
		GameOverTex.draw(batch);
		batch.end();
		}

	}

	int timer = 10;

	public void update(OrthographicCamera camera) {
		for (int i = 0; i < GameObject.objects.size(); i++) {
			GameObject.objects.get(i).update(camera);
		}
		if (timer > 0)
			timer--;
		x = (Gdx.input.getX()) / (float) Game.width;
		y = -(Gdx.input.getY()) / (float) Game.height + 1;
/*
		if (Gdx.input.isButtonPressed(0) && timer == 0) {
			timer = 10;
			Light newLight = new Light();
			newLight.setPos(x, y);

			newLight.setColor(
					new Vector3((float) Math.random() * 255, (float) Math.random() * 255, (float) Math.random() * 255));
			newLight.setAttenuation(
					new Vector3(0, att / 2 + (float) Math.random() * att, att / 2 + (float) Math.random() * att));
		}
		*/
		
		
		

		for (int i = 0; i < GameObject.objects.size(); i++) {
			if (GameObject.objects.get(i).isToDestroy()) {
				world.destroyBody(GameObject.objects.get(i).getBody());
				GameObject.objects.remove(i);
			}
		}

		for (int i = 0; i < Light.lights.size(); i++) {
			if (Light.lights.get(i).isToDestroy()) {
				Light.lights.remove(i);
			}
		}

	}

	public void onClose() {
		world.dispose();
		level.destroy();
	}

	public int getID() {
		return 0;
	}

}
