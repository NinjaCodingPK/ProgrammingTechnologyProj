/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.command.impl;

import com.wookie.devteam.constants.Attributes;
import com.wookie.devteam.controller.Controller;
import com.wookie.devteam.controller.command.Command;
import com.wookie.devteam.controller.command.impl.extractor.Extractor;
import com.wookie.devteam.entities.Person;
import com.wookie.devteam.service.PersonService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Method performs logging in. The information about user is saved in HttpSession.
 * 
 * <p>Method redirects user to his main page.
 * @author wookie
 */
public class LoginCommand extends Command {
    
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, RuntimeException {
        
    	PersonService personService = serviceFactory.getPersonService();
    	
        Person user = personService.getPerson(extractor.extractString(request, Attributes.USER_LOGIN));            
        personService.checkPassword(user, extractor.extractString(request, Attributes.USER_PASSWORD));
        
        HttpSession session = request.getSession(true);
        session.setAttribute(Attributes.USER_SESSION, user.getId());
        session.setAttribute(Attributes.USER_ROLE_SESSION, user.getRole().getName());
            
        return personService.processUser(request, user.getId(), user.getRole().getName());
    }
    
}
