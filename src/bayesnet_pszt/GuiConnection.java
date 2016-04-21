package bayesnet_pszt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;

public class GuiConnection {
    // fields
    private Point sourcePoint;
    private Point destinationPoint;

    // getters & setters
    public Point getSourcePoint() { return sourcePoint; }
    public Point getDestinationPoint() { return destinationPoint; }
    public void setSourcePoint(Point value) { this.sourcePoint = value; }
    public void setDestinationPoint(Point value) { this.destinationPoint = value; }

    // constructors
    public GuiConnection(Point sourcePoint, Point destinationPoint) {
        this.sourcePoint = sourcePoint;
        this.destinationPoint = destinationPoint;
    }

    //methods
    public void paintConponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.BLACK);
        Stroke thin = new BasicStroke(1);
        g2.setStroke(thin);

        g2.drawLine(sourcePoint.x, sourcePoint.y, destinationPoint.x, destinationPoint.y);
        Shape s = new Ellipse2D.Double(destinationPoint.x - 5, destinationPoint.y - 5, 10, 10);
        g2.fill(s);
    }
}
