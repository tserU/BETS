package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import businessLogic.BLFacadeImplementation;
import configuration.ConfigXML;
import dataAccess.DataAccess;
import domain.Event;
import domain.Question;
import exceptions.EventFinished;
import exceptions.QuestionAlreadyExist;
import utility.TestUtilityFacadeImplementation;

class CreateQuestionBLTest {
	private DataAccess da = new DataAccess(ConfigXML.getInstance().getDataBaseOpenMode().equals("initialize"));

	private  BLFacadeImplementation sut= new BLFacadeImplementation(da);
	private  TestUtilityFacadeImplementation testBL= new TestUtilityFacadeImplementation();

	private Event ev;


	@Test
	// sut.createQuestion: The event has one question with a queryText.
	void test1() {
		try {
			// configure the state of the system (create object in the dabatase)
			String eventText = "Event Text";
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date oneDate = sdf.parse("05/10/2022");
			String queryText = "Query Text";
			Float betMinimum = 2f;

			ev = testBL.addEventWithQuestion(eventText, oneDate, queryText, betMinimum);

			// invoke System Under Test (sut)
			assertThrows(QuestionAlreadyExist.class, () -> sut.createQuestion(ev, queryText, betMinimum));

			// Remove the created objects in the database (cascade removing)
			boolean b = testBL.removeEvent(ev);
			System.out.println("Finally " + b);
		} catch (ParseException e) {
			fail("It should be correct: check the date format");
		}
	}

	@Test
	// sut.createQuestion: The event has NOT one question with a queryText.
	void test2() {
		try {
			// configure the state of the system (create object in the dabatase)
			String eventText = "Event Text";
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date oneDate = sdf.parse("05/10/2022");

			ev = testBL.addEventWithQuestion(eventText, oneDate, "One", 3f);

			// invoke System Under Test (sut)
			String queryText = "Query Text";
			Float betMinimum = 2f;
			try {
				Question q = sut.createQuestion(ev, queryText, betMinimum);

				// verify the results
				assertNotNull(q);
				assertEquals(queryText, q.getQuesDescription());
				assertEquals(betMinimum, q.getQuesBetMinimum());
				
				// verify DB
				Vector<Event> es = testBL.getEvents(oneDate);

				assertEquals(1, es.size());
				assertEquals(2, es.get(0).getEvQuestions().size());
				assertEquals(queryText, es.get(0).getEvQuestions().get(1).getQuesDescription());
				assertEquals(betMinimum, es.get(0).getEvQuestions().get(1).getQuesBetMinimum());


				// Remove the created objects in the database (cascade removing)
				boolean b = testBL.removeEvent(ev);
				System.out.println("Finally " + b);

			} catch (QuestionAlreadyExist e) {
				// if the program goes to this point fail
				fail("The event does not have queryText");
			} catch (EventFinished e) {
				// if the program goes to this point fail
				fail("The event date is later than the current date");
			} 
		} catch (ParseException e) {
			fail("It should be correct: check the date format");
		}

	}

}

