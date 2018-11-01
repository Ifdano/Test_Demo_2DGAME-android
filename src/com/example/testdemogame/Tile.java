/*
Класс для текстур карты
 */

package com.example.testdemogame;

import android.graphics.Bitmap;

public class Tile{
	//изображение текстуры
	private Bitmap image;
	//прозрачная или нет
	private boolean blocked;
	
	public Tile(Bitmap image, boolean blocked){
		this.image = image;
		this.blocked = blocked;
	}
	
	public Bitmap getImage(){ return image; }
	public boolean isBlocked(){ return blocked; }	
}