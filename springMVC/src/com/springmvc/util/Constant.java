package com.springmvc.util;

public interface Constant
{
	/**
	 *	注册接口需要手动填写更新这些数据 
	 * */
	
	
	int attack = 25;
	int defence = 20;
	int hp = 30;
	int dodge = 2;
	
	int initAttack = 100;
	int initDefence = 50;
	int initHp = 500;
	int initDodge = 20;
//	500 + 15 * 30 = 450 + 500 = 950 + 150 = 1100
	// 950   475   350
	int life = 2000;
	
	float edefence_toukui = 1.5f;
	int edefence_pifeng = 1;
	float edefence_kuijia = 2.3f;
	int ehp = 12;
	int eattack = 21;
	
	// 人物身上可存物的格子数
	int count = 30;
}
