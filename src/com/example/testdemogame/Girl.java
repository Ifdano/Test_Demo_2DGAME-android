/*
 Класс с персонажем girl. »дентично как и у других
 */

package com.example.testdemogame;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Girl extends GoodPersons{
	public Girl(TileMap map, int mapSize, Context context, int density){
		super(map, mapSize, context, density);
		
		//устанавливаем размеры в зависимости от размеров экрана/текстур
		//1.0x
		if(mapSize == 72){
			width = 19;
			height = 42;
		}
		
		//1.5x
		if(mapSize == 108){
			width = 29;
			height = 63;
		}
		
		//2.0x
		if(mapSize == 144){
			width = 38;
			height = 84;
		}
		
		//2.2x
		if(mapSize == 158){
			width = 41;
			height = 92;
		}
				
		//2.6x
		if(mapSize == 187){
			width = 49;
			height = 109;
		}
		
		//3.0x
		if(mapSize == 216){
			width = 57;
			height = 126;
		}
		
		//3.5x
		if(mapSize == 252){
			width = 66;
			height = 147;
		}
		
		//4.0x
		if(mapSize == 288){
			width = 76;
			height = 168;
		}
		
		//спрайт
		walk = new Bitmap[12];
		
		//подгон¤ем спрайты под размеры экрана
		image = BitmapFactory.decodeResource(context.getResources(), R.drawable.girl_walk);
		Bitmap imageTemp;
		imageTemp = getImage();
		image = imageTemp;
		
		for(int col = 0; col < walk.length; col++){
			frame = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			pixel = new int[width * height];
			
			image.getPixels(pixel, 0, width, col * width, 0, width, height);
			frame.setPixels(pixel, 0, width, 0, 0, width, height);
			walk[col] = frame;
		}
	}
	
	//метод дл¤ расширени¤ спрайтов
	public Bitmap getImage(){
		Bitmap imageTemp = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
		
		//4.0x
		if(mapSize == 288)
			imageTemp = Bitmap.createScaledBitmap(image, 912, 168, true);
		
		//3.5x
		if(mapSize == 252)
			imageTemp = Bitmap.createScaledBitmap(image, 798, 147, true);
		
		//3.0x
		if(mapSize == 216)
			imageTemp = Bitmap.createScaledBitmap(image, 684, 126, true);
		
		//2.6x
		if(mapSize == 187)
			imageTemp = Bitmap.createScaledBitmap(image, 592, 109, true);
		
		//2.2x
		if(mapSize == 158)
			imageTemp = Bitmap.createScaledBitmap(image, 501, 92, true);
		
		//2.0x
		if(mapSize == 144)
			imageTemp = Bitmap.createScaledBitmap(image, 456, 84, true);
		
		//1.5x
		if(mapSize == 108)
			imageTemp = Bitmap.createScaledBitmap(image, 342, 63, true);
		
		//1.0x
		if(mapSize == 72)
			imageTemp = Bitmap.createScaledBitmap(image, 228, 42, false);
		
		return imageTemp;
	}
	
	//обновл¤ем
	public void update(){
		nextPosition();
		
		if(right && dx == 0){
			right = false;
			left = true;
		}else{
			if(left && dx == 0){
				right = true;
				left = false;
			}
		}
		
		if(left || right){
			animation.setFrame(walk);
			animation.setDelay(50);
		}
		
		animation.update();
	}
}