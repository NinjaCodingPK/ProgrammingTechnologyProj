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
import com.wookie.devteam.service.TaskService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;

/**
 * Method updates Task status using information from view. Then user will be
 * redirected back to his main page.
 * 
 * @author wookie
 */
public class ChangeTaskStatusCommand extends Command {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(ChangeTaskStatusCommand.class);
    
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, RuntimeException {
              
        int personId = (int) request.getSession().getAttribute(Attributes.USER_SESSION);
        String personRole = (String) request.getSession().getAttribute(Attributes.USER_ROLE_SESSION);

        serviceFactory.getTaskService().updateTask(extractor.extractInt(request, Attributes.TASK_ID), 
                            extractor.extractInt(request, Attributes.SELECTED_STATUS),
                            personId);

        
        return serviceFactory.getPersonService().processUser(request, personId, personRole);
    }
    
}
