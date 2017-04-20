package com.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.game.Game;
import com.game.GIF.GifDecoder;

public class Tank extends GameObject {
	public float speed = 0.1f;
	float pw = 0.25f, ph = 0.28f;
	protected Sprite tankGun;

	protected float gunRot = 0;

	protected boolean dying = false;
	private Sprite heart;
	protected int lives = 4;
	public Tank(World world) {
		super(new Sprite(new Texture("res/textures/tankBase.png")), BodyType.DynamicBody, world, 16, 32);
		tankGun = new Sprite(new Texture("res/textures/tankGun.png"));
		heart = new Sprite(new Texture("res/heart.png"));

		super.addBodyDef(0, 0, pw, ph, 1, 1, 0);
		body.setAngularDamping(1.8f);
		body.setLinearDamping(5);
	}

	public Tank(World world, float x, float y) {
		super(new Sprite(new Texture("res/textures/tankBase.png")), BodyType.DynamicBody, world, 16, 32);
		tankGun = new Sprite(new Texture("res/textures/tankGun.png"));
		heart = new Sprite(new Texture("res/heart.png"));

		super.addBodyDef(0, 0, pw, ph, 1, 1, 0);
		body.setAngularDamping(1.8f);
		body.setLinearDamping(5);
	}

	public void render(OrthographicCamera camera, SpriteBatch batch) {
		if (!dying) {
			sprite.setPosition(
					(x + camera.viewportWidth / 2 - pw / 2 - camera.position.x) * Game.scale - sprite.getWidth() / 4,
					(y + camera.viewportHeight / 2 - ph / 2 - camera.position.y) * Game.scale - sprite.getHeight() / 4);

			sprite.setRotation((float) Math.toDegrees(body.getAngle()));
			sprite.setSize(50, 50);
			sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
			sprite.draw(batch);

			tankGun.setPosition(sprite.getX() + 10, sprite.getY() + 10);
			tankGun.setOrigin(tankGun.getWidth() / 2, (tankGun.getHeight() / 2) - 10);
			tankGun.setRotation(gunRot);
			tankGun.setSize(30, 55);
			tankGun.draw(batch);
			renderLives(camera,batch);
		}
		
	}
	
	public void renderLives(OrthographicCamera camera,SpriteBatch batch) {
		for(int i = 0;i < lives;i++) {
			heart.setSize(10, 10);
			heart.setPosition(sprite.getX()+sprite.getWidth()/2+heart.getWidth()*i, sprite.getY()+sprite.getHeight()+10);
			heart.draw(batch);
		}
	}

	private int bulletCooldown = 20, current = 0;

	public void update(OrthographicCamera camera) {
		if (!dying) {
			x = body.getPosition().x;
			y = body.getPosition().y;
		}

	}

	public void shoot() {
		new Bullet(world, tankGun.getVertices()[5] / 100 + Game.camera.position.x - Game.width / Game.scale / 2,
				tankGun.getVertices()[6] / 100 + Game.camera.position.y - Game.height / Game.scale / 2,
				Math.toRadians(-gunRot));

	}

	public void hit() {
		lives--;
		if(lives <= 0 ) {
			destroy();
			dying = true;
		}
	}
}
