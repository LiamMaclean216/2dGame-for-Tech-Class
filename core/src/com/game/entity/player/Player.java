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
import com.game.entity.GameObject;

public class Player extends GameObject {
	public float speed = 0.1f;
	public float jump = 1f;
	float pw = 0.3f, ph = 0.4f;

	public Player(World world) {
		super(new Sprite(new Texture("res/player.png")), BodyType.DynamicBody, world, 16, 32);
		// super.addBodyDef(0, -ph / 2, pw, 0.1f, 1, 0);
		super.addBodyDef(0, 0, pw, ph, 1, 1, 0);
		body.setFixedRotation(true);
		body.setLinearDamping(5);
	}

	public void render(OrthographicCamera camera, SpriteBatch batch) {
		sprite.setPosition((x + camera.viewportWidth / 2 - pw / 2 - camera.position.x) * Game.scale,
				(y + camera.viewportHeight / 2 - ph / 2 - camera.position.y) * Game.scale);
		sprite.setRotation((float) Math.toDegrees(body.getAngle()));
		sprite.setSize(ph * 100, pw * 100);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.draw(batch);
	}

	public void update(OrthographicCamera camera) {
		x = body.getPosition().x;
		y = body.getPosition().y;
		float angle = body.getAngle();
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			y += Math.sin(body.getAngle()) * speed;
			x += Math.cos(body.getAngle()) * speed;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			angle -= 0.1f;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			angle += 0.1f;
		}

		body.setTransform(new Vector2(x, y), angle);
		Game.camera.position.set(new Vector2(x,y), 0);
		Game.camera.update();
	}
	
	public double[] getVertices() {
		if(sprite!=null){
		
			double[] vert = super.getVertices();
			for(int i = 0;i < vert.length;i+=2) {
				vert[i] += Game.camera.position.x*Game.scale;
				vert[i+1] += Game.camera.position.y*Game.scale;
			}
			return vert;
		}
		return new double[0];
	}
}
