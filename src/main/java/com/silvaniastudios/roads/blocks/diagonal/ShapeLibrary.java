package com.silvaniastudios.roads.blocks.diagonal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.silvaniastudios.roads.blocks.paint.properties.PaintGrid;
import com.silvaniastudios.roads.client.render.Quad;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.math.Vec3d;

import javax.imageio.ImageIO;

public class ShapeLibrary {
	
	/*
	 * 
	 *   ####  #   #    #    ####   #####   ####
	 *  #      #   #   # #   #   #  #      #
	 *  #      #   #  #   #  #   #  #      #
	 *   ###   #####  #####  ####   ####    ###
	 *      #  #   #  #   #  #      #          #
	 *      #  #   #  #   #  #      #          #
	 *  ####   #   #  #   #  #      #####  #### 
	 *  
	 */


	public static List<Quad> shapeFromGrid(boolean[][] grid, float top, TextureAtlasSprite sprite, VertexFormat format, boolean drawBottom) {
		return shapeFromGrid(grid, top, sprite, format, drawBottom, 1.0f);
	}

	public static List<Quad> shapeFromGrid(boolean[][] grid, float top, TextureAtlasSprite sprite, VertexFormat format, boolean drawBottom, float scale) {
		sprite.setFramesTextureData(null);
		List<Quad> quads = new ArrayList<>();
		float p = scale / grid.length;
		float p2 = scale / 64.0f;

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (grid[j][i]) {
					//north edge
					if (i == 0 || (i-1 >= 0 && !grid[j][i-1])) {
						quads.add(new Quad(
								new Vec3d(j*p+p, top-p2, i*p), 0,    0.5f, //BL
								new Vec3d(j*p,   top-p2, i*p), 0.5f, 0.5f, //BR
								new Vec3d(j*p,   top,    i*p), 0.5f, 0, //TR
								new Vec3d(j*p+p, top,    i*p), 0,    0, //TL
								sprite, format));
					}

					//east side
					if (j+1 == grid.length || (j+1 < grid.length && !grid[j+1][i])) {
						quads.add(new Quad(
								new Vec3d(j*p+p, top-p2, i*p+p),   0,    0.5f, //BL
								new Vec3d(j*p+p, top-p2, i*p), 0.5f, 0.5f, //BR
								new Vec3d(j*p+p, top,    i*p), 0.5f, 0, //TR
								new Vec3d(j*p+p, top,    i*p+p),   0,    0, //TL
								sprite, format));
					}

					//south side
					if (i+1 == grid.length || (i+1 < grid.length && !grid[j][i+1])) {
						quads.add(new Quad(
								new Vec3d(j*p,   top-p2, i*p+p), 0,    0.5f, //BL
								new Vec3d(j*p+p, top-p2, i*p+p), 0.5f, 0.5f, //BR
								new Vec3d(j*p+p, top,    i*p+p), 0.5f, 0, //TR
								new Vec3d(j*p,   top,    i*p+p), 0,    0, //TL
								sprite, format));
					}

					//west side
					if (j == 0 || (j-1 >= 0 && !grid[j-1][i])) {
						quads.add(new Quad(
								new Vec3d(j*p, top-p2, i*p), 0,    0.5f, //BL
								new Vec3d(j*p, top-p2, i*p+p),   0.5f, 0.5f, //BR
								new Vec3d(j*p, top,    i*p+p),   0.5f, 0, //TR
								new Vec3d(j*p, top,    i*p), 0,    0, //TL
								sprite, format));
					}



					quads.add(new Quad( //Top
							new Vec3d(j*p, top, i*p+p),   //BL
							new Vec3d(j*p+p, top, i*p+p),     //BR
							new Vec3d(j*p+p, top, i*p), //TR
							new Vec3d(j*p, top, i*p),     //TL
							sprite, format));

					if (drawBottom) {
						quads.add(new Quad( //Bottom
								new Vec3d(j*p+p, top-p2, i*p+p),   //BL
								new Vec3d(j*p, top-p2, i*p+p),     //BR
								new Vec3d(j*p, top-p2, i*p), //TR
								new Vec3d(j*p+p, top-p2, i*p),     //TL
								sprite, format));
					}
				}
			}
		}

		return quads;
	}

	public static List<Quad> shapeFromGridFlat(boolean[][] grid, float xMin, float xMax, float yMin, float yMax, float zMin, float zMax, TextureAtlasSprite sprite) {
		sprite.setFramesTextureData(null);
		List<Quad> quads = new ArrayList<>();

		float deltaX = xMax - xMin;
		float deltaY = yMax - yMin;
		float deltaZ = zMax - zMin;
		float x = deltaX / (grid.length);
		float y = deltaY / (grid.length);
		float z = deltaZ / (grid.length);

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (grid[j][i]) {
					quads.add(new Quad(
							new Vec3d(xMin + x*j + x, yMax - i*y,     zMin + z*i),   //BL
							new Vec3d(xMin + x*j,     yMax - i*y,     zMin + z*i),     //BR
							new Vec3d(xMin + x*j,     yMax - i*y - y, zMin + z*i + z), //TR
							new Vec3d(xMin + x*j + x, yMax - i*y - y, zMin + z*i + z),     //TL
							sprite, DefaultVertexFormats.ITEM));
				}
			}
		}

		return quads;
	}
	
	
	public static List<Quad> shapeTriangleLeft(HalfBlock block, double width, VertexFormat format) {
		List<Quad> quads = new ArrayList<>();
		float height = block.getHeight();
		TextureAtlasSprite sprite = block.getSprite();
		
		Quad top = null;
		Quad bottom = null;
		Quad north = null;
		Quad east = null;

		if (block.renderTop()) {
			if (block.isFluid()) {
				top = new Quad( //Top
						new Vec3d(0, block.heightSW(), 0.5),   //BL
						new Vec3d(0, block.heightSE(), 1),     //BR
						new Vec3d(1-width, block.heightNE(), 0), //TR
						new Vec3d(0, block.heightNW(), 0),     //TL
						sprite, format);
				bottom = new Quad( //bottom
						new Vec3d(0, block.heightSW(), 0),
						new Vec3d(1-width, block.heightNW(), 0),
						new Vec3d(0, block.heightNE(), 1),
						new Vec3d(0, block.heightSE(), 0.5),				
						sprite, format);
			} else {
				top = new Quad( //Top
						new Vec3d(0, height, 0.5),   //BL
						new Vec3d(0, height, 1),     //BR
						new Vec3d(1-width, height, 0), //TR
						new Vec3d(0, height, 0),     //TL
						sprite, format);
			}
		}

		if (block.renderBottom()) {
			bottom = new Quad( //bottom
					new Vec3d(0, 0, 0),
					new Vec3d(1-width, 0, 0),
					new Vec3d(0, 0, 1),
					new Vec3d(0, 0, 0.5),				
					sprite, format);
			
		}

		if (block.renderNear()) {
			north = new Quad( 
					new Vec3d(1-width, height, 0), 	(float) width*16f, 	16-height*16f,
					new Vec3d(1-width, 0, 0), 		(float) width*16f,	16,
					new Vec3d(0, 0, 0),				16, 				16,
					new Vec3d(0, height, 0), 		16, 				16-height*16f,
					sprite, format);
			
		}

		if (block.renderDivision()) {
			east = new Quad( //East (long) side
					new Vec3d(0, height, 1), 0, 16-height*16f, //TL
					new Vec3d(0, 0, 1), 0, 16, //BL
					new Vec3d(1-width, 0, 0), 16, 16, //BR
					new Vec3d(1-width, height, 0), 16, 16-height*16f,//TL
					sprite, format);
			
		}

		quads.add(top);
		quads.add(bottom);
		quads.add(north);
		quads.add(east);
		return quads;
	}


	public static List<Quad> shapeTriangleRight(HalfBlock block, double width, VertexFormat format) {
		List<Quad> quads = new ArrayList<>();
		float height = block.getHeight();
		TextureAtlasSprite sprite = block.getSprite();

		Quad top = null;
		Quad bottom = null;
		Quad north = null;
		Quad east = null;
		
		if (block.renderTop()) {
			if (block.isFluid()) {
				top = new Quad( //Top
						new Vec3d(0, block.heightSW(), 0.5),   //BL
						new Vec3d(0, block.heightSE(), 1),     //BR
						new Vec3d(1-width, block.heightNE(), 1), //TR
						new Vec3d(0, block.heightNW(), 0),     //TL
						sprite, format);
				bottom = new Quad( //bottom
						new Vec3d(1-width, block.heightSW(), 1),
						new Vec3d(0, block.heightNW(), 1),	
						new Vec3d(0, block.heightNE(), 0.5),
						new Vec3d(0, block.heightSE(), 0),			
						sprite, format);
			} else {
				top = new Quad( //Top
						new Vec3d(0, height, 0.5),   //BL
						new Vec3d(0, height, 1),     //BR
						new Vec3d(1-width, height, 1), //TR
						new Vec3d(0, height, 0),     //TL
						sprite, format);
			}
		}

		if (block.renderBottom()) {
			bottom = new Quad( //bottom
					new Vec3d(1-width, 0, 1),
					new Vec3d(0, 0, 1),	
					new Vec3d(0, 0, 0.5),
					new Vec3d(0, 0, 0),			
					sprite, format);
		}

		if (block.renderFarMirrored()) {
			north = new Quad(  //North side
					new Vec3d(0, height, 1), 		0, 					16-height*16f,
					new Vec3d(0, 0, 1), 			0, 					16,
					new Vec3d(1-width, 0, 1), 		(float) width*16f, 	16,
					new Vec3d(1-width, height, 1),  (float) width*16f, 	16-height*16f,
					sprite, format);
		}

		if (block.renderDivision()) {
			east = new Quad( //East (long) side
					new Vec3d(1-width, height, 1), 0, 16-height*16f, //TL
					new Vec3d(1-width, 0, 1), 0, 16, //BL
					new Vec3d(0, 0, 0), 16, 16, //BR
					new Vec3d(0, height, 0), 16, 16-height*16f,//TL
					sprite, format);
		}
		
		quads.add(top);
		quads.add(bottom);
		quads.add(north);
		quads.add(east);
		
		return quads;
	}


	//Omits the left side face
	public static List<Quad> shapeTrapeziumLeft(HalfBlock block, double widthN, double widthW, VertexFormat format) {
		List<Quad> quads = new ArrayList<>();
		float height = block.getHeight();
		TextureAtlasSprite sprite = block.getSprite();
		
		Quad top = null;
		Quad bottom = null;
		Quad south = null;
		Quad north = null;
		Quad front = null;
		
		if (block.renderTop()) {
			if (block.isFluid()) {
				top = new Quad( //Top
						new Vec3d(0.0, block.heightSW(), 1.0),//south west
						new Vec3d(widthN, block.heightSE(), 1.0), //south east
						new Vec3d(widthW, block.heightNE(), 0.0), //north east
						new Vec3d(0, block.heightNW(), 0.0), //north west
						sprite, format);
				bottom = new Quad( //Bottom
						new Vec3d(widthN, block.heightSW(), 1),  //Bottom left
						new Vec3d(0,      block.heightNW(), 1),
						new Vec3d(0,      block.heightNE(), 0), 
						new Vec3d(widthW, block.heightSE(), 0), 
						sprite, format);
			} else {
				top = new Quad( //Top
						new Vec3d(0.0, height, 1.0),//south west
						new Vec3d(widthN, height, 1.0), //south east
						new Vec3d(widthW, height, 0.0), //north east
						new Vec3d(0, height, 0.0), //north west
						sprite, format);
			}
		}

		if (block.renderBottom()) {
			bottom = new Quad( //Bottom
					new Vec3d(widthN, 0, 1),  //Bottom left
					new Vec3d(0,      0, 1),
					new Vec3d(0,      0, 0), 
					new Vec3d(widthW, 0, 0), 
					sprite, format);
		}

		if (block.renderFar()) {
			south = new Quad(
					new Vec3d(widthN, height, 1.0),	(float) (16f*widthN), 	16 - (float) (16f * height),//south west
					new Vec3d(0,      height, 1.0), 0, 						16 - (float) (16f * height),//south east
					new Vec3d(0,      0.0, 1.0), 	0, 						16,//north east
					new Vec3d(widthN, 0.0, 1.0), 	(float) (16f*widthN), 	16, //north west
					sprite, format);
		}

		if (block.renderNear()) {
			north = new Quad(
					new Vec3d(widthW, 0,      0), 0,                    16,//south west
					new Vec3d(0,      0,      0), (float) (16f*widthW), 16,//south east
					new Vec3d(0,      height, 0), (float) (16f*widthW), 16 - (float) (16f * height),//north east
					new Vec3d(widthW, height, 0), 0,                    16 - (float) (16f * height), //north west
					sprite, format);
		}

		if (block.renderDivision()) {
			front = new Quad( //Front (long side)
					new Vec3d(widthN, 0, 1.0), 			0, 	16,//south west
					new Vec3d(widthW, 0, 0.0), 			16, 16,//south east
					new Vec3d(widthW, height, 0.0), 	16, 16-height * 16f,//north east
					new Vec3d(widthN, height, 1.0), 	0, 	16-height * 16f,//north west
					sprite, format);
		}
		
		quads.add(top);
		quads.add(bottom);
		quads.add(south);
		quads.add(north);
		quads.add(front);
		
		return quads;
	}

	//Omits the right side face
	public static List<Quad> shapeTrapeziumRight(HalfBlock block, double widthN, double widthW, VertexFormat format) {
		List<Quad> quads = new ArrayList<>();
		float height = block.getHeight();
		TextureAtlasSprite sprite = block.getSprite();
		
		Quad top = null;
		Quad bottom = null;
		Quad south = null;
		Quad north = null;
		Quad front = null;
		
		if (block.renderTop()) {
			if (block.isFluid()) {
				top = new Quad( //Top
						new Vec3d(0.0, block.heightSW(), 1.0),//south west
						new Vec3d(widthW, block.heightSE(), 1.0), //south east
						new Vec3d(widthN, block.heightNE(), 0.0), //north east
						new Vec3d(0, block.heightNW(), 0.0), //north west
						sprite, format);
				bottom = new Quad( //Bottom
						new Vec3d(widthW, block.heightSW(), 1),  //Bottom left
						new Vec3d(0,      block.heightNW(), 1),
						new Vec3d(0,      block.heightNE(), 0), 
						new Vec3d(widthN, block.heightSE(), 0), 
						sprite, format);
			} else {
				top = new Quad( //Top
						new Vec3d(0.0, height, 1.0),//south west
						new Vec3d(widthW, height, 1.0), //south east
						new Vec3d(widthN, height, 0.0), //north east
						new Vec3d(0, height, 0.0), //north west
						sprite, format);
			}
		}

		if (block.renderBottom()) {
			bottom = new Quad( //Bottom
					new Vec3d(widthW, 0, 1),  //Bottom left
					new Vec3d(0,      0, 1),
					new Vec3d(0,      0, 0), 
					new Vec3d(widthN, 0, 0), 
					sprite, format);
		}

		if (block.renderFarMirrored()) {
			south = new Quad(
					new Vec3d(widthW, height, 1.0), 	(float) (16f*widthW), 			16 - (float) (16f * height),//south west
					new Vec3d(0, height, 1.0), 		0, 								16 - (float) (16f * height),//south east
					new Vec3d(0, 0.0, 1.0), 			0, 								16,//north east
					new Vec3d(widthW, 0.0, 1.0), 		(float) (16f*widthW), 			16, //north west
					sprite, format);
		}

		if (block.renderNearMirrored()) {
			north = new Quad(
					new Vec3d(widthN, 0, 0), 			16 - (float) (16f*widthN), 		16,//south west
					new Vec3d(0, 0, 0), 				16, 			16,//south east
					new Vec3d(0, height, 0), 			16, 			16 - (float) (16f * height),//north east
					new Vec3d(widthN, height, 0), 		16 - (float) (16f*widthN), 		16 - (float) (16f * height), //north west
					sprite, format);
		}

		if (block.renderDivision()) {
			front = new Quad( //Front (long side)
					new Vec3d(widthW, 0, 1.0), 			0, 	16,//south west
					new Vec3d(widthN, 0, 0.0), 			16, 16,//south east
					new Vec3d(widthN, height, 0.0), 	16, 16-height * 16f,//north east
					new Vec3d(widthW, height, 1.0), 	0, 	16-height * 16f,//north west
					sprite, format);
		}
		
		quads.add(top);
		quads.add(bottom);
		quads.add(south);
		quads.add(north);
		quads.add(front);

		return quads;
	}

	public static BufferedImage getImageFromGrid(String name, PaintGrid grid, int id) throws IOException {
		BufferedImage image = new BufferedImage(grid.size(), grid.size(), BufferedImage.TYPE_INT_ARGB);
		Color myWhite = new Color(255, 255, 255); // Color white
		int rgb = myWhite.getRGB();
		for (int i = 0; i < grid.size(); i++ ){
			for (int j = 0; j < grid.size(); j++) {
				if (grid.getGrid()[j][i]) {
					image.setRGB(j, i, rgb);
				}
			}
		}

		File outputfile = new File(name + "_" + id + ".png");
		ImageIO.write(image, "png", outputfile);

		return image;
	}

	public static int[][] get2DIntArrayFromImage(BufferedImage img) {
		int[][] image = new int[img.getHeight()][img.getWidth()];

		for (int i = 0; i < img.getHeight(); i++) {
			for (int j = 0; j < img.getWidth(); j++) {
				image[j][i] = img.getRGB(j, i);
			}
		}

		return image;
	}
}
