/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.command.impl;

import com.wookie.devteam.constants.WebPages;
import com.wookie.devteam.controller.command.Command;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Method redirects user to the page for creating new test.
 * @author wookie
 */
public class CreateProjectCommand extends Command {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, RuntimeException {
        
        return WebPages.CREATE_PROJECT_PAGE;
    }
    
}
