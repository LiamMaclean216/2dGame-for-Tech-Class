package com.game.states;

import com.badlogic.gdx.graphics.OrthographicCamera;

public abstract class State {

	public abstract void render(OrthographicCamera camera);

	public abstract void update(OrthographicCamera camera);

	public abstract int getID();

	protected void onClose() {

	}
}
