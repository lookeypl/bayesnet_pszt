package bayesnet_pszt;

import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class EditNodeDialog extends JDialog {
    private static final long serialVersionUID = -5363275156746295033L;
    private JTextField nodeNameTextField;
    private JTextField newAttributeNameTextField;
    private JTable probabilitiesTable;
    private JTable combinationsTable;
    private JList<String> attributesList;
    JCheckBox evidenceNodeCheckbox;

    private GuiNode guiNode;
    private DefaultListModel<String> attributesListModel = new DefaultListModel<String>();
    private DefaultTableModel probabilitiesTableModel = new DefaultTableModel();
    private DefaultTableModel combinationsTableModel = new DefaultTableModel();

    /**
     * Create the dialog.
     */
    public EditNodeDialog(Window owner, Dialog.ModalityType modalityType, GuiNode node) {
        super(owner, modalityType);

        if (node == null)
            throw new IllegalArgumentException();
        guiNode = node;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 699, 500);
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 197, 0};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0};
        gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
        getContentPane().setLayout(gridBagLayout);

        JLabel lblNewLabel = new JLabel("Node name:");
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel.gridwidth = 4;
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 0;
        getContentPane().add(lblNewLabel, gbc_lblNewLabel);

        JSeparator separator = new JSeparator();
        GridBagConstraints gbc_separator = new GridBagConstraints();
        gbc_separator.gridheight = 9;
        gbc_separator.insets = new Insets(0, 0, 5, 5);
        gbc_separator.gridx = 4;
        gbc_separator.gridy = 0;
        getContentPane().add(separator, gbc_separator);

        JLabel lblNewLabel_4 = new JLabel("Combinations:");
        GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
        gbc_lblNewLabel_4.gridwidth = 2;
        gbc_lblNewLabel_4.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 0);
        gbc_lblNewLabel_4.gridx = 5;
        gbc_lblNewLabel_4.gridy = 0;
        getContentPane().add(lblNewLabel_4, gbc_lblNewLabel_4);

        nodeNameTextField = new JTextField();
        GridBagConstraints gbc_nodeNameTextField = new GridBagConstraints();
        gbc_nodeNameTextField.gridwidth = 4;
        gbc_nodeNameTextField.insets = new Insets(0, 0, 5, 5);
        gbc_nodeNameTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_nodeNameTextField.gridx = 0;
        gbc_nodeNameTextField.gridy = 1;
        getContentPane().add(nodeNameTextField, gbc_nodeNameTextField);
        nodeNameTextField.setColumns(10);

        JScrollPane scrollPaneComb = new JScrollPane();
        scrollPaneComb.setViewportBorder(new EmptyBorder(2, 2, 2, 2));
        scrollPaneComb.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneComb.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        GridBagConstraints gbc_scrollPaneComb = new GridBagConstraints();
        gbc_scrollPaneComb.gridwidth = 2;
        gbc_scrollPaneComb.gridheight = 8;
        gbc_scrollPaneComb.insets = new Insets(0, 0, 5, 0);
        gbc_scrollPaneComb.fill = GridBagConstraints.BOTH;
        gbc_scrollPaneComb.gridx = 5;
        gbc_scrollPaneComb.gridy = 1;
        getContentPane().add(scrollPaneComb, gbc_scrollPaneComb);

        combinationsTable = new JTable(combinationsTableModel);
        scrollPaneComb.setViewportView(combinationsTable);

        JLabel lblNewLabel_1 = new JLabel("Attributes:");
        GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
        gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel_1.gridwidth = 4;
        gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_1.gridx = 0;
        gbc_lblNewLabel_1.gridy = 3;
        getContentPane().add(lblNewLabel_1, gbc_lblNewLabel_1);

        attributesList = new JList<String>(attributesListModel);
        GridBagConstraints gbc_attributesList = new GridBagConstraints();
        gbc_attributesList.gridwidth = 4;
        gbc_attributesList.insets = new Insets(0, 0, 5, 5);
        gbc_attributesList.fill = GridBagConstraints.BOTH;
        gbc_attributesList.gridx = 0;
        gbc_attributesList.gridy = 4;
        getContentPane().add(attributesList, gbc_attributesList);

        newAttributeNameTextField = new JTextField();
        GridBagConstraints gbc_newAttributeNameTextField = new GridBagConstraints();
        gbc_newAttributeNameTextField.insets = new Insets(0, 0, 5, 5);
        gbc_newAttributeNameTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_newAttributeNameTextField.gridx = 0;
        gbc_newAttributeNameTextField.gridy = 5;
        getContentPane().add(newAttributeNameTextField, gbc_newAttributeNameTextField);
        newAttributeNameTextField.setColumns(10);

        JButton addAttributeButton = new JButton("Add");
        addAttributeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAttribute(newAttributeNameTextField.getText());
            }
        });
        GridBagConstraints gbc_addAttributeButton = new GridBagConstraints();
        gbc_addAttributeButton.insets = new Insets(0, 0, 5, 5);
        gbc_addAttributeButton.gridx = 1;
        gbc_addAttributeButton.gridy = 5;
        getContentPane().add(addAttributeButton, gbc_addAttributeButton);

        JButton removeAttributeButton = new JButton("Delete");
        removeAttributeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeAttribute(attributesList.getSelectedIndex());
            }
        });

        JButton btnRename = new JButton("Rename");
        btnRename.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renameAttribute(attributesList.getSelectedIndex(),
                        newAttributeNameTextField.getText());
            }
        });
        GridBagConstraints gbc_btnRename = new GridBagConstraints();
        gbc_btnRename.insets = new Insets(0, 0, 5, 5);
        gbc_btnRename.gridx = 2;
        gbc_btnRename.gridy = 5;
        getContentPane().add(btnRename, gbc_btnRename);
        GridBagConstraints gbc_removeAttributeButton = new GridBagConstraints();
        gbc_removeAttributeButton.insets = new Insets(0, 0, 5, 5);
        gbc_removeAttributeButton.gridx = 3;
        gbc_removeAttributeButton.gridy = 5;
        getContentPane().add(removeAttributeButton, gbc_removeAttributeButton);

        JLabel lblNewLabel_2 = new JLabel("Probabilities:");
        GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
        gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel_2.gridwidth = 2;
        gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_2.gridx = 0;
        gbc_lblNewLabel_2.gridy = 7;
        getContentPane().add(lblNewLabel_2, gbc_lblNewLabel_2);

        evidenceNodeCheckbox = new JCheckBox("Evidence node");
        GridBagConstraints gbc_evidenceNodeCheckbox = new GridBagConstraints();
        gbc_evidenceNodeCheckbox.anchor = GridBagConstraints.EAST;
        gbc_evidenceNodeCheckbox.gridwidth = 2;
        gbc_evidenceNodeCheckbox.insets = new Insets(0, 0, 5, 5);
        gbc_evidenceNodeCheckbox.gridx = 2;
        gbc_evidenceNodeCheckbox.gridy = 7;
        getContentPane().add(evidenceNodeCheckbox, gbc_evidenceNodeCheckbox);
        // TODO reenable when evidence nodes are implemented
        evidenceNodeCheckbox.setEnabled(false);

        JScrollPane scrollPaneProb = new JScrollPane();
        scrollPaneProb.setViewportBorder(new EmptyBorder(2, 2, 2, 2));
        scrollPaneProb.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneProb.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        GridBagConstraints gbc_scrollPaneProb = new GridBagConstraints();
        gbc_scrollPaneProb.gridwidth = 4;
        gbc_scrollPaneProb.insets = new Insets(0, 0, 5, 5);
        gbc_scrollPaneProb.fill = GridBagConstraints.BOTH;
        gbc_scrollPaneProb.gridx = 0;
        gbc_scrollPaneProb.gridy = 8;
        getContentPane().add(scrollPaneProb, gbc_scrollPaneProb);

        probabilitiesTable = new JTable(probabilitiesTableModel);
        scrollPaneProb.setViewportView(probabilitiesTable);

        JSeparator separator_1 = new JSeparator();
        GridBagConstraints gbc_separator_1 = new GridBagConstraints();
        gbc_separator_1.insets = new Insets(0, 0, 5, 0);
        gbc_separator_1.gridwidth = 7;
        gbc_separator_1.gridx = 0;
        gbc_separator_1.gridy = 9;
        getContentPane().add(separator_1, gbc_separator_1);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        });
        GridBagConstraints gbc_cancelButton = new GridBagConstraints();
        gbc_cancelButton.fill = GridBagConstraints.HORIZONTAL;
        gbc_cancelButton.insets = new Insets(0, 0, 0, 5);
        gbc_cancelButton.gridx = 5;
        gbc_cancelButton.gridy = 10;
        getContentPane().add(cancelButton, gbc_cancelButton);

        JButton saveButton = new JButton("Save changes");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SaveDataToNode();
                setVisible(false);
                dispose();
            }
        });
        GridBagConstraints gbc_saveButton = new GridBagConstraints();
        gbc_saveButton.fill = GridBagConstraints.HORIZONTAL;
        gbc_saveButton.gridx = 6;
        gbc_saveButton.gridy = 10;
        getContentPane().add(saveButton, gbc_saveButton);

        JButton fillBGButton = new JButton("FillBGMatrix");
        fillBGButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((combinationsTable.getColumnCount() == 5) &&
                    (combinationsTable.getRowCount() == 9)) {
                    combinationsTable.setValueAt(1.0f, 0, 1);
                    combinationsTable.setValueAt(0.0f, 0, 2);
                    combinationsTable.setValueAt(0.0f, 0, 3);
                    combinationsTable.setValueAt(1.0f, 1, 3);
                    combinationsTable.setValueAt(1.0f, 2, 1);
                    combinationsTable.setValueAt(1.0f, 3, 3);
                    combinationsTable.setValueAt(1.0f, 4, 2);
                    combinationsTable.setValueAt(1.0f, 5, 2);
                    combinationsTable.setValueAt(1.0f, 6, 1);
                    combinationsTable.setValueAt(1.0f, 7, 2);
                    combinationsTable.setValueAt(1.0f, 8, 4);
                } else {
                    System.out.println("ERROR: Cannot fill - Node is probably not a BG node!");
                }
            }
        });
        GridBagConstraints gbc_FillBGButton = new GridBagConstraints();
        gbc_FillBGButton.fill = GridBagConstraints.HORIZONTAL;
        gbc_FillBGButton.gridx = 4;
        gbc_FillBGButton.gridy = 10;
        getContentPane().add(fillBGButton, gbc_FillBGButton);

        JButton fillAllelButton = new JButton("FillAllelMatrix");
        fillAllelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (combinationsTable.getColumnCount() != 4) {
                    System.out.println("ERROR: Cannot fill - Node is probably not an Allel node!");
                    return;
                }

                if (combinationsTable.getRowCount() == 1) {
                    combinationsTable.setValueAt(0.28, 0, 1);
                    combinationsTable.setValueAt(0.06, 0, 2);
                    combinationsTable.setValueAt(0.66, 0, 3);
                } else if (combinationsTable.getRowCount() == 9) {
                    combinationsTable.setValueAt(1.0f, 0, 1);
                    combinationsTable.setValueAt(0.0f, 0, 2);
                    combinationsTable.setValueAt(0.0f, 0, 3);
                    combinationsTable.setValueAt(0.5f, 1, 1);
                    combinationsTable.setValueAt(0.5f, 1, 2);
                    combinationsTable.setValueAt(0.5f, 2, 1);
                    combinationsTable.setValueAt(0.5f, 2, 3);

                    combinationsTable.setValueAt(0.5f, 3, 1);
                    combinationsTable.setValueAt(0.5f, 3, 2);
                    combinationsTable.setValueAt(1.0f, 4, 2);
                    combinationsTable.setValueAt(0.5f, 5, 2);
                    combinationsTable.setValueAt(0.5f, 5, 3);

                    combinationsTable.setValueAt(0.5f, 6, 1);
                    combinationsTable.setValueAt(0.5f, 6, 3);
                    combinationsTable.setValueAt(0.5f, 7, 2);
                    combinationsTable.setValueAt(0.5f, 7, 3);
                    combinationsTable.setValueAt(1.0f, 8, 3);
                } else {
                    System.out.println("ERROR: Cannot fill - Node is probably not an Allel node!");
                }
            }
        });
        GridBagConstraints gbc_FillAllelButton = new GridBagConstraints();
        gbc_FillAllelButton.fill = GridBagConstraints.HORIZONTAL;
        gbc_FillAllelButton.gridx = 3;
        gbc_FillAllelButton.gridy = 10;
        getContentPane().add(fillAllelButton, gbc_FillAllelButton);

        LoadDataFromNode();
    }

    /** Fills first column of combinationsTableModel with cartesian product
     * of all names of atributes of all parents of selected node.
     */
    private void FillCombinationsColumn()
    {
        BayesNode bayesNode = guiNode.getBayesNode();
        int parentCount = bayesNode.mParents.size();

        if (parentCount == 0)
            return;

        int[] parentAttrCounts = new int[parentCount];
        for (int i = 0; i < parentCount; i++)
        {
            parentAttrCounts[i] = bayesNode.mParents.elementAt(i).mAttrs.size();
        }

        int[] divs = new int[parentCount];
        for (int i = 0; i < parentCount ; i++)
        {
            divs[i] = 1;
            for (int j = i + 1; j < parentCount; j++)
            {
                divs[i] *= parentAttrCounts[j];
            }
        }

        int[] indexes = new int[parentCount];
        for (int i = 0; i < bayesNode.GetCombinationCount(); i++)
        {
            String line = "";
            for (int j = 0; j < parentCount; j++)
            {
                if (i != 0 && i % divs[j] == 0)
                {
                    indexes[j]++;
                    if (indexes[j] >= parentAttrCounts[j])
                        indexes[j] = 0;
                }
                line += bayesNode.mParents.elementAt(j).mAttrs.elementAt(indexes[j]).key;
                if (j < parentCount - 1)
                    line += ", ";
            }
            combinationsTableModel.setValueAt(line, i, 0);
        }
    }

    private void LoadDataFromNode()
    {
        BayesNode bayesNode = guiNode.getBayesNode();

        nodeNameTextField.setText(guiNode.getName());
        attributesListModel.clear();
        evidenceNodeCheckbox.setSelected(guiNode.getBayesNode().IsEvidence());

        probabilitiesTableModel.setRowCount(1);
        probabilitiesTableModel.setColumnCount(0);
        combinationsTableModel.setColumnCount(0);
        combinationsTableModel.addColumn("Combinations:");
        for (int i = 0; i < bayesNode.mAttrs.size(); i++)
        {
            BayesAttributePair<Float> pair = bayesNode.mAttrs.elementAt(i);
            addAttribute(pair.key);
            probabilitiesTableModel.setValueAt(pair.value.toString(), 0, i);
        }

        combinationsTableModel.setRowCount(bayesNode.GetCombinationCount());
        FillCombinationsColumn();
        for (int i = 0; i < bayesNode.mProbMatrix.size(); i++)
        {
            int column = i % bayesNode.GetAttributeCount() + 1;
            int row = i / bayesNode.GetAttributeCount();
            combinationsTableModel.setValueAt(bayesNode.mProbMatrix.get(i), row, column);
        }
    }

    private void SaveDataToNode()
    {
        BayesNode bayesNode = guiNode.getBayesNode();

        guiNode.setName(nodeNameTextField.getText());

        // attributes & probabilities
        Vector<BayesAttributePair<Float>> newAttrs = new Vector<BayesAttributePair<Float>>();
        for (int i = 0; i < attributesListModel.size(); i++)
        {
            Float val;
            try
            {
                String s = (String)probabilitiesTableModel.getValueAt(0, i);
                val = (s != null)? Float.parseFloat(s) : BayesNode.UNFILLED_FIELD;
            }
            catch (NumberFormatException e)
            {
                val = BayesNode.UNFILLED_FIELD;
            }
            newAttrs.add(new BayesAttributePair<Float>(attributesListModel.getElementAt(i), val));
        }
        bayesNode.SetAttributes(newAttrs);
        bayesNode.SetEvidence(evidenceNodeCheckbox.isSelected());

        // set our combinations ONLY if we have a non-evidence node
        if (!evidenceNodeCheckbox.isSelected())
        {
            Vector<Float> newProbMatrix = new Vector<Float>();
            for (int i = 0; i < combinationsTableModel.getRowCount() *
                    (combinationsTableModel.getColumnCount() - 1); i++)
            {
                int column = i % (combinationsTableModel.getColumnCount() - 1) + 1;
                int row = i / (combinationsTableModel.getColumnCount() - 1);

                Float val;
                try
                {
                    Object obj = combinationsTableModel.getValueAt(row, column);
                    if (obj instanceof Float)
                        val = (Float)obj;
                    else if (obj instanceof Double)
                        val = ((Double)obj).floatValue();
                    else if (obj instanceof String)
                        val = Float.parseFloat((String)obj);
                    else
                        val = BayesNode.UNFILLED_FIELD;;
                }
                catch (NumberFormatException e)
                {
                    val = BayesNode.UNFILLED_FIELD;;
                }
                newProbMatrix.add(val);
            }
            bayesNode.SetProbability(newProbMatrix);
        }
    }

    private void addAttribute(String name)
    {
        attributesListModel.addElement(name);
        probabilitiesTableModel.addColumn(name);
        combinationsTableModel.addColumn(name);
    }

    private Vector<String>[] generateTablesHeaders()
    {
        @SuppressWarnings("unchecked")
        Vector<String>[] result = new Vector[2];

        Vector<String> columnIdentifiersProb = new Vector<String>();
        Vector<String> columnIdentifiersComb = new Vector<String>();
        columnIdentifiersComb.add("Combinations:");
        for (int i = 0; i < attributesListModel.size(); i++)
        {
            columnIdentifiersProb.add(attributesListModel.getElementAt(i));
            columnIdentifiersComb.add(attributesListModel.getElementAt(i));
        }
        result[0] = columnIdentifiersProb;
        result[1] = columnIdentifiersComb;
        return result;
    }

    private void renameAttribute(int index, String newName)
    {
        attributesListModel.setElementAt(newName, index);

        Vector<String>[] headers = generateTablesHeaders();
        probabilitiesTableModel.setColumnIdentifiers(headers[0]);
        combinationsTableModel.setColumnIdentifiers(headers[1]);
    }

    private void removeAttribute(int index)
    {
        if (index == -1)
            return;

        attributesListModel.remove(index);
        Vector<String>[] headers = generateTablesHeaders();

        @SuppressWarnings("unchecked")
        Vector<Vector<Object>> pDataVector = probabilitiesTableModel.getDataVector();
        for (Vector<Object> v : pDataVector)
        {
            v.remove(index);
        }
        probabilitiesTableModel.setDataVector(pDataVector, headers[0]);

        @SuppressWarnings("unchecked")
        Vector<Vector<Object>> cDataVector = combinationsTableModel.getDataVector();
        for (Vector<Object> v : cDataVector)
        {
            v.remove(index + 1);
        }
        combinationsTableModel.setDataVector(cDataVector, headers[1]);
    }

}
