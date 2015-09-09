package model;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Named
@Entity
@RequestScoped
public class User implements Serializable {
	
	@Id
	@GeneratedValue
	private long id;
	
	@Column(unique=true)
	@Size(min=5, max=15)
	private String username;
	
	@Size(min=5, max=20)
	@Pattern(regexp=".*\\d.*")
	private String password;
	
	public User() {
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getId() {
		return id;
	}
	
}
