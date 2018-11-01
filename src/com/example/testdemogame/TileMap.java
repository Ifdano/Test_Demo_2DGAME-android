/*
 Класс с картой
 */

package com.example.testdemogame;

import android.content.Context;

import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.util.Log;

public class TileMap{
	
	//карта хранится в текстовом файле
	public static final String MAP = "map.txt";
	
	//координаты "камеры"
	private int x;
	private int y;
	
	//границы "камеры"
	private int minX;
	private int minY;
	private int maxX;
	private int maxY;
	
	//размеры карты, текстур и сама карта
	private int mapWidth;
	private int mapHeight;
	private int mapSize;
	private int[][] map;
	
	//размеры экрана
	private int displayWidth;
	private int displayHeight;
	
	private Context context;
	private int density;
	
	//текстуры
	private Bitmap tileSet;
	private Tile[][] tiles;
	
	//для оптимизации
	private int numRowToDraw;
	private int numColToDraw;
	private int colOffset;
	private int rowOffset;
	
	public TileMap(int mapSize, Context context){
		this.mapSize = mapSize;
		this.context = context;
		
		//размеры
		mapWidth = 30;
		mapHeight = 20;
		
		maxX = 0;
		maxY = 0;
	}
	
	//установка координаты х для камеры
	public void setX(int i){
		x = i;
		
		if(x < minX)
			x = minX;
		
		if(x > maxX)
			x = maxX;
		
		colOffset = (int)-this.x/mapSize;
	}
	
	//установка координаты y для "камеры"
	public void setY(int i){
		y = i;
		
		if(y < minY)
			y = minY;
		
		if(y > maxY)
			y = maxY;
		
		rowOffset = (int)-this.y/mapSize;
	}
	
	//получаем координаты "камеры"
	public int getX(){ return x; }
	public int getY(){ return y; }
	
	//получаем строку и столбец, где находится персонаж
	public int getRowTile(int y){ return y/mapSize; }
	public int getColTile(int x){ return x/mapSize; }
	
	//получаем размеры текстур и узнаем: прозрачный блок или нет
	public int getMapSize(){ return mapSize; }
	public int getMap(int row, int col){ return map[row][col]; }
	public boolean isBlocked(int row, int col){
		int rc = map[row][col];
		
		int r = rc / tiles[0].length;
		int c = rc % tiles[0].length;
		
		return tiles[r][c].isBlocked();
	}
	
	public void setHeight(int height){ displayHeight = height; }
	public void setWidth(int width){ displayWidth = width; }
	
	public void setDensity(int density){ this.density = density; 
		Log.v("HEEEEEEEEY", "density: " + density + "\n mapSize: " + mapSize);
	}
	
	//сначала сохраняем карту в текстовом файле
	public void saveMap(){
		try{
			OutputStreamWriter out = new OutputStreamWriter(context.openFileOutput(MAP, Context.MODE_PRIVATE));
			BufferedWriter bufW = new BufferedWriter(out);
			
			//1
			for(int i=1; i<=mapWidth; i++){
				if(i < mapWidth)
					bufW.write(25 + " ");
				else
					bufW.write(25 + "\n");
			}
			
			//2
			for(int i=1; i<=mapWidth; i++){
				if(i == 1)
					bufW.write(25 + " ");
				if(i > 1 && i < 4)
					bufW.write(0 + " ");
				if(i == 4)
					bufW.write(2 + " ");
				if(i >= 5 && i < 12)
					bufW.write(0 + " ");
				if(i == 12)
					bufW.write(18 + " ");
				if(i > 12 && i < 27)
					bufW.write(0 + " ");
				if(i == 27)
					bufW.write(1 + " ");
				if(i > 27 && i < mapWidth)
					bufW.write(0 + " ");
				if(i == mapWidth)
					bufW.write(25 + "\n");
			}
			
			//3
			for(int i=1; i<=mapWidth; i++){
				if(i >= 1 && i < 6)
					bufW.write(25 + " ");
				if(i >= 6 && i < 11)
					bufW.write(0 + " ");
				if(i == 11)
					bufW.write(18 + " ");
				if(i > 11 && i < 21)
					bufW.write(0 + " ");
				if(i == 21)
					bufW.write(3 + " ");
				if(i > 21 && i < 26)
					bufW.write(0 + " ");
				if(i >= 26 && i < mapWidth)
					bufW.write(25 + " ");
				if(i == mapWidth)
					bufW.write(25 + "\n");
			}
			
			//4
			for(int i=1; i<=mapWidth; i++){
				if(i == 1)
					bufW.write(25 + " ");
				if(i > 1 && i < 7)
					bufW.write(0 + " ");
				if(i == 7)
					bufW.write(1 + " ");
				if(i > 7 && i < 10)
					bufW.write(0 + " ");
				if(i == 10)
					bufW.write(18 + " ");
				if(i > 10 && i < 18)
					bufW.write(0 + " ");
				if(i == 18)
					bufW.write(25 + " ");
				if(i > 18 && i < 21)
					bufW.write(0 + " ");
				if(i >= 21 && i < 24)
					bufW.write(25 + " ");
				if(i >= 24 && i < mapWidth)
					bufW.write(0 + " ");
				if(i == mapWidth)
					bufW.write(25 + "\n");
			}
			
			//5
			for(int i=1; i<=mapWidth; i++){
				if(i == 1)
					bufW.write(25 + " ");
				if(i > 1 && i < 6)
					bufW.write(0 + " ");
				if(i == 6)
					bufW.write(21 + " ");
				if(i == 7)
					bufW.write(22 + " ");
				if(i == 8)
					bufW.write(23 + " ");
				if(i > 8 && i < 15)
					bufW.write(0 + " ");
				if(i == 15)
					bufW.write(13 + " ");
				if(i == 16)
					bufW.write(15 + " ");
				if(i > 16 && i < 19)
					bufW.write(0 + " ");
				if(i == 19)
					bufW.write(2 + " ");
				if(i > 19 && i < 25)
					bufW.write(0 + " ");
				if(i == 25)
					bufW.write(13 + " ");
				if(i == 26)
					bufW.write(15 + " ");
				if(i > 26 && i < mapWidth)
					bufW.write(0 + " ");
				if(i == mapWidth)
					bufW.write(25 + "\n");
			}
			
			//6
			for(int i=1; i<=mapWidth; i++){
				if(i == 1)
					bufW.write(25 + " ");
				if(i > 1 && i < 4)
					bufW.write(0 + " ");
				if(i == 4)
					bufW.write(22 + " ");
				if(i > 4 && i < 10)
					bufW.write(0 + " ");
				if(i == 10)
					bufW.write(3 + " ");
				if(i == 11)
					bufW.write(2 + " ");
				if(i > 11 && i < 18)
					bufW.write(0 + " ");
				if(i == 18)
					bufW.write(21 + " ");
				if(i == 19)
					bufW.write(22 + " ");
				if(i == 20)
					bufW.write(23 + " ");
				if(i > 20 && i < 24)
					bufW.write(0 + " ");
				if(i == 24)
					bufW.write(13 + " ");
				if(i == 25)
					bufW.write(15 + " ");
				if(i == 26)
					bufW.write(0 + " ");
				if(i == 27)
					bufW.write(15 + " ");
				if(i > 27 && i < mapWidth)
					bufW.write(0 + " ");
				if(i == mapWidth)
					bufW.write(25 + "\n");
			}
			
			//7
			for(int i=1; i<=mapWidth; i++){
				if(i == 1)
					bufW.write(25 + " ");
				if(i > 1 && i < 10)
					bufW.write(0 + " ");
				if(i == 10)
					bufW.write(21 + " ");
				if(i == 11)
					bufW.write(22 + " ");
				if(i == 12)
					bufW.write(23 + " ");
				if(i > 12 && i < 23)
					bufW.write(0 + " ");
				if(i == 23)
					bufW.write(13 + " ");
				if(i == 24)
					bufW.write(14 + " ");
				if(i == 25)
					bufW.write(15 + " ");
				if(i > 25 && i < mapWidth)
					bufW.write(0 + " ");
				if(i == mapWidth)
					bufW.write(25 + "\n");
			}
			
			//8
			for(int i=1; i<=mapWidth; i++){
				if(i >= 1 && i < 3)
					bufW.write(25 + " ");
				if(i >= 3 && i < 7)
					bufW.write(0 + " ");
				if(i == 7)
					bufW.write(25 + " ");
				if(i > 7 && i < 20)
					bufW.write(0 + " ");
				if(i == 20)
					bufW.write(25 + " ");
				if(i > 20 && i < mapWidth-1)
					bufW.write(0 + " ");
				if(i == mapWidth-1)
					bufW.write(25 + " ");
				if(i == mapWidth)
					bufW.write(25 + "\n");
			}
			
			//9
			for(int i=1; i<=mapWidth; i++){
				if(i == 1)
					bufW.write(25 + " ");
				if(i > 1 && i < 18)
					bufW.write(0 + " ");
				if(i == 18)
					bufW.write(24 + " ");
				if(i > 18 && i < 22)
					bufW.write(0 + " ");
				if(i == 22)
					bufW.write(25 + " ");
				if(i > 22 && i < 25)
					bufW.write(0 + " ");
				if(i == 25)
					bufW.write(1 + " ");
				if(i == 26)
					bufW.write(0 + " ");
				if(i == 27)
					bufW.write(2 + " ");
				if(i > 27 && i < mapWidth)
					bufW.write(0 + " ");
				if(i == mapWidth)
					bufW.write(25 + "\n");
			}
			
			//10
			for(int i=1; i<=mapWidth; i++){
				if(i >= 1 && i < 3)
					bufW.write(25 + " ");
				if(i == 3)
					bufW.write(0 + " ");
				if(i == 4)
					bufW.write(13 + " ");
				if(i == 5)
					bufW.write(15 + " ");
				if(i > 5 && i < 8)
					bufW.write(0 + " ");
				if(i == 8)
					bufW.write(4 + " ");
				if(i == 9)
					bufW.write(0 + " ");
				if(i == 10)
					bufW.write(13 + " ");
				if(i == 11)
					bufW.write(15 + " ");
				if(i > 11 && i < 17)
					bufW.write(0 + " ");
				if(i == 17)
					bufW.write(24 + " ");
				if(i > 17 && i < 24)
					bufW.write(0 + " ");
				if(i == 24)
					bufW.write(21 + " ");
				if(i == 25)
					bufW.write(22 + " ");
				if(i == 26)
					bufW.write(22 + " ");
				if(i == 27)
					bufW.write(23 + " ");
				if(i > 27 && i < mapWidth)
					bufW.write(0 + " ");
				if(i == mapWidth)
					bufW.write(25 + "\n");
			}
			
			//11
			for(int i=1; i<=mapWidth; i++){
				if(i == 1)
					bufW.write(25 + " ");
				if(i > 1 && i < 7)
					bufW.write(0 + " ");
				if(i == 7)
					bufW.write(13 + " ");
				if(i == 8)
					bufW.write(15 + " ");
				if(i > 8 && i < 13)
					bufW.write(0 + " ");
				if(i == 13)
					bufW.write(24 + " ");
				if(i > 13 && i < 16)
					bufW.write(0 + " ");
				if(i == 16)
					bufW.write(24 + " ");
				if(i > 16 && i < mapWidth-1)
					bufW.write(0 + " ");
				if(i == mapWidth-1)
					bufW.write(24 + " ");
				if(i == mapWidth)
					bufW.write(25 + "\n");
			}
			
			//12
			for(int i=1; i<=mapWidth; i++){
				if(i == 1)
					bufW.write(25 + " ");
				if(i > 1 && i < 11)
					bufW.write(0 + " ");
				if(i == 11)
					bufW.write(25 + " ");
				if(i > 11 && i < 14)
					bufW.write(0 + " ");
				if(i == 14)
					bufW.write(24 + " ");
				if(i > 14 && i < 18)
					bufW.write(0 + " ");
				if(i == 18)
					bufW.write(3 + " ");
				if(i == 19)
					bufW.write(1 + " ");
				if(i == 20)
					bufW.write(0 + " ");
				if(i == 21)
					bufW.write(2 + " ");
				if(i > 21 && i < 24)
					bufW.write(0 + " ");
				if(i == 24)
					bufW.write(15 + " ");
				if(i > 24 && i < 28)
					bufW.write(0 + " ");
				if(i == 28)
					bufW.write(13 + " ");
				if(i == 29)
					bufW.write(15 + " ");
				if(i == mapWidth)
					bufW.write(25 + "\n");
			}
			
			//13
			for(int i=1; i<=mapWidth; i++){
				if(i == 1)
					bufW.write(25 + " ");
				if(i > 1 && i < 5)
					bufW.write(0 + " ");
				if(i == 5)
					bufW.write(24 + " ");
				if(i > 5 && i < 9)
					bufW.write(0 + " ");
				if(i == 9)
					bufW.write(14 + " ");
				if(i > 9 && i < 15)
					bufW.write(0 + " ");
				if(i == 15)
					bufW.write(24 + " ");
				if(i > 15 && i < 18)
					bufW.write(0 + " ");
				if(i == 18)
					bufW.write(16 + " ");
				if(i == 19)
					bufW.write(17 + " ");
				if(i == 20)
					bufW.write(17 + " ");
				if(i == 21)
					bufW.write(17 + " ");
				if(i == 22)
					bufW.write(2 + " ");
				if(i == 23)
					bufW.write(0 + " ");
				if(i == 24)
					bufW.write(18 + " ");
				if(i == 25)
					bufW.write(0 + " ");
				if(i == 26)
					bufW.write(25 + " ");
				if(i == 27)
					bufW.write(0 + " ");
				if(i == 28)
					bufW.write(16 + " ");
				if(i == 29)
					bufW.write(18 + " ");
				if(i == mapWidth)
					bufW.write(25 + "\n");
			}
			
			//14
			for(int i=1; i<=mapWidth; i++){
				if(i == 1)
					bufW.write(25 + " ");
				if(i == 2)
					bufW.write(0 + " ");
				if(i == 3)
					bufW.write(4 + " ");
				if(i == 4)
					bufW.write(24 + " ");
				if(i > 4 && i < 10)
					bufW.write(0 + " ");
				if(i == 10)
					bufW.write(14 + " ");
				if(i == 11)
					bufW.write(1 + " ");
				if(i > 11 && i < 21)
					bufW.write(0 + " ");
				if(i == 21)
					bufW.write(19 + " ");
				if(i == 22)
					bufW.write(17 + " ");
				if(i == 23)
					bufW.write(17 + " ");
				if(i == 24)
					bufW.write(20 + " ");
				if(i > 24 && i < 28)
					bufW.write(0 + " ");
				if(i == 28)
					bufW.write(16 + " ");
				if(i == 29)
					bufW.write(18 + " ");
				if(i == mapWidth)
					bufW.write(25 + "\n");
			}
			
			//15
			for(int i=1; i<=mapWidth; i++){
				if(i == 1)
					bufW.write(25 + " ");
				if(i == 2)
					bufW.write(0 + " ");
				if(i == 3)
					bufW.write(24 + " ");
				if(i > 3 && i < 7)
					bufW.write(0 + " ");
				if(i == 7)
					bufW.write(24 + " ");
				if(i > 7 && i < 10)
					bufW.write(0 + " ");
				if(i == 10)
					bufW.write(21 + " ");
				if(i == 11)
					bufW.write(22 + " ");
				if(i == 12)
					bufW.write(23 + " ");
				if(i > 12 && i < 16)
					bufW.write(0 + " ");
				if(i == 16)
					bufW.write(25 + " ");
				if(i == 17)
					bufW.write(0 + " ");
				if(i == 18)
					bufW.write(25 + " ");
				if(i > 18 && i < 27)
					bufW.write(0 + " ");
				if(i == 27)
					bufW.write(19 + " ");
				if(i == 28)
					bufW.write(17 + " ");
				if(i == 29)
					bufW.write(20 + " ");
				if(i == mapWidth)
					bufW.write(25 + "\n");
			}
			
			//16
			for(int i=1; i<=mapWidth; i++){
				if(i == 1)
					bufW.write(25 + " ");
				if(i == 2)
					bufW.write(24 + " ");
				if(i >= 3 && i < 5)
					bufW.write(0 + " ");
				if(i == 5)
					bufW.write(25 + " ");
				if(i > 5 && i < 8)
					bufW.write(0 + " ");
				if(i == 8)
					bufW.write(24 + " ");
				if(i > 8 && i < 12)
					bufW.write(0 + " ");
				if(i == 12)
					bufW.write(21 + " ");
				if(i == 13)
					bufW.write(23 + " ");
				if(i > 13 && i < 20)
					bufW.write(0 + " ");
				if(i == 20)
					bufW.write(4 + " ");
				if(i > 20 && i < 26)
					bufW.write(0 + " ");
				if(i == 26)
					bufW.write(14 + " ");
				if(i > 26 && i < mapWidth)
					bufW.write(0 + " ");
				if(i == mapWidth)
					bufW.write(25 + "\n");
			}
			
			//17
			for(int i=1; i<=mapWidth; i++){
				if(i == 1)
					bufW.write(25 + " ");
				if(i > 1 && i < 4)
					bufW.write(0 + " ");
				if(i == 4)
					bufW.write(25 + " ");
				if(i == 5)
					bufW.write(0 + " ");
				if(i == 6)
					bufW.write(25 + " ");
				if(i > 6 && i < 9)
					bufW.write(0 + " ");
				if(i == 9)
					bufW.write(24 + " ");
				if(i > 9 && i < 14)
					bufW.write(0 + " ");
				if(i == 14)
					bufW.write(22 + " ");
				if(i > 14 && i < 17)
					bufW.write(0 + " ");
				if(i == 17)
					bufW.write(0 + " ");
				if(i == 18)
					bufW.write(0 + " ");
				if(i > 18 && i < 21)
					bufW.write(24 + " ");
				if(i >= 21 && i < 25)
					bufW.write(0 + " ");
				if(i == 25)
					bufW.write(14 + " ");
				if(i > 25 && i < mapWidth)
					bufW.write(0 + " ");
				if(i == mapWidth)
					bufW.write(25 + "\n");
			}
			
			//18
			for(int i=1; i<=mapWidth; i++){
				if(i == 1)
					bufW.write(25 + " ");
				if(i == 2)
					bufW.write(1 + " ");
				if(i == 3)
					bufW.write(25 + " ");
				if(i > 3 && i < 7)
					bufW.write(0 + " ");
				if(i == 7)
					bufW.write(25 + " ");
				if(i == 8)
					bufW.write(0 + " ");
				if(i == 9)
					bufW.write(24 + " ");
				if(i == 10)
					bufW.write(0 + " ");
				if(i == 11)
					bufW.write(24 + " ");
				if(i > 11 && i < 16)
					bufW.write(0 + " ");
				if(i == 16)
					bufW.write(19 + " ");
				if(i == 17)
					bufW.write(17 + " ");
				if(i == 18)
					bufW.write(17 + " ");
				if(i == 19)
					bufW.write(17 + " ");
				if(i == 20)
					bufW.write(17 + " ");
				if(i == 21)
					bufW.write(20 + " ");
				if(i >= 22 && i < 24)
					bufW.write(0 + " ");
				if(i == 24)
					bufW.write(14 + " ");
				if(i > 24 && i < mapWidth)
					bufW.write(0 + " ");
				if(i == mapWidth)
					bufW.write(25 + "\n");
			}
			
			//19
			for(int i=1; i<=mapWidth; i++){
				if(i >= 1 && i < 3)
					bufW.write(25 + " ");
				if(i >= 3 && i < 5)
					bufW.write(0 + " ");
				if(i == 5)
					bufW.write(2 + " ");
				if(i > 5 && i < 9)
					bufW.write(0 + " ");
				if(i >= 9 && i < 11)
					bufW.write(24 + " ");
				if(i >= 11 && i < 14)
					bufW.write(0 + " ");
				if(i >= 14 && i < 16)
					bufW.write(1 + " ");
				if(i == 16)
					bufW.write(0 + " ");
				if(i == 17)
					bufW.write(4 + " ");
				if(i > 17 && i < 20)
					bufW.write(0 + " ");
				if(i == 20)
					bufW.write(2 + " ");
				if(i > 20 && i < 23)
					bufW.write(0 + " ");
				if(i == 23)
					bufW.write(14 + " ");
				if(i > 23 && i < mapWidth)
					bufW.write(0 + " ");
				if(i == mapWidth)
					bufW.write(25 + "\n");
			}
			
			//20
			for(int i=1; i<=mapWidth; i++){
				if(i < mapWidth)
					bufW.write(25 + " ");
				else
					bufW.write(25 + "\n");
			}
			
			bufW.close();
		}catch(Exception ex){}
	}
	
	//а потом считываем карту с текстового файла
	public void loadMap(){
		try{
			InputStreamReader in = new InputStreamReader(context.openFileInput(MAP));
			BufferedReader bufR = new BufferedReader(in);
			
			minX = displayWidth - mapWidth*mapSize;
			minY = displayHeight - mapHeight*mapSize;
			
			//количество блоков для прорисовки
			numRowToDraw = displayHeight/mapSize + 2;
			numColToDraw = displayWidth/mapSize + 2;
			
			//переносим карту в массив
			map = new int[mapHeight][mapWidth];
			String delimeters = "\\s+";
			
			for(int row = 0; row < mapHeight; row++){
				String line = bufR.readLine();
				String[] tokens = line.split(delimeters);
				
				for(int col = 0; col < mapWidth; col++)
					map[row][col] = Integer.parseInt(tokens[col]);
			}
			
		}catch(Exception ex){}
	}
	
	//загрузка текстур
	public void loadTile(){
		tileSet = BitmapFactory.decodeResource(context.getResources(), R.drawable.tileset);
		Bitmap tileSetTemp;
		
		/*
		if(density < 480 && density > 240)
			if(density != 320){
				tileSetTemp = getTileSet();
				tileSet = tileSetTemp;
			}
		*/
		
		tileSetTemp = getTileSet();
		tileSet = tileSetTemp;
		
		int numTileAcross = tileSet.getWidth() / mapSize;
		tiles = new Tile[2][numTileAcross];
		
		Bitmap subImage;
		int[] pixel;
		
		for(int col = 0; col < numTileAcross; col++){
			subImage = Bitmap.createBitmap(mapSize, mapSize, Bitmap.Config.ARGB_8888);
			pixel = new int[mapSize * mapSize];
			
			tileSet.getPixels(pixel, 0, mapSize, col * mapSize, 0, mapSize, mapSize);
			subImage.setPixels(pixel, 0, mapSize, 0, 0, mapSize, mapSize);
			tiles[0][col] = new Tile(subImage, false);
			
			subImage = Bitmap.createBitmap(mapSize, mapSize, Bitmap.Config.ARGB_8888);
			pixel = new int[mapSize * mapSize];
			
			tileSet.getPixels(pixel, 0, mapSize, col * mapSize, mapSize, mapSize, mapSize);
			subImage.setPixels(pixel, 0, mapSize, 0, 0, mapSize, mapSize);
			tiles[1][col] = new Tile(subImage, true);
		}
	}
	
	//метод для расширения спрайтов 
	public Bitmap getTileSet(){
		Bitmap tileSetTemp = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
		
		//4.0x
		if(mapSize == 288)
			tileSetTemp = Bitmap.createScaledBitmap(tileSet, 3744, 576, true);
		
		//3.5x
		if(mapSize == 252)
			tileSetTemp = Bitmap.createScaledBitmap(tileSet, 3276, 504, true);
		
		//3.0x
		if(mapSize == 216)
			tileSetTemp = Bitmap.createScaledBitmap(tileSet, 2808, 432, true);
		
		//2.6x
		if(mapSize == 187)
			tileSetTemp = Bitmap.createScaledBitmap(tileSet, 2433, 374, true);
		
		//2.2x
		if(mapSize == 158)
			tileSetTemp = Bitmap.createScaledBitmap(tileSet, 2059, 316, true);
		
		//2.0x
		if(mapSize == 144)
			tileSetTemp = Bitmap.createScaledBitmap(tileSet, 1872, 288, true);
		
		//1.5x
		if(mapSize == 108)
			tileSetTemp = Bitmap.createScaledBitmap(tileSet, 1404, 216, true);
		
		//1.0x
		if(mapSize == 72)
			tileSetTemp = Bitmap.createScaledBitmap(tileSet, 936, 144, false);
		
		return tileSetTemp;
	}
	
	public void update(){}
	
	//старый метод для прорисовки
	public void drawTemp(Canvas canvas, Paint paint){
		for(int row = 0; row < mapHeight; row++){
			for(int col = 0; col < mapWidth; col++){
				int rc = map[row][col];
				
				int r = rc / tiles[0].length;
				int c = rc % tiles[0].length;
				
				canvas.drawBitmap(
						tiles[r][c].getImage(),
						x + col * mapSize,
						y + row * mapSize,
						paint
						);
			}
		}
	}
	
	//обновленный метод с оптимизацией
	public void draw(Canvas canvas, Paint paint){
		for(int row = rowOffset; row < rowOffset + numRowToDraw; row++){
			if(row >= mapHeight)
				break;
			
			for(int col = colOffset; col < colOffset + numColToDraw; col++){
				if(col >= mapWidth)
					break;
				
				if(map[row][col] == 0)
					continue;
				
				int rc = map[row][col];
				
				int r = rc / tiles[0].length;
				int c = rc % tiles[0].length;
				
				canvas.drawBitmap(
						tiles[r][c].getImage(),
						x + col * mapSize,
						y + row * mapSize,
						paint
						);
			}
		}
	}
	
}