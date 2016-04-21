package bayesnet_pszt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import javax.swing.JOptionPane;

public class GuiDiagram {
    // fields
    private int width;
    private int height;
    private List<GuiNode> nodes = new ArrayList<GuiNode>();
    private List<GuiConnection> connections = new ArrayList<GuiConnection>();
    private List<GuiElement> selectedElements = new LinkedList<GuiElement>();
    private DiagramToolType selectedTool = DiagramToolType.REPOSITION;
    private GuiNode connectFrom;
    private Point mousePressedPos;
    private GuiElement mousePressedElement;
    private Point mousePressedElementPos;

    // getters & setters
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public List<GuiNode> getNodes() { return nodes; }
    public List<GuiConnection> getConnections() { return connections; }
    public List<GuiElement> getSelectedElements() { return selectedElements; }
    public DiagramToolType getSelectedTool() { return selectedTool; }
    public void setSelectedTool(DiagramToolType selectedTool) { this.selectedTool = selectedTool; }

    //constructors
    public GuiDiagram(int width, int height) {
        this.width = width;
        this.height = height;
    }

    //methods
    public GuiNode createNode(String name, int x, int y) {
        GuiNode node = new GuiNode(this, name, x, y);
        nodes.add(node);
        return node;
    }

    public void removeNode(GuiNode node) {
        Iterator<GuiConnection> i = connections.iterator();
        while (i.hasNext()) {
            GuiConnection c = i.next();
            if (c.getSource() == node || c.getDestination() == node)
                i.remove();
        }
        nodes.remove(node);
    }

    public GuiConnection createConnection(GuiNode from, GuiNode to) {
        if (from == null || to == null || from == to)
            return null;

        for (GuiConnection c : from.getConnections()) {
            if (c.areNodesConnected(from, to))
                return null;
        }

        GuiConnection connection = new GuiConnection(this, from, to);
        connections.add(connection);
        from.getConnections().add(connection);
        to.getConnections().add(connection);
        return connection;
    }

    public void removeConnection(GuiConnection connection) {
        connection.getSource().getConnections().remove(connection);
        connection.getDestination().getConnections().remove(connection);
        connections.remove(connection);
    }

    public void clearAllElements() {
        selectedElements.clear();
        connectFrom = null;
        mousePressedPos = null;
        mousePressedElement = null;
        mousePressedElementPos = null;
        nodes.clear();
        connections.clear();
    }

    public void removeSelectedElements() {
        for (GuiElement e1 : selectedElements) {
            if (e1 instanceof GuiConnection) {
                removeConnection((GuiConnection)e1);
            }
        }

        for (GuiElement e2 : selectedElements) {
            if (e2 instanceof GuiNode) {
                removeNode((GuiNode)e2);
            }
        }
    }

    public GuiElement getElementAtLocation(int x, int y) {
        // backward iteration
        ListIterator<GuiConnection> itc = connections.listIterator(connections.size());
        while (itc.hasPrevious()) {
            GuiConnection c = itc.previous();
            if (c.isLocationHit(x, y))
                return c;
        }

        ListIterator<GuiNode> itn = nodes.listIterator(nodes.size());
        // backward iteration
        while (itn.hasPrevious()) {
            GuiNode n = itn.previous();
            if (n.isLocationHit(x, y))
                return n;
        }
        return null;
    }

    public void addElementSelection(GuiElement element) {
        if (element == null || isElementSelected(element))
            return;

        selectedElements.add(element);
    }

    public void removeElementSelection(GuiElement element) {
        if (element == null)
            return;

        selectedElements.remove(element);
    }

    public boolean isElementSelected(GuiElement element) {
        if (element == null)
            return false;

        for (GuiElement e : selectedElements) {
            if (e == element)
                return true;
        }
        return false;
    }

    public void handleMousePressed(MouseEvent e) {
        mousePressedPos = e.getPoint();
        mousePressedElement = getElementAtLocation(e.getX(), e.getY());

        if (mousePressedElement != null && mousePressedElement instanceof GuiNode) {
            GuiNode n = (GuiNode)mousePressedElement;
            mousePressedElementPos = new Point(n.getX(), n.getY());
        }
        else
            mousePressedElementPos = null;
    }

    public void handleMouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2)
        {
            if (mousePressedElement == null)
                return;

            JOptionPane.showMessageDialog(null, "double click");
            // TODO: open new window, when editing node data is possible
            return;
        }

        if (selectedTool != DiagramToolType.SELECT &&
            selectedTool != DiagramToolType.CONNECT &&
            selectedTool != DiagramToolType.ADDNODE)
            return;

        if (selectedTool == DiagramToolType.ADDNODE) {
            createNode("NewNode", e.getX() - GuiNode.NODE_WIDTH/2, e.getY() - 25);
            return;
        }

        if (mousePressedElement == null) {
            connectFrom = null;
            return;
        }

        if (selectedTool == DiagramToolType.SELECT ) {
            if (isElementSelected(mousePressedElement))
                removeElementSelection(mousePressedElement);
            else
                addElementSelection(mousePressedElement);
            return;
        }

        if (selectedTool == DiagramToolType.CONNECT) {
            if (!(mousePressedElement instanceof GuiNode))
                return;
            GuiNode n = (GuiNode)mousePressedElement;

            if (connectFrom == null)
                connectFrom = n;
            else {
                createConnection(connectFrom, n);
                connectFrom = null;
            }
        }
    }

    public void handleMouseDragged(MouseEvent e) {
        if (selectedTool != DiagramToolType.REPOSITION)
            return;

        if (mousePressedElement == null || mousePressedElementPos == null)
            return;

        if (!(mousePressedElement instanceof GuiNode))
            return;
        GuiNode n = (GuiNode)mousePressedElement;

        int dx = e.getX() - mousePressedPos.x;
        int dy = e.getY() - mousePressedPos.y;
        int newX = mousePressedElementPos.x + dx;
        int newY = mousePressedElementPos.y + dy;
        n.setX(newX);
        n.setY(newY);
    }

    public void paintConponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        for (GuiConnection c : connections) {
            c.paintConponent(g);
        }

        for (GuiNode n : nodes) {
            n.paintConponent(g);
        }
    }
}

