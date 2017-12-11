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
import javax.servlet.http.HttpSession;

/**
 * Method performs logging out action. During logging out the information about
 * user in HttpSession deletes.
 * 
 * <p>Method redirects user to logging in page.
 * @author wookie
 */
public class LogoutCommand extends Command {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, RuntimeException {

        HttpSession session = request.getSession(false);
        
        if(session != null) {
            session.invalidate();
        }

        return WebPages.LOGIN_PAGE;
    }
    
}
