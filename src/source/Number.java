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
public class Number extends GridObject{
    private int num;

    public Number(int num, int xPosition, int yPosititon) {
        super(xPosition, yPosititon);
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
    
    
}
