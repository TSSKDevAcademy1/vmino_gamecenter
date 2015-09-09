/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package games.guessnumber;

import java.io.Serializable;
import java.util.Random;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import controller.LoggedUser;
import data.ScoreDAO;

@Named
@SessionScoped
public class UserNumberBean implements Serializable {
	
	@Inject
	LoggedUser user;

	@Inject
	ScoreDAO scoreDAO;

	private static final long serialVersionUID = 5443351151396868724L;
	Integer randomInt = null;
	private Integer userNumber = null;
	String response = null;
	private int maximum = 100;
	private int minimum = 0;
	private int tryCount = 0;
	
	private final String GAME_NAME = "Guess Number";

	public UserNumberBean() {
		Random randomGR = new Random();
		randomInt = new Integer(randomGR.nextInt(maximum + 1));
		// Print number to server log
		System.out.println("Duke's number: " + randomInt);
	}

	public void setUserNumber(Integer user_number) {
		userNumber = user_number;
	}

	public Integer getUserNumber() {
		return userNumber;
	}

	public String getResponse() {
		tryCount++;
		if ((userNumber == null) || (userNumber.compareTo(randomInt) != 0)) {
			if (userNumber.compareTo(randomInt) > 0) {
				return "Sorry, " + userNumber + " is bigger.";
			} else {
				return "Sorry, " + userNumber + " is smaller.";
			}
		} else {
			setNewNumber();
			if (user.isLogged()) {
				scoreDAO.addScore(GAME_NAME, user.getName(), (double)tryCount);
			}
			return "Yay! You got it! (Count of tries: "+tryCount+")";
		}
	}

	public int getMaximum() {
		return (this.maximum);
	}

	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}

	public int getMinimum() {
		return (this.minimum);
	}

	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}

	public void setNewNumber() {
		Random randomGR = new Random();
		randomInt = new Integer(randomGR.nextInt(maximum + 1));
	}
}
