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
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.globaltester.logging.Activator;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

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

		Preferences preferences = new DefaultScope()
				.getNode(Activator.PLUGIN_ID);
		

		// switch to set manual directory settings
		preferences.putBoolean(PreferenceConstants.P_MANUALFRAMEWORKDIRSETTINGS, false);
		preferences.putBoolean(PreferenceConstants.P_MANUALDIRSETTINGS, false);
		preferences.putBoolean(PreferenceConstants.P_USE_SIM_LOG, false);

		// directory name of logging files
		preferences.put(PreferenceConstants.P_GT_LOGGINGDIR, Platform
				.getLocation().append(".metadata").toString());
		preferences.put(PreferenceConstants.P_TEST_LOGGINGDIR,
				System.getProperty("user.home"));
		preferences.put(PreferenceConstants.P_GT_SIM_LOGGINGDIR,
				System.getProperty("user.home"));

		// log level of log file
		preferences.putInt(PreferenceConstants.P_GT_LOGLEVEL,
				PreferenceConstants.LOGLEVEL_TRACE);
		preferences.putInt(PreferenceConstants.P_TEST_LOGLEVEL,
				PreferenceConstants.LOGLEVEL_INFO);
		preferences.putInt(PreferenceConstants.P_GT_SIM_LOGLEVEL,
				PreferenceConstants.LOGLEVEL_INFO);

		// switch to use html log file
		preferences.putBoolean(PreferenceConstants.P_GT_HTMLLOGGING, false);
		preferences.putBoolean(PreferenceConstants.P_TEST_HTMLLOGGING, false);
		preferences.putBoolean(PreferenceConstants.P_GT_SIM_HTMLLOGGING, false);

		// switch to use plain log file
		preferences.putBoolean(PreferenceConstants.P_GT_PLAINLOGGING, true);
		preferences.putBoolean(PreferenceConstants.P_TEST_PLAINLOGGING, true);
		preferences.putBoolean(PreferenceConstants.P_GT_SIM_PLAINLOGGING, true);
		preferences.putBoolean(PreferenceConstants.P_TEST_LOG_SINGLE_TESTCASES, true);

		// log in ISO8601 format
		preferences.putBoolean(PreferenceConstants.P_GT_USEISO8601LOGGING, true);
		preferences.putBoolean(PreferenceConstants.P_TEST_USEISO8601LOGGING, true);
		preferences.putBoolean(PreferenceConstants.P_GT_SIM_USEISO8601LOGGING, true);

		// switch to use console logging
		preferences.putBoolean(PreferenceConstants.P_GT_CONSOLELOGGING, true);

		// store all marker of log file persistent
		preferences.putBoolean(PreferenceConstants.P_TEST_PERSISTENTMARKER, false);
		
		//if TestCase or log file should open on double click
		preferences.putInt(PreferenceConstants.P_DOUBLECLICKRESULTVIEW, 0);
		
		// Force the application to save the preferences
		try {
			preferences.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}

	}

}
