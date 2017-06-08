package org.globaltester.logging.legacy.preferences;

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
import org.globaltester.logging.legacy.Activator;
import org.globaltester.logging.legacy.LinkedLogDirHelper;
import org.globaltester.logging.legacy.logger.GTLogger;

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
	DirectoryFieldEditor dfeFrameworkLoggingDir;
	
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
	DirectoryFieldEditor dfeTestLoggingDir;
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
	BooleanFieldEditor bfeSimPlainLogger;
	BooleanFieldEditor bfeSimISO8601Logging;
	ScaleFieldEditor sfeSimLogLevel;
	Label lblSimMinLevel;
	Label lblSimMaxLevel;

	public GlobalTesterPreferencePageLogging() {
		super(GRID);
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		setPreferenceStore(store);
		setDescription("GlobalTester preference page");
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
		
		dfeFrameworkLoggingDir = new DirectoryFieldEditor(
				PreferenceConstants.P_GT_LOGGINGDIR,
				"&Framework logging directory:", compFrameworkDirEditor);
		dfeFrameworkLoggingDir.setEmptyStringAllowed(false);
		dfeFrameworkLoggingDir.setEnabled(getPreferenceStore().getBoolean(PreferenceConstants.P_MANUALFRAMEWORKDIRSETTINGS), compFrameworkDirEditor);
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
		
		dfeTestLoggingDir = new StaticLogDirectoryFieldEditor(
				PreferenceConstants.P_TEST_LOGGINGDIR,
				"&Test logging directory:", compTestDirEditor);
		dfeTestLoggingDir.setEmptyStringAllowed(false);
		dfeTestLoggingDir.setEnabled(getPreferenceStore().getBoolean(PreferenceConstants.P_MANUALDIRSETTINGS), compTestDirEditor);
		addField(dfeTestLoggingDir);
		
		bfeTestPersistentMarker = new BooleanFieldEditor(
				PreferenceConstants.P_TEST_PERSISTENTMARKER,
				"Store markers in log file persistently (and not only for the current session)", testOptionsGroup);
		addField(bfeTestPersistentMarker);

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
		

	}

	@Override
	public void init(IWorkbench workbench) {
		//no initialization needed
	}

	@Override
	public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent event) {
		super.propertyChange(event);

		if (event.getProperty().equals(FieldEditor.VALUE)) {
			if (event.getSource() == bfeFrameworkManualSettings) {
				dfeFrameworkLoggingDir.setEnabled(((Boolean) event.getNewValue()).booleanValue(),
						compFrameworkDirEditor);
			}

			if (event.getSource() == bfeTestManualSettings) {
				dfeTestLoggingDir.setEnabled(((Boolean) event.getNewValue()).booleanValue(), compTestDirEditor);
			}
			
			checkState();
		}

	}
	
	@Override
    protected void checkState() {
		setErrorMessage(null);
		super.checkState();
		if (!validateManualFrameworkLogSettings()) return;
		if (!validateManualTestLogSettings()) return;
	}


	private boolean validateManualFrameworkLogSettings() {		
		if (bfeFrameworkManualSettings.getBooleanValue()) {
			if (!dfeFrameworkLoggingDir.isValid() || "".equals(dfeFrameworkLoggingDir.getStringValue())) {
				setErrorMessage("Use a valid directory for framework logging!");
				setValid(false);
				return false;
			}
			
			return validateRequiredWorkspaceRefreshPrefs();
		}
		return true;
	}


	private boolean validateManualTestLogSettings() {
		if (bfeTestManualSettings.getBooleanValue()) {
			if (!dfeTestLoggingDir.isValid() || "".equals(dfeTestLoggingDir.getStringValue())) {
				setErrorMessage("Use a valid directory for test logging!");
				setValid(false);
				return false;
			}
			
			if (!LinkedLogDirHelper.canBeLinkedAsLogDir(dfeTestLoggingDir.getStringValue())) {
				setErrorMessage("Selected static logging directory can not be properly linked into the workspace (possible reason: another directory with same name is already present.");
				setValid(false);
				return false;
			}
			
			return validateRequiredWorkspaceRefreshPrefs();
			
		}
		return true;
	}
	
	
	/**
	 * Checks if necessary options for workspace refreshing are checked. If not
	 * it shows an error in the headline of the pref page.
	 * 
	 * @return true if all prefs are set or false if not
	 */
	private boolean validateRequiredWorkspaceRefreshPrefs(){
		// get workspace preferences
		boolean refreshOpt = Platform.getPreferencesService().getBoolean("org.eclipse.core.resources", "refresh.enabled", false, null);
		boolean refreshLightOpt = Platform.getPreferencesService().getBoolean("org.eclipse.core.resources", "refresh.lightweight.enabled", false, null);
		
		
		if(!refreshOpt && !refreshLightOpt){
			setErrorMessage("Also set 'Refresh using native hook or polling' and 'Refresh on Access' in your workspace preferences (General->Workspace)");
			setValid(false);
			return false;
		}
		if(!refreshOpt){
			setErrorMessage("Also set 'Refresh using native hook or polling' in your workspace preferences (General->Workspace)");
			setValid(false);
			return false;
		}
		if(!refreshLightOpt){
			setErrorMessage("Also set 'Refresh on Access' in your workspace preferences (General->Workspace)");
			setValid(false);
			return false;
		}
		return true;
	}

	@Override
	protected void performDefaults() {
		super.performDefaults();
		GTLogger.getInstance().debug(
				"Switched GT Logging Preference Page back do default values");

		//enable or disable the loggingDir editor according to selection
		dfeFrameworkLoggingDir.setEnabled(getPreferenceStore().getBoolean(PreferenceConstants.P_MANUALFRAMEWORKDIRSETTINGS), compFrameworkDirEditor);
		dfeTestLoggingDir.setEnabled(getPreferenceStore().getBoolean(PreferenceConstants.P_MANUALDIRSETTINGS), compTestDirEditor);
		
		//disable the simulator logging
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
