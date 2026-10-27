package es.cristichi.kaiju.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JComponent;

import es.cristichi.kaiju.game.obj.Board;

public class BoardComponent extends JComponent {
    public static Color[] boardColors = new Color[]{
        Color.BLACK.brighter().brighter(), Color.DARK_GRAY.darker().darker()
    };

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

            //g2d.setColor(Color.BLACK);
            //g2d.fillRect(0, 0, canvas.width, canvas.height);

            Dimension squareSize = new Dimension((int) canvas.getWidth()/board.getColumnsLength(), (int) canvas.getHeight()/board.getRowsLength());

            final AtomicInteger colorI = new AtomicInteger(0);
            board.forEachSquare((pos, gameObj) -> {
                Rectangle boardSquare = new Rectangle(squareSize.width*pos.col(), squareSize.height*pos.row(), squareSize.width, squareSize.height);
                g2d.setColor(boardColors[colorI.getAndAdd(1)]);
                if (colorI.get() >= boardColors.length){
                    colorI.set(0);
                }
                g2d.fillRect(boardSquare.x, boardSquare.y, boardSquare.width, boardSquare.height);

                if (gameObj != null){
                    g2d.setFont(GAMEOBJ_DISPLAY_FONT);
                    gameObj.displayInfo(g2d, boardSquare);
                }
            });
        }
    }
}
