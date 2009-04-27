package net.jodybrewster.ant.taskdefs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import net.jodybrewster.ant.exceptions.FlashException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.sun.jndi.toolkit.url.Uri;


/**
 * Use this to add movie tags to your ant file to publish multiple targets
 * 
 * Example:
 * <flashcommand failonerror="true" logError="true" >
 *    <movie export="true" source="${fla.dir}/site.fla" output="${swf.output.dir}/site.swf" />
 *    <movie export="true" source="${fla.dir}/shared.fla" output="${swf.output.dir}/shared.swf" />
 * </flashcommand>
 * 
 * @author Jody Brewster
 *
 */
public class Movie extends Task {
	
	private String _flashApp;
	private static final String TEMP_DIR = "tmp_flashcommand";
	private static final String JSFL_FILE = "temp.jsfl";
	private String BASE_DIR = "";
	private Boolean _export = false;
	private String _source;
	private String _output;
	private Boolean _saveAndCompact = false;
	private String _version = "CS4";
	private Boolean _verbose = false;
	private Boolean _closeDocAfterComplete = false;
	private Boolean _closeFlashAfterComplete = false;
	private Boolean _publish = false;
	private Boolean _testMovie = false;
	private String _logFile = TEMP_DIR + File.separator + "error.log";
	
	/**
	 * Executes the app
	 */
	public void execute() throws BuildException
	{
		if (_verbose)
			System.out.println("Movie export=" + _export + " source=" + _source + " output" + _output);
		
		
		
		BASE_DIR = this.getLocation().toString().split("build.xml")[0];
		_logFile = BASE_DIR + _logFile;
		_logFile = _logFile.replace('\\', '/');
		
		if (_verbose)
			System.out.println("BASE_DIR=" + BASE_DIR);
		
		System.out.println("Compiling " + _source);
		
		
		Boolean offerExit = false;
		try
		{
			checkForAllNodes();
			createJsfl();
			run();
		}
		catch(Exception e)
		{
			System.out.println(e);
			offerExit = true;
		}
		finally
		{
			cleanUp();
		}
		
		if (offerExit)
			System.exit(0);
		
	}
	
	/**
	 * Creates the JSFL file
	 * 
	 * @throws IOException
	 */
	public void createJsfl() throws IOException
	{
		if (_verbose)
			System.out.println("Creating jsfl: " + BASE_DIR + TEMP_DIR + File.separator + JSFL_FILE);
			
		
		FileWriter stream = new FileWriter(BASE_DIR + TEMP_DIR + File.separator + JSFL_FILE);
		BufferedWriter out = new BufferedWriter(stream);
		out.write("var sourceFile = \"file:///" + _source + "\";");
		out.newLine();
		out.write("var outputFile = \"file:///" + _output + "\";");
		out.newLine();
		out.write("var logFile = \"file:///" + _logFile + "\";");
		out.newLine();
		out.write("var doc = fl.openDocument(sourceFile);");
		out.newLine();
		
		if (this._saveAndCompact)
		{
			out.write("doc.saveAndCompact(false);");
			out.newLine();
		}
		
		out.write("fl.outputPanel.clear();");
		out.newLine();
		
		if (this._export)
		{
			out.write("doc.exportSWF(outputFile, true);");
			out.newLine();
		}
		
		if (this._testMovie)
		{
			out.write("doc.testMovie();");
			out.newLine();
		}
		
		if (this._publish)
		{
			out.write("fl.getDocumentDOM().publish();");
			out.newLine();
		}
		
		out.write("fl.outputPanel.save(logFile, true);");
		out.newLine();
		out.write("fl.compilerErrors.save(logFile, true);");
		out.newLine();
		
		if(_closeDocAfterComplete)
		{
	        out.write("fl.getDocumentDOM().close(false);");
	        out.newLine();
		}
	    

	    if(_closeFlashAfterComplete)
	    {
	        out.write("fl.quit(false);");
	        out.newLine();
	    }
	   
		out.close();
		
	}
	
	/**
	 * Runs the executable
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws FlashException 
	 */
	public void run() throws IOException, InterruptedException, FlashException
	{
		if (_verbose)
			System.out.println("Running the script");
		 
		String[] command = new String[]{};
		if (_flashApp != null && _flashApp.length() > 0)
		{
			command = new String[]{ _flashApp, BASE_DIR + TEMP_DIR + File.separator + JSFL_FILE };
		}
		else
		{
			command = new String[]{"osascript", "-e", "tell app \"" + getFlashVersionName() + "\" to open posix file \""+ BASE_DIR + TEMP_DIR + File.separator + JSFL_FILE +"\""};
		}
        
        Process result = Runtime.getRuntime().exec(command);
        result.waitFor();
        
        while(true)
        {
        	File f = new File(_logFile);
        	if (f.isFile())
        		break;
        }
        
        String line;
        StringBuffer output = new StringBuffer();

        /* if something bad happened while trying to run the script, throw an AppleScriptException 
           letting the user know what the problem was */
        if (result.exitValue() != 0) {
           
          // read in the description of the error
           BufferedReader err = new BufferedReader(new
               InputStreamReader(result.getErrorStream()));
           while ((line = err.readLine()) != null) {
               output.append(line + "\n");
           }
           
           System.out.println(output.toString().trim());
           
          
                 
        // otherwise the script ran successfully
        } else {

           /* read in the output */
           BufferedReader out = new BufferedReader(new
               InputStreamReader(result.getInputStream()));
           while ((line = out.readLine()) != null) {
              output.append(line + "\n");
           }
        }
        
        result.destroy();
        
        
        
        // Read the error log and print it out
        File log = new File(_logFile);
        FileReader fr = new FileReader(log);
        BufferedReader br = new BufferedReader(fr);
        
        StringBuffer sb = new StringBuffer();
        String eachLine = br.readLine();
        Boolean failed = false;
        String exp = "";
        while (eachLine != null) {
          exp += eachLine + "\n";
          eachLine = br.readLine();
        }
        if (exp.trim().length() > 5)
        {
        	failed = true;
        	System.out.println("FlashException: " + exp);
        }
        
        if (failed)
        {
        	 System.out.println("BUILD FINISHED");
             throw new FlashException(exp);
        }
        
        
	}
	
	/**
	 * Cleans up everything
	 * 
	 * @throws IOException
	 */
	public void cleanUp()
	{	
		
		deleteFile(BASE_DIR + TEMP_DIR + File.separator + JSFL_FILE, "jsfl");
		deleteFile(_logFile, "log");
		
	}
	
	/**
	 * Checks for all of the nodes in this movie node
	 * @throws FlashException
	 */
	private void checkForAllNodes() throws FlashException
	{
		if (!_export && !_testMovie)
		{
			throw new FlashException("A movie node needs at least an export=\"true\" or test=\"true\" attribute");
			
		}
		
		if (_output.length() <= 0 || _source.length() <= 0)
		{
			throw new FlashException("A movie node needs a source attribute and an output attribute");
		}
	}
	
	/**
	 * simple deleting of a file
	 * 
	 * @param path of the file
	 * @param type of whatever is being deleted
	 * @throws IOException
	 */
	private void deleteFile(String path, String type) 
	{
		File f = new File(path);
		Boolean b1 = f.delete();
		if (_verbose)
		{
			if (b1)
				System.out.println("Deleted "+type+": " + path);
			else
				System.out.println("Could not delete "+type+": " + path);
		}
	}
	
	/**
	 * Returns the flash version name of the app, (os x)
	 * 
	 * @return the flash version name
	 */
	private String getFlashVersionName()
	{
		String v;
		if (_version == "CS4")
		{
			v = "Adobe Flash CS4";
		}
		else if (_version == "CS3")
		{
			v = "Adobe Flash CS3";
		}
		else
		{
			v = "flash";
		}
		return v;
	}
	
	
	//
	//
	//  SETTERS
	//
	//
	
	
	/**
	 * closes the document after completion
	 * 
	 * @param true or false
	 */
	public void setClosedocaftercomplete(Boolean x)
	{
		_closeDocAfterComplete = x;
	}
	
	/**
	 * sets the flash exe for windows
	 * @param path to the flash executable
	 */
	public void setFlashApp(String x)
	{
		_flashApp = x;
	}
	/**
	 * closes the flash application after complete
	 * 
	 * @param true or false
	 */
	public void setCloseflashaftercomplete(Boolean x)
	{
		_closeFlashAfterComplete = x;
	}
	
	/**
	 * sets whether or not to test the movie
	 * 
	 * @param x
	 */
	public void setTest(Boolean x)
	{
		_testMovie = x;
	}
	
	/**
	 * whether or not to publish the file
	 * 
	 * @param true or false
	 */
	public void setPublish(Boolean x)
	{
		_publish = x;
	}
	
	/**
	 * sets the version
	 * 
	 * @param x
	 */
	public void setVersion(String x)
	{
		_version = x;
	}
	
	public void setVerbose(Boolean x)
	{
		_verbose = x;
	}
	
	public void setExport(Boolean x)
	{
		_export = x;
	}
	
	public void setSaveAndCompact(Boolean x)
	{
		_saveAndCompact = x;
	}
	
	public void setSource(String x)
	{
		_source = x.replace('\\', '/');
	}
	
	public void setOutput(String x)
	{
		_output = x.replace('\\', '/');
	}
}
