package net.jodybrewster.ant.taskdefs;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class FlashCommandTest {
	
	private FlashCommand command;
	
	@Before
	public void setup() {
		command = new FlashCommand();
	}
	
	@Test
	public void testSetFlashVersionCS6() {
		command.setFlashversion("CS6");
		assertEquals("CS6", command.getFlashVersion());
	}
	
	@Test
	public void testSetFlashVersionCS6PropigateToMovie() {
		command.setFlashversion("CS6");
		Movie movie = new Movie();
		command.add(movie);
		assertEquals("Adobe Flash CS6", movie.getFlashVersionName());
	}
	
	@Test
	public void testSetFlashVersionCS5() {
		command.setFlashversion("CS5");
		assertEquals("CS5", command.getFlashVersion());
	}
	
	@Test
	public void testSetFlashVersionCS5PropigateToMovie() {
		command.setFlashversion("CS5");
		Movie movie = new Movie();
		command.add(movie);
		assertEquals("Adobe Flash CS5", movie.getFlashVersionName());
	}
	
	@Test
	public void testSetFlashVersionCS3() {
		command.setFlashversion("CS3");
		assertEquals("CS3", command.getFlashVersion());
	}
	
	@Test
	public void testSetFlashVersionCS3PropigateToMovie() {
		command.setFlashversion("CS3");
		Movie movie = new Movie();
		command.add(movie);
		assertEquals("Adobe Flash CS3", movie.getFlashVersionName());
	}
	
	@Test
	public void testSetFlashVersionCS4() {
		command.setFlashversion("CS4");
		assertEquals("CS4", command.getFlashVersion());
	}
	
	@Test
	public void testSetFlashVersionCS4PropigateToMovie() {
		command.setFlashversion("CS4");
		Movie movie = new Movie();
		command.add(movie);
		assertEquals("Adobe Flash CS4", movie.getFlashVersionName());
	}
}
