package org.globaltester.logging.legacy.preferences;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.globaltester.logging.legacy.Activator;

/**
 * This class stores the properties of the plugin Class used to initialize
 * default preference values
 * 
 * @author amay, hfunke
 * 
 */

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * Use this to store plugin preferences For meaning of each preference look
	 * at PreferenceConstants.java
	 */
	public void initializeDefaultPreferences() {
		
		IPreferenceStore store = Activator.getDefault()
				.getPreferenceStore();
		
		// switch to set manual directory settings
		store.setDefault(PreferenceConstants.P_MANUALFRAMEWORKDIRSETTINGS, false);
		store.setDefault(PreferenceConstants.P_MANUALDIRSETTINGS, false);
		store.setDefault(PreferenceConstants.P_USE_SIM_LOG, false);

		// directory name of logging files
		store.setDefault(PreferenceConstants.P_GT_LOGGINGDIR, Platform
				.getLocation().append(".metadata").toString());
		store.setDefault(PreferenceConstants.P_TEST_LOGGINGDIR,
				System.getProperty("user.home"));

		// log level of log file
		store.setDefault(PreferenceConstants.P_GT_LOGLEVEL,
				PreferenceConstants.LOGLEVEL_TRACE);
		store.setDefault(PreferenceConstants.P_TEST_LOGLEVEL,
				PreferenceConstants.LOGLEVEL_INFO);

		// switch to use plain log file
		store.setDefault(PreferenceConstants.P_GT_PLAINLOGGING, true);
		store.setDefault(PreferenceConstants.P_TEST_PLAINLOGGING, true);
		store.setDefault(PreferenceConstants.P_TEST_LOG_SINGLE_TESTCASES, false);

		// log in ISO8601 format
		store.setDefault(PreferenceConstants.P_GT_USEISO8601LOGGING, false);
		store.setDefault(PreferenceConstants.P_TEST_USEISO8601LOGGING, false);

		// switch to use console logging
		store.setDefault(PreferenceConstants.P_GT_CONSOLELOGGING, true);

		// store all marker of log file persistent
		store.setDefault(PreferenceConstants.P_TEST_PERSISTENTMARKER, false);
		
		//if TestCase or log file should open on double click
		store.setDefault(PreferenceConstants.P_DOUBLECLICKRESULTVIEW, 0);

	}

}
