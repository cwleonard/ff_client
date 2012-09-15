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
    public void testGetById() {
    	
    	Members.authenticate("test", "password");
    	Member m = Members.getByUserId("0");
    	assertEquals(0, m.getId());
    	
    }
    
    @Test
    public void setGetSelf() {
    	
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
		m.setPassword("password");
		
		String uri = Members.create(m);
		System.out.println("URI of created Member: " + uri);
		
		String id = uri.substring(uri.lastIndexOf('/') + 1);
		
		Member m2 = Members.getByUserId(id);
		assertEquals(m2.getEmail(), m.getEmail());
    	
    }
    
    @Test
    public void testUpdate() {
    	
		
    	
    }

    
    
}
