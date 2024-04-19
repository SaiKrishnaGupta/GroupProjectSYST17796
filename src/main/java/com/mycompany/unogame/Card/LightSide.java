package com.mycompany.unogame.Card;

public class LightSide {
    public enum Color {
        WILD, WILD_DRAW_TWO, RED, YELLOW, BLUE, GREEN
    }

    public enum values {
        NONE, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, ZERO, DRAW_ONE, SKIP, REVERSE, FLIP
    }
    private values value;
    private Color color;
    LightSide(int colorIndex, int valueIndex){
        if ((colorIndex == 0)||(colorIndex == 1)){
            valueIndex = 0;
        }
        this.color = LightSide.Color.values()[colorIndex];
        this.value = LightSide.values.values()[valueIndex];
    }
    public LightSide(int colorIndex){
        if ((colorIndex == 0)||(colorIndex == 1)){
            int valueIndex = 0;
            this.color = LightSide.Color.values()[colorIndex];
            this.value = LightSide.values.values()[valueIndex];
        }
        else{
            System.out.println("Enter Value");
        }
    }
    public values getValue() {
        return value;
    }
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    @Override
    public String toString() {
        return "LightSide (" + color + ", " + value + ")";
    }
}
