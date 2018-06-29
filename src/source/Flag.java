/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

enum flag {
    markFlag,questionFlag;
}

/**
 *
 * @author Nouman
 */
public class Flag extends GridObject{
    
    private flag type;

    public Flag(flag type, int xPosition, int yPosition) {
        super(xPosition, yPosition);
        this.type = type;
    }
    
}
