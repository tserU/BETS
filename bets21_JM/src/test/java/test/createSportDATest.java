package test;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.TypedQuery;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import configuration.ConfigXML;
import dataAccess.DataAccess;
import domain.Sport;
import utility.TestUtilityDataAccess;

class createSportDATest {
	
	static DataAccess sut = new DataAccess(ConfigXML.getInstance().getDataBaseOpenMode().equals("initialize"));;
	static TestUtilityDataAccess testDA = new TestUtilityDataAccess();



	
		@Test
	 void test1() {
		

		
	
		String spDescription = "Futbol";
	
		testDA.open();
		Sport s =testDA.createSport(spDescription);
		testDA.close();
		
		Sport obtained = sut.createSport(spDescription);
	
		Sport expected = null;

		assertEquals(expected,obtained);
		

		
		testDA.open();
		boolean b = testDA.removeSport(s);
		System.out.println("Removed sport " + b);
		System.out.println("Removed sport1 " + sut.findSport(spDescription));
		testDA.close();
		
		
	
	}
	@Test
	 void test2() {
		

			String spDescription = "Tenis";
	
	
			
			
		Sport obtained =sut.createSport(spDescription);
		
		System.out.println(obtained);
		
			testDA.open();
		Sport expected =testDA.findSport(spDescription);
	    testDA.close();
		

		assertEquals(expected,obtained);
		

		
	
		testDA.open();
		boolean b = testDA.removeSport(obtained);
		System.out.println("Removed sport " + b);

		testDA.close();
		
	
	}
}
