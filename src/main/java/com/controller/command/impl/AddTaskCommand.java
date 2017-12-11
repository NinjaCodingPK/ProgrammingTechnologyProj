/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.command.impl;

import com.wookie.devteam.constants.Attributes;
import com.wookie.devteam.constants.WebPages;
import com.wookie.devteam.controller.command.Command;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Method redirects user to the web-page for adding Task to the 
 * Project.
 * 
 * @author wookie
 */
public class AddTaskCommand extends Command{

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, RuntimeException {
        
        String projectId = request.getParameter(Attributes.PROJECT_ID);
        request.setAttribute(Attributes.PROJECT_ID, projectId);
        
        return WebPages.ADD_TASK_PAGE;
    }
    
}
