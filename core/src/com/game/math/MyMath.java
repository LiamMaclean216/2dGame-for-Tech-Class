package com.game.math;

public class MyMath {
	public static double[] getMiddlePoint(double[] vertices) {
		float x=0,y=0;
		for(int i =0;i < vertices.length;i+=2) {
			x+=vertices[i];
			y+= vertices[i+1];
		}
		x/=vertices.length/2;
		y/=vertices.length/2;
		return new double[] {x,y};
	}
	//return leftmost and rightmost point
	public static double[] getLeftRight(double[]vertices) {
		double[] mid = getMiddlePoint(vertices);
		double left = Float.POSITIVE_INFINITY;
		double right = Float.NEGATIVE_INFINITY;
		for(int i = 0;i < vertices.length;i+=2) {
			if(vertices[i] < left){
				left = vertices[i];
			}
			if(vertices[i] > right){
				right = vertices[i];
			}
		}
		return new double[] {left,right};
	}
}
