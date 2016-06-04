package bayesnet_pszt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class GuiNode implements GuiElement {
    // fields
    public static int NODE_WIDTH = 125;
    private static Color NORMAL_NODE_COLOR = Color.decode("0xDFDAD2");
    private static Color EVIDENCE_NODE_COLOR = Color.decode("0xDEBD68");

    private GuiDiagram diagram;
    private String name;
    private int x;
    private int y;
    private int height;
    private int width;
    private List<GuiNodesConnection> connections = new ArrayList<GuiNodesConnection>();

    private BayesNode mNode;
    // TODO: List of possible options for a node
    // TODO: collection containing 'combination data' entries for a node that has any parents
    // TODO: collection containing 'probalilities data' entries for every node
    //          - given by user when evidence node, or calculated by Bayes algorithm when not

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
    public void setIsEvidence(boolean value) { mNode.SetEvidence(value); }
    public boolean getIsEvidence() { return mNode.IsEvidence(); }

    // constructors
    public GuiNode(GuiDiagram diagram, String name, int x, int y, int attrCount) {
        if (diagram == null || x < 0 || y < 0 || x > diagram.getWidth() || y > diagram.getHeight() || attrCount < 0)
            throw new IllegalArgumentException();

        this.diagram = diagram;
        this.name = name;
        this.x = x;
        this.y = y;

        this.width = NODE_WIDTH;

        mNode = Bayes.getInstance().CreateNode(name, null);
        Vector<BayesAttributePair<Float>> attributes = new Vector<BayesAttributePair<Float>>();
        for (Integer i = 0; i < attrCount; i++)
        {
            attributes.add(new BayesAttributePair<Float>("Attr"+i.toString(), 0.0f));
        }
        mNode.SetAttributes(attributes);
    }

    // methods
    public void paintConponent(Graphics g) {
        this.height = 15 * (mNode.GetAttributeCount() + 1);

        Graphics2D g2 = (Graphics2D)g;
        Stroke normal = new BasicStroke(1);
        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        Font textFont = new Font("TimesRoman", Font.PLAIN, 12);

        Shape s = new RoundRectangle2D.Double(x, y, width, height, 2, 2);
        g2.setColor(getIsEvidence() ? EVIDENCE_NODE_COLOR : NORMAL_NODE_COLOR);
        g2.fill(s);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(textFont);
        g2.setStroke(normal);
        g2.setColor(Color.BLACK);

        if (mNode.IsEvidence())
            g2.drawString("[E] " + this.name, x + 2, y + 12);
        else
            g2.drawString(this.name, x + 2, y + 12);

        g2.drawLine(x, y + 14, x + NODE_WIDTH, y + 14);

        g2.setStroke(normal);
        g2.setColor(Color.BLACK);
        g2.draw(s);

        int offsetY = 28;
        if (mNode.mAttrs != null)
            for (BayesAttributePair<Float> attr : mNode.mAttrs)
            {
                g2.drawString(attr.key + ": " + attr.value.floatValue(), x + 2, y + offsetY);
                offsetY += 15;
            }

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

    public BayesNode getBayesNode() {
        return mNode;
    }

    @Override
    public boolean isLocationHit(int x, int y) {
        boolean result = ((x >= this.x && x <= this.x + this.width) &&
                          (y >= this.y && y <= this.y + this.height));
        if (result)
        {
            mNode.PrintProbsToStdout();
        }

        return result;
    }

    @Override
    public void onDestroy() {
        mNode.RemoveAllParents();
        mNode.RemoveAllChildren();
    }
}

