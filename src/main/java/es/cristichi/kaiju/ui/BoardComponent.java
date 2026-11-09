package es.cristichi.kaiju.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JComponent;

import es.cristichi.kaiju.game.obj.Board;
import es.cristichi.kaiju.game.obj.Kaiju;

public class BoardComponent extends JComponent {
    public static Color[] boardColors = new Color[]{
        Color.BLACK.brighter().brighter(), Color.DARK_GRAY.darker().darker()
    };

    public static final Font GAMEOBJ_DISPLAY_FONT = new Font("Arial", Font.BOLD, 15);

    protected KaijuGameWindow window;
    protected int padding = 0;

    public BoardComponent(KaijuGameWindow window, int padding){
        this.window = window;
        this.padding = padding;
        
        setBackground(Color.BLACK);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resized(e);
            }
        });
    }

    public BoardComponent(KaijuGameWindow window){
        this(window, 20);
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
            g2d.setColor(getBackground());
            g2d.fillRect(0,0, canvas.width, canvas.height);
            int side = Math.min(canvas.width, canvas.height)-padding*2;
            int x = 0;
            int y = 0;
            if (canvas.width > canvas.height){
                x = (canvas.width - canvas.height)/2;
            } else {
                y = (canvas.height - canvas.width)/2;
            }
            Rectangle bounds = new Rectangle(x+padding, y+padding, side, side);

            Dimension squareSize = new Dimension(
                bounds.width/board.getColumnsLength(),
                bounds.height/board.getRowsLength()
            );

            Kaiju currentTurn = window.getGame().getCurrentTurnKaiju();

            final AtomicInteger colorI = new AtomicInteger(0);
            board.forEachSquare((pos, gameObj) -> {
                g2d.setStroke(new BasicStroke(1));
                Rectangle boardSquare = new Rectangle(bounds.x+squareSize.width*pos.col(), bounds.y+squareSize.height*pos.row(), squareSize.width, squareSize.height);
                g2d.setColor(boardColors[colorI.getAndAdd(1)]);
                if (colorI.get() >= boardColors.length){
                    colorI.set(0);
                }
                g2d.fillRect(boardSquare.x, boardSquare.y, boardSquare.width, boardSquare.height);

                if (gameObj != null){
                    g2d.setFont(GAMEOBJ_DISPLAY_FONT);
                    gameObj.displayInfo(g2d, boardSquare);

                    if (gameObj instanceof Kaiju kaiju && kaiju.equals(currentTurn)){
                        g2d.setColor(kaiju.getColorDisplay());
                        g2d.setStroke(new BasicStroke(6));
                        g2d.drawRect(boardSquare.x+3, boardSquare.y+3, boardSquare.width-6, boardSquare.height-6);
                    }
                }
            });

            g2d.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawRect(bounds.x, bounds.y, bounds.width-1, bounds.height-1);
        }
    }

    private void resized(ComponentEvent e){
        int newSize = getSize().height;
        setPreferredSize(new Dimension(newSize, newSize));
    }
}
