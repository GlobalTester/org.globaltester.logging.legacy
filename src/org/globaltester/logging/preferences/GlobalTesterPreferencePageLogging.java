package org.globaltester.logging.preferences;

/*
 * Project GlobalTester-Plugin File GTPreferencesPageLogging.java
 * 
 * Date 03.07.2006
 * 
 * 
 * Developed by HJP Consulting GmbH Lanfert 24 33106 Paderborn Germany
 * 
 * 
 * This software is the confidential and proprietary information of HJP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * Non-Disclosure Agreement you entered into with HJP.
 */

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.ScaleFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.globaltester.logger.GTLogger;
import org.globaltester.logging.Activator;

/**
 * This class stores the properties of the plugin
 * 
 * @version Release 2.2.0
 * @author Holger Funke
 * 
 */

public class GlobalTesterPreferencePageLogging extends
		FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	//editors for directory options
	Group directoryGroup;
	BooleanFieldEditor bfeManualSettings;
	DirectoryFieldEditor dfeFrameworkLoggingDir;
	DirectoryFieldEditor dfeTestLoggingDir;

	//editors for framework logging options
	Group frameworkOptionsGroup;
	BooleanFieldEditor bfeFrameworkHtmlLogger;
	BooleanFieldEditor bfeFrameworkPlainLogger;
	BooleanFieldEditor bfeFrameworkISO8601Logging;
	BooleanFieldEditor bfeFrameworkConsoleLogger;
	ScaleFieldEditor sfeFrameworkLogLevel;
	Label lblFrameworkMinLevel;
	Label lblFrameworkMaxLevel;

	//editors for test logging options
	Group testOptionsGroup;
	BooleanFieldEditor bfeTestSameOptions;
	BooleanFieldEditor bfeTestHtmlLogger;
	BooleanFieldEditor bfeTestPlainLogger;
	BooleanFieldEditor bfeTestISO8601Logging;
	ScaleFieldEditor sfeTestLogLevel;
	Label lblTestMinLevel;
	Label lblTestMaxLevel;

	//variables needed for enabling/disabling of FieldEditors
	private boolean manualDirSetting; // whether logging directories are manually selected
	private boolean sameOptions; // whether test logger uses same options as framework logger

	public GlobalTesterPreferencePageLogging() {
		super(GRID);
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		setPreferenceStore(store);
		setDescription("GlobalTester preference page");

		manualDirSetting = store
				.getBoolean(PreferenceConstants.P_MANUALDIRSETTINGS);
		sameOptions = store.getBoolean(PreferenceConstants.P_TEST_SAME_OPTIONS);
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {

		Composite container = new Composite(this.getFieldEditorParent(),
				SWT.NONE);
		GridData containerData = new GridData(GridData.FILL, GridData.FILL,
				true, false);
		containerData.horizontalSpan = 1;

		container.setLayoutData(containerData);
		GridLayout layout = new GridLayout(1, false);
		container.setLayout(layout);

		//settings for log directories
		directoryGroup = new Group(container, SWT.NONE);
		directoryGroup.setText("Directories and files");
		GridData gd = new GridData(GridData.FILL, GridData.FILL, true, false);
		gd.horizontalSpan = 2;
		directoryGroup.setLayoutData(gd);
		directoryGroup.setLayout(new GridLayout(2, false));

		bfeManualSettings = new BooleanFieldEditor(
				PreferenceConstants.P_MANUALDIRSETTINGS,
				"Manual setting of directories and files", directoryGroup);
		addField(bfeManualSettings);

		dfeFrameworkLoggingDir = new DirectoryFieldEditor(
				PreferenceConstants.P_GT_LOGGINGDIR,
				"&Framework logging directory:", directoryGroup);
		dfeTestLoggingDir = new DirectoryFieldEditor(
				PreferenceConstants.P_TEST_LOGGINGDIR,
				"&Test logging directory:", directoryGroup);

		dfeFrameworkLoggingDir.setEnabled(manualDirSetting, directoryGroup);
		dfeTestLoggingDir.setEnabled((manualDirSetting && !sameOptions),
				directoryGroup);

		addField(dfeFrameworkLoggingDir);
		addField(dfeTestLoggingDir);

		//preferences for logging of framework (independent from test runs)
		frameworkOptionsGroup = new Group(container, SWT.NONE);
		frameworkOptionsGroup.setText("Logging of GlobalTester Framework");
		GridData gd2 = new GridData(GridData.FILL, GridData.FILL, true, false);
		gd2.horizontalSpan = 2;
		frameworkOptionsGroup.setLayoutData(gd2);
		frameworkOptionsGroup.setLayout(new GridLayout(15, false));

		bfeFrameworkHtmlLogger = new BooleanFieldEditor(
				PreferenceConstants.P_GT_HTMLLOGGING,
				"Activate additional HTML log file", frameworkOptionsGroup);
		addField(bfeFrameworkHtmlLogger);

		bfeFrameworkPlainLogger = new BooleanFieldEditor(
				PreferenceConstants.P_GT_PLAINLOGGING,
				"Use standard logging (plain text file)", frameworkOptionsGroup);
		addField(bfeFrameworkPlainLogger);

		bfeFrameworkISO8601Logging = new BooleanFieldEditor(
				PreferenceConstants.P_GT_USEISO8601LOGGING,
				"Use ISO 8601 logging in text file", frameworkOptionsGroup);
		addField(bfeFrameworkISO8601Logging);

		bfeFrameworkConsoleLogger = new BooleanFieldEditor(
				PreferenceConstants.P_GT_CONSOLELOGGING,
				"Activate additional logging to STDOUT", frameworkOptionsGroup);
		addField(bfeFrameworkConsoleLogger);

		sfeFrameworkLogLevel = new ScaleFieldEditor(
				PreferenceConstants.P_GT_LOGLEVEL, "Level of logging",
				frameworkOptionsGroup, PreferenceConstants.LOGLEVEL_TRACE,
				PreferenceConstants.LOGLEVEL_FATAL, 1, 1);
		addField(sfeFrameworkLogLevel);

		//create caption for the loglevel scale field
		Composite labelComposite1 = new Composite(frameworkOptionsGroup,
				SWT.NONE);
		labelComposite1.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData labelData1 = new GridData(GridData.FILL, GridData.FILL, true,
				false);
		labelData1.horizontalSpan = 2;
		labelData1.grabExcessHorizontalSpace = true;
		labelComposite1.setLayoutData(labelData1);
		//add each label followed by an empty spacer label
		for (int i = 0; i < PreferenceConstants.LOGLEVELS.length; i++) {
			Label lbl = new Label(labelComposite1, SWT.CENTER);
			lbl.setText(PreferenceConstants.LOGLEVELS[i]);
			if (i + 1 < PreferenceConstants.LOGLEVELS.length) {
				new Label(labelComposite1, SWT.CENTER);
			}
		}

		lblFrameworkMinLevel = new Label(frameworkOptionsGroup, SWT.LEFT);
		lblFrameworkMinLevel.setText("everything is logged");
		lblFrameworkMinLevel.setLayoutData(new GridData(GridData.FILL,
				GridData.FILL, true, false));

		lblFrameworkMaxLevel = new Label(frameworkOptionsGroup, SWT.RIGHT);
		lblFrameworkMaxLevel.setText("only fatal problems are logged");
		lblFrameworkMaxLevel.setLayoutData(new GridData(GridData.END,
				GridData.FILL, true, false));

		//preferences for logging of tests
		testOptionsGroup = new Group(container, SWT.NONE);
		testOptionsGroup.setText("Logging of test runs");
		GridData gd3 = new GridData(GridData.FILL, GridData.FILL, true, false);
		gd3.horizontalSpan = 2;
		testOptionsGroup.setLayoutData(gd3);
		testOptionsGroup.setLayout(new GridLayout(2, false));

		bfeTestSameOptions = new BooleanFieldEditor(
				PreferenceConstants.P_TEST_SAME_OPTIONS,
				"Use same options as for framework logging", testOptionsGroup);
		addField(bfeTestSameOptions);

		bfeTestHtmlLogger = new BooleanFieldEditor(
				PreferenceConstants.P_TEST_HTMLLOGGING,
				"Activate additional HTML log file", testOptionsGroup);
		addField(bfeTestHtmlLogger);

		bfeTestPlainLogger = new BooleanFieldEditor(
				PreferenceConstants.P_TEST_PLAINLOGGING,
				"Use standard logging (plain text file)", testOptionsGroup);
		addField(bfeTestPlainLogger);

		bfeTestISO8601Logging = new BooleanFieldEditor(
				PreferenceConstants.P_TEST_USEISO8601LOGGING,
				"Use ISO 8601 logging in text file", testOptionsGroup);
		addField(bfeTestISO8601Logging);

		sfeTestLogLevel = new ScaleFieldEditor(
				PreferenceConstants.P_TEST_LOGLEVEL, "Level of logging",
				testOptionsGroup, PreferenceConstants.LOGLEVEL_TRACE,
				PreferenceConstants.LOGLEVEL_FATAL, 1, 1);
		addField(sfeTestLogLevel);
		
		//create caption for the loglevel scale field
		Composite labelComposite2 = new Composite(testOptionsGroup,
				SWT.NONE);
		labelComposite2.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData labelData2 = new GridData(GridData.FILL, GridData.FILL, true,
				false);
		labelData2.horizontalSpan = 2;
		labelData2.grabExcessHorizontalSpace = true;
		labelComposite2.setLayoutData(labelData2);
		//add each label followed by an empty spacer label
		for (int i = 0; i < PreferenceConstants.LOGLEVELS.length; i++) {
			Label lbl = new Label(labelComposite2, SWT.CENTER);
			lbl.setText(PreferenceConstants.LOGLEVELS[i]);
			if (i + 1 < PreferenceConstants.LOGLEVELS.length) {
				new Label(labelComposite2, SWT.CENTER);
			}
		}

		lblTestMinLevel = new Label(testOptionsGroup, SWT.LEFT);
		lblTestMinLevel.setText("everything is logged");
		lblTestMinLevel.setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL,
				true, false));

		lblTestMaxLevel = new Label(testOptionsGroup, SWT.RIGHT);
		lblTestMaxLevel.setText("only fatal problems are logged");
		lblTestMaxLevel.setLayoutData(new GridData(GridData.END, GridData.FILL,
				true, false));

		if (Activator.getDefault().getPreferenceStore().getBoolean(
				PreferenceConstants.P_TEST_SAME_OPTIONS)) {
			bfeTestHtmlLogger.setEnabled(false, testOptionsGroup);
			bfeTestPlainLogger.setEnabled(false, testOptionsGroup);
			bfeTestISO8601Logging.setEnabled(false, testOptionsGroup);
			sfeTestLogLevel.setEnabled(false, testOptionsGroup);
			sfeTestLogLevel.getScaleControl().setEnabled(false);
			lblTestMinLevel.setEnabled(false);
			lblTestMaxLevel.setEnabled(false);
		}

	}

	public void init(IWorkbench workbench) {

	}

	public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent event) {
		super.propertyChange(event);
		if (event.getProperty().equals(FieldEditor.VALUE)) {

			if (event.getSource() == bfeManualSettings) {
				manualDirSetting = ((Boolean) event.getNewValue())
						.booleanValue();

				dfeFrameworkLoggingDir.setEnabled(manualDirSetting,
						directoryGroup);

				dfeTestLoggingDir.setEnabled(manualDirSetting, directoryGroup);

			}

			if (event.getSource() == bfeTestSameOptions) {
				sameOptions = ((Boolean) event.getNewValue()).booleanValue();

				// enable the field editors if same Options is disabled and vice versa
				bfeTestHtmlLogger.setEnabled(!sameOptions, testOptionsGroup);
				bfeTestPlainLogger.setEnabled(!sameOptions, testOptionsGroup);
				bfeTestISO8601Logging
						.setEnabled(!sameOptions, testOptionsGroup);
				sfeTestLogLevel.setEnabled(!sameOptions, testOptionsGroup);
				sfeTestLogLevel.getScaleControl().setEnabled(!sameOptions);
				lblTestMinLevel.setEnabled(!sameOptions);
				lblTestMaxLevel.setEnabled(!sameOptions);

			}
		}
	}

	protected void performDefaults() {
		super.performDefaults();
		GTLogger.getInstance().debug(
				"Switched GT Logging Preference Page back do default values");

		//enable/disable test logging options
		sameOptions = Activator.getDefault().getPreferenceStore().getBoolean(
				PreferenceConstants.P_TEST_SAME_OPTIONS);
		manualDirSetting = Activator.getDefault().getPreferenceStore()
				.getBoolean(PreferenceConstants.P_MANUALDIRSETTINGS);

		// enable the field editors if same Options is disabled and vice versa
		bfeTestHtmlLogger.setEnabled(!sameOptions, testOptionsGroup);
		bfeTestPlainLogger.setEnabled(!sameOptions, testOptionsGroup);
		bfeTestISO8601Logging.setEnabled(!sameOptions, testOptionsGroup);
		sfeTestLogLevel.setEnabled(!sameOptions, testOptionsGroup);
		sfeTestLogLevel.getScaleControl().setEnabled(!sameOptions);
		lblTestMinLevel.setEnabled(!sameOptions);
		lblTestMaxLevel.setEnabled(!sameOptions);

		//enable or disable the loggingDir editor according to selection
		dfeFrameworkLoggingDir.setEnabled(manualDirSetting, directoryGroup);
		dfeTestLoggingDir.setEnabled(manualDirSetting, directoryGroup);

	}

	public boolean performOk() {
		boolean retVal = super.performOk();

		//adapt values for test logging when same options are selected
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		if (store.getBoolean(PreferenceConstants.P_TEST_SAME_OPTIONS)) {
			store.setValue(PreferenceConstants.P_TEST_PLAINLOGGING, store
					.getBoolean(PreferenceConstants.P_GT_PLAINLOGGING));
			store.setValue(PreferenceConstants.P_TEST_HTMLLOGGING, store
					.getBoolean(PreferenceConstants.P_GT_HTMLLOGGING));
			store.setValue(PreferenceConstants.P_TEST_LOGLEVEL, store
					.getBoolean(PreferenceConstants.P_GT_LOGLEVEL));
			store.setValue(PreferenceConstants.P_TEST_USEISO8601LOGGING, store
					.getBoolean(PreferenceConstants.P_GT_USEISO8601LOGGING));
		}

		//make sure that loggers change their options accordingly
		GTLogger.getInstance().checkOptions();

		return retVal;
	}

}