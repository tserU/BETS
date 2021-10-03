package test;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import dataAccess.DataAccess;
import domain.Event;

class getEventsBLMockTest {

	DataAccess dataAccess = Mockito.mock(DataAccess.class);
	Event mockedEvent = Mockito.mock(Event.class);

	BLFacade sut = new BLFacadeImplementation(dataAccess);
	
	
	private Event ev1;
	private 	Event ev2;
	private 	Event ev3;
	@Test
	void test1() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
			Date oneDate = sdf.parse("01/11/2021");
			
			
			Vector<Event> events  = new Vector<Event>();

			Mockito.doReturn(events).when(dataAccess).getEvents(oneDate);
			
			Vector<Event> obtained = sut.getEvents(oneDate);
			assertEquals(events,obtained);
		
			}catch(ParseException e) {
				fail("It should be correct: check the date format");
			}
	}
	
	@Test
	void test2() {
		
		
		try {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
		Date date2 = sdf.parse("03/11/2021");
		Date oneDate = sdf.parse("03/11/2021");
		
		String eventText3 ="Getafe-Celta";
		
		

		ev3 = new Event(eventText3,date2,null);
		Vector<Event> events  = new Vector<Event>();
		events.add(ev3);
		
		Mockito.doReturn(events).when(dataAccess).getEvents(oneDate);
		
		
		Vector<Event> obtained = sut.getEvents(oneDate);
		assertEquals(events,obtained);
		
		}catch(ParseException e) {
			fail("It should be correct: check the date format");
		}
	}
	
	@Test
	void test3() {
		
	
		try {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date1 = sdf.parse("12/11/2021");
		
		Date oneDate = sdf.parse("12/11/2021");
		String eventText1 ="Atlético-Athletic";
		String eventText2 ="Eibar-Barcelona";
		
		
		
		ev1 = new Event(eventText1, date1,null);
		ev2 = new Event(eventText2, date1,null);
		
		
		
		Vector<Event> events  = new Vector<Event>();
		events.add(ev1);
		events.add(ev2);
		
       Mockito.doReturn(events).when(dataAccess).getEvents(oneDate);
		
		
		Vector<Event> obtained = sut.getEvents(oneDate);
		assertEquals(events,obtained);
		
		
		}catch(ParseException e) {
			fail("It should be correct: check the date format");
		}
	}

}
