/*
 Общий класс для персонажей
 */

package com.example.testdemogame;

import android.content.Context;

import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.util.HashMap;

public class Persons extends MapObjects{
	//координаты и временное значение для движения
	protected float x;
	protected float y;
	protected float dx;
	protected float dy;
	
	//размеры
	protected int width;
	protected int height;
	
	//скорость, тормоз и прочее
	protected float moveSpeed;
	protected float stopSpeed;
	protected float maxSpeed;
	protected float gravity;
	protected float maxFallingSpeed;
	protected float jumpStart;
	protected float jumpStop;
	
	//направление движения
	protected boolean left;
	protected boolean right;
	protected boolean jumping;
	protected boolean falling;
	protected boolean reverse;
	protected boolean focus;
	
	//для вычисления углов
	protected int topTile;
	protected int bottomTile;
	protected int leftTile;
	protected int rightTile;
	
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	
	//размеры экрана
	protected int displayHeight;
	protected int displayWidth;
	
	//хранение спрайтов персонажей
	protected Bitmap[] id;
	protected Bitmap[] walk;
	protected Bitmap[] fall;
	protected Bitmap[] jump;
	
	//сама анимация
	protected Animation animation;
	protected boolean facingRight;
	
	//временные для создания спрайтов
	protected Bitmap image;
	protected Bitmap frame;
	protected int[] pixel;
	
	protected Matrix matrix;
	protected boolean isChange;
	
	//звук для прыжка
	protected HashMap<String, Audio> sfx;
	
	public Persons(TileMap map, int mapSize, Context context, int density){
		super(map, mapSize, context, density);
		
		//устанавливаем значения скорости и прочего для разных экранов
		//1.0x
		moveSpeed = 0.6f;
		stopSpeed = 0.4f;
		maxSpeed = 5.0f;
		gravity = 0.64f;
		maxFallingSpeed = 12.0f;
		jumpStart = -17.3f;
		
		//1.5x
		if(mapSize == 108){
			moveSpeed *= 1.5;
			stopSpeed *= 1.5;
			maxSpeed *= 1.5;
			gravity *= 1.5;
			maxFallingSpeed *= 1.5;
			jumpStart *= 1.5;
		}
		
		//2.0x
		if(mapSize == 144){
			moveSpeed *= 2;
			stopSpeed *= 2;
			maxSpeed *= 2;
			gravity *= 2;
			maxFallingSpeed *= 2;
			jumpStart *= 2;
		}
		
		//2.2x
		if(mapSize == 158){
			moveSpeed *= 2.2;
			stopSpeed *= 2.2;
			maxSpeed *= 2.2;
			gravity *= 2.2;
			maxFallingSpeed *= 2.2;
			jumpStart *= 2.2;
		}
		
		//2.6x
		if(mapSize == 187){
			moveSpeed *= 2.6;
			stopSpeed *= 2.6;
			maxSpeed *= 2.6;
			gravity *= 2.6;
			maxFallingSpeed *= 2.6;
			jumpStart *= 2.6;
		}
		
		//3.0x
		if(mapSize == 216){
			moveSpeed *= 3;
			stopSpeed *= 3;
			maxSpeed *= 3;
			gravity *= 3;
			maxFallingSpeed *= 3;
			jumpStart *= 3;
		}
		
		//3.5x
		if(mapSize == 252){
			moveSpeed *= 3.5;
			stopSpeed *= 3.5;
			maxSpeed *= 3.5;
			gravity *= 3.5;
			maxFallingSpeed *= 3.5;
			jumpStart *= 3.5;
		}
		
		//4.0x
		if(mapSize == 288){
			moveSpeed *= 4;
			stopSpeed *= 4;
			maxSpeed *= 4;
			gravity *= 4;
			maxFallingSpeed *= 4;
			jumpStart *= 4;
		}
		
		animation = new Animation();
		facingRight = true;
		
		//добавляем звук прыжка в Хэш
		sfx = new HashMap<String, Audio>();
		sfx.put("jump", new Audio(0, context));
	}
	
	//установка координат
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	//установка направления и прыжка
	public void setLeft(boolean b){ left = b; }
	public void setRight(boolean b){ right = b; }
	public void setJumping(boolean b){
		if(!falling)
			jumping = b;
	}
	
	//отключение гравитации
	public void setReverse(boolean b){
		if(!falling)
			reverse = b;
	}
	
	public boolean getReverse(){ return reverse; }
	
	//фокусировка
	public void setFocus(boolean b){ focus = b; }
	public boolean getFocus(){ return focus; }

	//получение координат и размеров персонажей
	public float getX(){ return x; }
	public float getY(){ return y; }
	
	public int getWidth(){ return width; }
	public int getHeight(){ return height; }
	
	//установка размеров экрана
	public void setHeight(int height){ displayHeight = height; }
	public void setWidth(int width){ displayWidth = width; }
	
	//торможение для прыжка
	public void setJumpStop(){
		jumpStop = 0;
		
		if(falling){
			if(reverse){
				if(dy > 0)
					jumpStop = 2 * gravity;
			}else{
				if(dy < 0)
					jumpStop = 2 * gravity;
			}
		}
	}
	
	//вычисление углов 
	protected void calculateCorners(float x, float y){
		if(reverse){
			topTile = map.getRowTile((int)(y + height/2) - 1);
			bottomTile = map.getRowTile((int)(y - height/2));
		}else{
			topTile = map.getRowTile((int)(y - height/2));
			bottomTile = map.getRowTile((int)(y + height/2) - 1);
		}
		
		leftTile = map.getColTile((int)(x - width/2));
		rightTile = map.getColTile((int)(x + width/2) - 1);
		
		topLeft = map.isBlocked(topTile, leftTile);
		topRight = map.isBlocked(topTile, rightTile);
		bottomLeft = map.isBlocked(bottomTile, leftTile);
		bottomRight = map.isBlocked(bottomTile, rightTile);
	}
	
	//вычисление следующей позиции 
	public void nextPosition(){
		
		//движение влево, вправо и остановка
		if(left){
			dx -= moveSpeed;
			if(dx < -maxSpeed)
				dx = -maxSpeed;
		}else{
			if(right){
				dx += moveSpeed;
				if(dx > maxSpeed)
					dx = maxSpeed;
			}else{
				if(dx > 0){
					dx -= stopSpeed;
					if(dx < 0)
						dx = 0;
				}
				
				if(dx < 0){
					dx += stopSpeed;
					if(dx > 0)
						dx = 0;
				}
			}
		}
		
		//прыжок
		if(jumping){
			sfx.get("jump").play();
			
			dy = jumpStart;
			jumping = false;
			falling = true;
		}
		
		//падение
		if(falling){
			if(reverse){
				if(gravity > 0){
					gravity *= -1;
					jumpStart *= -1;
					
					jumpStop *= -1;
				}
				
				dy += gravity + jumpStop;
				if(dy < -maxFallingSpeed)
					dy = -maxFallingSpeed;
				
				if(dy <= 0)
					jumpStop = 0;
			}else{
				if(gravity < 0){
					gravity *= -1;
					jumpStart *= -1;
					
					jumpStop *= -1;
				}
				
				dy += gravity + jumpStop;
				if(dy > maxFallingSpeed)
					dy = maxFallingSpeed;
				
				if(dy >= 0)
					jumpStop = 0;
			}
		}else
			dy = 0;
		
		//текущее положение на карте
		int curColTile = map.getColTile((int)x);
		int curRowTile = map.getRowTile((int)y);
		
		float toX = x + dx;
		float toY = y + dy;
		
		float tempX = x;
		float tempY = y;
		
		//само взаимодействие персонажей с картой по горизонтали
		calculateCorners(toX, y);
		if(dx < 0){
			if(topLeft || bottomLeft){
				dx = 0;
				tempX = curColTile * map.getMapSize() + width/2;
			}else
				tempX += dx;
		}
		
		if(dx > 0){
			if(topRight || bottomRight){
				dx = 0;
				tempX = (curColTile + 1) * map.getMapSize() - width/2;
			}else
				tempX += dx;
		}
		
		//само взаимодействие персонажей с картой по вертикали
		calculateCorners(x, toY);
		if(dy < 0){
			if(reverse){
				if(bottomLeft || bottomRight){
					dy = 0;
					falling = false;
					tempY = curRowTile * map.getMapSize() + height/2;
				}else
					tempY += dy;
			}else{
				if(topLeft || topRight){
					dy = 0;
					tempY = curRowTile * map.getMapSize() + height/2;
				}else
					tempY += dy;
			}
		}
		
		if(dy > 0){
			if(reverse){
				if(topLeft || topRight){
					dy = 0;
					tempY = (curRowTile + 1) * map.getMapSize() - height/2;
				}else
					tempY += dy;
			}else{
				if(bottomLeft || bottomRight){
					dy = 0;
					falling = false;
					tempY = (curRowTile + 1) * map.getMapSize() - height/2;
				}else
					tempY += dy;
			}
		}
		
		//проверка на падение без прыжка
		if(!falling){
			if(reverse)
				calculateCorners(x, y - 1);
			else
				calculateCorners(x, y + 1);
			
			if(!bottomLeft && !bottomRight)
				falling = true;
		}
		
		x = tempX;
		y = tempY;
		
		//фокусировка/на ком камера?
		if(focus){
			map.setX((int)(displayWidth/2 - x));
			map.setY((int)(displayHeight/2 - y));
		}
		
		//направление для спрайта
		if(dx < 0)
			facingRight = false;
		if(dx > 0)
			facingRight = true;	
	}
	
	public void update(){}
	
	//зарисовка
	public void draw(Canvas canvas, Paint paint){
		//получаем координаты "камеры"
		int tx = map.getX();
		int ty = map.getY();
		
		//получаем кадры из спрайтов
		matrix = new Matrix();
		Bitmap image = animation.getImage();
		
		if(facingRight){
			if(reverse)
				matrix.postScale(1, -1);
			else
				matrix.postScale(1, 1);
		}else{
			if(reverse)
				matrix.postScale(-1, -1);
			else
				matrix.postScale(-1, 1);
		}
		
		//рисуем
		Bitmap frame = Bitmap.createBitmap(image, 0, 0, width, height, matrix, false);
		canvas.drawBitmap(frame, (int)(tx + x - width/2), (int)(ty + y - height/2), paint);
	}
}