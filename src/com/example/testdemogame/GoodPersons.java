/*
 Общий класс для положительных персонажей
 */

package com.example.testdemogame;

import android.content.Context;

import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Color;

public class GoodPersons extends Persons{
	public GoodPersons(TileMap map, int mapSize, Context context, int density){
		super(map, mapSize, context, density);
	}
}