package es.cristichi.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import es.cristichi.obj.GameObject;
import es.cristichi.obj.game.Board;

public class BoardComponent extends JComponent {
    private static final Font GAMEOBJ_DISPLAY_FONT = new Font("Arial", Font.BOLD, 30);
    private Board cacheBoard;

    public BoardComponent(Board starting){
        this.cacheBoard = starting;
    }

    public synchronized void updateBoard(Board updated){
        this.cacheBoard = updated;
        invalidate();
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (g instanceof Graphics2D g2d){
            Dimension canvas = getSize();

            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, canvas.width, canvas.height);

            Dimension squareSize = new Dimension((int) canvas.getWidth()/cacheBoard.getColumnsLength(), (int) canvas.getHeight()/cacheBoard.getRowsLength());

            g2d.setFont(GAMEOBJ_DISPLAY_FONT);
            cacheBoard.forEachSquare((pos, square) -> {
                if (square.getObject() != null){
                    GameObject gameObject = square.getObject();
                    String str = ""+gameObject.getTextDisplay();
                    
                    Rectangle boardSquare = new Rectangle(squareSize.width*pos.column(), squareSize.height*pos.row(), squareSize.width, squareSize.height);

                    g2d.setColor(gameObject.getColorDisplay());
                    g2d.fillRect(boardSquare.x, boardSquare.y, boardSquare.width, boardSquare.height);

                    FontMetrics fm = g2d.getFontMetrics();
                    Rectangle2D r = fm.getStringBounds(str, g2d);
                    int x = (int) (boardSquare.x+boardSquare.width/2 - r.getWidth()/2);
                    int y = (int) (boardSquare.y+boardSquare.height/2 - r.getHeight()/2) + fm.getAscent();
                    g2d.setColor(Color.BLACK);
                    g.drawString(str, x-1, y-1);
                    g2d.setColor(Color.BLACK);
                    g.drawString(str, x+1, y+1);
                    g2d.setColor(Color.GREEN);
                    g.drawString(str, x, y);
                }
            });
        }
    }
}
