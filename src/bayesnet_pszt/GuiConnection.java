package bayesnet_pszt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class GuiConnection implements GuiElement {
    // fields
    private GuiDiagram diagram;
    private GuiNode source;
    private GuiNode destination;

    // getters & setters
    public GuiNode getSource() { return source; }
    public GuiNode getDestination() { return destination; }

    // constructors
    public GuiConnection(GuiDiagram diagram, GuiNode source, GuiNode destination) {
        this.diagram = diagram;
        this.source = source;
        this.destination = destination;
    }

    // methods
    public boolean areNodesConnected(GuiNode a, GuiNode b) {
        return ((source == a && destination == b) ||
                (source == b && destination == a));
    }

    public void paintConponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        Point sourcePoint = source.getConnectionSourcePoint();
        Point destPoint = destination.getConnectionDestinationPoint();

        g2.setColor(Color.BLACK);
        Stroke fat = new BasicStroke(3);
        Stroke thin = new BasicStroke(1);
        g2.setStroke(diagram.isElementSelected(this)? fat : thin);

        g2.drawLine(sourcePoint.x, sourcePoint.y, destPoint.x, destPoint.y);
        Shape s = new Ellipse2D.Double(destPoint.x - 5, destPoint.y - 5, 10, 10);
        g2.fill(s);
    }

    @Override
    public boolean isLocationHit(int x, int y) {
        Point sourcePoint = source.getConnectionSourcePoint();
        Point destPoint = destination.getConnectionDestinationPoint();

        Rectangle2D rect = new Rectangle();
        rect.setFrameFromDiagonal(sourcePoint, destPoint);
        if (x + 5 < rect.getX() || x - 5 > (rect.getX() + rect.getWidth()) ||
            y + 5 < rect.getY() || y - 5 > (rect.getY() + rect.getHeight()))
            return false;

        Line2D line = new Line2D.Double(sourcePoint, destPoint);
        return (line.ptLineDist(x, y) <= 5);
    }
}

