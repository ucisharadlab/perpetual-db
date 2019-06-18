package edu.uci.ics.perpetual.app;

import edu.uci.ics.perpetual.CachingManager;
import edu.uci.ics.perpetual.Commands;
import edu.uci.ics.perpetual.JSQLParserException;
import edu.uci.ics.perpetual.SchemaManager;
import edu.uci.ics.perpetual.acquisition.AcquisitionManager;
import edu.uci.ics.perpetual.acquisition.requestmanagement.AcquisitionRequestManager;
import edu.uci.ics.perpetual.handler.QueryHandler;
import edu.uci.ics.perpetual.parser.CCJSqlParserManager;
import edu.uci.ics.perpetual.parser.ParseException;
import edu.uci.ics.perpetual.request.SqlRequest;
import edu.uci.ics.perpetual.statement.Statement;
import edu.uci.ics.perpetual.statement.add.AddRequest;

import java.util.List;
import java.util.Scanner;

public class PerpetualCMDClient extends Thread {

    private final CCJSqlParserManager parser;
    private final SchemaManager schemaManager;
    private final CachingManager cachingManager;
    private final AcquisitionRequestManager acquisitionRequestManager;

    public PerpetualCMDClient(SchemaManager schemaManager, CachingManager cachingManager,
                              AcquisitionManager acquisitionManager) {
        parser = new CCJSqlParserManager();
        this.schemaManager = schemaManager;
        this.cachingManager = cachingManager;
        this.acquisitionRequestManager = acquisitionManager.getRequestManager();
    }

    @Override
    public void run() {

        Scanner scanner = new Scanner(System.in);
        while (true) {

            System.out.print("PerperualDB >>> ");
            String query = scanner.nextLine();

            System.out.println();
            // Handle Special Commands
            if (Commands.SHOW_SCHEMA.equalsIgnoreCase(query)) {
                System.out.println(schemaManager.getSchema());
                continue;
            }

            if (Commands.SHOW_C_RULE.equalsIgnoreCase(query)) {
                System.out.println(cachingManager.getRules());
                continue;
            }

            if (Commands.SHOW_REQ.equalsIgnoreCase(query)) {
                System.out.println(acquisitionRequestManager);
                continue;
            }

            if (Commands.EXIT.equalsIgnoreCase(query)) {
                System.exit(0);
            }
            // Handle Other Commands
            try {
                Statement stmt = parser.parse(query);

                // Handle Add Request
                if (stmt instanceof AddRequest) {
                    acquisitionRequestManager.addRequest(stmt);
                    System.out.println("Request Added\n");
                    continue;
                }

                schemaManager.accept(new SqlRequest(stmt));
                System.out.println("Command Successful\n");
                QueryHandler queryHandler = new QueryHandler(stmt);

            } catch (JSQLParserException | ParseException e) {
                System.out.println(String.format("Error parsing query, check query again\n"));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public void runStatement(String stmt) {}

    public List<String> runQuery(String query) {  return null;}


}
