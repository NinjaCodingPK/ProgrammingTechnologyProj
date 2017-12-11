package com.controller.command.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.wookie.devteam.constants.Attributes;
import com.wookie.devteam.constants.ErrorMessages;
import com.wookie.devteam.controller.command.Command;
import com.wookie.devteam.entities.Person;
import com.wookie.devteam.service.PersonService;

public class BackToMainPageCommand extends Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, RuntimeException {
		
		PersonService personService = serviceFactory.getPersonService();
		
		HttpSession session = request.getSession(true);
		Integer userId = (Integer)session.getAttribute(Attributes.USER_SESSION);
		
		if(userId == null)
			throw new RuntimeException(ErrorMessages.BROKEN_RIGHTS);
		
        Person user = personService.getPerson(userId);            
            
        return personService.processUser(request, user.getId(), user.getRole().getName());
	}

}
