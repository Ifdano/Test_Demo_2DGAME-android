/*
 Класс с аудио
 */

package com.example.testdemogame;

import android.content.Context;
import android.media.MediaPlayer;

public class Audio{
	//наш плеер
	private MediaPlayer player;
	
	//текущий номер аудио/звука
	private int curSound;
	private Context context;
	
	public Audio(int curSound, Context context){
		this.curSound = curSound;
		this.context = context;
		
		//если прыжок
		if(curSound == 0)
			if(player == null)
				player = MediaPlayer.create(context, R.raw.jump);
		
		//если фонова¤ музыка
		if(curSound == 1)
			if(player == null)
				player = MediaPlayer.create(context, R.raw.gametheme);
	}
	
	//создание плеера
	public void createAudio(){
		if(curSound == 0)
			if(player == null)
				player = MediaPlayer.create(context, R.raw.jump);
		
		if(curSound == 1)
			if(player == null)
				player = MediaPlayer.create(context, R.raw.gametheme);
	}
	
	//воспроизведение
	public void play(){
		if(player == null)
			return;
		
		//сначала выключаем все
		stop();
		player.reset();
		player = null;
		
		//потом включаем
		createAudio();
		player.start();
		
	}
	
	//выключаем
	public void stop(){
		if(player != null)
			player.stop();
	}
	
	//закрываем
	public void close(){
		stop();
		
		player.release();
		player = null;
	}
}