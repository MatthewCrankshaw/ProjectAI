package game.graphics;

import game.graphics.sprite.FontSprite;
import game.graphics.sprite.Sprite;
import game.levels.tile.Tile;
import game.levels.tile.animated_tiles.AnimatedTile;

import java.awt.*;

/**
 * Created by Matthew.c on 25/01/2017.
 */
public class Screen {

    public int width, height;

    public double xOffset;
    public double yOffset;

    public int[] pixels;


    public Screen(int width, int height){
        this.width = width;
        this.height = height;
        pixels = new int[width * height];
    }

    public void clear(){
        for (int i = 0; i < pixels.length; i++){
            pixels[i] = 0;
        }
    }

    public void renderSprite(int xp, int yp, Sprite sprite, boolean fixed, int colour){
        if(fixed) {
            xp -= xOffset;
            yp -= yOffset;
        }
        for(int y = 0; y < sprite.SIZE; y ++) {
            int ya = y + yp;
            for(int x = 0; x < sprite.SIZE; x++) {
                int xa = x + xp;
                if (xa < -sprite.SIZE || xa >= width || ya < 0 || ya >= height) break;
                if (xa < 0) xa = 0;

                //Set the colour -1 for original colour on sprite
                int col = sprite.pixels[x + y * sprite.SIZE];

                if(sprite.pixels[x + y * sprite.SIZE] != 0xffff00ff){
                    col = colour;
                }

                if(colour == -1) {
                    col = sprite.pixels[x + y * sprite.SIZE];
                }

                if (col != 0xffff00ff){
                    pixels[xa+ya*width] = col;
                }
            }
        }
    }

    public void renderString(int xp, int yp, String string, boolean center, int colour){
        for(int i = 0; i < string.length(); i++){
            if(string.charAt(i) == ' '){
                continue;
            }
            int len = string.length();
            int centerOffset;
            if(center){
                centerOffset = ((len*8)/2);
            }else centerOffset = 0;
            renderSprite(xp + (i*8) - centerOffset, yp, FontSprite.getCharacterSprite(string.charAt(i)), false, colour);
        }
    }

    public void renderTile(int xp, int yp, Tile tile){
        xp -= xOffset;
        yp -= yOffset;
        for(int y = 0; y < tile.currentSprite.SIZE; y ++) {
            int ya = y + yp;
            for(int x = 0; x < tile.currentSprite.SIZE; x++) {
                int xa = x + xp;
                if (xa < -tile.currentSprite.SIZE || xa >= width || ya < 0 || ya >= height) break;
                if (xa < 0) xa = 0;
                pixels[xa+ya*width] = tile.currentSprite.pixels[x+ y * tile.currentSprite.SIZE];
            }
        }
        tile.tick();
    }


    public void renderAnimatedTile(int xp, int yp, AnimatedTile animTile, Sprite[] sprite){

        int animIndex = animTile.getCurrentAnimationIndex();
        xp -= xOffset;
        yp -= yOffset;
        for(int y = 0 ; y < 16; y ++) {
            int ya = y + yp;
            for(int x = 0; x < 16; x++) {
                int xa = x + xp;
                if (xa < -16 || xa >= width || ya < 0 || ya >= height) break;
                if (xa < 0) xa = 0;
                int col = sprite[animIndex].pixels[x+y*Tile.TILE_SIZE*2];
                if (col != 0xffff00ff){
                    pixels[xa+ya*width] = sprite[animIndex].pixels[x + y * Tile.TILE_SIZE*2];
                }
            }
        }
    }

    public void drawLine(double xp1, double yp1, double xp2, double yp2, int colour, boolean fixed){
        if(fixed) {
            xp1 -= xOffset;
            yp1 -= yOffset;
            xp2 -= xOffset;
            yp2 -= yOffset;
        }

        double tempx = xp1;
        double tempy = yp1;

        double x = Math.abs(xp2 - tempx);
        double y = Math.abs(yp2 - tempy);

        double max = Math.max(x, y);

        //Inside try catch block because there is a possibility to have divide by zero
        try {
            x /= max;
            y /= max;
        }catch (ArithmeticException e){
            //If divide by zero then just return
            //We don't need to draw the line
            return;
        }

        for(int i = 0; i < max; i++){
            if(tempx < 0 || tempx >= width || tempy < 0 || tempy >= height) break;
            if(xp2 < 0 || xp2 >= width || yp2 < 0 || yp2 >= height) break;
            pixels[(int)tempx + (this.width * (int)tempy)] = colour;
            if(tempx < xp2) {
                tempx += x;
            }else{
                tempx -= x;
            }
            if(tempy < yp2){
                tempy += y;
            }else{
                tempy -= y;
            }
        }
    }

    public void renderRectangle(int xp, int yp, int width, int height, int currentPercent, int colourFill, int colourBorder, boolean fixed){
        if(fixed) {
            xp -= xOffset;
            yp -= yOffset;
        }

        for(int y = yp; y <= yp + height; y++) {
            for (int x = xp; x <= (xp + width); x++) {
                if(y == yp || y == yp+height || x == xp || x == xp+width) {
                    pixels[x + (this.width*y) ] = colourBorder;
                }else{
                    if(x <= (currentPercent * 0.01 * width) + xp){
                        pixels[x + (this.width * y)] = colourFill;
                    }
                }
            }
        }
    }

    public void renderCircle(int xp, int yp, int radius, int fill, int colour, int borderColour, boolean filled, boolean fixed){
        //whether the circle moves with the screen or is relative to the ground
        if(fixed) {
            xp -= xOffset;
            yp -= yOffset;
        }

        //fills from bottom to top
        //0 being empty 100 being full
        int fillAmount = (radius * fill) / 100;

        for(int y = -radius; y <= radius; y++){
            for(int x = -radius; x <=radius*2; x++){
                if(y+xp < 0 || y+xp >= width || x+yp < 0 || x+yp >= height) continue;

                if(x >= radius - (fillAmount*2)) {
                    if (filled) {
                        if (Math.round(Math.sqrt(y * y + x * x)) < radius) {
                            pixels[(y + xp) + (this.width * (x + yp))] = colour;
                        }
                    }
                }

                if(Math.round(Math.sqrt(y*y + x*x)) == radius){
                    pixels[(y+xp) + (this.width * (x+yp))] = borderColour;
                }

            }
        }
    }

    public void renderPlayer(int xp, int yp , int pixelsLong, int pixelshigh, Sprite sprite){
        xp -= xOffset;
        yp -= yOffset;
        for(int y = 0 ; y < pixelshigh; y ++) {
            int ya = y + yp;
            for(int x = 0; x < pixelsLong; x++) {
                int xa = x + xp;
                if (xa < -Tile.TILE_SIZE*2 || xa >= width || ya < 0 || ya >= height) break;
                if (xa < 0) xa = 0;
                int col = sprite.pixels[x + y * Tile.TILE_SIZE*2];
                if (col != 0xffff00ff){
                    pixels[xa+ya*width] = sprite.pixels[x + y * 16];
                }
            }
        }
    }

    public void setOffset(double x, double y){
        this.xOffset = x;
        this.yOffset = y;
    }
}
