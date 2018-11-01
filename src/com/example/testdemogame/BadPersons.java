/*
 ќбщий класс дл¤ отрицательных персонажей
 */

package com.example.testdemogame;

import android.content.Context;

import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Color;

public class BadPersons extends Persons{
	public BadPersons(TileMap map, int mapSize, Context context, int density){
		super(map, mapSize, context, density);
	}
}