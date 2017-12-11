package com.controller.command.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;

import com.wookie.devteam.constants.Attributes;
import com.wookie.devteam.constants.WebPages;
import com.wookie.devteam.controller.Controller;
import com.wookie.devteam.controller.command.Command;
import com.wookie.devteam.controller.command.impl.extractor.Extractor;
import com.wookie.devteam.service.PersonService;

public class SubmitRegistrationCommand extends Command {
	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(SubmitRegistrationCommand.class);
    
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, RuntimeException {
		
		logger.info("Start creating new person");
		serviceFactory.getPersonService().createPerson(
				extractor.extractString(request, Attributes.USER_NAME),
				extractor.extractString(request, Attributes.USER_SURNAME), 
				extractor.extractString(request, Attributes.USER_LOGIN),
				extractor.extractEmail(request, Attributes.USER_EMAIL), 
				extractor.extractPhoneNumber(request, Attributes.USER_PHONE), 
				extractor.extractPastDate(request, Attributes.USER_BIRTHDAY), 
				extractor.extractString(request, Attributes.USER_PASSWORD)
		);
		
		return WebPages.LOGIN_PAGE;
	}

}
