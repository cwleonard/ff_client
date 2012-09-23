package edu.psu.sweng.ff.client;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.psu.sweng.ff.common.Member;

/**
 * Unit test for Members
 */
public class MembersTest
{
    @Test
    public void testGetByUserName() {
    	
    	Members.authenticate("test", "password");
    	Member m = Members.getByUserName("test");
    	assertEquals("test", m.getUserName());
    	
    }
    
    @Test
    public void testUpdateMember() {
    	
    	Member m = new Member();
		m.setUserName("johndoe");
		m.setEmail("test@test.net");
		m.setFirstName("John");
		m.setLastName("Doe");
		m.setHideEmail(false);
		m.setMobileNumber("717-555-1212");
		m.setPassword("test_password");
		
		m = Members.create(m);
		m.setEmail("new@email.com");
    	Members.authenticate("johndoe", "test_password");

    	// must re-authenticate after updating
		Members.update(m);
    	Members.authenticate("johndoe", "test_password");
    	
		
		Member m2 = Members.getByUserId(String.valueOf(m.getId()));
		assertEquals("new@email.com", m2.getEmail());
		
    	
    }
    
    @Test
    public void testGetById() {
    	
    	Members.authenticate("test", "password");
    	Member m = Members.getByUserId("1");
    	assertEquals(1, m.getId());
    	
    }
    
    @Test
    public void testGetSelf() {
    	
    	Members.authenticate("test", "password");
    	Member m = Members.getTokenOwner();
    	assertEquals("test", m.getUserName());
    	
    }
    
    @Test
    public void testCreate() {
    	
    	Member m = new Member();
		m.setUserName("johndoe");
		m.setEmail("test@test.net");
		m.setFirstName("John");
		m.setLastName("Doe");
		m.setHideEmail(false);
		m.setMobileNumber("717-555-1212");
		m.setPassword("test_password");
		
		Member c = Members.create(m);
		
    	Members.authenticate("johndoe", "test_password");

		Member m2 = Members.getByUserId(String.valueOf(c.getId()));
		assertEquals(m2.getEmail(), m.getEmail());
    	
    }
    
    
}
