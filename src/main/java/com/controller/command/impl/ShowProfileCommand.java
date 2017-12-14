/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.command.impl;

import com.wookie.devteam.constants.Attributes;
import com.wookie.devteam.constants.WebPages;
import com.wookie.devteam.controller.Controller;
import com.wookie.devteam.controller.command.Command;
import com.wookie.devteam.service.PersonService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Method redirects user to the user's profile page.
 * @author wookie
 */
public class ShowProfileCommand extends Command {
    
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, RuntimeException {

        int personId = (int) request.getSession().getAttribute(Attributes.USER_SESSION);
        request.setAttribute(Attributes.USER, serviceFactory.getPersonService().getPerson(personId));
        
        return WebPages.PROFILE_PAGE;
    }
    
}
