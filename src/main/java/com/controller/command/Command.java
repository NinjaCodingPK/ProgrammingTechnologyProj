/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.command;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wookie.devteam.controller.command.impl.extractor.Extractor;
import com.wookie.devteam.service.factory.ServiceFactory;

/**
 * Pattern "Command" in use.
 * @author wookie
 */
public abstract class Command {
	protected Extractor extractor = Extractor.getInstance();
	protected ServiceFactory serviceFactory;
	
	public Command setServiceFactory(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
		
		return this;
	}
	
	
    /**
     * Method executes a chosen command.
     * @param request HTTP request.
     * @param response HTTP response.
     * @return name of page or null if redirection takes place.
     * @throws ServletException
     * @throws IOException
     * @throws RuntimeException if some problem in model appears.
     */
    public abstract String execute(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, RuntimeException;
}
