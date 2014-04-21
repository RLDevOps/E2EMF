package com.relevance.e2emf.commons;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.log4j.*;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * @author emanuel
 * E2emf Log Utility  
 *
 */
public class Log {

	public static int LOG_REFRESH_FREQ = 5*60; //seconds
	
	public static final int HIGH = 5;
    public static final int MED = 3;
    public static final int LOW = 1;
    
	public static Logger _logger = null;
	public static Logger _errorlogger = null;
	
	public static boolean isLoggerInitialized = false;
	
	static String logFile = "log4j.xml";

	public static void initialize() {	
		
		try{
			//PropertyConfigurator.configure("log4j.properties");				
			
			if(_logger == null && !isLoggerInitialized){
				
				Log.class.getClassLoader().getResourceAsStream(logFile);
				DOMConfigurator.configureAndWatch(logFile, Log.LOG_REFRESH_FREQ * 1000);
				
				_logger = Logger.getLogger("e2emf");
				_errorlogger = Logger.getLogger("e2emfError");
				
				isLoggerInitialized = true;
				
				_logger.info("Logger sucessfully initialized on e2emf");
				
			}
			
		}catch(Exception e) {
			System.err.println("Exception initializing the e2emf logger...");
			PropertyConfigurator.configure("log4j.xml");
			
		}
		
        System.out.println("Logger set to : " + _logger.getName()+ " and Error Logger set to : " + _errorlogger.getName());
        _logger.info("E2emf Welcomes INFO Logger : ");
        _errorlogger.error("E2emf Welcomes INFO Logger : ");
	}
	
		
	   /**
     *  Write "Error" to syslog.
     *  To be used for logging errors e.g. "db connection failed".
     */
    public static void Error(String msg) {
        try {
          
        	_errorlogger.error(msg);          
          
        } catch (Exception ex) {
        }
    }

    /**
     *  This method will print stack traces to the Syslogs.
     *  @param throwable error object.
     */
    public static void Error(Throwable throwable) {
        try {
        	_errorlogger.error("",throwable);
           
        } catch (Exception ex) {
           System.out.println("Exception logging Error..."); // ignore
        }
    }
    
    /**
     *  Write "Warning" to syslog.
     *  To be used for logging warnings.
     */
    public static void Info(String msg) {
        try {
           
                _logger.info(msg);
           } catch (Exception ex) {
        }
    }


    /**
     *  Write "Warning" to syslog.
     *  To be used for logging warnings.
     */
    public static void Warning(String msg) {
        try {
                _logger.warn(msg);             
        	} catch (Exception ex) {
        }
    }
    
    
    /**
     * Write "Debug Trace" to syslog.<br>
     *
     *  To be used for logging debug messages.
     *  Current tracelevel is set as a property in the database.
     *  Programmer should set the trace level to HIGH for things
     *  like "entering Constructor()", etc.
     */
    public static void Debug(int level, String msg) {
    	
    	if(_logger != null && _logger.isDebugEnabled())
        {
    		 _logger.debug(msg);
        }         
    }

    public static void Debug(String msg) {
    	if(_logger != null && _logger.isDebugEnabled())
        {
    		 _logger.debug(msg);
        }         
    }
    
    public static String getStackTrace(Throwable aThrowable) {
        String ret = "";
        try
        {
            Writer result = new StringWriter();
            PrintWriter printWriter= new PrintWriter(result);
            aThrowable.printStackTrace(printWriter);
            ret= result.toString();
            result.close();
            printWriter.close();
        }
        catch(Exception e)
        {
        	System.out.println("Exception writing throwable Exception to the PrintWriter...");
        }

        return ret;
    }
	
}