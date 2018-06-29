/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static java.awt.font.TextAttribute.FONT;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.paint.Color;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import static javax.swing.text.StyleConstants.Bold;
import java.util.Date;

/**
 *
 * @author Nouman
 */
public class Game extends javax.swing.JFrame {
    
    public final int GRID_SIZE = 81;
    public JToggleButton[] gridGUIArr = new JToggleButton[81];
    public Grid gameGridJava = new Grid();
    public boolean flagArray[] = new boolean[GRID_SIZE];
    int FLAGS = 10;
    public boolean checkedArray[] = new boolean[GRID_SIZE];
    public int minePos[] = new int[10];
    public int flagPos[] = new int[10];
    boolean gameContinue = true;
    MsAccess ms;
    Statistics stat;
    
    Timer timer = new Timer(1000, new ActionListener(){
            int time =0;
            @Override
            public void actionPerformed(ActionEvent e) {
                time++;
                lblTimer.setText(Integer.toString(time));
        
            }
    });
    
    /**
     * Creates new form Game
     */            
    
    
    public void displayMine(int position){
        ImageIcon mine = new ImageIcon(getClass().getResource("/source/mine.png"));
        ImageIcon mineBlasted = new ImageIcon(getClass().getResource("/source/mine_blasted.png"));
        
        if(!flagArray[position])
        {
            gridGUIArr[position].setIcon(new ImageIcon(mineBlasted.getImage().getScaledInstance(45, 45, java.awt.Image.SCALE_SMOOTH)));

            for(int i=0;i<GRID_SIZE;i++)
            {
                if(gameGridJava.getObjectAtPositon(i).getClass().getSimpleName().equals("Mine") && i!=position)
                {
                    gridGUIArr[i].setIcon(new ImageIcon(mine.getImage().getScaledInstance(45, 45, java.awt.Image.SCALE_SMOOTH)));
                }
            }

            timer.stop();
            JOptionPane.showMessageDialog(null, "You clicked on a mine! You lost. \n"+"Time: "+lblTimer.getText(), "Game Over", JOptionPane.INFORMATION_MESSAGE);
            gameContinue = false;
            
            DateFormat dateFormat;
            dateFormat = new SimpleDateFormat("dd/MM/yyy");
            Date date = new Date();
            String d = dateFormat.format(date);
            ms.insertStat(d, Integer.parseInt(lblTimer.getText()), "Lost");
        }
    }
    
    private void getMinePositions(){
        int index = 0;
        for(int i=0;i<GRID_SIZE;i++)
        {
            if(gameGridJava.getObjectAtPositon(i).getClass().getSimpleName().equals("Mine"))
            {
                minePos[index] = i;
                index++;
            }
        }
    }
    
    public void displayFlag(int position){
       ImageIcon flag = new ImageIcon(getClass().getResource("/source/flag.png"));

        if(flagArray[position]==false && checkedArray[position]==false)
        {
            gridGUIArr[position].setIcon(new ImageIcon(flag.getImage().getScaledInstance(45, 45, java.awt.Image.SCALE_SMOOTH)));
            flagArray[position] = true;
            FLAGS--;
            
            if(FLAGS>=0)
                flagPos[FLAGS] = position;
            
            lblFlags.setText(String.valueOf(FLAGS));
        }
        else if(flagArray[position]==true)
        {
            gridGUIArr[position].setIcon(null);
            flagArray[position] = false;
            flagPos[FLAGS] = -1;
            FLAGS++;
            lblFlags.setText(String.valueOf(FLAGS));
        }
   }
     
    private boolean checkForNum(int xPos,int yPos){
        
        if((xPos>=0 && xPos<9) && (yPos>=0 && yPos<9)){
            int position = (yPos*9) + xPos;
            if(!gameGridJava.getObjectAtPositon(position).getClass().getSimpleName().equals("Mine") && gridGUIArr[position].isVisible() && !flagArray[position]){
                return true;
            }
        }
        return false;
    }
    
    private boolean checkWinStatus(){
        
        boolean winStatus = true;
        boolean flagCheck;
        
        for(int i=0;i<GRID_SIZE && winStatus;i++){
            if(checkedArray[i]==false && flagArray[i]==false && !gameGridJava.getObjectAtPositon(i).getClass().getSimpleName().equals("Mine"))
                winStatus = false;
        }
       
        
        if(FLAGS>=0)
        {
            for(int i=9;i>FLAGS-1 && winStatus;i--)
            {
                flagCheck = false;
                
                for(int j=0;j<10;j++)
                {
                    if(flagPos[i]==minePos[j])
                        flagCheck = true;
                }
                    
                if(!flagCheck)
                    winStatus = false;
            }
        }
        else
            winStatus = false;
        
        
        return winStatus;
    }
    
    public void displayNumber(int position){
        Number n = (Number)gameGridJava.getObjectAtPositon(position);
        
        if(!flagArray[position])
        {
            if(n.getNum()!=0)
            {
                gridGUIArr[position].setBackground(new java.awt.Color(221, 221, 255));
                gridGUIArr[position].setText(String.valueOf(n.getNum()));
                checkedArray[position] = true;

                if(this.checkWinStatus())
                {
                    JOptionPane.showMessageDialog(null, "Congratulations! You have won. \n"+"Time: "+lblTimer.getText(), "Game Won", JOptionPane.INFORMATION_MESSAGE);
                    
                    DateFormat dateFormat;
                    dateFormat = new SimpleDateFormat("dd/MM/yyy");
                    Date date = new Date();
                    String d = dateFormat.format(date);
                    ms.insertStat(d, Integer.parseInt(lblTimer.getText()), "Won");
                }
            }
            else if((n.getNum()==0))
            {
                gridGUIArr[position].setVisible(false);
                checkedArray[position] = true;

                int yPos = position/9;
                int xPos = position-((position/9)*9);

                if(checkForNum(xPos-1,yPos-1))
                    displayNumber(position-10);
                if(checkForNum(xPos,yPos-1))
                    displayNumber(position-9);
                if(checkForNum(xPos+1,yPos-1))
                    displayNumber(position-8);
                if(checkForNum(xPos-1,yPos))
                    displayNumber(position-1);
                if(checkForNum(xPos+1,yPos))
                    displayNumber(position+1);
                if(checkForNum(xPos-1,yPos+1))
                    displayNumber(position+8);
                if(checkForNum(xPos,yPos+1))
                    displayNumber(position+9);
                if(checkForNum(xPos+1,yPos+1))
                    displayNumber(position+10); 

            }
        }
    }
    
    public void displayStats(){
        int games = stat.getGamesPlayed();
        int won = stat.getGamesWon();
        int lost = stat.getGamesLost();
        float winPercentage = ((float)won/(float)games)*100;
        int winStreak = stat.getLongestWinStreak();
        int lostStreak = stat.getLongestLosingStreak();
        int bestTime = stat.getBestTime();
       
        JOptionPane.showMessageDialog(null, "Total Games Played: "+games+"\n"+
                "Games Won: "+won+"\n"+
                "Games Lost: "+lost+"\n"+
                "Win Percentage: "+winPercentage+"\n"+
                "Longest Win Streak: "+winStreak+"\n"+
                "Longest Losing Streak: "+lostStreak+"\n"+
                "Best Time: "+bestTime+"\n", "Statistics", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public Game() {
        initComponents();
       
        gameGridJava.initializeGrid();
        GameGrid.setLayout(new java.awt.GridLayout(9, 9));
        lblFlags.setText(String.valueOf(10));
        ms = new MsAccess();
        stat = new Statistics();
        ms.fillStatistics(stat);
       
        
        for (int i = 0; i < GRID_SIZE; i++) {
            JToggleButton b = new JToggleButton();
            b.setBackground(new java.awt.Color(102, 102, 255));
            b.setName(String.valueOf(i));
            b.setFont(new Font(Font.SANS_SERIF,Font.BOLD,14));
            flagArray[i] = false;
            checkedArray[i] = false;
            
            b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    
                    
                }
            });
            
            b.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if(gameContinue)
                    {
                        if (e.getButton() == MouseEvent.BUTTON3) {
                            int position = Integer.parseInt(b.getName());
                            displayFlag(position);
                        }
                        else
                        {
                            int position = Integer.parseInt(b.getName());
                            GridObject g = gameGridJava.getObjectAtPositon(position);

                            if(g.getClass().getSimpleName().equals("Mine"))
                            {
                                displayMine(position);
                            }
                            else if(g.getClass().getSimpleName().equals("Number"))
                            {
                                displayNumber(position);
                            }
                        }
                    }
                }
            });
            
            GameGrid.add(b);
            gridGUIArr[i] = b;
        }
        
        getMinePositions();
        
        timer.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        lblTimer = new javax.swing.JLabel();
        lblFlags = new javax.swing.JLabel();
        GameGrid = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        btnStats = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setBackground(new java.awt.Color(204, 204, 255));

        lblTimer.setBackground(new java.awt.Color(0, 102, 255));
        lblTimer.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        lblTimer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTimer.setText("0");
        lblTimer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblFlags.setBackground(new java.awt.Color(0, 102, 204));
        lblFlags.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        lblFlags.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFlags.setText("8");
        lblFlags.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        GameGrid.setBackground(new java.awt.Color(221, 221, 255));
        GameGrid.setPreferredSize(new java.awt.Dimension(457, 465));

        javax.swing.GroupLayout GameGridLayout = new javax.swing.GroupLayout(GameGrid);
        GameGrid.setLayout(GameGridLayout);
        GameGridLayout.setHorizontalGroup(
            GameGridLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 457, Short.MAX_VALUE)
        );
        GameGridLayout.setVerticalGroup(
            GameGridLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 465, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(185, 185, 185)
                        .addComponent(lblTimer, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(187, 187, 187)
                        .addComponent(lblFlags, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(116, 116, 116)
                        .addComponent(GameGrid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(163, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(GameGrid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFlags, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTimer, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jButton1.setText("jButton1");

        btnStats.setText("Statistics");
        btnStats.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnStatsMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(jButton1)
                .addGap(64, 64, 64)
                .addComponent(btnStats)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(btnStats))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnStatsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnStatsMouseClicked
        displayStats();
    }//GEN-LAST:event_btnStatsMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {   
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Game().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel GameGrid;
    private javax.swing.JButton btnStats;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblFlags;
    private javax.swing.JLabel lblTimer;
    // End of variables declaration//GEN-END:variables
}
