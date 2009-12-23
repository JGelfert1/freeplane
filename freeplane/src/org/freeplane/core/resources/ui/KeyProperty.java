/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2008 Dimitry Polivaev
 *
 *  This file author is Dimitry Polivaev
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.freeplane.core.resources.ui;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.freeplane.core.resources.FpStringUtils;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.RowSpec;

/**
 * @author Dimitry Polivaev
 * 27.12.2008
 */
public class KeyProperty extends PropertyBean implements IPropertyControl {
	private static RowSpec rowSpec;
	private Icon icon;
	private String labelText;
	JButton mButton = new JButton();
	private int modifierMask = 0;

	/**
	 */
	public KeyProperty(final String name) {
		super(name);
	}

	public void disableModifiers() {
		modifierMask = KeyEvent.ALT_MASK | KeyEvent.CTRL_MASK;
	}

	@Override
	public String getValue() {
		return mButton.getText();
	}

	public void layout(final DefaultFormBuilder builder) {
		mButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				final Dialog dialog = (Dialog) SwingUtilities.getAncestorOfClass(Dialog.class, builder.getContainer());
				final GrabKeyDialog keyDialog = new GrabKeyDialog(dialog, getValue(), modifierMask);
				keyDialog.setVisible(true);
				if (keyDialog.isOK()) {
					setValue(keyDialog.getShortcut());
					firePropertyChangeEvent();
				}
			}
		});
		if (labelText == null) {
			labelText = FpStringUtils.getOptionalText(getLabel());
		}
		final JLabel label = new JLabel(labelText, icon, JLabel.RIGHT);
		label.setToolTipText(FpStringUtils.getOptionalText(getDescription()));
		if (KeyProperty.rowSpec == null) {
			KeyProperty.rowSpec = new RowSpec("fill:20dlu");
		}
		if (3 < builder.getColumn()) {
			builder.appendRelatedComponentsGapRow();
			builder.appendRow(KeyProperty.rowSpec);
			builder.nextLine(2);
		}
		else {
			builder.nextColumn(2);
		}
		builder.add(label);
		builder.nextColumn(2);
		builder.add(mButton);
	}

	public void setEnabled(final boolean pEnabled) {
		mButton.setEnabled(pEnabled);
	}

	public void setImageIcon(final Icon icon) {
		this.icon = icon;
	}

	public void setLabelText(final String labelText) {
		this.labelText = labelText;
	}

	@Override
	public void setValue(final String value) {
		mButton.setText(value);
		mButton.setToolTipText(mButton.getText());
	}
}
