/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wookie.controller;

import com.wookie.devteam.constants.Attributes;
import com.wookie.devteam.constants.Services;
import com.wookie.devteam.controller.command.Command;
import com.wookie.devteam.controller.command.CommandList;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import com.wookie.devteam.service.factory.ServiceFactory;

/**
 *
 * @author wookie
 */
@WebServlet("/Controller/*")
public class Controller extends HttpServlet  {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(Controller.class);
    
    private CommandList commandList;
    
    @Override
    public void init() throws ServletException {
        logger.info("Servlet initialization started");   
        
        commandList = new CommandList((ServiceFactory) getServletContext().getAttribute(Services.FACTORY));
    }
    
    private void processCommand(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String commandName = request.getParameter(Attributes.COMMAND);

        try {
            logger.info("Start handling command " + commandName);
        
            Command command = commandList.getList().get(commandName);
            String redirect = command.execute(request, response);
        
            request.getRequestDispatcher(redirect).forward(request, response);
        } catch (RuntimeException e) {
        	Object obj = request.getSession().getAttribute(Attributes.USER_SESSION);
        	int userId = 0;
        	if(obj != null)
        		userId = (int) obj; 
            logger.error("Exception. UserId: " + userId + ". Exception: " + e + ". Message:" + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processCommand(request, response);
    }
}
