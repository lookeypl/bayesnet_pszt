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
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
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
        gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
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

        combinationsTable = new JTable(combinationsTableModel);
        GridBagConstraints gbc_combinationsTable = new GridBagConstraints();
        gbc_combinationsTable.gridwidth = 2;
        gbc_combinationsTable.insets = new Insets(0, 0, 5, 0);
        gbc_combinationsTable.gridheight = 8;
        gbc_combinationsTable.fill = GridBagConstraints.BOTH;
        gbc_combinationsTable.gridx = 5;
        gbc_combinationsTable.gridy = 1;
        getContentPane().add(combinationsTable, gbc_combinationsTable);

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

        JButton removeAttributeButton = new JButton("Remove selected");
        removeAttributeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeAttribute(attributesList.getSelectedIndex());
            }
        });
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

        probabilitiesTable = new JTable(probabilitiesTableModel);
        GridBagConstraints gbc_probabilitiesTable = new GridBagConstraints();
        gbc_probabilitiesTable.gridwidth = 4;
        gbc_probabilitiesTable.insets = new Insets(0, 0, 5, 5);
        gbc_probabilitiesTable.fill = GridBagConstraints.BOTH;
        gbc_probabilitiesTable.gridx = 0;
        gbc_probabilitiesTable.gridy = 8;
        getContentPane().add(probabilitiesTable, gbc_probabilitiesTable);

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

        LoadDataFromNode();
    }

    private void LoadDataFromNode()
    {
        nodeNameTextField.setText(guiNode.getName());

        attributesListModel.clear();
        probabilitiesTableModel.setColumnCount(0);
        probabilitiesTableModel.setRowCount(1);
        for (int i = 0; i < guiNode.getBayesNode().mAttrs.size(); i++)
        {
            BayesAttributePair<Float> pair = guiNode.getBayesNode().mAttrs.elementAt(i);
            addAttribute(pair.key);
            probabilitiesTableModel.setValueAt(pair.value.toString(), 0, i);
        }
        probabilitiesTableModel.setRowCount(1);
        evidenceNodeCheckbox.setSelected(guiNode.getBayesNode().IsEvidence());

        // TODO: Temp-only format of displaying combinations table, for tests.
        BayesNode bayesNode = guiNode.getBayesNode();
        //combinationsTableModel.setColumnCount(bayesNode.GetAttributeCount());
        combinationsTableModel.setRowCount(bayesNode.GetCombinationCount());
        for (int i = 0; i < bayesNode.mProbMatrix.size(); i++)
        {
            int column = i % bayesNode.GetAttributeCount();
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
                val = Float.parseFloat((String)probabilitiesTableModel.getValueAt(0, i));
            }
            catch (NumberFormatException e)
            {
                val = BayesNode.UNFILLED_FIELD;
            }
            newAttrs.add(new BayesAttributePair<Float>(attributesListModel.getElementAt(i), val));
        }
        bayesNode.SetEvidence(evidenceNodeCheckbox.isSelected(), newAttrs);

        // combinations
        Vector<Float> newProbMatrix = new Vector<Float>();
        for (int i = 0; i < combinationsTableModel.getRowCount() *
                combinationsTableModel.getColumnCount(); i++)
        {
            int column = i % combinationsTableModel.getColumnCount();
            int row = i / combinationsTableModel.getColumnCount();

            Float val;
            try
            {
                Object obj = combinationsTableModel.getValueAt(row, column);
                if (obj instanceof Float)
                    val = (Float)obj;
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

    private void addAttribute(String name)
    {
        attributesListModel.addElement(name);
        probabilitiesTableModel.addColumn(name);
        combinationsTableModel.addColumn(name);
    }

    private void removeAttribute(int index)
    {
        if (index == -1)
            return;

        attributesListModel.remove(index);
        Vector<String> pColumnIdentifiers = new Vector<String>();
        Vector<String> cColumnIdentifiers = new Vector<String>();
        for (int i = 0; i < attributesListModel.getSize(); i++)
        {
            pColumnIdentifiers.add(attributesListModel.elementAt(i));
            cColumnIdentifiers.add(attributesListModel.elementAt(i));
        }

        @SuppressWarnings("unchecked")
        Vector<Vector<Object>> pDataVector = probabilitiesTableModel.getDataVector();
        for (Vector<Object> v : pDataVector)
        {
            v.remove(index);
        }
        probabilitiesTableModel.setDataVector(pDataVector, pColumnIdentifiers);

        // TODO: TEMP-ONLY; this will change when combinations table change
        @SuppressWarnings("unchecked")
        Vector<Vector<Object>> cDataVector = combinationsTableModel.getDataVector();
        for (Vector<Object> v : cDataVector)
        {
            v.remove(index);
        }
        combinationsTableModel.setDataVector(cDataVector, cColumnIdentifiers);
    }

}
