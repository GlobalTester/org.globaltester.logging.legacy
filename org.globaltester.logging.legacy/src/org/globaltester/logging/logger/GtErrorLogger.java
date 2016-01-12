package org.globaltester.logging.logger;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;

public class GtErrorLogger {

	/**
	 * Log an exception to the appropriate bundle logfile
	 * @param pluginID
	 * @param ex
	 */
	public static void log(String pluginID, Exception ex) {
		Bundle bundle;
		Status status = new Status(IStatus.WARNING, pluginID,
				ex.getLocalizedMessage(), ex);
		bundle = Platform.getBundle(pluginID);
		Platform.getLog(bundle).log(status);
	}

}
