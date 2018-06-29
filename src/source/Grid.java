/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Nouman
 */
public class Grid {
    
    private GridObject[] gridList;
    public final int GRID_SIZE = 81;

    public Grid() {
        gridList = new GridObject[81];
        
        for(int i=0;i<GRID_SIZE;i++)
            gridList[i] = null;
    }
    
    void addGridObject(GridObject g, int position){
        gridList[position] = g;
    }
    
    GridObject getObjectAtPositon(int position){
        return gridList[position];
    }
    
    private boolean checkMine(int xPos,int yPos){
        if((xPos>=0 && xPos<9) && (yPos>=0 && yPos<9)){
            int position = (yPos*9) + xPos;
            
            if(gridList[position]!=null){
                if(gridList[position].getClass().getSimpleName().equals("Mine")){
                    return true;
                }
            }
        }
        return false;
    }
    
    private int getXPos(int position){
        return position-((position/9)*9);
    }
    
    private int getYPos(int position){
        return position/9;
           
    }
    
    void initializeGrid(){
        
        Random random;
        int position,xPos,yPos;
        GridObject g;
        
        for(int i=0;i<10;i++){
            random = new Random();
            position = random.nextInt(GRID_SIZE);
            
            while(gridList[position]!=null)
            {
                position = random.nextInt(GRID_SIZE);
            }
            
            yPos = this.getYPos(position);
            
            xPos = this.getXPos(position);
           
            g = new Mine(xPos,yPos);
            this.addGridObject(g,position);
        }
        
        int num;
        for(int i=0;i<GRID_SIZE;i++)
        {
            num=0;
            
            if(gridList[i]==null)
            {
                yPos = this.getYPos(i);
                xPos = this.getXPos(i);
                
                if(this.checkMine(xPos-1,yPos-1))
                    num++;
                if(this.checkMine(xPos,yPos-1))
                    num++;
                if(this.checkMine(xPos+1,yPos-1))
                    num++;
                if(this.checkMine(xPos-1,yPos))
                    num++;
                if(this.checkMine(xPos+1,yPos))
                    num++;
                if(this.checkMine(xPos-1,yPos+1))
                    num++;
                if(this.checkMine(xPos,yPos+1))
                    num++;
                if(this.checkMine(xPos+1,yPos+1))
                    num++;  

                g = new Number(num,xPos,yPos);
                
                this.addGridObject(g,i);
            }
        }
        
    }
    
}
