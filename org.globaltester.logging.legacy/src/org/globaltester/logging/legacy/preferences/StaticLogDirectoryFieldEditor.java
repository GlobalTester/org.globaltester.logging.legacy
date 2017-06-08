package org.globaltester.logging.legacy.preferences;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.widgets.Composite;

/**
 * Specific {@link DirectoryFieldEditor} implementation that performs checking
 * on every keystroke, while keeping the parents error message mostly unchanged.
 * 
 * @author amay
 *
 */
public class StaticLogDirectoryFieldEditor extends DirectoryFieldEditor {

	public StaticLogDirectoryFieldEditor(String name, String labelText, Composite parent) {
		init(name, labelText);
		setErrorMessage(JFaceResources.getString("DirectoryFieldEditor.errorMessage"));//$NON-NLS-1$
		setChangeButtonText(JFaceResources.getString("openBrowse"));//$NON-NLS-1$
		setValidateStrategy(VALIDATE_ON_KEY_STROKE);
		createControl(parent);
	}

	@Override
	protected void clearErrorMessage() {
		// empty implementation to prevent super implementation from changing
		// page error message
	}

}
