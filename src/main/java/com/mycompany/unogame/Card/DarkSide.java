package com.mycompany.unogame.Card;

public class DarkSide {
    public enum Color {
        WILD, WILD_DRAW_COLOR, TEAL, PURPLE, ORANGE, PINK
    }

    public enum values {
        NONE, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, ZERO, DRAW_FIVE, SKIP_EVERYONE, REVERSE, FLIP
    }
    private values value;
    private Color color;
    DarkSide(int colorIndex, int valueIndex){
        if ((colorIndex == 0)||(colorIndex == 1)){
            valueIndex = 0;
        }
        this.color = DarkSide.Color.values()[colorIndex];
        this.value = DarkSide.values.values()[valueIndex];
    }
    DarkSide(int colorIndex){
        if ((colorIndex == 0)||(colorIndex == 1)){
            int valueIndex = 0;
            this.color = DarkSide.Color.values()[colorIndex];
            this.value = DarkSide.values.values()[valueIndex];
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
        return "DarkSide (" + color + ", " + value + ")";
    }
}
