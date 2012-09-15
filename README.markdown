SWENG500 Fantasy Football Client
--------------------------------

A Java client library for accessing SWENG500 Team 1's Fantasy Football server.

This client requires ff_common to work.


Example Member Creation
-----------------------

    	Member m = new Member();
	m.setUserName("johndoe");
	m.setEmail("test@test.net");
	m.setFirstName("John");
	m.setLastName("Doe");
	m.setHideEmail(false);
	m.setMobileNumber("717-555-1212");
	m.setPassword("password");
		
	String uri = Members.create(m);


Example Login
-------------

	Members.authenticate("test", "password");
	Member m = Members.getSelf(); // gives you the member that just authenticated

	


Authors
-------

SWENG500 Team 1