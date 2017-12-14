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
import com.wookie.devteam.controller.command.impl.extractor.Extractor;
import com.wookie.devteam.entities.Person;
import com.wookie.devteam.service.PersonProjectService;
import com.wookie.devteam.service.PersonService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Method set current Project's status as "done".
 * 
 * <p>Method redirects user to the web-page for showing all projects connected
 * with current user.
 * @author wookie
 */
public class SetProjectDoneCommand extends Command {
    
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, RuntimeException {
               
        int personId = (int) request.getSession().getAttribute(Attributes.USER_SESSION);
        
        serviceFactory.getPersonProjectService()
        	.deleteProjectsDevs(extractor.extractInt(request, Attributes.PROJECT_ID));
        
        request.setAttribute(Attributes.PROJECT_LIST, 
        		serviceFactory.getPersonService().getProjectsByPerson(personId));
        
        return WebPages.MANAGER_PROJECTS_PAGE;        
    }
    
}
