package org.globaltester.logger;

/* 
 * Project 	GlobalTester 
 * File		LoggerFactory.java
 *
 * Date    	14.10.2005
 * 
 * 
 * Developed by HJP Consulting GmbH
 * Lanfert 24
 * 33106 Paderborn
 * Germany
 * 
 * 
 * This software is the confidential and proprietary information
 * of HJP ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance 
 * with the terms of the Non-Disclosure Agreement you entered 
 * into with HJP.
 * 
 */

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.HTMLLayout;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;


/**
 * This factory is needed to create new Logger (log4j).
 * 
 * @version		Release 1.5.0
 * @author 		Holger Funke
 * 
 */

public class LoggerFactory {
	
	// internal logger
	private static Logger log = null;
	
	// root of logger
	private static Logger root = null;

	// initial class 
	private static Class<Object> initClass = null;
	
	// default names of log files
	private static String htmlFileName = "globaltester.html";
	private static String logFileName = "globaltester.log";
	
	private boolean useISO8601Logging = true;
	private boolean usePlainLogging = true;
	private boolean useHTMLLogging = false;
	private String loggingDir = "";
	
	
	/**
	 * Constructor of LoggerFactory
	 * @param initClass Class that invokes LoggerFactory
	 */
	public LoggerFactory(Class<Object> initClass)
	{ 
		LoggerFactory.initClass = initClass;
		if ( log == null ) { 
			init();
		}
	}

	/**
	 * Initialization of LoggerFactory 
	 *
	 */
	private void init() {
		root = Logger.getRootLogger();
		root.removeAllAppenders();
		BasicConfigurator.configure();
		log = Logger.getLogger(initClass);
	}
	
	
	/**
	 * Start a new log file (plain or html)
	 *
	 */
	public void startNewLogFile(){
		
		root.getLoggerRepository().shutdown();
		if (log != null) {
			log.getLoggerRepository().shutdown();
		}
		
		if (root != null) {
			root.getLoggerRepository().shutdown();
		}

		setFileNames();
		
		// settings for logfiles		
		Layout fileLayout;
		if (useISO8601Logging) {				
			fileLayout = new PatternLayout("%d %-5p - %m%n");
		}
		else {
			fileLayout = new PatternLayout("%m%n");
		}
		
		// settings for 'plain' logging
       	if (usePlainLogging) {       	
    		try {
    			FileAppender fileAppender = new FileAppender(fileLayout, logFileName);
    			root.addAppender(fileAppender);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
       	}
       	else {
       		logFileName = "";
       	}

		

		//settings for html file
		if (useHTMLLogging) {
			HTMLLayout htmlLayout = new HTMLLayout();
		    htmlLayout.setTitle(htmlFileName);
			WriterAppender writerAppender = null;
		    try {
		    	FileOutputStream output = new FileOutputStream(htmlFileName);
		        writerAppender = new WriterAppender(htmlLayout, output);
		    } catch(Exception e)
		    {
		    	e.printStackTrace();
		    }
		    writerAppender.activateOptions();
		    root.addAppender(writerAppender);
		}
		else {
			htmlFileName = "";
		}
	}
	
	/**
	 * Getter for logger name (html)
	 * @return file name of html logger
	 */
	public String getHtmlFileName() {
		return htmlFileName;
	}

	/**
	 * Getter for logger name (plain)
	 * @return file name of plain logger
	 */
	public String getLogFileName() {
		return logFileName;
	}

	/**
	 * Getter for internal Logger
	 * @return	internal logger
	 */
	public static Logger getLogger()
	{
	  if (log != null)
		return log;
	  else
		  return null;
	}
	
	public void setLoggingDirectory(String path){
		this.loggingDir = path;
	}
	

	public void useISO8601Logging(boolean set){
		useISO8601Logging = set;
	}
	
	public void usePlainLogging(boolean set){
		usePlainLogging = set;
	}

	public void useHTMLLogging(boolean set){
		useHTMLLogging = set;
	}
	
	/**
	 * Create file names for log files. Add iso formated date to 
	 * file name.
	 */
	private void setFileNames() {
		
        //IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        //String loggingDir = store.getString(PreferenceConstants.P_LOGGINGDIR);
		
		htmlFileName = loggingDir +"/gt_"
			+getIsoDate("yyyyMMddHHmmss")+".html";
		logFileName = loggingDir +"/gt_"
			+getIsoDate("yyyyMMddHHmmss")+".log";
	}

	/**
	 * Returns the current date inclusive time in ISO format
	 */
	public static String getIsoDate(String DATE_FORMAT) {
		//String DATE_FORMAT = "yyyyMMddHHmmss";
	    java.text.SimpleDateFormat sdf = 
	          new java.text.SimpleDateFormat(DATE_FORMAT);
	    // get current date:
	    Calendar cal = Calendar.getInstance();
	    return sdf.format(cal.getTime());
	}
	
	
}
