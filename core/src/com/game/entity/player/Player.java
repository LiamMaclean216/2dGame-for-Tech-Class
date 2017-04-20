package com.game.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.game.Game;
import com.game.entity.Bullet;
import com.game.entity.GameObject;
import com.game.entity.Tank;
import com.game.gamestates.Play;

public class Player extends Tank {
	public float speed = 0.1f;
	float pw = 0.25f, ph = 0.3f;

	public Player(World world) {
		super(world);

		/*super.addBodyDef(0.1f, 0.05f, pw, ph, 1, 1, 0);

		body.setFixedRotation(true);
		body.setLinearDamping(5);*/
	}

	private int bulletCooldown = 5, current = 0;

	public void update(OrthographicCamera camera) {
		
		x = body.getPosition().x;
		y = body.getPosition().y;
		
		float angle = body.getAngle();
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			y += Math.sin(body.getAngle() + Math.toRadians(90)) * speed;
			x += Math.cos(body.getAngle() + Math.toRadians(90)) * speed;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			y -= Math.sin(body.getAngle() + Math.toRadians(90)) * speed;
			x -= Math.cos(body.getAngle() + Math.toRadians(90)) * speed;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			angle -= 0.1f;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			angle += 0.1f;
		}
		gunRot = (float) -Math
				.toDegrees(Math.atan2(Gdx.input.getY() - tankGun.getY(), Gdx.input.getX() - tankGun.getX())) - 90;
		body.setTransform(new Vector2(x, y), angle);
		
		if (current <= bulletCooldown)
			current++;
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && current >= bulletCooldown) {
			current = 0;
			shoot();
		}

		Game.camera.position.set(new Vector2(x, y), 0);
		Game.camera.update();
	}
	
	public void hit() {
		lives--;
		if(lives <= 0 ) {
			destroy();
			dying = true;
			Play.GameOver = true;
		}
	}

}
