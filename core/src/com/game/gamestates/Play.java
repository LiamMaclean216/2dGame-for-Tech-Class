package com.game.gamestates;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.game.Game;
import com.game.entity.GameObject;
import com.game.entity.Light;
import com.game.entity.player.Player;
import com.game.states.State;

public class Play extends State {
	private SpriteBatch batch;
	private Texture texture, background;

	private World world;
	private Box2DDebugRenderer renderer;

	private ShaderProgram shader, shadowShader;

	private Player player;

	ShapeRenderer shapeR;
	public static final int MAX_LIGHTS = 100;

	private ArrayList<Light> lights = new ArrayList<Light>();
	Mesh mesh;

	public Play() {

		shader = new ShaderProgram(Gdx.files.internal("vertex"), Gdx.files.internal("frag"));
		shadowShader = new ShaderProgram(Gdx.files.internal("shadowvert"), Gdx.files.internal("shadowfrag"));
		batch = new SpriteBatch();
		batch.setShader(shader);
		background = new Texture(Gdx.files.internal("res/background.png"));

		world = new World(new Vector2(0, 0), false);
		

		renderer = new Box2DDebugRenderer();

		shapeR = new ShapeRenderer();

		player = new Player(world);

		GameObject floor = new GameObject(BodyType.StaticBody, world, Gdx.graphics.getWidth(), 1);
		floor.addBodyDef(-2, -1, 30, 0.1f, 0.1f, 1, 0);
		texture = new Texture(Gdx.files.internal("res/download.png"));

		mesh = new Mesh(true, 6, 0,
				new VertexAttribute(VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE),
				new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2,
						ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));
		float[] shape = new float[] { 100, 200, 200, 100, 100, 400 };

		mesh.setVertices(shape);

	}

	float x = 0, y = 0;
	int att = 10000;

	public void render(OrthographicCamera camera) {
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl20.glEnable(GL20.GL_TEXTURE_2D);
		batch.begin();
		batch.draw(background, 0, 0, Game.width, Game.height);
		player.render(camera, batch);

		renderLights(camera);

		batch.end();
		renderShadows(camera);
		renderer.render(world, camera.combined);

		world.step(1 / 60f, 60, 20);
	}

	public void renderLights(OrthographicCamera camera) {

		for (int i = 0; i < MAX_LIGHTS && i < lights.size(); ++i) {
			shader.setUniformf("lightPos[" + i + "]",
					new Vector2(lights.get(i).getPos().x - camera.position.x/camera.viewportWidth,
							lights.get(i).getPos().y - camera.position.y/camera.viewportHeight));
			shader.setUniformf("lightColor[" + i + "]", lights.get(i).getColor());
			shader.setUniformf("lightAttenuation[" + i + "]", lights.get(i).getAttenuation());
		}
		shader.setUniformf("dimensions", new Vector2(Game.width, Game.height));

	}

	public void renderShadows(OrthographicCamera camera) {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		for (int j = 0; j < GameObject.objects.size(); ++j) {
			for (int i = 0; i < lights.size(); i++) {
				shadowShader.begin();
				shadowShader.setUniformf("lightPos", lights.get(i).getPos());
				shadowShader.setUniformMatrix("u_projTrans", batch.getProjectionMatrix());
				if (GameObject.objects.get(j).getVertices().length > 0) {
					lights.get(i).generateShadowGLSL(GameObject.objects.get(j).getVertices(),camera).render(shadowShader,
							GL20.GL_TRIANGLES);
				}
				shadowShader.end();
			}
		}
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	int timer = 10;

	public void update(OrthographicCamera camera) {

		player.update(camera);
		if (timer > 0)
			timer--;
		x = (Gdx.input.getX() / (float) Game.width);
		y = -(Gdx.input.getY() / (float) Game.height) + 1;
		if(lights.size() > 0)lights.get(lights.size() - 1).setPos(x, y);

		if (Gdx.input.isButtonPressed(0) && timer == 0) {
			timer = 10;
			Light newLight = new Light();
			newLight.setPos(x, y);

			newLight.setColor(
					new Vector3((float) Math.random() * 255, (float) Math.random() * 255, (float) Math.random() * 255));
			newLight.setAttenuation(
					new Vector3(0, att / 2 + (float) Math.random() * att, att / 2 + (float) Math.random() * att));
			lights.add(newLight);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.E))
			att += 10;
		if (Gdx.input.isKeyPressed(Input.Keys.Q))
			att -= 10;
	}

	
	public void onClose() {
		world.dispose();
		batch.dispose();
	}

	public int getID() {
		return 0;
	}

}
