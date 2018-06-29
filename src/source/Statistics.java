/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.util.ArrayList;

/**
 *
 * @author Nouman
 */
public class Statistics {
    private ArrayList<GameStat> gameList;

    public Statistics() {
        gameList = new ArrayList<>();
    }
    
    public void addGameToList(GameStat g){
        gameList.add(g);
    }
    
    public int getGamesPlayed(){
        return gameList.size();
    }
    
    public int getGamesWon(){
        int won = 0;
        
        for(GameStat g: gameList)
        {
            if(g.getStatus().equals("Won"))
                won++;
        }
        
        return won;
    }
    
    public int getGamesLost(){
        int lost = 0;
        
        for(GameStat g: gameList)
        {
            if(g.getStatus().equals("Lost"))
                lost++;
        }
        
        return lost;
    }
    
    public int getLongestWinStreak(){
        int streak = 0;
        int seq = 0;
        
        for(GameStat g: gameList){
            if(g.getStatus().equals("Won"))
            {
                seq++;
            }
            else {
                if(seq>streak)
                    streak=seq;
                
                seq = 0;
            }      
        }
        
        if(seq>streak)
            streak = seq;
        
        return streak;
    }
    
    public int getLongestLosingStreak(){
        int streak = 0;
        int seq = 0;
        
        for(GameStat g: gameList){
            if(g.getStatus().equals("Lost"))
            {
                seq++;
            }
            else {
                if(seq>streak)
                    streak=seq;
                seq = 0;
            }      
        }
        
        if(seq>streak)
            streak = seq;
        
        return streak;
    }
    
    public int getBestTime(){
        int time = -1;
        
        for(GameStat g : gameList){
            if((g.getStatus().equals("Won") && time==-1) || (g.getStatus().equals("Won") &&  g.getTimePlayed()<time))
                time = g.getTimePlayed();
        }
        
        return time;
    }
    
}
