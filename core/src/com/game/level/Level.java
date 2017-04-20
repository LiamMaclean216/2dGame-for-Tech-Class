package com.game.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.game.Game;
import com.game.entity.GameObject;
import com.game.entity.Light;

public class Level {
	private float[] level;

	private ShaderProgram lightShader;
	private SpriteBatch lightBatch;

	private ShaderProgram shadowShader;

	public static final int MAX_LIGHTS = 25;

	private Texture background;
	private ChainShape walls;
	int att = 10000;

	private World world;
	public Level(World world,float[] level) {
		this.world = world;
		this.level = level;
		shadowShader = new ShaderProgram(Gdx.files.internal("shadowvert"), Gdx.files.internal("shadowfrag"));

		lightShader = new ShaderProgram(Gdx.files.internal("vertex"), Gdx.files.internal("frag"));

		lightBatch = new SpriteBatch();
		lightBatch.setShader(lightShader);
		background = new Texture(Gdx.files.internal("res/background.png"));
		
		walls = new ChainShape();
		walls.createChain(new float[] {
				-Game.width/Game.scale/2,-Game.height/Game.scale/2,
				-Game.width/Game.scale/2,Game.height/Game.scale-Game.height/Game.scale/2,
				Game.width/Game.scale-Game.width/Game.scale/2,Game.height/Game.scale-Game.height/Game.scale/2,
				Game.width/Game.scale-Game.width/Game.scale/2,-Game.height/Game.scale/2,
				-Game.width/Game.scale/2,-Game.height/Game.scale/2
				
		});
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = walls;
		world.createBody(def).createFixture(fixture);
		
		for(int i = 0;i < 12;i++) {
			Light newLight = new Light();
			newLight.setPos((float)Math.random(), (float)Math.random());

			newLight.setColor(
					new Vector3((float) Math.random() * 255, (float) Math.random() * 255, (float) Math.random() * 255));
			newLight.setAttenuation(
					new Vector3(0, att / 2 + (float) Math.random() * att, att / 2 + (float) Math.random() * att));
		}
	}

	public void render() {
		lightBatch.begin();
		lightBatch.draw(background, -Game.camera.position.x * 100, -Game.camera.position.y * 100, Game.width,
				Game.height);
		// player.render(camera, batch);
		for (int i = 0; i < GameObject.objects.size(); i++) {
			GameObject.objects.get(i).render(Game.camera, lightBatch);
		}
		renderLights();

		lightBatch.end();
		renderShadows();
	}

	public void renderLights() {

		for (int i = 0; i < MAX_LIGHTS; ++i) {
			if(i < Light.lights.size()) {
			lightShader.setUniformf("lightPos[" + i + "]",
					new Vector2(Light.lights.get(i).getPos().x - Game.camera.position.x / Game.camera.viewportWidth,
							Light.lights.get(i).getPos().y - Game.camera.position.y / Game.camera.viewportHeight));
			lightShader.setUniformf("lightColor[" + i + "]", Light.lights.get(i).getColor());
			lightShader.setUniformf("lightAttenuation[" + i + "]", Light.lights.get(i).getAttenuation());
			} else {
				lightShader.setUniformf("lightPos[" + i + "]",
						new Vector2(0,0));
			}
		}
		lightShader.setUniformf("dimensions", new Vector2(Game.width, Game.height));

	}

	public void renderShadows() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		for (int j = 0; j < GameObject.objects.size(); ++j) {
			for (int i = 0; i < Light.lights.size(); i++) {
				shadowShader.begin();
				shadowShader.setUniformf("lightPos", Light.lights.get(i).getPos());
				shadowShader.setUniformMatrix("u_projTrans", lightBatch.getProjectionMatrix());
				if (GameObject.objects.get(j).getVertices().length > 0) {
					Light.lights.get(i).generateShadowGLSL(GameObject.objects.get(j).getVertices(), Game.camera)
							.render(shadowShader, GL20.GL_TRIANGLES);
				}
				shadowShader.end();
			}
		}
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	public void destroy() {
		lightBatch.dispose();
	}

}
