package controller;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Transient;

import data.UserDAO;
import model.User;

@Named
@RequestScoped
public class UserController {

	@Inject
	UserDAO userDao;
	
	@Inject
	User user;
	
	@Inject
	LoggedUser loggedUser;
	
	public String validateLogin(){
		
		if (userDao.verifyUser(user.getUsername(), user.getPassword())){
			loggedUser.setName(user.getUsername());
            return "gamecenter.jsf";
		} else {
			FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Incorrect Username and Passowrd",
                            "Please enter correct username and Password"));
			return "loginreg.jsf";
		}
	}
	
	public String logout() {
        loggedUser.setName(null);
        return "gamecenter.jsf";
    }
	
	public String registerUser(){
		if (userDao.registerUser(user.getUsername(), user.getPassword())){ 
			loggedUser.setName(user.getUsername());
			return "gamecenter.jsf";
		} else {
			FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Incorrect Username and Password",
                            "Please enter correct username and Password"));
			return "loginreg.jsf";
		}
	}
}
