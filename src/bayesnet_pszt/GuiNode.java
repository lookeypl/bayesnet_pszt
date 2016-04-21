package bayesnet_pszt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

public class GuiNode implements GuiElement {
    // fields
    public static int NODE_WIDTH = 100;

    private GuiDiagram diagram;
    private String name;
    private int x;
    private int y;
    private int height;
    private int width;
    private List<GuiNodesConnection> connections = new ArrayList<GuiNodesConnection>();

    // getters & setters
    public GuiDiagram getDiagram() { return diagram; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public int getHeight() { return height; }
    public int getWidth() { return width; }
    public List<GuiNodesConnection> getConnections() { return connections; }

    // constructors
    public GuiNode(GuiDiagram diagram, String name, int x, int y) {
        this.diagram = diagram;
        this.name = name;
        this.x = x;
        this.y = y;

        this.width = NODE_WIDTH;
        this.height = 50; // TODO: temp only; will be calculated from the number of options
    }

    // methods
    public void paintConponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        Stroke normal = new BasicStroke(1);
        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);

        Shape s = new RoundRectangle2D.Double(x, y, width, height, 5, 5);
        g2.setColor(Color.LIGHT_GRAY);
        g2.fill(s);
        g2.setStroke(normal);
        g2.setColor(Color.BLACK);
        g2.draw(s);

        if (diagram.isElementSelected(this)) {
            g2.setStroke(dashed);
            Shape s2 = new RoundRectangle2D.Double(x - 5, y - 5, width + 10, height + 10, 5, 5);
            g2.draw(s2);
        }
    }

    public Point getConnectionSourcePoint() {
        return new Point(x + width/2, y + height);
    }

    public Point getConnectionDestinationPoint() {
        return new Point(x + width/2, y);
    }

    @Override
    public boolean isLocationHit(int x, int y) {
        return ((x >= this.x && x <= this.x + this.width) &&
                (y >= this.y && y <= this.y + this.height));
    }
}

