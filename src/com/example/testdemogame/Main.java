/*
 Тестовая демо-версия для простенькой игры
 */

package com.example.testdemogame;

import android.app.Activity;
import android.os.Bundle;

public class Main extends Activity{
	//Объявление нашей "игровой панели"/холст
	private GamePanel gameOne;
	
	protected void onCreate(Bundle saves){
		super.onCreate(saves);
		
		//Создаем экземпляр "игровой панели"
		gameOne = new GamePanel(this);
		//Устанавливаем на экран
		setContentView(gameOne);
	}
}