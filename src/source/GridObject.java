/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

/**
 *
 * @author Nouman
 */
public abstract class GridObject {
    protected int xPosition;
    protected int yPosititon;

    public GridObject(int xPosition, int yPosititon) {
        this.xPosition = xPosition;
        this.yPosititon = yPosititon;
    }

    
    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosititon() {
        return yPosititon;
    }

    public void setyPosititon(int yPosititon) {
        this.yPosititon = yPosititon;
    }
    
}
