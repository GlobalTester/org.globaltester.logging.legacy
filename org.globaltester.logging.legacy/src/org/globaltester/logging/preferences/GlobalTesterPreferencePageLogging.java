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
import org.globaltester.preferences.ValidateDirectoryFieldEditor;

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
	Group frameworkOptionsGroup;
	BooleanFieldEditor bfeFrameworkManualSettings;
	private Composite compFrameworkDirEditor;
	ValidateDirectoryFieldEditor dfeFrameworkLoggingDir;
	
	BooleanFieldEditor bfeFrameworkPlainLogging;
	BooleanFieldEditor bfeFrameworkISO8601Logging;
	BooleanFieldEditor bfeFrameworkConsoleLogger;
	ScaleFieldEditor sfeFrameworkLogLevel;
	Label lblFrameworkMinLevel;
	Label lblFrameworkMaxLevel;
	
	//editors for test logging options
	Group testOptionsGroup;
	BooleanFieldEditor bfeTestManualSettings;
	private Composite compTestDirEditor;
	ValidateDirectoryFieldEditor dfeTestLoggingDir;
	BooleanFieldEditor bfeTestPersistentMarker;
	BooleanFieldEditor bfeTestHtmlLogger;
	BooleanFieldEditor bfeTestPlainLogger;
	BooleanFieldEditor bfeTestISO8601Logging;
	BooleanFieldEditor bfeTestConsoleLogger;
	BooleanFieldEditor bfeTestSingleTestcaseLogging;
	ScaleFieldEditor sfeTestLogLevel;
	Label lblTestMinLevel;
	Label lblTestMaxLevel;
	
	//editors for simulator logging options
	Group simOptionsGroup;
	BooleanFieldEditor bfeUseSimLogs;
	private Composite compSimDirEditor;
	ValidateDirectoryFieldEditor dfeSimLoggingDir;
	BooleanFieldEditor bfeSimHtmlLogger;
	BooleanFieldEditor bfeSimPlainLogger;
	BooleanFieldEditor bfeSimISO8601Logging;
	ScaleFieldEditor sfeSimLogLevel;
	Label lblSimMinLevel;
	Label lblSimMaxLevel;

	//variables needed for enabling/disabling of FieldEditors
	private boolean manualFrameworkDirSetting; // whether framework logging directory is manually selected
	private boolean manualTestDirSetting; // whether test logging directory is manually selected
	private boolean useSimLogs; // whether logging directories are manually selected

	public GlobalTesterPreferencePageLogging() {
		super(GRID);
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		setPreferenceStore(store);
		setDescription("GlobalTester preference page");

		manualFrameworkDirSetting = store
				.getBoolean(PreferenceConstants.P_MANUALFRAMEWORKDIRSETTINGS);
		
		manualTestDirSetting = store
				.getBoolean(PreferenceConstants.P_MANUALDIRSETTINGS);
		
		useSimLogs = store
				.getBoolean(PreferenceConstants.P_USE_SIM_LOG);
	}
	
	
	/**
	 * Checks if necessary options for workspace refreshing are checked. If not
	 * it shows an error in the headline of the pref page.
	 * 
	 * @return true if all prefs are set or false if not
	 */
	private boolean workspacePrefsOk(){
		// get workspace preferences
		boolean refreshOpt = Platform.getPreferencesService().getBoolean("org.eclipse.core.resources", "refresh.enabled", false, null);
		boolean refreshLightOpt = Platform.getPreferencesService().getBoolean("org.eclipse.core.resources", "refresh.lightweight.enabled", false, null);
		String warn;
		
		if(!refreshOpt && !refreshLightOpt){
			warn = "Also set 'Refresh using native hook or polling' and 'Refresh on Access' in your workspace preferences (General->Workspace)";
			setErrorMessage(warn);
			return false;
		}
		if(!refreshOpt){
			warn = "Also set 'Refresh using native hook or polling' in your workspace preferences (General->Workspace)";
			setErrorMessage(warn);
			return false;
		}
		if(!refreshLightOpt){
			warn = "Also set 'Refresh on Access' in your workspace preferences (General->Workspace)";
			setErrorMessage(warn);
			return false;
		}
		return true;
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

		//preferences for framework logging
		frameworkOptionsGroup = new Group(container, SWT.NONE);
		frameworkOptionsGroup.setText("Framework logging");
		GridData gd = new GridData(GridData.FILL, GridData.FILL, true, false);
		gd.horizontalSpan = 2;
		frameworkOptionsGroup.setLayoutData(gd);
		frameworkOptionsGroup.setLayout(new GridLayout(2, false));

		bfeFrameworkManualSettings = new BooleanFieldEditor(
				PreferenceConstants.P_MANUALFRAMEWORKDIRSETTINGS,
				"Use manual directory setting", frameworkOptionsGroup);
		addField(bfeFrameworkManualSettings);

		compFrameworkDirEditor = new Composite(frameworkOptionsGroup,
				SWT.NONE);
		compFrameworkDirEditor.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_compFrameworkDirEditor = new GridData(GridData.FILL, GridData.FILL, true,
				false);
		gd_compFrameworkDirEditor.horizontalSpan = 4;
		gd_compFrameworkDirEditor.grabExcessHorizontalSpace = true;
		compFrameworkDirEditor.setLayoutData(gd_compFrameworkDirEditor);
		
		dfeFrameworkLoggingDir = new ValidateDirectoryFieldEditor(
				PreferenceConstants.P_GT_LOGGINGDIR,
				"&Framework logging directory:", compFrameworkDirEditor);
		dfeFrameworkLoggingDir.setEmptyStringAllowed(false);
		dfeFrameworkLoggingDir.setEnabled(manualFrameworkDirSetting, compFrameworkDirEditor);
		addField(dfeFrameworkLoggingDir);
		
		
		bfeFrameworkPlainLogging = new BooleanFieldEditor(
				PreferenceConstants.P_GT_PLAINLOGGING,
				"Use standard logging (plain text file)", frameworkOptionsGroup);
		addField(bfeFrameworkPlainLogging);

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
		
		//create caption for the log level scale field
		Composite compFrameworkLogLevelLabels= new Composite(frameworkOptionsGroup,
				SWT.NONE);
		compFrameworkLogLevelLabels.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gdFrameworkLogLevelLabels = new GridData(GridData.FILL, GridData.FILL, true,
				false);
		gdFrameworkLogLevelLabels.horizontalSpan = 3;
		gdFrameworkLogLevelLabels.grabExcessHorizontalSpace = true;
		compFrameworkLogLevelLabels.setLayoutData(gdFrameworkLogLevelLabels);
		//add each label followed by an empty spacer label
		for (int i = 0; i < PreferenceConstants.LOGLEVELS.length; i++) {
			Label lbl = new Label(compFrameworkLogLevelLabels, SWT.CENTER);
			lbl.setText(PreferenceConstants.LOGLEVELS[i]);
			if (i + 1 < PreferenceConstants.LOGLEVELS.length) {
				new Label(compFrameworkLogLevelLabels, SWT.CENTER);
			}
		}
		
		lblFrameworkMinLevel = new Label(frameworkOptionsGroup, SWT.LEFT);
		lblFrameworkMinLevel.setText("everything is logged");
		lblFrameworkMinLevel.setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL,
				true, false));

		lblFrameworkMaxLevel = new Label(frameworkOptionsGroup, SWT.RIGHT);
		lblFrameworkMaxLevel.setText("only fatal problems are logged");
		lblFrameworkMaxLevel.setLayoutData(new GridData(GridData.END, GridData.FILL,
						true, false));
				
				
		//preferences for logging of tests
		testOptionsGroup = new Group(container, SWT.NONE);
		testOptionsGroup.setText("Logging of test runs");
		GridData gd3 = new GridData(GridData.FILL, GridData.FILL, true, false);
		gd3.horizontalSpan = 2;
		testOptionsGroup.setLayoutData(gd3);
		testOptionsGroup.setLayout(new GridLayout(2, false));

		bfeTestManualSettings = new BooleanFieldEditor(
				PreferenceConstants.P_MANUALDIRSETTINGS,
				"Use manual directory setting", testOptionsGroup);
		addField(bfeTestManualSettings);

		compTestDirEditor = new Composite(testOptionsGroup,
				SWT.NONE);
		compTestDirEditor.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_compTestDirEditor = new GridData(GridData.FILL, GridData.FILL, true,
				false);
		gd_compTestDirEditor.horizontalSpan = 4;
		gd_compTestDirEditor.grabExcessHorizontalSpace = true;
		compTestDirEditor.setLayoutData(gd_compTestDirEditor);
		
		dfeTestLoggingDir = new ValidateDirectoryFieldEditor(
				PreferenceConstants.P_TEST_LOGGINGDIR,
				"&Test logging directory:", compTestDirEditor);
		dfeTestLoggingDir.setEmptyStringAllowed(false);
		dfeTestLoggingDir.setEnabled(manualTestDirSetting, compTestDirEditor);
		addField(dfeTestLoggingDir);
		
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
		
		bfeTestSingleTestcaseLogging = new BooleanFieldEditor(
				PreferenceConstants.P_TEST_LOG_SINGLE_TESTCASES,
				"Add separate logfile for each testcase", testOptionsGroup);
		addField(bfeTestSingleTestcaseLogging);

		sfeTestLogLevel = new ScaleFieldEditor(
				PreferenceConstants.P_TEST_LOGLEVEL, "Level of logging",
				testOptionsGroup, PreferenceConstants.LOGLEVEL_TRACE,
				PreferenceConstants.LOGLEVEL_FATAL, 1, 1);
		addField(sfeTestLogLevel);
		
		//create caption for the log level scale field
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
			
			bfeUseSimLogs = new BooleanFieldEditor(
					PreferenceConstants.P_USE_SIM_LOG,
					"Generate simulator logging files with following options", simOptionsGroup);
			addField(bfeUseSimLogs);
			
			compSimDirEditor = new Composite(simOptionsGroup,
					SWT.NONE);
			compSimDirEditor.setLayout(new FillLayout(SWT.HORIZONTAL));
			GridData gd_compSimDirEditor = new GridData(GridData.FILL, GridData.FILL, true,
					false);
			gd_compSimDirEditor.horizontalSpan = 4;
			gd_compSimDirEditor.grabExcessHorizontalSpace = true;
			compSimDirEditor.setLayoutData(gd_compSimDirEditor);
			
			dfeSimLoggingDir = new ValidateDirectoryFieldEditor(
					PreferenceConstants.P_GT_SIM_LOGGINGDIR,
					"&Simulator logging directory:", compSimDirEditor);
			dfeSimLoggingDir.setEmptyStringAllowed(false);
			dfeSimLoggingDir.setEnabled(useSimLogs, compSimDirEditor);
			addField(dfeSimLoggingDir);
			
			bfeSimHtmlLogger = new BooleanFieldEditor(
					PreferenceConstants.P_GT_SIM_HTMLLOGGING,
					"Activate additional HTML log file", simOptionsGroup);
			bfeSimHtmlLogger.setEnabled(useSimLogs, simOptionsGroup);
			addField(bfeSimHtmlLogger);
	
			bfeSimPlainLogger = new BooleanFieldEditor(
					PreferenceConstants.P_GT_SIM_PLAINLOGGING,
					"Use standard logging (plain text file)", simOptionsGroup);
			bfeSimPlainLogger.setEnabled(useSimLogs, simOptionsGroup);
			addField(bfeSimPlainLogger);
	
			bfeSimISO8601Logging = new BooleanFieldEditor(
					PreferenceConstants.P_GT_SIM_USEISO8601LOGGING,
					"Use ISO 8601 logging in text file", simOptionsGroup);
			bfeSimISO8601Logging.setEnabled(useSimLogs, simOptionsGroup);
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

			if (event.getSource() == bfeFrameworkManualSettings) {
				manualFrameworkDirSetting = ((Boolean) event.getNewValue())
						.booleanValue();
				if (manualFrameworkDirSetting == true) {
					dfeFrameworkLoggingDir.setEnabled(true,compFrameworkDirEditor);
					if (dfeFrameworkLoggingDir.isValid() && dfeFrameworkLoggingDir.getStringValue() !="") {
						setErrorMessage("");	
						setValid(true);
					} else {	
						setErrorMessage("Use a valid directory!");
						setValid(false);
					}
				} else {
					dfeFrameworkLoggingDir.setEnabled(false,compFrameworkDirEditor);
					setErrorMessage(null);
					setValid(true);
				}



			}
			
			if (event.getSource() == bfeTestManualSettings) {
				manualTestDirSetting = ((Boolean) event.getNewValue())
						.booleanValue();
				if (manualTestDirSetting == true) {
					dfeTestLoggingDir.setEnabled(true, compTestDirEditor);
					if (dfeTestLoggingDir.isValid() && dfeTestLoggingDir.getStringValue() !=""	) {
						
						if (workspacePrefsOk()) {
							//everything is ok
							setErrorMessage(null);
							setValid(true);
						}else{
							//workspace prefs not set as required
//							setValid(false);
						}
					} else {	
						setErrorMessage("Use a valid directory!");
						setValid(false);
					}
				} else {
					dfeTestLoggingDir.setEnabled(false, compTestDirEditor);
					setErrorMessage(null);
					setValid(true);
				}



			}
			
			if (event.getSource() == bfeUseSimLogs) {
				useSimLogs = ((Boolean) event.getNewValue())
						.booleanValue();
				if (useSimLogs == true) {
					dfeSimLoggingDir.setEnabled(true,compSimDirEditor);
					if (dfeSimLoggingDir.isValid() && dfeSimLoggingDir.getStringValue() !="") {
						setErrorMessage(null);	
						setValid(true);
					} else {	
						setErrorMessage("Use a valid directory!");
						setValid(false);
					}
						
				} else {
					dfeSimLoggingDir.setEnabled(false,compSimDirEditor);
					setErrorMessage(null);
					setValid(true);
				}
				
				bfeSimHtmlLogger.setEnabled(useSimLogs, simOptionsGroup);
				bfeSimPlainLogger.setEnabled(useSimLogs, simOptionsGroup);
				bfeSimISO8601Logging.setEnabled(useSimLogs, simOptionsGroup);
			}
			
		}
	}

	protected void performDefaults() {
		super.performDefaults();
		GTLogger.getInstance().debug(
				"Switched GT Logging Preference Page back do default values");

		//enable/disable test logging options
		manualTestDirSetting = Activator.getDefault().getPreferenceStore()
				.getBoolean(PreferenceConstants.P_MANUALDIRSETTINGS);

		//enable or disable the loggingDir editor according to selection
		dfeFrameworkLoggingDir.setEnabled(manualFrameworkDirSetting, compFrameworkDirEditor);
		dfeTestLoggingDir.setEnabled(manualTestDirSetting, compTestDirEditor);
		
		//disable the simulator logging
		dfeSimLoggingDir.setEnabled(false, compSimDirEditor);
		bfeSimHtmlLogger.setEnabled(false, simOptionsGroup);
		bfeSimPlainLogger.setEnabled(false, simOptionsGroup);
		bfeSimISO8601Logging.setEnabled(false, simOptionsGroup);

	}

	public boolean performOk() {
		boolean retVal = super.performOk();

		//make sure that loggers change their options accordingly
		GTLogger.getInstance().checkOptions();

		return retVal;
	}

}
