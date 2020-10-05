package edu.brown.cs.student.CovCal;

import edu.brown.cs.student.Organization.ShiftRequest;
import freemarker.template.Configuration;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Date;
import java.util.List;

import static edu.brown.cs.student.Utilities.JsonUtil.json;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Class representing a controller for the server that initiates all requests
 * listening for and runs server on creation.
 */
public class CovCalController {
  public static final int DEFAULT_PORT = 5555;

  /**
   * Constructor for a CovCalController instance that runs the spark server
   * and creates post + get requests to listen for.
   * @param cal - CovCal instance associated with this CovCalController
   */
  public CovCalController(final CovCal cal) {
    runSparkServer(DEFAULT_PORT);
    assert cal.getProxy() != null;

    post("/pushuserinfo", (req, response) -> {
      String body = req.body();
      return cal.getProxy().userLogin(
        jsonDecoder("id", body),
        jsonDecoder("email", body),
        jsonDecoder("name", body),
        jsonDecoder("image", body));
    }, json());

    post("/user_profile/upload_image", (req, response) -> {
      String body = req.body();
      cal.getProxy().updateUserImg(
          jsonDecoder("userId", body),
          jsonDecoder("newImage", body));
      return null;
    }, json());

    post("/user_profile/upload_name", (req, response) -> {
      String body = req.body();
      if (body.length() < 1) {
        return "error";
      } else {
        cal.getProxy().updateUserName(
            jsonDecoder("userId", body),
            jsonDecoder("newName", body),
            jsonDecoder("newDesc", body));
        return null;
      }
    }, json());

    post("/get_user", (req, response) -> {
      String body = req.body();
      return cal.getProxy().getUser(body);
    }, json());

    post("/get_user_facts", (req, response) -> {
      String body = req.body();
      return cal.getProxy().getUserFunFacts(body);
    }, json());

    post("/createnewgroup", (req, res) -> {
      String body = req.body();
      return cal.getProxy().createOrganization(
              jsonDecoder("gName", body),
              jsonDecoder("gType", body),
              jsonDecoder("gOwner", body),
              null,
              jsonDecoder("gDescription", body));
    }, json());

    post("/add_employee", (req, res) -> {
      String body = req.body();
      return cal.getProxy().addEmployeeToOrg(
              jsonDecoder("gID", body),
              jsonDecoder("eID", body),
              null
      );
    }, json());

    post("/update_group", (req, res) -> {
      String body = req.body();
      return cal.getProxy().updateOrg(
              jsonDecoder("empID", body),
              Integer.parseInt(jsonDecoder("gID", body)),
              jsonDecoder("gName", body),
              jsonDecoder("gType", body),
              null,
              jsonDecoder("gDescription", body));
    }, json());

    post("/delete_org/:orgID", (req, res) -> {
      String body = req.body();
      return cal.getProxy().deleteOrg(
              Integer.parseInt(req.params(":orgID")), jsonDecoder("empID", body));
    }, json());

    post("/get_orgs_user/:empID", (req, res) -> {
      String empID = req.params(":empID");
      return cal.getProxy().getOrgsEmployeeIn(empID);
    }, json());

    post("/remove_employee/:gID", (req, res) -> {
      String body = req.body();
      return cal.getProxy().removeEmployeeFromOrg(Integer.parseInt(req.params(":gID")),
              jsonDecoder("empID", body));
    }, json());

    get("/get_org_ledger/:orgID", (req, res) -> {
      String orgID = req.params(":orgID");
      return cal.getOrg(Integer.parseInt(orgID)).getJSONLedgerData();
    }, json());

    post("/get_display_requests/:eID", (req, res) -> {
      String empID = req.params(":eID");
      return cal.getProxy().getDisplayRequestTimeOrdered(empID);
    }, json());

    post("/new_shift", (req, res) -> {
      try {
        String body = req.body();
        int orgID = Integer.parseInt(req.queryParams("orgID"));
        int start = Integer.parseInt(req.queryParams("start"));
        int end = Integer.parseInt(req.queryParams("end"));
        Date date = Date.valueOf(jsonDecoder("date", body));
        return cal.getProxy().addNewShift(jsonDecoder("empID", body), orgID,
                date, start, end);
      } catch (NumberFormatException e) {
        e.printStackTrace();
        return "Invalid input. Please try again.";
      }
    }, json());

    post("/accept_request", (req, res) -> {
      try {
        String body = req.body();
        int shiftID = Integer.parseInt(req.queryParams("orgID"));
        return cal.getProxy().acceptShift(jsonDecoder("empID", body), shiftID);
      } catch (NumberFormatException e) {
        e.printStackTrace();
        return false;
      }
    }, json());

    post("/decline_request", (req, res) -> {
      try {
        String body = req.body();
        int shiftID = Integer.parseInt(req.queryParams("orgID"));
        return cal.getProxy().declineShift(jsonDecoder("empID", body), shiftID);
      } catch (NumberFormatException e) {
        e.printStackTrace();
        return false;
      }
    }, json());

    post("/get_org", (req, res) -> {
      try {
        int orgID = Integer.parseInt(req.queryParams("orgID"));
        return cal.getProxy().getOrganization(orgID);
      } catch (NumberFormatException e) {
        e.printStackTrace();
        return false;
      }
    }, json());

    post("/release_shift", (req, res) -> {
      String empID = req.queryParams("empID");
      try {
        int shiftID = Integer.parseInt(req.queryParams("shiftID"));
        return cal.getProxy().releaseAcceptedRequest(empID, shiftID);
      } catch (NumberFormatException e) {
        e.printStackTrace();
        return false;
      }
    }, json());

    post("/get_accepted_reqs/:empID", (req, res) -> {
      String empID = req.params(":empID");
      List<ShiftRequest> lst = cal.getProxy().getAcceptedRequests(empID);
      return lst;
    }, json());

    post("/get_reqs/:empID", (req, res) -> {
      String empID = req.params(":empID");
      return cal.getProxy().getUserRequests(empID);
    }, json());

    post("/cancel_req", (req, res) -> {
      String empID = req.queryParams("empID");
      try {
        int shiftID = Integer.parseInt(req.queryParams("shiftID"));
        return cal.getProxy().cancelRequest(shiftID, empID);
      } catch (NumberFormatException e) {
        e.printStackTrace();
        return false;
      }
    }, json());
  }

  /**
   * Creates a freemarker engine.
   * @return
   *        A freemarker engine.
   */
  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
              templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  /**
   * Runs the Spark server.
   */
  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src");
    Spark.exception(Exception.class, new ExceptionPrinter());
    enableCors();
  }

  /**
   * Display an error page when an exception occurs in the server.
   *
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

  /**
   * Creates headers to enable CORS cross-domain usage.
   */
  public void enableCors() {
    Spark.before((request, response) -> {
      response.header("Access-Control-Allow-Origin", "*");
      response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
      response.header("Access-Control-Allow-Headers", "Content-Type, api_key, Authorization");
    });
  }

  /**
   * Method that grabs the parameter value that you want from the request body.
   * @param parameter - the parameter
   * @param fullCode - full body of json output
   * @return the value of the specified parameter in the json output
   */
  private String jsonDecoder(String parameter, String fullCode) {
    // We get a substring of where the parameter name ends, and then find where the value ends
    String startWithParam =
        fullCode.substring(fullCode.indexOf(parameter) + parameter.length() + 3);
    // Now we chop the substring at the end of the value
    return startWithParam.substring(0, startWithParam.indexOf("\""));
  }

}
