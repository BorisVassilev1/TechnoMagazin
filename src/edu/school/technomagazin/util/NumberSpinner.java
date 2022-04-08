package edu.school.technomagazin.util;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.NumberFormatter;

public class NumberSpinner extends JSpinner {
	private static final long serialVersionUID = 1L;

	NumberFormatter formatter;
	
	public NumberSpinner(int value, int min, int max) {
		super(new SpinnerNumberModel(value, min, max, 1));
		JSpinner.NumberEditor productIdEditor = new JSpinner.NumberEditor(this);
		setEditor(productIdEditor);
		formatter = (NumberFormatter) productIdEditor.getTextField().getFormatter();
		formatter.setAllowsInvalid(false);
		formatter.setOverwriteMode(true);
	}
}
