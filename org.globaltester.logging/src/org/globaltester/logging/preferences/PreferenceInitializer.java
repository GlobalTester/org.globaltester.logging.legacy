/*
 * Project GlobalTester-Plugin File PreferencesInitializer.java
 * 
 * Date 14.10.2005
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

package org.globaltester.logging.preferences;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.globaltester.logging.Activator;

/**
 * This class stores the properties of the plugin Class used to initialize
 * default preference values
 * 
 * @version Release 2.2.0
 * @author Holger Funke
 * 
 */

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * Use this to store plugin preferences For meaning of each preference look
	 * at PreferenceConstants.java
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		// switch to set manual directory settings
		store.setDefault(PreferenceConstants.P_MANUALFRAMEWORKDIRSETTINGS, false);
		store.setDefault(PreferenceConstants.P_MANUALDIRSETTINGS, false);
		store.setDefault(PreferenceConstants.P_USE_SIM_LOG, false);

		// directory name of logging files
		store.setDefault(PreferenceConstants.P_GT_LOGGINGDIR, Platform
				.getLocation().append(".metadata").toString());
		store.setDefault(PreferenceConstants.P_TEST_LOGGINGDIR, System.getProperty("user.home"));
		store.setDefault(PreferenceConstants.P_GT_SIM_LOGGINGDIR, System.getProperty("user.home"));

		// log level of log file
		store.setDefault(PreferenceConstants.P_GT_LOGLEVEL,
				PreferenceConstants.LOGLEVEL_TRACE);
		store.setDefault(PreferenceConstants.P_TEST_LOGLEVEL,
				PreferenceConstants.LOGLEVEL_INFO);
		store.setDefault(PreferenceConstants.P_GT_SIM_LOGLEVEL,
				PreferenceConstants.LOGLEVEL_INFO);

		// switch to use html log file
		store.setDefault(PreferenceConstants.P_GT_HTMLLOGGING, false);
		store.setDefault(PreferenceConstants.P_TEST_HTMLLOGGING, false);
		store.setDefault(PreferenceConstants.P_GT_SIM_HTMLLOGGING, false);

		// switch to use plain log file
		store.setDefault(PreferenceConstants.P_GT_PLAINLOGGING, true);
		store.setDefault(PreferenceConstants.P_TEST_PLAINLOGGING, true);
		store.setDefault(PreferenceConstants.P_GT_SIM_PLAINLOGGING, true);
		store.setDefault(PreferenceConstants.P_TEST_LOG_SINGLE_TESTCASES, false);

		// log in ISO8601 format
		store.setDefault(PreferenceConstants.P_GT_USEISO8601LOGGING, true);
		store.setDefault(PreferenceConstants.P_TEST_USEISO8601LOGGING, true);
		store.setDefault(PreferenceConstants.P_GT_SIM_USEISO8601LOGGING, true);
		
		// switch to use console logging
		store.setDefault(PreferenceConstants.P_GT_CONSOLELOGGING, true);

		// store all marker of log file persistent
		store.setDefault(PreferenceConstants.P_TEST_PERSISTENTMARKER, false);

		
	}

}
