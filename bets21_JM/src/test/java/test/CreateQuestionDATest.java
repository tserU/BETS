package test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import org.junit.jupiter.api.Test;

import configuration.ConfigXML;
import dataAccess.DataAccess;
import domain.Event;
import domain.Question;
import exceptions.QuestionAlreadyExist;
import utility.TestUtilityDataAccess;

class CreateQuestionDATest {

	static DataAccess sut = new DataAccess(ConfigXML.getInstance().getDataBaseOpenMode().equals("initialize"));;
	static TestUtilityDataAccess testDA = new TestUtilityDataAccess();

	private Event ev;

	@Test
	// sut.createQuestion: The event has one question with a queryText.
	void test1() {

		try {
			// configure the state of the system (create object in the dabatase)
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date oneDate = sdf.parse("05/10/2022");
			String eventText = "Event Text";
			String queryText = "Query Text";
			Float betMinimum = 2f;

			testDA.open();
			ev = testDA.addEventWithQuestion(eventText, oneDate, queryText, betMinimum);
			testDA.close();

			// invoke System Under Test (sut) and Assert
			assertThrows(QuestionAlreadyExist.class, () -> sut.createQuestion(ev, queryText, betMinimum));

		} catch (ParseException e) {
			fail("It should be correct: check the date format");
		}

		// Remove the created objects in the database (cascade removing)
		testDA.open();
		boolean b = testDA.removeEvent(ev);
		System.out.println("Removed event " + b);
		testDA.close();

	}

	@Test
	// sut.createQuestion: The event has NOT one question with a queryText.
	void test2() {
		try {

			// configure the state of the system (create object in the dabatase)
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date oneDate = sdf.parse("05/10/2022");
			String eventText = "Event Text";
			Float betMinimum = 2f;
			
			testDA.open();
			ev = testDA.addEventWithQuestion(eventText, oneDate, "otra", 10.0f);
			testDA.close();

			String queryText = "Query Text";
			try {
				// invoke System Under Test (sut)
				Question q = sut.createQuestion(ev, queryText, betMinimum);

				// verify the results returned
				assertNotNull(q);
				assertEquals(queryText, q.getQuesDescription());
				assertEquals(betMinimum, q.getQuesBetMinimum());
				
				// verify DB
				testDA.open();
				Vector<Event> es = testDA.getEvents(oneDate);
				testDA.close();

				assertEquals(1, es.size());
				assertEquals(2, es.get(0).getEvQuestions().size());
				assertEquals(queryText, es.get(0).getEvQuestions().get(1).getQuesDescription());
				assertEquals(betMinimum, es.get(0).getEvQuestions().get(1).getQuesBetMinimum());
			} catch (QuestionAlreadyExist e) {
				// if the program goes to this point fail
				fail();
			} finally {
				// Remove the created objects in the database (cascade removing)
				testDA.open();
				boolean b = testDA.removeEvent(ev);
				testDA.close();
				System.out.println("Finally " + b);
			}
		} catch (ParseException e) {
			fail("It should be correct: check the date format");
		}

	}
	
	@Test
	// sut.createQuestion: The event is null.
	void test3() {

			// configure the state of the system (create object in the dabatase)
			Float betMinimum = 2f;
			String queryText = "Query Text";
			try {
				// invoke System Under Test (sut)
				Question q= sut.createQuestion(null, queryText, betMinimum);

				// verify the results returned
				assertNull(q);

			} catch (QuestionAlreadyExist e) {
				// if the program goes to this point fail
				fail("The event is null. Impossible to search for a question in it");
			} 
	}
	
	@Test
	// sut.createQuestion: The queryText is null.
	void test4() {
		try {

			// configure the state of the system (create object in the dabatase)
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date oneDate = sdf.parse("05/10/2022");
			String eventText = "Event Text";
			Float betMinimum = 2f;

			testDA.open();
			ev = testDA.addEventWithQuestion(eventText, oneDate, "una", 0.0f);
			System.out.println("**************"+ev.getEvId());
			testDA.close();

			String queryText = null;
			try {
				// invoke System Under Test (sut)
				Question q= sut.createQuestion(ev, queryText, betMinimum);

				// verify the results returned
				assertNull(q);
				
				// verify DB
				Vector<Event> es = testDA.getEvents(oneDate);
				testDA.close();

				assertTrue(es.contains(ev));

			} catch (QuestionAlreadyExist e) {
				// if the program goes to this point fail
				fail("No, the question is null");
			} finally {
				// Remove the created objects in the database (cascade removing)
				testDA.open();
				boolean b = testDA.removeEvent(ev);
				System.out.println("Finally " + b);
				testDA.close();
			}
		} catch (ParseException e) {
			fail("It should be correct: check the date format");
		}

	}
	
	@Test
	// sut.createQuestion: The betMinimum is negative.
	void test5() {
		try {

			// configure the state of the system (create object in the dabatase)
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date oneDate = sdf.parse("05/10/2022");
			String eventText = "Event Text";
			Float betMinimum = -2f;
			
			testDA.open();
			ev = testDA.addEventWithQuestion(eventText, oneDate, "otra", 0.0f);
			testDA.close();

			String queryText = "Query Text";
			try {
				// invoke System Under Test (sut)
				Question q = sut.createQuestion(ev, queryText, betMinimum);

				// verify the results returned
				assertNotNull(q);
				assertEquals(queryText, q.getQuesDescription());
				assertEquals(betMinimum, q.getQuesBetMinimum(), 0);
				
				// verify DB
				testDA.open();
				Vector<Event> es = testDA.getEvents(oneDate);
				testDA.close();				assertEquals(1, es.size());
				assertEquals(eventText, es.get(0).getEvDescription());
				assertEquals(oneDate, es.get(0).getEvDate());


			} catch (QuestionAlreadyExist e) {
				// if the program goes to this point fail
				fail();
			} finally {
				// Remove the created objects in the database (cascade removing)
				testDA.open();
				boolean b = testDA.removeEvent(ev);
				testDA.close();
				System.out.println("Finally " + b);
			}
		} catch (ParseException e) {
			fail("It should be correct: check the date format");
		}

	}

}
