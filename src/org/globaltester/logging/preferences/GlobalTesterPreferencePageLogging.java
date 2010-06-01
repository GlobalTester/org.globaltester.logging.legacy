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

import org.eclipse.core.runtime.Platform;
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

	//editors for test logging options
	Group testOptionsGroup;
	BooleanFieldEditor bfeTestPersistentMarker;
	BooleanFieldEditor bfeTestHtmlLogger;
	BooleanFieldEditor bfeTestPlainLogger;
	BooleanFieldEditor bfeTestISO8601Logging;
	BooleanFieldEditor bfeFrameworkConsoleLogger;
	ScaleFieldEditor sfeTestLogLevel;
	Label lblTestMinLevel;
	Label lblTestMaxLevel;
	
	//editors for simulator logging options
	Group simOptionsGroup;
	DirectoryFieldEditor dfeSimLoggingDir;
	BooleanFieldEditor bfeSimHtmlLogger;
	BooleanFieldEditor bfeSimPlainLogger;
	BooleanFieldEditor bfeSimISO8601Logging;
	ScaleFieldEditor sfeSimLogLevel;
	Label lblSimMinLevel;
	Label lblSimMaxLevel;

	//variables needed for enabling/disabling of FieldEditors
	private boolean manualDirSetting; // whether logging directories are manually selected

	public GlobalTesterPreferencePageLogging() {
		super(GRID);
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		setPreferenceStore(store);
		setDescription("GlobalTester preference page");

		manualDirSetting = store
				.getBoolean(PreferenceConstants.P_MANUALDIRSETTINGS);
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
		dfeTestLoggingDir.setEnabled(manualDirSetting, directoryGroup);
		
		addField(dfeFrameworkLoggingDir);
		addField(dfeTestLoggingDir);
		
		//preferences for logging of tests
		testOptionsGroup = new Group(container, SWT.NONE);
		testOptionsGroup.setText("Logging of test runs");
		GridData gd3 = new GridData(GridData.FILL, GridData.FILL, true, false);
		gd3.horizontalSpan = 2;
		testOptionsGroup.setLayoutData(gd3);
		testOptionsGroup.setLayout(new GridLayout(2, false));

		bfeTestPersistentMarker = new BooleanFieldEditor(
				PreferenceConstants.P_TEST_PERSISTENTMARKER,
				"Store markers in log file persistently (and not only for the current session)", testOptionsGroup);
		addField(bfeTestPersistentMarker);

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
		
		bfeFrameworkConsoleLogger = new BooleanFieldEditor(
				PreferenceConstants.P_GT_CONSOLELOGGING,
				"Activate additional logging to STDOUT", testOptionsGroup);
		addField(bfeFrameworkConsoleLogger);

		sfeTestLogLevel = new ScaleFieldEditor(
				PreferenceConstants.P_TEST_LOGLEVEL, "Level of logging",
				testOptionsGroup, PreferenceConstants.LOGLEVEL_TRACE,
				PreferenceConstants.LOGLEVEL_FATAL, 1, 1);
		addField(sfeTestLogLevel);
		
		//create caption for the loglevel scale field
		Composite labelComposite3 = new Composite(testOptionsGroup,
				SWT.NONE);
		labelComposite3.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData labelData3 = new GridData(GridData.FILL, GridData.FILL, true,
				false);
		labelData3.horizontalSpan = 3;
		labelData3.grabExcessHorizontalSpace = true;
		labelComposite3.setLayoutData(labelData3);
		//add each label followed by an empty spacer label
		for (int i = 0; i < PreferenceConstants.LOGLEVELS.length; i++) {
			Label lbl = new Label(labelComposite3, SWT.CENTER);
			lbl.setText(PreferenceConstants.LOGLEVELS[i]);
			if (i + 1 < PreferenceConstants.LOGLEVELS.length) {
				new Label(labelComposite3, SWT.CENTER);
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
		
		//preferences for logging of GT Simulator
		if (Platform.getBundle("com.hjp.globaltester.simulator")!= null) {
			simOptionsGroup = new Group(container, SWT.NONE);
			simOptionsGroup.setText("Logging of GT Simulator runs");
			GridData gd4 = new GridData(GridData.FILL, GridData.FILL, true, false);
			gd4.horizontalSpan = 2;
			simOptionsGroup.setLayoutData(gd4);
			simOptionsGroup.setLayout(new GridLayout(2, false));
			
			Composite comp1 = new Composite(simOptionsGroup,
					SWT.NONE);
			comp1.setLayout(new FillLayout(SWT.HORIZONTAL));
			GridData gd_comp1 = new GridData(GridData.FILL, GridData.FILL, true,
					false);
			gd_comp1.horizontalSpan = 4;
			gd_comp1.grabExcessHorizontalSpace = true;
			comp1.setLayoutData(gd_comp1);
			
			dfeSimLoggingDir = new DirectoryFieldEditor(
					PreferenceConstants.P_GT_SIM_LOGGINGDIR,
					"&Simulator logging directory:", comp1);
			addField(dfeSimLoggingDir);
			
			bfeSimHtmlLogger = new BooleanFieldEditor(
					PreferenceConstants.P_GT_SIM_HTMLLOGGING,
					"Activate additional HTML log file", simOptionsGroup);
			addField(bfeSimHtmlLogger);
	
			bfeSimPlainLogger = new BooleanFieldEditor(
					PreferenceConstants.P_GT_SIM_PLAINLOGGING,
					"Use standard logging (plain text file)", simOptionsGroup);
			addField(bfeSimPlainLogger);
	
			bfeSimISO8601Logging = new BooleanFieldEditor(
					PreferenceConstants.P_GT_SIM_USEISO8601LOGGING,
					"Use ISO 8601 logging in text file", simOptionsGroup);
			addField(bfeSimISO8601Logging);
			
			
			sfeSimLogLevel = new ScaleFieldEditor(
					PreferenceConstants.P_GT_SIM_LOGLEVEL, "Level of logging",
					simOptionsGroup, PreferenceConstants.LOGLEVEL_TRACE,
					PreferenceConstants.LOGLEVEL_FATAL, 1, 1);
			addField(sfeSimLogLevel);
			
			//create caption for the loglevel scale field
			Composite labelComposite4 = new Composite(simOptionsGroup,
					SWT.NONE);
			labelComposite4.setLayout(new FillLayout(SWT.HORIZONTAL));
			GridData labelData4 = new GridData(GridData.FILL, GridData.FILL, true,
					false);
			labelData4.horizontalSpan = 4;
			labelData4.grabExcessHorizontalSpace = true;
			labelComposite4.setLayoutData(labelData4);
			//add each label followed by an empty spacer label
			for (int i = 0; i < PreferenceConstants.LOGLEVELS.length; i++) {
				Label lbl = new Label(labelComposite4, SWT.CENTER);
				lbl.setText(PreferenceConstants.LOGLEVELS[i]);
				if (i + 1 < PreferenceConstants.LOGLEVELS.length) {
					new Label(labelComposite4, SWT.CENTER);
				}
			}
	
			lblSimMinLevel = new Label(simOptionsGroup, SWT.LEFT);
			lblSimMinLevel.setText("everything is logged");
			lblSimMinLevel.setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL,
					true, false));
	
			lblSimMaxLevel = new Label(simOptionsGroup, SWT.RIGHT);
			lblSimMaxLevel.setText("only fatal problems are logged");
			lblSimMaxLevel.setLayoutData(new GridData(GridData.END, GridData.FILL,
					true, false));

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

		}
	}

	protected void performDefaults() {
		super.performDefaults();
		GTLogger.getInstance().debug(
				"Switched GT Logging Preference Page back do default values");

		//enable/disable test logging options
		manualDirSetting = Activator.getDefault().getPreferenceStore()
				.getBoolean(PreferenceConstants.P_MANUALDIRSETTINGS);

		//enable or disable the loggingDir editor according to selection
		dfeFrameworkLoggingDir.setEnabled(manualDirSetting, directoryGroup);
		dfeTestLoggingDir.setEnabled(manualDirSetting, directoryGroup);
	}

	public boolean performOk() {
		boolean retVal = super.performOk();

		//make sure that loggers change their options accordingly
		GTLogger.getInstance().checkOptions();

		return retVal;
	}

}