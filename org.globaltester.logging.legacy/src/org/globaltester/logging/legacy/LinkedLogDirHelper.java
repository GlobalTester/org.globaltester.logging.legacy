package org.globaltester.logging.legacy;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IPathVariableManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.globaltester.logging.legacy.logger.GtErrorLogger;
import org.globaltester.logging.legacy.logger.TestLogger;

/**
 * Helper methods that simplify processing of linked external logging
 * directories
 * 
 * @author amay
 *
 */
public final class LinkedLogDirHelper {

	public static final String LINKED_LOG_WORKSPACE_PROJECT_NAME = "GTLogFiles";

	/**
	 * Empty constructor to hide the public implicit one
	 */
	private LinkedLogDirHelper() {
	}

	/**
	 * Checks the existence of the given Project by name and returns it. If it
	 * doesn't exist it creates it.
	 * 
	 * @return the project
	 * @throws CoreException
	 */
	public static IProject getLogProject() throws CoreException {

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		// Check if it already exists
		IProject[] projects = root.getProjects();
		for (int i = 0; i < projects.length; i++) {
			if (projects[i].getName().equalsIgnoreCase(LINKED_LOG_WORKSPACE_PROJECT_NAME)) {
				return projects[i]; // already exists
			}
		}

		// Create a new logging project
		IProject project = root.getProject(LINKED_LOG_WORKSPACE_PROJECT_NAME);
		project.create(null);
		project.open(null);
		return project;

	}

	/**
	 * Returns the IFolder representing the currently used log directory of
	 * {@link TestLogger}. If this is not present in the workspace it is created
	 * as linked resource.
	 * <p/>
	 * This might lead to strange behavior in cases where the linked resource
	 * already exists but points to somewhere else.
	 * 
	 * @return
	 * @throws CoreException
	 */
	public static IFolder getLinkedLogFolder() throws CoreException {

		// get the IPath of the used logging dir
		IPath linkedFolderIPath = new Path(TestLogger.getLogDir());

		// transform it into an URI and set path variable (for later
		// linking)
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IPathVariableManager pathMan = workspace.getPathVariableManager();
		URI path = null;
		try {
			path = URIUtil.toURI(linkedFolderIPath);
			pathMan.setURIValue("LOG", path);
		} catch (CoreException e1) {
			TestLogger.error(e1);
		}

		// link the folder into the logging project
		IFolder logFolder = getLogFolder(TestLogger.getLogDir());
		try {
			if (!logFolder.isLinked()) {
				// only link if not already linked to avoid exception
				logFolder.createLink(path, IResource.NONE, null);
			}
		} catch (CoreException e1) {
			TestLogger.error("Linking of logging folder failed.");
			TestLogger.error(e1);
		}
		return logFolder;
	}

	/**
	 * Returns true iff the referenced fullPath can be used as linked LogDir
	 * without problems.
	 * 
	 * @param fullPath
	 * @return
	 */
	public static boolean canBeLinkedAsLogDir(String fullPath) {
		IFolder logFolder;
		try {
			logFolder = getLogFolder(fullPath);
		

			if (!logFolder.exists())
				return true;
			if (logFolder.isLinked()) {
				return new File(logFolder.getLocationURI()).getCanonicalPath()
						.equals(new File(fullPath).getCanonicalPath());
			}
		} catch (CoreException | IOException e) {
			GtErrorLogger.log(Activator.PLUGIN_ID, e);
			return false;
		}
		return false;
	}

	/**
	 * Builds the workspace folder that can be linked to a static logging directory.
	 * <p/>
	 * Note: This call will create the generic logging workspace project regardless of the result.
	 * @param fullPath
	 * @return
	 * @throws CoreException
	 */
	private static IFolder getLogFolder(String fullPath) throws CoreException {
		String[] temp = fullPath.split("/|\\\\");
		IProject loggingProject = getLogProject();
		return loggingProject.getFolder(temp[temp.length - 1]);

	}

}
