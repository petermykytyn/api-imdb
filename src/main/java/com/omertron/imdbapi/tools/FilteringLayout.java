/*
 *      Copyright (c) 2004-2012 YAMJ Members
 *      http://code.google.com/p/moviejukebox/people/list
 *
 *      Web: http://code.google.com/p/moviejukebox/
 *
 *      This software is licensed under a Creative Commons License
 *      See this page: http://code.google.com/p/moviejukebox/wiki/License
 *
 *      For any reuse or distribution, you must make clear to others the
 *      license terms of this work.
 */
package com.omertron.imdbapi.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Log4J Filtering routine to remove strings from the output
 *
 * @author Stuart.Boston
 *
 */
public class FilteringLayout extends PatternLayout {

    private static final String REPLACEMENT = "movieapi.com";
    private static Pattern replacementPattern = Pattern.compile("DO_NOT_MATCH");

    /**
     * Add the string to replace in the log output
     * @param replacementString
     */
    public static void addReplacementString(String replacementString) {
        replacementPattern = Pattern.compile(replacementString);
    }

    /**
     * Extend the format to remove the API_KEYS from the output
     *
     * @param event
     * @return
     */
    @Override
    public String format(LoggingEvent event) {
        if (event.getMessage() instanceof String) {
            String message = event.getRenderedMessage();

            Matcher matcher = replacementPattern.matcher(message);
            if (matcher.find()) {
                String maskedMessage = matcher.replaceAll(REPLACEMENT);

                Throwable throwable = event.getThrowableInformation() != null
                        ? event.getThrowableInformation().getThrowable() : null;

                LoggingEvent maskedEvent = new LoggingEvent(event.fqnOfCategoryClass,
                        Logger.getLogger(event.getLoggerName()), event.timeStamp,
                        event.getLevel(), maskedMessage, throwable);

                return super.format(maskedEvent);
            }
        }
        return super.format(event);
    }
}