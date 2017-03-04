package com.game.states;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class StateManager {
	private static ArrayList<State> states = new ArrayList<State>();
	private static int currentState = 0;

	private StateManager() {
	}

	public static void addState(State state) {
		states.add(state);
	}

	public static void renderStates(OrthographicCamera camera) {
		for (int i = 0; i < states.size(); i++) {
			if (states.get(i).getID() == currentState)
				states.get(i).render(camera);
		}
	}

	public static void updateStates(OrthographicCamera camera) {
		for (int i = 0; i < states.size(); i++) {
			if (states.get(i).getID() == currentState)
				states.get(i).update(camera);
		}
	}

	public static void onClose() {
		for (int i = 0; i < states.size(); i++) {
			if (states.get(i).getID() == currentState)
				states.get(i).onClose();
		}
	}

	public static int getCurrentState() {
		return currentState;
	}

	public static void setCurrentState(int currentState) {
		StateManager.currentState = currentState;
	}

}
