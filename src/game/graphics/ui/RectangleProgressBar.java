package game.graphics.ui;

import game.graphics.Screen;

public class RectangleProgressBar {

    private Screen screen;
    private int screenPosX, screenPosY;
    private int width, height;
    private int barFillColour, barBorderColour;
    private String label;

    private int currentBarPercentage; // 0 - 100 %

    public RectangleProgressBar(Screen screen, int xPos, int yPos, int width, int height, String label){
        this.screen = screen;
        this.screenPosX = xPos;
        this.screenPosY = yPos;
        this.width = width;
        this.height = height;
        this.label = label;

        currentBarPercentage = 0;
        barBorderColour = 0x000000;
        barFillColour = 0x000000;
    }

    public void render(){
        this.screen.renderString(screenPosX + (width/2) - ((label.length()*8)/2) + 8, screenPosY - height, label);
        screen.renderRectangle(screenPosX, screenPosY, width, height,currentBarPercentage, barFillColour, barBorderColour,false);
    }

    public void setCurrentBarPercentage(int max, int current) {
        this.currentBarPercentage = (100 * current)/max;
    }

    public void setBarFillColour(int barFillColour) {
        this.barFillColour = barFillColour;
    }

    public void setBarBorderColour(int barBorderColour) {
        this.barBorderColour = barBorderColour;
    }
}
