package net.jodybrewster.ant.taskdefs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;


/**
 * Wrapper for the rest of the Movie tasks
 * 
 * @author Jody Brewster
 *
 */
public class FlashCommand extends Task {
	
	private String TEMP_DIR = "tmp_flashcommand";
	
	private String _flashApp;
	private List<Movie> _movies = new ArrayList<Movie>();
	private Boolean _failOnError = false;
	private Boolean _logError = false;
	private Boolean _verbose = false;
	private String _flashVersion = "CS4";
	
	/**
	 * Executes the ant task
	 */
	public void execute() throws BuildException
	{
		if (_verbose)
			System.out.println("FlashCommand logError=" + _logError + " failOnError=" + _failOnError);
	
		for (Movie m : _movies)
		{
			try
			{
				createTempDirectory();
				m.execute();
				cleanUp();
			}
			catch(Exception e)
			{
				System.out.println(e);
				System.exit(0);
			}
			
		}
	}
	
	/**
	 * Creates a temp directory to hold all of the logs
	 * @throws IOException
	 */
	private void createTempDirectory() throws IOException
	{
		if (_verbose)
			System.out.println("Creating the temp dir: " + TEMP_DIR);
		
		java.io.File dir = new java.io.File(TEMP_DIR);
		dir.mkdir();
		
		
	}
	
	/**
	 * Deletes the temp directory
	 * 
	 * @throws IOException
	 */
	private void cleanUp() throws IOException
	{
		if (_verbose)
			System.out.println("Deleting the temp dir: " + TEMP_DIR);
		
		java.io.File dir = new java.io.File(TEMP_DIR);
		Boolean d = dir.delete();
		
		if (_verbose)
		{
			if (d)
				System.out.println("Temp dir deleted: " + TEMP_DIR);
			else
				System.out.println("Could not delete temp dir: " + TEMP_DIR);
		}
	}
	
	
	public void add(Movie f)
	{
		f.setVerbose(_verbose);
		f.setVersion(_flashVersion);
		f.setFlashApp(_flashApp);
		_movies.add(f);
	}
	
	public void setFailonerror(Boolean x)
	{
		_failOnError = x;
	}
	
	public void setLogError(Boolean x)
	{
		_logError = x;
	}
	
	public void setFlashApp(String x)
	{
		_flashApp = x;
	}
	
	public void setVerbose(Boolean x)
	{
		_verbose = x;
	}
	
	
	public void setFlashversion(String x)
	{
		if ( !x.toUpperCase().equals( "CS6" ) &&
				!x.toUpperCase().equals( "CS5" ) &&
				!x.toUpperCase().equals( "CS4" ) &&
				!x.toUpperCase().equals( "CS3" ) )
			x = "flash";
			
		_flashVersion = x.toUpperCase();
	}
}
