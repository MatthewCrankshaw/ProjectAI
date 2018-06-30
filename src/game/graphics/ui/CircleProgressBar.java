package game.graphics.ui;
import game.graphics.Screen;

public class CircleProgressBar {

    private Screen screen;
    private int currentBarPercent;
    private int barFillColour, barBorderColour;
    private int screenPosX, screenPosY;
    private int sizeRadius;
    private String label;

    public CircleProgressBar(Screen screen, int xPos, int yPos, int size, String label){
        this.screen = screen;
        this.screenPosX = xPos;
        this.screenPosY = yPos;
        this.sizeRadius = size;
        this.label = label;

        currentBarPercent = 100;
        barFillColour = 0x000000;
        barBorderColour = 0x000000;
    }

    public void render(){
        this.screen.renderString(screenPosX + (sizeRadius/2) - ((label.length()*8)) - 8, screenPosY - sizeRadius-10, label);
        this.screen.renderCircle(screenPosX, screenPosY, sizeRadius, currentBarPercent, barFillColour, barBorderColour, true, false);
    }

    public void setCurrentBarPercent(int max, int current){
        this.currentBarPercent = (100 * current) / max;
    }

    public void setBarFillColour(int colour){
        this.barFillColour = colour;
    }

    public void setBarBorderColour(int colour){
        this.barBorderColour = colour;
    }
}
