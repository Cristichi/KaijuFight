package es.cristichi.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;

import es.cristichi.game.GameObject;
import es.cristichi.game.obj.Board;

public class BoardComponent extends JComponent {
    public static final Font GAMEOBJ_DISPLAY_FONT = new Font("Arial", Font.BOLD, 15);
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

            g2d.setFont(GAMEOBJ_DISPLAY_FONT);
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, canvas.width, canvas.height);

            Dimension squareSize = new Dimension((int) canvas.getWidth()/board.getColumnsLength(), (int) canvas.getHeight()/board.getRowsLength());

            board.forEachSquare((pos, square) -> {
                if (square.getObject() != null){
                    GameObject gameObject = square.getObject();
                    Rectangle boardSquare = new Rectangle(squareSize.width*pos.col(), squareSize.height*pos.row(), squareSize.width, squareSize.height);

                    g2d.setFont(GAMEOBJ_DISPLAY_FONT);
                    g2d.setColor(Color.BLACK);
                    gameObject.displayInfo(g2d, boardSquare);
                }
            });
        }
    }
}
