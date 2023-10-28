package com.datadog.ese.sandbox;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.apache.logging.log4j.LogManager;
import org.slf4j.LoggerFactory;

@WebFilter("/*")
public class SimpleFilter implements Filter {

    final static org.slf4j.Logger slf4jLogger = LoggerFactory.getLogger("sandbox");
    final static org.apache.logging.log4j.Logger log4j2Logger = LogManager.getLogger("sandbox");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        slf4jLogger.info("Logging with SLF4J from Filter - before calling doFilter");
        chain.doFilter(request, response);
        log4j2Logger.info("Logging with log4j2 - after calling doFilter");
    }

}
