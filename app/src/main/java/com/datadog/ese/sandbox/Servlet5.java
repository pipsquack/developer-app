package com.datadog.ese.sandbox;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/servletAPI5") 
public class Servlet5 extends HttpServlet {

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
