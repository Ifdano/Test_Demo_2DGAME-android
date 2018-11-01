/*
 Класс с "игровой панелью"
 */

package com.example.testdemogame;

import android.view.View;
import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Paint;

import android.view.MotionEvent;
import android.view.View.OnTouchListener;

import android.util.TypedValue;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import android.os.Handler;
import android.content.res.Configuration;

public class GamePanel extends View implements Runnable, OnTouchListener{
	
	//"кисть" для рисования
	private Paint paint;
	//контекст для всей игры
	private Context context;
	
	//игровой FPS
	private int FPS = 30;
	private int targetTime = 1000/FPS;
	
	//поток
	private Thread thread;
	private boolean running;
	
	//переменные для получения размеров экрана
	private int heightDisplay;
	private int widthDisplay;
	private int heightStatus;
	private int statusID;
	private int heightAction;
	private int heightMain;
	
	//размер текстур карты
	private int mapSize;
	//общее разрешение для всех
	private int densityDpi;
	
	//карта и задний фон
	private TileMap tileMap;
	private Background back;
	
	//персонажи
	private Sonic sonic;
	private Braid braid;
	private Girl girl;
	private Croco croco;
	
	//переменные для двойного нажатия
	private Handler doubleHandler;
	private boolean doubleClick;
	
	//фоновая музыка
	private Audio bgMusic;
	
	//конструктор с "игровой панелью"
	public GamePanel(Context context){
		super(context);
		//задаем общий контекст
		this.context = context;
		
		//устанавливаем фокус
		setFocusable(true);
		requestFocus();
		
		//получаем общие размеры экрана[высоту и ширину]
		WindowManager window = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = window.getDefaultDisplay();
		heightDisplay = display.getHeight();
		widthDisplay = display.getWidth();
		
		//получаем размеры статус-бара
		heightStatus = 0;
		statusID = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if(statusID > 0)
			heightStatus = context.getResources().getDimensionPixelSize(statusID);
		
		//получаем размеры экшн-бара
		heightAction = 0;
		TypedValue typedV = new TypedValue();
		if(context.getTheme().resolveAttribute(android.R.attr.actionBarSize, typedV, true))
			heightAction = TypedValue.complexToDimensionPixelSize(typedV.data, context.getResources().getDisplayMetrics());
		
		//находим размеры "игровой области"/доступную часть экрана для игры
		heightMain = heightDisplay - heightStatus - heightAction;
		
		//получаем размеры текстур для карты
		mapSize = getMapSize();
		
		doubleHandler = new Handler();
		
		//задаем общий поток для игры
		addNotify();
	}
	
	//задаем общий поток для игры
	public void addNotify(){
		if(thread == null){
			thread = new Thread(this);
			thread.start();
		}
		//устанавливаем слушателя
		setOnTouchListener(this);
	}
	
	//метод для рисования
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		//если "игра" началась
		if(running){
			//рисуем два задних фона
			back.draw0(canvas, paint);
			back.draw1(canvas, paint);
			
			//карту
			tileMap.draw(canvas, paint);
			
			//персонажей
			sonic.draw(canvas, paint);
			braid.draw(canvas, paint);
			girl.draw(canvas, paint);
			croco.draw(canvas, paint);
		}
	}
	
	//метод от Runnable для создания игрового цикла
	public void run(){
		init();
		
		long startTime;
		long upgTime;
		long waitTime;
		
		//пока игра работает, 
		while(running){
			startTime = System.nanoTime();
			
			//обновляем и перерисовываем холст
			update();
			postInvalidate();
			
			upgTime = (System.nanoTime() - startTime)/1000000;
			waitTime = targetTime - upgTime;
			
			//"усыпляем" поток на определенное время
			try{
				thread.sleep(waitTime);
			}catch(Exception ex){}
		}
	}
	
	//инициализация 
	public void init(){
		//карты
		tileMap = new TileMap(mapSize, context);
		tileMap.setDensity(densityDpi);
		tileMap.setHeight(heightMain);
		tileMap.setWidth(widthDisplay);
		tileMap.saveMap();
		tileMap.loadMap();
		tileMap.loadTile();
		
		//заднего фона
		back = new Background(1.3f, context);
		back.setHeight(heightMain);
		back.setWidth(widthDisplay);
		back.setBack();
		
		//фоновой музыки
		bgMusic = new Audio(1, context);
		bgMusic.play();
		
		//персонажей
		sonic = new Sonic(tileMap, mapSize, context, densityDpi);
		sonic.setHeight(heightMain);
		sonic.setWidth(widthDisplay);
		sonic.setPosition(mapSize + 150, mapSize + 50);
		
		braid = new Braid(tileMap, mapSize, context, densityDpi);
		braid.setHeight(heightMain);
		braid.setWidth(widthDisplay);
		braid.setPosition(mapSize + 350, mapSize + 50);
		
		girl = new Girl(tileMap, mapSize, context, densityDpi);
		girl.setPosition(mapSize + 100, mapSize + 50);
		girl.setRight(true);
		
		croco = new Croco(tileMap, mapSize, context, densityDpi);
		croco.setPosition(mapSize + 150, mapSize + 50);
		croco.setLeft(true);
		
		//создание "кисти" и запуск "игры"
		paint = new Paint();
		running = true;
	}
	
	//обновляем 
	public void update(){
		tileMap.update();
		
		back.update(tileMap.getX());
		
		sonic.update();
		braid.update();
		girl.update();
		croco.update();
	}
	
	//обработка нажатий
	public boolean onTouch(View view, MotionEvent event){
		//получаем координаты нажатия
		float tempX = event.getX();
		float tempY = event.getY();
		
		//получаем координаты "камеры"/видимой области
		int tx = tileMap.getX();
		int ty = tileMap.getY();
		
		//данные для управления одним из 2-х персонажей
		float xSonic = sonic.getX();
		float ySonic = sonic.getY();
		int widthSonic = sonic.getWidth();
		int heightSonic = sonic.getHeight();
		
		float xBraid = braid.getX();
		float yBraid = braid.getY();
		int widthBraid = braid.getWidth();
		int heightBraid = braid.getHeight();
		
		//если нажали на экран
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			//если двойной клик
			if(doubleClick){
				if(sonic.getFocus()){
					if(sonic.getReverse())
						sonic.setReverse(false);
					else
						sonic.setReverse(true);
				}
				
				if(braid.getFocus()){
					if(braid.getReverse())
						braid.setReverse(false);
					else
						braid.setReverse(true);
				}
			}else{
				doubleClick = true;
				doubleHandler.postDelayed(checkDouble, 200);
				
				//выбираем персонажа или продолжаем управлять
				if((tempX >= tx + xSonic - widthSonic/2 && tempX <= tx + xSonic + widthSonic/2) && 
				   (tempY >= ty + ySonic - heightSonic/2 && tempY <= ty + ySonic + heightSonic/2)){
					sonic.setFocus(true);
					braid.setFocus(false);
				}else{
					if((tempX >= tx + xBraid - widthBraid/2 && tempX <= tx + xBraid + widthBraid/2) && 
					   (tempY >= ty + yBraid - heightBraid/2 && tempY <= ty + yBraid + heightBraid/2)){
						sonic.setFocus(false);
						braid.setFocus(true);
					}else{
						if(sonic.getFocus()){
							if(sonic.getReverse()){
								if(tempY <= heightMain/2){
									if(tempX <= widthDisplay/2)
										sonic.setLeft(true);
									if(tempX > widthDisplay/2)
										sonic.setRight(true);
								}
								
								if(tempY > heightMain/2){
									sonic.setJumping(true);
									
									if(tempX <= widthDisplay/2)
										sonic.setLeft(true);
									if(tempX > widthDisplay/2)
										sonic.setRight(true);
								}
							}else{
								if(tempY <= heightMain/2){
									sonic.setJumping(true);
									
									if(tempX <= widthDisplay/2)
										sonic.setLeft(true);
									if(tempX > widthDisplay/2)
										sonic.setRight(true);
								}
								
								if(tempY > heightMain/2){
									if(tempX <= widthDisplay/2)
										sonic.setLeft(true);
									if(tempX > widthDisplay/2)
										sonic.setRight(true);
								}
							}
						}
						
						if(braid.getFocus()){
							if(braid.getReverse()){
								if(tempY <= heightMain/2){
									if(tempX <= widthDisplay/2)
										braid.setLeft(true);
									if(tempX > widthDisplay/2)
										braid.setRight(true);
								}
								
								if(tempY > heightMain/2){
									braid.setJumping(true);
									
									if(tempX <= widthDisplay/2)
										braid.setLeft(true);
									if(tempX > widthDisplay/2)
										braid.setRight(true);
								}
							}else{
								if(tempY <= heightMain/2){
									braid.setJumping(true);
									
									if(tempX <= widthDisplay/2)
										braid.setLeft(true);
									if(tempX > widthDisplay/2)
										braid.setRight(true);
								}
								
								if(tempY > heightMain/2){
									if(tempX <= widthDisplay/2)
										braid.setLeft(true);
									if(tempX > widthDisplay/2)
										braid.setRight(true);
								}
							}
						}
					}
				}
			}
		}
		
		//отпускаем нажатие
		if(event.getAction() == MotionEvent.ACTION_UP){
			if(sonic.getFocus()){
				sonic.setLeft(false);
				sonic.setRight(false);
				
				sonic.setJumpStop();
			}
			
			if(braid.getFocus()){
				braid.setLeft(false);
				braid.setRight(false);
				
				braid.setJumpStop();
			}
		}
		
		return true;
	}
	
	//метод для получения размеров текстур для карты
	//подстройка под экраны
	public int getMapSize(){
		boolean isSmall;
		boolean isNormal;
		boolean isLarge;
		boolean isXLarge;
		isSmall = isNormal = isLarge = isXLarge = false;
		
		//вычисление размеров экрана
		if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 
		   Configuration.SCREENLAYOUT_SIZE_XLARGE)
				isXLarge = true;
		
		if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
		   Configuration.SCREENLAYOUT_SIZE_LARGE)
				isLarge = true;
		
		if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 
		    Configuration.SCREENLAYOUT_SIZE_NORMAL)
				isNormal = true;
		
		if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
		    Configuration.SCREENLAYOUT_SIZE_SMALL)
				isSmall = true;
		
		//вычисление dpi экрана и определение размера текстур
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        densityDpi = (int)(metrics.density * 160f);
        
        //4.0x
        //XXXHIGH = 640 Dpi
        if(densityDpi >= 600)
        return 288;

        //3.5x
        if(densityDpi >= 520 && densityDpi < 600)
        return 252;
        
        //3.0x
        //XXHIGH = 480 Dpi
        if(densityDpi >= 460 && densityDpi < 520)
        return 216;
        
        //2.6x
        if(densityDpi >= 380 && densityDpi < 460)
        return 187;
        
        //2.2x
        if(densityDpi >= 330 && densityDpi < 380)
        return 158;
        
        //2.0x
        //XHIGH = 320 Dpi
        if(densityDpi >= 270 && densityDpi < 330)
        return 144;
        
        //1.5x
        //HIGH = 240 Dpi
        if(densityDpi >= 200 && densityDpi < 270)
        return 108;
        
        //1.0x
        //MDPI = 160 Dpi
        if(densityDpi >= 140 && densityDpi < 200)
        return 72;
		
        //0.75x
		return 50;
	}
	
	//для двойного клика
	public Runnable checkDouble = new Runnable(){
		public void run(){
			doubleClick = false;
			doubleHandler.removeCallbacks(checkDouble);
		}
	};
	
}