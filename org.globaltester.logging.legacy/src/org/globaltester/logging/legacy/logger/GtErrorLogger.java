package org.globaltester.logging.legacy.logger;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;

public final class GtErrorLogger {
	
	private GtErrorLogger(){
		//no instances intended
	}

	/**
	 * Log an exception to the appropriate bundle log
	 * @param pluginID
	 * @param ex
	 */
	public static void log(String pluginID, Throwable ex) {
		log(pluginID, ex.getLocalizedMessage(), ex);
	}

	/**
	 * Log an exception to the appropriate bundle log
	 * @param pluginID
	 * @param message
	 * @param ex
	 */
	public static void log(String pluginID, String message, Throwable ex) {
		Status status = new Status(IStatus.WARNING, pluginID,
				message, ex);
		Bundle bundle = Platform.getBundle(pluginID);
		Platform.getLog(bundle).log(status);
		
	}

}
