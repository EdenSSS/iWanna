package com.example.whatareyoudoing;

public class Feature {
	/*
	 * 方向传感器
	 */
	float oBegin[]; // 初始角度
	float oEnd[];   // 终止角度
	float oMax[];   // 最大角度
	float oMin[];   // 最小角度
	
	/*
	 * 陀螺仪传感器 
	 */
	float gMax[];   // 角速度最大值
	float gMin[];   // 角速度最小值
	double gSqrt[];  // 平方求和开根号（最大0/最小1）
	
	/*
	 * 加速度传感器 
	 */
	double aMax;    // 平方求和开根号最大值  
	
	public Feature() {
		oBegin = new float[3];
		oEnd = new float[3];
		oMax = new float[3];
		oMin = new float[3];
		gMax = new float[3];
		gMin = new float[3];
		gSqrt = new double[2];		
	}
}
