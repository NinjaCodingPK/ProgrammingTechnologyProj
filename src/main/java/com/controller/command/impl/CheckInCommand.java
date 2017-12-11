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
import com.wookie.devteam.service.PersonProjectService;
import com.wookie.devteam.service.PersonService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Method updates developer's time of work using information from front-end.
 * The user will be redirected back to his main page.
 * @author wookie
 */
public class CheckInCommand extends Command {
    
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, RuntimeException {
        
        int personId = (int) request.getSession().getAttribute(Attributes.USER_SESSION);
        String personRole = (String) request.getSession().getAttribute(Attributes.USER_ROLE_SESSION);
        
        serviceFactory.getPersonProjectService().updateTime(personId,
                extractor.extractInt(request, Attributes.PROJECT_ID),
                extractor.extractInt(request, Attributes.TIME));
        
        return serviceFactory.getPersonService().processUser(request, personId, personRole);
    }
    
}
