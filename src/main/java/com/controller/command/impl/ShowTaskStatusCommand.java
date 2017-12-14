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
import com.wookie.devteam.service.StatusService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Method redirects user to the web page for changing status of a task.
 * @author wookie
 */
public class ShowTaskStatusCommand extends Command {
    
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, RuntimeException {

        request.setAttribute(Attributes.TASK_ID, extractor.extractString(request, Attributes.TASK_ID));
        request.setAttribute(Attributes.STATUS_LIST, serviceFactory.getStatusService().getAll());
        return WebPages.CHANGE_TASK_STATUS_PAGE;
    }
    
}
