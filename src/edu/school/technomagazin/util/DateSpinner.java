package edu.school.technomagazin.util;

import java.util.Calendar;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.text.DateFormatter;

public class DateSpinner extends JSpinner {

	private static final long serialVersionUID = 1L;

	public DateSpinner() {
		super(new SpinnerDateModel(new Date(), null, new Date(), Calendar.SECOND));
		JSpinner.DateEditor editor = new JSpinner.DateEditor(this, "dd.MM.yyyy, HH:mm:ss");
		setEditor(editor);
		DateFormatter formatter = (DateFormatter) editor.getTextField().getFormatter();
		formatter.setAllowsInvalid(false);
		formatter.setOverwriteMode(true);
	}

	public Date getDate() {
		return getModel().getDate();
	}

	@Override
	public SpinnerDateModel getModel() {
		return (SpinnerDateModel) super.getModel();
	}
	
	@Override
	public JComponent getEditor() {
		return (DateEditor) super.getEditor();
	}
}
