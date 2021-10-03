package test;

import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.text.ParseException;
import org.junit.jupiter.api.Test;

import configuration.ConfigXML;
import configuration.UtilDate;
import dataAccess.DataAccess;
import domain.Event;
import domain.Sport;
import utility.TestUtilityDataAccess;

class getEventsDATest {

	static DataAccess sut = new DataAccess(ConfigXML.getInstance().getDataBaseOpenMode().equals("initialize"));;
	static TestUtilityDataAccess testDA = new TestUtilityDataAccess();

	Sport s = new Sport("Futbol");
	private Vector<Event> obtained;
	private Event ev;
	private Event ev21;
	private Event ev22;
	private Event ev23;
	private Event ev1;
	private 	Event ev2;
	private 	Event ev3;
	@Test
	void test1() {
		
		
		try {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date1 = sdf.parse("12/11/2021");
		Date date2 = sdf.parse("03/11/2021");
		Date oneDate = sdf.parse("01/11/2021");
		String eventText1 ="Atlético-Athletic";
		String eventText2 ="Eibar-Barcelona";
		String eventText3 ="Getafe-Celta";
		
		testDA.open();
		ev21 = testDA.addEvent(eventText1, date1);
		ev22 = testDA.addEvent(eventText2, date1);
		ev23 = testDA.addEvent(eventText3, date2);
		testDA.close();

		
		
		obtained =sut.getEvents(oneDate);
	
		Vector<Event> expected  = new Vector<Event>();

		assertEquals(expected,obtained);
		
		}catch(ParseException e) {
			fail("It should be correct: check the date format");
		}finally {
			// Remove the created objects in the database (cascade removing)
			testDA.open();
			boolean b = testDA.removeEvent(ev21);
			boolean b2 = testDA.removeEvent(ev22);
			boolean b3 = testDA.removeEvent(ev23);
			System.out.println("Finally " + b+b2+b3);
			testDA.close();
		}
	}
	
	@Test
	void test2() {
		
		
		try {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date1 = sdf.parse("12/11/2021");
		Date date2 = sdf.parse("03/11/2021");
		Date oneDate = sdf.parse("03/11/2021");
		String eventText1 ="Atlético-Athletic";
		String eventText2 ="Eibar-Barcelona";
		String eventText3 ="Getafe-Celta";
		
		testDA.open();
		ev1 = testDA.addEvent(eventText1, date1);
		ev2 = testDA.addEvent(eventText2, date1);
		ev3 = testDA.addEvent(eventText3, date2);
		testDA.close();

		
		
		obtained =sut.getEvents(oneDate);
	
		Vector<Event> expected  = new Vector<Event>();
		expected.add(ev3);
		
		assertEquals(expected,obtained);
		
		}catch(ParseException e) {
			fail("It should be correct: check the date format");
		}finally {
			// Remove the created objects in the database (cascade removing)
			testDA.open();
			boolean b = testDA.removeEvent(ev1);
			boolean b2 = testDA.removeEvent(ev2);
			boolean b3 = testDA.removeEvent(ev3);
			System.out.println("Finally " + b+b2+b3);
			testDA.close();
		}
	}
	
	@Test
	void test3() {
		
	
		try {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date1 = sdf.parse("12/11/2021");
		Date date2 = sdf.parse("03/11/2021");
		Date oneDate = sdf.parse("12/11/2021");
		String eventText1 ="Atlético-Athletic";
		String eventText2 ="Eibar-Barcelona";
		String eventText3 ="Getafe-Celta";
		
		testDA.open();
		ev1 = testDA.addEvent(eventText1, date1);
		ev2 = testDA.addEvent(eventText2, date1);
		ev3 = testDA.addEvent(eventText3, date2);
		testDA.close();

		
		
		obtained =sut.getEvents(oneDate);
	
		Vector<Event> expected  = new Vector<Event>();
		expected.add(ev1);
		expected.add(ev2);
		
		assertEquals(expected,obtained);
		
		}catch(ParseException e) {
			fail("It should be correct: check the date format");
		}finally {
			// Remove the created objects in the database (cascade removing)
			testDA.open();
			boolean b = testDA.removeEvent(ev1);
			boolean b2 = testDA.removeEvent(ev2);
			boolean b3 = testDA.removeEvent(ev3);
			System.out.println("Finally " + b+b2+b3);
			testDA.close();
		}
	}

}
