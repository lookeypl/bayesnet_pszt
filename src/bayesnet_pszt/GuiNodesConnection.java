package bayesnet_pszt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class GuiNodesConnection extends GuiConnection implements GuiElement {
    // fields
    private GuiDiagram diagram;
    private GuiNode source;
    private GuiNode destination;

    // getters & setters
    public GuiNode getSource() { return source; }
    public GuiNode getDestination() { return destination; }

    // constructors
    public GuiNodesConnection(GuiDiagram diagram, GuiNode source, GuiNode destination) {
        super((source != null)? source.getConnectionSourcePoint() : null,
                (destination != null)? destination.getConnectionDestinationPoint() : null);

        if (diagram == null || source == null || destination == null)
            throw new IllegalArgumentException();

        this.diagram = diagram;
        this.source = source;
        this.destination = destination;
    }

    // methods
    public void updateConnectionPoints() {
        setSourcePoint(source.getConnectionSourcePoint());
        setDestinationPoint(destination.getConnectionDestinationPoint());
    }

    public boolean areNodesConnected(GuiNode a, GuiNode b) {
        return ((source == a && destination == b) ||
                (source == b && destination == a));
    }

    @Override
    public void paintConponent(Graphics g) {
        updateConnectionPoints();
        super.paintConponent(g);

        if (!diagram.isElementSelected(this))
            return;

        // selected connection needs special painting
        Graphics2D g2 = (Graphics2D)g;
        Point sourcePoint = getSourcePoint();
        Point destinationPoint = getDestinationPoint();

        g2.setColor(Color.BLACK);
        Stroke fat = new BasicStroke(3);
        g2.setStroke(fat);
        g2.drawLine(sourcePoint.x, sourcePoint.y, destinationPoint.x, destinationPoint.y);
    }

    @Override
    public boolean isLocationHit(int x, int y) {
        Point sourcePoint = getSourcePoint();
        Point destinationPoint = getDestinationPoint();

        Rectangle2D rect = new Rectangle();
        rect.setFrameFromDiagonal(sourcePoint, destinationPoint);
        if (x + 5 < rect.getX() || x - 5 > (rect.getX() + rect.getWidth()) ||
            y + 5 < rect.getY() || y - 5 > (rect.getY() + rect.getHeight()))
            return false;

        Line2D line = new Line2D.Double(sourcePoint, destinationPoint);
        return (line.ptLineDist(x, y) <= 5);
    }

    @Override
    public void onDestroy() {
    }
}

