package com.dji.comm.og.service.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class WaitDialog {

	private JDialog dialog;

	public void makeWait(String msg, ActionEvent evt) {

		Window win = SwingUtilities.getWindowAncestor((AbstractButton) evt.getSource());
		dialog = new JDialog(win, msg, Dialog.ModalityType.APPLICATION_MODAL);

		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(progressBar, BorderLayout.CENTER);
		panel.add(new JLabel("Please wait......."), BorderLayout.PAGE_START);
		dialog.add(panel);
		dialog.pack();
		dialog.setLocationRelativeTo(win);
		dialog.setVisible(true);
	}

	public void close() {
		dialog.dispose();
	}
}
