/*
 Класс с задним фоном
 */

package com.example.testdemogame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.content.Context;

public class Background{
	//координаты и скорость перемещения для переднего фона
	private float x1;
	private float y;
	private float moveScale1;
	
	//координаты и скорость перемещения для переднего фона
	private float x0;
	private float moveScale0;
	
	//передний фон и его временная копия
	private Bitmap back1;
	private Bitmap backTemp1;
	
	//задний фон и его временная копия
	private Bitmap back0;
	private Bitmap backTemp0;
	
	//размеры экрана
	private int displayHeight;
	private int displayWidth;
	
	public Background(float moveScale, Context context){
		//устанавливаем скорость перемещения
		moveScale1 = moveScale;
		moveScale0 = 1.1f;
		
		//получаем исходные фоны
		backTemp0 = BitmapFactory.decodeResource(context.getResources(), R.drawable.back01);
		backTemp1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.back1);
	}
	
	public void setHeight(int height){ displayHeight = height; }
	public void setWidth(int width){ displayWidth = width; }
	
	//подгоняем исходные фоны под размеры экрана
	public void setBack(){
		back0 = Bitmap.createScaledBitmap(backTemp0, displayWidth, displayHeight, true);
		back1 = Bitmap.createScaledBitmap(backTemp1, displayWidth, displayHeight, true);
	}
	
	//обвновляем перемещение относительно карты
	public void update(float x){
		x0 = (x * moveScale0) % displayWidth;
		x1 = (x * moveScale1) % displayWidth;
	}
	
	//прорисовка заднего фона, условия для эффкта движения
	public void draw0(Canvas canvas, Paint paint){
		canvas.drawBitmap(back0, (int)x0, (int)y, paint);
		
		if(x0 < 0)
			canvas.drawBitmap(
					back0,
					(int)x0 + displayWidth,
					(int)y,
					paint
					);
		
		if(x0 > 0)
			canvas.drawBitmap(
					back0,
					(int)x0 - displayWidth,
					(int)y,
					paint
					);
	}
	
	//прорисовка переднего фона, условия для эффкта движения
	public void draw1(Canvas canvas, Paint paint){
		canvas.drawBitmap(back1, (int)x1, (int)y, paint);
		
		if(x1 < 0)
			canvas.drawBitmap(
					back1,
					(int)x1 + displayWidth,
					(int)y,
					paint
					);
		
		if(x1 > 0)
			canvas.drawBitmap(
					back1,
					(int)x1 - displayWidth,
					(int)y,
					paint
					);
	}
}