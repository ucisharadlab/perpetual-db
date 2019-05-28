package edu.uci.ics.perpetual.app;

import edu.uci.ics.perpetual.JSQLParserException;
import edu.uci.ics.perpetual.handler.QueryHandler;
import edu.uci.ics.perpetual.parser.CCJSqlParserManager;
import edu.uci.ics.perpetual.parser.ParseException;
import edu.uci.ics.perpetual.statement.Statement;

import javax.management.Query;
import java.util.List;
import java.util.Scanner;

public class PerpetualCMDClient extends Thread {

    private final CCJSqlParserManager parser;

    public PerpetualCMDClient() {
        parser = new CCJSqlParserManager();
    }

    @Override
    public void run() {

        Scanner scanner = new Scanner(System.in);
        while (true) {

            String query = scanner.nextLine();
            try {
                Statement stmt = parser.parse(query);
                QueryHandler queryHandler = new QueryHandler(stmt);

            } catch (JSQLParserException | ParseException e) {
                System.out.println(String.format("Error parsing query, check query again"));
            }
        }

    }

    public void runStatement(String stmt) {}

    public List<String> runQuery(String query) {  return null;}


}
