/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.command.impl.extractor;

import com.wookie.devteam.constants.ErrorMessages;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class for extraction information from Http Request.
 * 
 * @author wookie
 */
public class Extractor {
    private static final Logger logger = LogManager.getLogger(Extractor.class);
    private static final Extractor instance = new Extractor();
    
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final Pattern EMAIL_PATTERN =  Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
    															Pattern.CASE_INSENSITIVE);
    public static final Pattern PHONE_NUMBER_PATTERN =  Pattern.compile("\\d+");
    
    public static Extractor getInstance() {
        return instance;
    }
    
    /**
     * Method extracts String value from Http request.
     * @param request HttpServletRequest.
     * @param parameter parameter which should be extracted.
     * @return extracted String value.
     */
    public String extractString(HttpServletRequest request, String parameter) {
        String result = request.getParameter(parameter);
        
        if(result == null) {
            logger.error("Exception during extraction. Parameter: " + parameter + ". Value: " + result);
            throw new RuntimeException(ErrorMessages.WRONG_INPUT_DATA);
        }
        
        logger.info("extractString return: " + result);
        return result;
    }
    
    /**
     * Method extracts int value from Http request.
     * @param request HttpServletRequest.
     * @param parameter parameter which should be extracted.
     * @return extracted integer value.
     */
    public int extractInt(HttpServletRequest request, String parameter) {
    	Integer result = null;
    	try {
            result =  Integer.parseInt(request.getParameter(parameter));
            logger.info("extractInt return: " + result);
            return result;
        } catch (NumberFormatException e) {
            logger.error("Exception during extraction. Parameter: " + parameter + ". Value: " + result);
            throw new RuntimeException(ErrorMessages.WRONG_INPUT_DATA);
        }
    }
    
    /**
     * Method extracts {@link java.math.BigDecimal} values from Http request and add them to {@link java.util.Set}
     * @param request HttpServletRequest.
     * @param parameter parameter which should be extracted.
     * @return extracted integer values.
     */
    public BigDecimal extractBigDecimal(HttpServletRequest request, String parameter) {
    	try {
    		return new BigDecimal(request.getParameter(parameter));
    	} catch (Exception e) {
    		logger.error("Exception during extraction. Parameter: " + parameter);
            throw new RuntimeException(ErrorMessages.WRONG_INPUT_DATA);
    	}
    	
    }
    
    /**
     * Method extracts int values from Http request and add them to {@link java.util.Set}
     * @param request HttpServletRequest.
     * @param parameter parameter which should be extracted.
     * @return extracted integer values.
     */
    public Set<Integer> extractIntSet(HttpServletRequest request, String parameter) {
        Set<Integer> result = new HashSet<>();
        int step = 1;
        String devId;
           
        try {
            while((devId = request.getParameter(parameter + step)) != null) {
                result.add(Integer.parseInt(devId));
                step++;
            }
        } catch (NumberFormatException e) {
            logger.error("Exception during extraction. Parameter: " + parameter + ". Value: " + result);
            throw new RuntimeException(ErrorMessages.WRONG_INPUT_DATA);
        }
        
        logger.info("extractIntSet return: " + result);
        return result;
    }
    
    /**
     * Method extracts date which should be in future.
     * @param request HttpServletRequest.
     * @param parameter parameter which should be extracted.
     * @return extracted {@link java.util.Date}.
     */
    public Date extractFutureDate(HttpServletRequest request, String parameter) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        Date result = null;
        
        try {
            result = formatter.parse(request.getParameter(parameter));
            Date currentDate = new Date();

            if(!currentDate.before(result))
                throw new RuntimeException();
            
            logger.info("extractFutureDate return: " + result);
            return result;
        } catch (Exception e) {
            logger.error("Exception during extraction. Parameter " + parameter + ". Value: " + result);
            throw new RuntimeException(ErrorMessages.WRONG_FUTURE_DATE);
        } 
    }
    
    /**
     * Method extracts date which should be in past.
     * @param request HttpServletRequest.
     * @param parameter parameter which should be extracted.
     * @return extracted {@link java.util.Date}.
     */
    public Date extractPastDate(HttpServletRequest request, String parameter) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        Date result = null;
        
        try {
            result = formatter.parse(request.getParameter(parameter));
            Date currentDate = new Date();

            if(!currentDate.after(result))
                throw new RuntimeException();
            
            logger.info("extractPastDate return: " + result);
            return result;
        } catch (Exception e) {
            logger.error("Exception during extraction. Parameter " + parameter + ". Value: " + result);
            throw new RuntimeException(ErrorMessages.WRONG_PAST_DATE);
        } 
    }
    
    /**
     * Method extracts email from request. Email string must match email pattern.
     * @param request HttpServletRequest.
     * @param parameter parameter which should be extracted.
     * @return extracted email.
     */
    public String extractEmail(HttpServletRequest request, String parameter) {
        String result = request.getParameter(parameter);
        Matcher matcher = EMAIL_PATTERN.matcher(result);
		
        if(!matcher.matches()) {
            logger.error("Exception during extraction. Parameter: " + parameter + ". Value: " + result);
            throw new RuntimeException(ErrorMessages.WRONG_EMAIL);
        }
        
        logger.info("extractEmail return: " + result);
        return result;
    }
    
    /**
     * Method extracts phone number from request. Email string must match phone number pattern.
     * @param request HttpServletRequest.
     * @param parameter parameter which should be extracted.
     * @return extracted email.
     */
    public String extractPhoneNumber(HttpServletRequest request, String parameter) {
    	String result = request.getParameter(parameter);
    	Matcher matcher = PHONE_NUMBER_PATTERN.matcher(result);
    	
    	if(!matcher.matches()) {
    		logger.error("Exception during extraction. Parameter: " + parameter + ". Value: " + result);
            throw new RuntimeException(ErrorMessages.WRONG_PHONE_NUMBER);
    	}
    	
    	return result;
    }
}


