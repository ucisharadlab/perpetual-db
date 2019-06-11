package edu.uci.ics.perpetual.app;

import edu.uci.ics.perpetual.CachingManager;
import edu.uci.ics.perpetual.JSQLParserException;
import edu.uci.ics.perpetual.SchemaManager;
import edu.uci.ics.perpetual.handler.QueryHandler;
import edu.uci.ics.perpetual.parser.CCJSqlParserManager;
import edu.uci.ics.perpetual.parser.ParseException;
import edu.uci.ics.perpetual.request.SqlRequest;
import edu.uci.ics.perpetual.statement.Statement;

import javax.management.Query;
import java.util.List;
import java.util.Scanner;

public class PerpetualCMDClient extends Thread {

    private final CCJSqlParserManager parser;
    private final SchemaManager schemaManager;
    private final CachingManager cachingManager;

    public PerpetualCMDClient(SchemaManager schemaManager,
                              CachingManager cachingManager) {
        parser = new CCJSqlParserManager();
        this.schemaManager = schemaManager;
        this.cachingManager = cachingManager;
    }

    @Override
    public void run() {

        Scanner scanner = new Scanner(System.in);
        while (true) {

            System.out.print("PerperualDB >>> ");
            String query = scanner.nextLine();

            if (query.equalsIgnoreCase("show schema;")) {
                System.out.println(schemaManager.getSchema());
                continue;
            }

            if (query.equalsIgnoreCase("show caching rules;")) {
                System.out.println(cachingManager.getRules());
                continue;
            }

            try {
                Statement stmt = parser.parse(query);
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
