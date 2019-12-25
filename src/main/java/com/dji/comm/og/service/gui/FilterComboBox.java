package com.dji.comm.og.service.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.dji.comm.og.service.utils.CommPortLister;

/**
 * A class for filtered combo box.
 */
public class FilterComboBox
    extends JComboBox
{
    /**
     * Entries to the combobox list.
     */
    private List<String> entries;
    private JTextField textfield;

    public String getEnteredText() {
    	return this.textfield.getText();
    }
    
    public List<String> getEntries()
    {
    	return entries;
        //this.setModel(new DefaultComboBoxModel<String>(entries.toArray(new String[0])));
		
    }

    public void setEntries(List<String> entries) {
		this.entries = entries;
		super.setModel(new DefaultComboBoxModel<String>(entries.toArray(new String[0])));
        this.textfield.setText("");
        this.hidePopup();
	}

	public FilterComboBox(List<String> entries)
    {
        super(entries.toArray());
        this.entries = entries  ;
        this.setEditable(true);

        this.textfield =
            (JTextField) this.getEditor().getEditorComponent();

        /**
         * Listen for key presses.
         */
        textfield.addKeyListener(new KeyAdapter()
        {
            public void keyReleased(KeyEvent ke)
            {
                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        /**
                         * On key press filter the list.
                         * If there is "text" entered in text field of this combo use that "text" for filtering.
                         */
                        comboFilter(textfield.getText());
                    }
                });
            }
        });

    }

    /**
     * Build a list of entries that match the given filter.
     */
    public void comboFilter(String enteredText)
    {
        List<String> entriesFiltered = new ArrayList<String>();

        for (String entry : getEntries())
        {
            if (entry.toLowerCase().contains(enteredText.toLowerCase()))
            {
                entriesFiltered.add(entry);
            }
        }

        if (entriesFiltered.size() > 0)
        {
            this.setModel(
                    new DefaultComboBoxModel(
                        entriesFiltered.toArray()));
            this.setSelectedItem(enteredText);
            this.showPopup();
        }
        else
        {
            this.hidePopup();
        }
    }
}