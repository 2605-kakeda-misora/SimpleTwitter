package chapter6.beans;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String account;

    public String getAccount() {
		return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    private String name;

    public String getName() {
		return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String email;

    public String getEmail() {
 		return email;
     }

     public void setEmail(String email) {
         this.email = email;
     }

    private String password;

    public String getPassword() {
 		return password;
     }

     public void setPassword(String password) {
         this.password = password;
     }

    private String description;

    public String getDescription() {
 		return description;
     }

     public void setDescription(String description) {
         this.description = description;
     }

    private Date createdDate;

    public Date getCreatedDate() {
    	return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
    	this.createdDate = createdDate;
    }

    private Date updatedDate;

    public Date getUpdatedDate() {
    	return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
    	this.updatedDate = updatedDate;
    }
}