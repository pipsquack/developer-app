package com.datadog.ese.sandbox;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.slf4j.LoggerFactory;

@WebServlet("/servletAPI4") 
public class Servlet4 extends HttpServlet {

  final static org.slf4j.Logger slf4jLogger = LoggerFactory.getLogger("sandbox");
  final static org.apache.logging.log4j.Logger log4j2Logger = LogManager.getLogger("sandbox");

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("text/html");
    slf4jLogger.info("Logging with SLF4J");
    log4j2Logger.info("Logging with log4j2");
    resp.getWriter().println("<h1>Hello world!</h1>");
  }

}
