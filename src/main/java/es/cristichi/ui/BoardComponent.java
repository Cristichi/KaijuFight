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

import es.cristichi.game.GameObject;
import es.cristichi.game.obj.Board;
import es.cristichi.game.obj.Kaiju;

public class BoardComponent extends JComponent {
    private static final Font GAMEOBJ_DISPLAY_FONT = new Font("Arial", Font.BOLD, 15);
    private KaijuGameWindow window;

    public BoardComponent(KaijuGameWindow window){
        this.window = window;
    }

    public KaijuGameWindow getWindow() {
        return window;
    }

    public synchronized void updateBoard(){
        invalidate();
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Board board = window.getGame().getBoard();
        if (g instanceof Graphics2D g2d){
            Dimension canvas = getSize();
            FontMetrics fm = g2d.getFontMetrics();

            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, canvas.width, canvas.height);

            Dimension squareSize = new Dimension((int) canvas.getWidth()/board.getColumnsLength(), (int) canvas.getHeight()/board.getRowsLength());

            g2d.setFont(GAMEOBJ_DISPLAY_FONT);
            board.forEachSquare((pos, square) -> {
                if (square.getObject() != null){
                    GameObject gameObject = square.getObject();
                    
                    Rectangle boardSquare = new Rectangle(squareSize.width*pos.col(), squareSize.height*pos.row(), squareSize.width, squareSize.height);

                    g2d.setColor(gameObject.getColorDisplay());
                    g2d.fillRect(boardSquare.x, boardSquare.y, boardSquare.width, boardSquare.height);

                    Rectangle2D r = fm.getStringBounds(gameObject.getTextDisplay(), g2d);
                    int x = (int) (boardSquare.x + boardSquare.width/2 - r.getWidth()/2);
                    int y = (int) (boardSquare.y + boardSquare.height/2 - r.getHeight()/2) + fm.getAscent();
                    g2d.setColor(Color.BLACK);
                    g2d.drawString(gameObject.getTextDisplay(), x-1, y+1);
                    g2d.drawString(gameObject.getTextDisplay(), x+1, y+1);
                    g2d.drawString(gameObject.getTextDisplay(), x-1, y-1);
                    g2d.drawString(gameObject.getTextDisplay(), x+1, y-1);
                    g2d.setColor(Color.WHITE);
                    g2d.drawString(gameObject.getTextDisplay(), x, y);

                    if (gameObject instanceof Kaiju kaiju){
                        String hp = "Health: "+kaiju.getHealth();
                        r = fm.getStringBounds(kaiju.getStatBlock().getDamage()+"", g2d);
                        x = boardSquare.x+5;
                        y = (int) (boardSquare.y + r.getHeight()/2) + fm.getAscent();
                        g2d.setColor(Color.BLACK);
                        g2d.drawString(hp, x-1, y+1);
                        g2d.drawString(hp, x+1, y+1);
                        g2d.drawString(hp, x-1, y-1);
                        g2d.drawString(hp, x+1, y-1);
                        g2d.setColor(Color.GREEN);
                        g2d.drawString(hp, x, y);
                    }
                }
            });
        }
    }
}
