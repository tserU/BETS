package utility;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import domain.Event;
import domain.Sport;

public class TestUtilityDataAccess {
	protected  EntityManager  db;
	protected  EntityManagerFactory emf;

	ConfigXML  c=ConfigXML.getInstance();


	public TestUtilityDataAccess()  {		
		System.out.println("Creating TestDataAccess instance");

		open();		
	}

	
	public void open(){
		
		System.out.println("Opening TestDataAccess instance ");

		String fileName=c.getDbFilename();
		
		if (c.isDatabaseLocal()) {
			  emf = Persistence.createEntityManagerFactory("objectdb:"+fileName);
			  db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<String, String>();
			  properties.put("javax.persistence.jdbc.user", c.getUser());
			  properties.put("javax.persistence.jdbc.password", c.getPassword());

			  emf = Persistence.createEntityManagerFactory("objectdb://"+c.getDatabaseNode()+":"+c.getDatabasePort()+"/"+fileName, properties);

			  db = emf.createEntityManager();
    	   }
		
	}
	public void close(){
		db.close();
		System.out.println("DataBase closed");
	}

	public boolean removeEvent(Event ev) {
		System.out.println(">> DataAccessTest: removeEvent");
		Event e = db.find(Event.class, ev.getEvId());
		if (e!=null) {
			db.getTransaction().begin();
			db.remove(e);
			db.getTransaction().commit();
			return true;
		} else 
		return false;
    }
	
	public boolean removeSport(Sport s) {
		System.out.println(">> DataAccessTest: removeSport");
		Sport sp = db.find(Sport.class, s.getSpId());
		if (sp!=null) {
			db.getTransaction().begin();
			db.remove(sp);
			db.getTransaction().commit();
			return true;
		} else 
		return false;
    }
		
		public Event addEventWithQuestion(String desc, Date d, String question, float qty) {
			System.out.println(">> DataAccessTest: addEvent");
			Event ev=null;
				db.getTransaction().begin();
				try {
				    ev=new Event(desc,d,null);
				    ev.addEvQuestion(question,  qty);
					db.persist(ev);
					db.getTransaction().commit();
				}
				catch (Exception e){
					e.printStackTrace();
				}
				return ev;
	    }
		
		public Event addEvent(String desc,Date d) {
			System.out.println(">> DataAccessTest: addEvent");
			Event ev=null;
				db.getTransaction().begin();
				try {
				    ev=new Event(desc,d,null);
				    
					db.persist(ev);
					db.getTransaction().commit();
				}
				catch (Exception e){
					e.printStackTrace();
				}
				return ev;
	    }
		
		public Vector<Event> getEvents(Date date) {
			System.out.println(">> DataAccess: getEvents");
			Vector<Event> res = new Vector<Event>();	
			TypedQuery<Event> query = db.createQuery("SELECT ev FROM Event ev WHERE ev.evDate=?1",Event.class);   
			query.setParameter(1, date);
			List<Event> events = query.getResultList();
		 	 for (Event ev:events){
		 	   System.out.println(ev.toString());		 
			   res.add(ev);
			  }
		 	return res;
		}
		
		public boolean existQuestion(Event event, String question) {
			System.out.println(">> DataAccess: existQuestion=> event= "+event+" question= "+question);
			Event ev = db.find(Event.class, event.getEvId());
			return ev.doesEvQuestionExists(question);
			
		}
		public Sport createSport(String spDescription) {
			System.out.println(">>Creando nuevo Deporte: " + spDescription);
		
			Sport sp = new Sport(spDescription);

			if (findSport(spDescription) == null) {
				db.getTransaction().begin();
				db.persist(sp);
				db.getTransaction().commit();
				return sp;
			} else {
				return null;
			}
		}
		public Sport findSport(String spDesciption) {
			try {
				String q = "SELECT sp FROM Sport sp WHERE sp.spDescription = '" + spDesciption + "'";
				TypedQuery<Sport> query = db.createQuery(q, Sport.class);
				return query.getResultList().get(0);
			} catch (Exception e) {
				return null;
			}
		}
}

