/*
 Класс с персонажем sonic
 */

package com.example.testdemogame;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Sonic extends GoodPersons{
	public Sonic(TileMap map, int mapSize, Context context, int density){
		super(map, mapSize, context, density);
		
		//устанавливаем размеры в зависимости от размеров экрана/текстур
		//1.0x
		if(mapSize == 72){
			width = 32;
			height = 42;
		}
		
		//1.5x
		if(mapSize == 108){
			width = 48;
			height = 63;
		}
		
		//2.0x
		if(mapSize == 144){
			width = 64;
			height = 84;
		}
		
		//2.2x
		if(mapSize == 158){
			width = 70;
			height = 92;
		}
		
		//2.6x
		if(mapSize == 187){
			width = 83;
			height = 109;
		}
		
		//3.0x
		if(mapSize == 216){
			width = 96;
			height = 126;
		}
		
		//3.5x
		if(mapSize == 252){
			width = 112;
			height = 147;
		}
		
		//4.0x
		if(mapSize == 288){
			width = 128;
			height = 168;
		}
		
		//спрайты
		id = new Bitmap[1];
		fall = new Bitmap[1];
		jump = new Bitmap[9];
		walk = new Bitmap[9];
		
		//подгоняем спрайты под размеры экрана
		image = BitmapFactory.decodeResource(context.getResources(), R.drawable.sonic_id);
		Bitmap imageTemp;
		imageTemp = getImage(0);
		image = imageTemp;
		
		id[0] = image;
		
		image = BitmapFactory.decodeResource(context.getResources(), R.drawable.sonic_fall);
		imageTemp = getImage(0);
		image = imageTemp;
		
		fall[0] = image;
		
		image = BitmapFactory.decodeResource(context.getResources(), R.drawable.sonic_walk);
		imageTemp = getImage(1);
		image = imageTemp;
		
		for(int col = 0; col < walk.length; col++){
			frame = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			pixel = new int[width * height];
			
			image.getPixels(pixel, 0, width, col * width, 0, width, height);
			frame.setPixels(pixel, 0, width, 0, 0, width, height);
			walk[col] = frame;
		}
		
		image = BitmapFactory.decodeResource(context.getResources(), R.drawable.sonic_jump);	
		imageTemp = getImage(1);
		image = imageTemp;
		
		for(int col = 0; col < jump.length; col++){
			frame = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			pixel = new int[width * height];
			
			image.getPixels(pixel, 0, width, col * width, 0, width, height);
			frame.setPixels(pixel, 0, width, 0, 0, width, height);
			jump[col] = frame;
		}
		
	}
	
	//метод для расширения спрайтов
	//0 - 1 кадр, 1 - 9 кадров
	public Bitmap getImage(int num){
		Bitmap imageTemp = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
		
		if(num == 1){
			//4.0x
			if(mapSize == 288)
				imageTemp = Bitmap.createScaledBitmap(image, 1152, 168, true);
	
			//3.5x
			if(mapSize == 252)
				imageTemp = Bitmap.createScaledBitmap(image, 1008, 147, true);
			
			//3.0x
			if(mapSize == 216)
				imageTemp = Bitmap.createScaledBitmap(image, 864, 126, true);
			
			//2.6x
			if(mapSize == 188)
				imageTemp = Bitmap.createScaledBitmap(image, 748, 109, true);
			
			//2.2x
			if(mapSize == 158)
				imageTemp = Bitmap.createScaledBitmap(image, 633, 92, true);
			
			//2.0x
			if(mapSize == 144)
				imageTemp = Bitmap.createScaledBitmap(image, 576, 84, true);
			
			//1.5x
			if(mapSize == 108)
				imageTemp = Bitmap.createScaledBitmap(image, 432, 63, true);		
			
			//1.0x
			if(mapSize == 72)
				imageTemp = Bitmap.createScaledBitmap(image, 288, 42, false);
		}else{
			//4.0x
			if(mapSize == 288)
				imageTemp = Bitmap.createScaledBitmap(image, 128, 168, true);
			
			//3.5x
			if(mapSize == 252)
				imageTemp = Bitmap.createScaledBitmap(image, 112, 147, true);
			
			//3.0x
			if(mapSize == 216)
				imageTemp = Bitmap.createScaledBitmap(image, 96, 126, true);
			
			//2.6x
			if(mapSize == 187)
				imageTemp = Bitmap.createScaledBitmap(image, 83, 109, true);
			
			//2.2x
			if(mapSize == 158)
				imageTemp = Bitmap.createScaledBitmap(image, 70, 92, true);
			
			//2.0x
			if(mapSize == 144)
				imageTemp = Bitmap.createScaledBitmap(image, 64, 84, true);
			
			//1.5x
			if(mapSize == 108)
				imageTemp = Bitmap.createScaledBitmap(image, 48, 63, true);
			
			//1.0x
			if(mapSize == 72)
				imageTemp = Bitmap.createScaledBitmap(image, 32, 42, false);
		}
		
		return imageTemp;
	}
	
	//обновляем
	public void update(){
		nextPosition();
		
		if(left || right){
			animation.setFrame(walk);
			animation.setDelay(90);
		}else{
			animation.setFrame(id);
			animation.setDelay(-1);
		}
		
		if(dy < 0){
			if(reverse){
				animation.setFrame(fall);
				animation.setDelay(-1);
			}else{
				animation.setFrame(jump);
				animation.setDelay(90);
			}
		}
		
		if(dy > 0){
			if(reverse){
				animation.setFrame(jump);
				animation.setDelay(90);
			}else{
				animation.setFrame(fall);
				animation.setDelay(-1);
			}
		}
		
		animation.update();
	}
}