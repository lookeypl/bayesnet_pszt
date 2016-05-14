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
    private List<GuiNodesConnection> connections = new ArrayList<GuiNodesConnection>();
    private List<GuiElement> selectedElements = new LinkedList<GuiElement>();
    private DiagramToolType selectedTool = DiagramToolType.SELECT;
    private GuiNode connectFrom;
    private Point mousePressedPos;
    private GuiElement mousePressedElement;
    private Point mousePressedElementPos;
    private GuiConnection connectionInProgressSemiConnection;

    // getters & setters
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public List<GuiNode> getNodes() { return nodes; }
    public List<GuiNodesConnection> getConnections() { return connections; }
    public List<GuiElement> getSelectedElements() { return selectedElements; }
    public DiagramToolType getSelectedTool() { return selectedTool; }

    //constructors
    public GuiDiagram(int width, int height) {
        this.width = width;
        this.height = height;
    }

    //methods
    private void setConnectFrom(GuiNode value) {
        GuiNode oldValue = connectFrom;
        connectFrom = value;

        if (value == null) {
            connectionInProgressSemiConnection = null;
            return;
        }

        if (connectionInProgressSemiConnection == null || oldValue != value)
            connectionInProgressSemiConnection = new GuiConnection(
                    value.getConnectionSourcePoint(),
                    value.getConnectionSourcePoint()); // forst mouse move will change this
    }

    public void setSelectedTool(DiagramToolType selectedTool) {
        this.selectedTool = selectedTool;
        setConnectFrom(null);
    }

    public GuiNode createNode(String name, int x, int y) {
        GuiNode node = new GuiNode(this, name, x, y);
        nodes.add(node);
        return node;
    }

    public void removeNode(GuiNode node) {
        Iterator<GuiNodesConnection> i = connections.iterator();
        while (i.hasNext()) {
            GuiNodesConnection c = i.next();
            if (c.getSource() == node || c.getDestination() == node)
                i.remove();
        }
        nodes.remove(node);
    }

    public GuiNodesConnection createConnection(GuiNode from, GuiNode to) {
        if (from == null || to == null || from == to)
            return null;

        for (GuiNodesConnection c : from.getConnections()) {
            if (c.areNodesConnected(from, to))
                return null;
        }

        GuiNodesConnection connection = new GuiNodesConnection(this, from, to);
        connections.add(connection);
        from.getConnections().add(connection);
        to.getConnections().add(connection);
        from.getBayesNode().AddChild(to.getBayesNode());
        return connection;
    }

    public void removeConnection(GuiNodesConnection connection) {
        connection.getSource().getBayesNode().RemoveChild(connection.getDestination().getBayesNode());
        connection.getSource().getConnections().remove(connection);
        connection.getDestination().getConnections().remove(connection);
        connections.remove(connection);
    }

    public void clearAllElements() {
        selectedElements.clear();
        setConnectFrom(null);
        mousePressedPos = null;
        mousePressedElement = null;
        mousePressedElementPos = null;
        nodes.clear();
        connections.clear();
    }

    public void removeSelectedElements() {
        for (GuiElement e1 : selectedElements) {
            if (e1 instanceof GuiNodesConnection) {
                removeConnection((GuiNodesConnection)e1);
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
        ListIterator<GuiNodesConnection> itc = connections.listIterator(connections.size());
        while (itc.hasPrevious()) {
            GuiNodesConnection c = itc.previous();
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

        if (selectedTool == DiagramToolType.ADDNODE) {
            createNode("NewNode", e.getX() - GuiNode.NODE_WIDTH/2, e.getY() - 25);
            return;
        }

        if (mousePressedElement == null) {
            setConnectFrom(null);
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
                setConnectFrom(n);
            else {
                createConnection(connectFrom, n);
                setConnectFrom(null);
            }
            return;
        }
    }

    public void handleMouseDragged(MouseEvent e) {
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

        GuiConnection semiConnection = connectionInProgressSemiConnection;
        if (semiConnection == null)
            return;

        semiConnection.getSourcePoint().setLocation(connectFrom.getConnectionSourcePoint());
        semiConnection.getDestinationPoint().setLocation(e.getPoint());
    }

    public void handleMouseMoved(MouseEvent e) {
        if (selectedTool != DiagramToolType.CONNECT)
            return;

        GuiConnection semiConnection = connectionInProgressSemiConnection;
        if (semiConnection == null)
            return;

        semiConnection.getDestinationPoint().setLocation(e.getPoint());
    }

    public void paintConponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        if (connectionInProgressSemiConnection != null)
            connectionInProgressSemiConnection.paintConponent(g);

        for (GuiNodesConnection c : connections) {
            c.paintConponent(g);
        }

        for (GuiNode n : nodes) {
            n.paintConponent(g);
        }
    }
}

