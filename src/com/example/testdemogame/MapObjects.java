/*
 Класс для объединения всех на одной карте.
 Можно передавать разные параметры, которые будут нужны всем элементам на карте.
 */

package com.example.testdemogame;

import android.content.Context;

public class MapObjects{
	protected TileMap map;
	protected int mapSize;
	protected Context context;
	protected int density;
	
	public MapObjects(TileMap map, int mapSize, Context context, int density){
		this.map = map;
		this.mapSize = mapSize;
		this.context = context;
		this.density = density;
	}
}