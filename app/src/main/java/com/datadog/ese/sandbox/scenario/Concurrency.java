package com.datadog.ese.sandbox.scenario;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;

@WebServlet("/concurrency")
public class Concurrency extends HttpServlet {

  final static org.slf4j.Logger logger = LoggerFactory.getLogger("sandbox");

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("text/html");

    new Thread(() -> sub(this)).start();
    logger.info("Waiting...");
    try {
      synchronized (this) {
        wait();
      }
    } catch (InterruptedException e) {
      logger.error(e.getMessage(), e);
    }
    logger.info("I am up...");

    resp.getWriter().println("<h1>Hello world!</h1>");
  }

  private void sub(Object o) {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      logger.error(e.getMessage(), e);
    }
    logger.info("Notifing...");
    synchronized (o) {
      o.notify();
    }
  }

}
