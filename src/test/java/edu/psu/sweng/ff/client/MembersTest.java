package edu.psu.sweng.ff.client;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sun.jersey.spi.service.ServiceFinder;

import edu.psu.sweng.ff.common.Member;

/**
 * Unit test for Members
 */
public class MembersTest
{
	
	public MembersTest() {
		//ServiceFinder.setIteratorProvider(new AndroidServiceIteratorProvider());
	}
	
    @Test
    public void testGetByUserName() {
    	
    	Members.authenticate("test", "password");
    	Member m = Members.getByUserName("test");
    	assertEquals("test", m.getUserName());
    	
    }
    
    @Test
    public void testUpdateMember() {
    	
    	String newUserName = this.generateRandomUserName();
    	
    	Member m = new Member();
		m.setUserName(newUserName);
		m.setEmail("test@test.net");
		m.setFirstName("John");
		m.setLastName("Doe");
		m.setHideEmail(false);
		m.setMobileNumber("717-555-1212");
		m.setPassword("test_password");
		
		m = Members.create(m);
		m.setEmail("new@email.com");
    	Members.authenticate(newUserName, "test_password");

    	// must re-authenticate after updating
		Members.update(m);
    	Members.authenticate(newUserName, "test_password");
    	
		
		Member m2 = Members.getByUserName(m.getUserName());
		assertEquals("new@email.com", m2.getEmail());
    	
    }
    
    @Test
    public void testGetSelf() {
    	
    	Members.authenticate("test", "password");
    	Member m = Members.getTokenOwner();
    	assertEquals("test", m.getUserName());
    	
    }
    
    @Test
    public void testCreate() {
    	
    	String newUserName = this.generateRandomUserName();
    	
    	Member m = new Member();
		m.setUserName(newUserName);
		m.setEmail("test@test.net");
		m.setFirstName("John");
		m.setLastName("Doe");
		m.setHideEmail(false);
		m.setMobileNumber("717-555-1212");
		m.setPassword("test_password");
		
		Member c = Members.create(m);
		assertNotNull(c.getAccessToken());
		
    	Members.authenticate(newUserName, "test_password");

		Member m2 = Members.getByUserName(c.getUserName());
		assertEquals(m2.getEmail(), m.getEmail());
    	
    }
    
    private String generateRandomUserName() {
    	
    	String un = "johndoe";
    	int r = 1000 + (int)(Math.random() * ((999999 - 1000) + 1));
    	return un + r;
    	
    }
}
