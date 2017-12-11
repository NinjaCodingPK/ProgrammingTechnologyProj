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
import com.wookie.devteam.service.PersonService;
import com.wookie.devteam.service.ProjectQualificationService;
import com.wookie.devteam.service.TaskService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Method redirects user to the web-page for setting up new project.  
 * @author wookie
 */
public class ProcessProjectCommand extends Command {
        
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, RuntimeException {
    
        int projectId = extractor.extractInt(request, Attributes.PROJECT_ID);
        
        request.setAttribute(Attributes.TASK_LIST, 
        			serviceFactory.getTaskService().getByProject(projectId));
        request.setAttribute(Attributes.PROJECT_QUALIFICATION_LIST, 
        			serviceFactory.getProjectQualificationService()
        				.getProjectQualificationFullList(projectId));
        request.setAttribute(Attributes.DEV_LIST, serviceFactory.getPersonService().fetchFreeDevs());
        request.setAttribute(Attributes.PROJECT_ID, projectId);
        
        return WebPages.PROJECT_PROCESS_PAGE;
    }
    
}
