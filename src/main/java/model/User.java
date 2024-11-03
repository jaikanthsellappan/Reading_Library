package model;

public class User {
	private String username;
	private String password;
	private String firstName;
    private String lastName;
    private boolean isAdmin;

	public User() {
	}
	
	public User(String username, String password, String firstName, String lastName, boolean isAdmin) {
		this.username = username;
		this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isAdmin = isAdmin;
	}

	
	
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	

    public String getFirstName() { 
    	return firstName; 
    }
    
    public String getLastName() { 
    	return lastName; 
    }
    
    public boolean isAdmin() {
    	return isAdmin; 
    }

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setFirstName(String firstName) { 
		this.firstName = firstName; 
	}
    public void setLastName(String lastName) {
    	this.lastName = lastName; 
    }
    
    public void setisAdmin(Boolean admin) {
    	this.isAdmin = admin; 
    }
	
}
