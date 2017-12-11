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
import com.wookie.devteam.entities.Project;
import com.wookie.devteam.service.ProjectQualificationService;
import com.wookie.devteam.service.ProjectService;
import com.wookie.devteam.service.TaskService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Method creates new Project in database and redirects user to the web-page for 
 * filling this projects with some information.
 * 
 * @author wookie
 */
public class FillProjectCommand extends Command{
      
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, RuntimeException {
      
        int personId = (int) request.getSession().getAttribute(Attributes.USER_SESSION);
        
        Project p = serviceFactory.getProjectService()
        				.create(extractor.extractString(request, Attributes.PROJECT_NAME),
        						extractor.extractFutureDate(request, Attributes.PROJECT_DEADLINE),
                                personId);
        
        request.setAttribute(Attributes.TASK_LIST, 
        			serviceFactory.getTaskService().getByProject(p.getId()));
        request.setAttribute(Attributes.PROJECT_QUALIFICATION_LIST, 
        			serviceFactory.getProjectQualificationService().getProjectQualification(p.getId()));
        request.setAttribute(Attributes.PROJECT_ID, p.getId());
        
        return WebPages.FILL_PROJECT_PAGE;
    }
    
}
