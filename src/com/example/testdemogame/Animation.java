/*
 Класс с анимацией
 */

package com.example.testdemogame;

import android.graphics.Bitmap;

public class Animation{
	//хранение кадров спрайтов
	private Bitmap[] frames;
	//текущий кадр
	private int curFrame;
	
	private long startTime;
	private long delay;
	
	public Animation(){}
	
	//устанавливаем спрайт
	public void setFrame(Bitmap[] frames){
		this.frames = frames;
		
		if(curFrame >= frames.length)
			curFrame = 0;
	}
	
	//что-то типа скорости обновления
	public void setDelay(long d){ delay = d; }
	
	//обновление
	public void update(){
		if(delay == -1)
			return;
		
		long elapsedTime = (System.nanoTime() - startTime)/1000000;
		if(elapsedTime > delay){
			curFrame++;
			startTime = System.nanoTime();
		}
		
		if(curFrame == frames.length)
			curFrame = 0;
	}
	
	//получение кадров из спрайтов
	public Bitmap getImage(){ return frames[curFrame]; }
}