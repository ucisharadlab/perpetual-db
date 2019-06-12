package edu.uci.ics.perpetual.workload.parser;

import edu.uci.ics.perpetual.JSQLParserException;
import edu.uci.ics.perpetual.parser.CCJSqlParserManager;
import edu.uci.ics.perpetual.parser.ParseException;
import edu.uci.ics.perpetual.statement.Statement;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class QueryBotWorkloadParser implements IWorkloadParser, Iterable<Statement> {

    private final CCJSqlParserManager parser;
    private final String qwFilePath;

    public QueryBotWorkloadParser(String filePath) {
        qwFilePath = filePath;
        parser = new CCJSqlParserManager();
    }

    public List<Statement> parseFileInMemory() {

        Stream<String> lineStream = null;
        try {
            lineStream = Files.lines(Paths.get(qwFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Statement> statments = new ArrayList<>();

        lineStream.forEach(line -> {
            Statement stmt = processLine(line);
            if (stmt != null) {
                statments.add(stmt);
            }
        });
        return statments;
    }

    private Statement processLine(String line) {

        CSVParser csvParser = null;
        try {
            csvParser = new CSVParser(new StringReader(line), CSVFormat.DEFAULT);
            for (CSVRecord record : csvParser) {
                return parser.parse(record.get(3).split(":")[1]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSQLParserException | ParseException e) {
//            System.out.println(String.format("Error Parsing Query %s", e.getMessage()));
        }

        return null;
    }

    @Override
    public Iterator<Statement> iterator() {
        Iterator<Statement> iterator = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(qwFilePath));
            iterator = new Iterator<Statement>() {

                private String line = null;

                @Override
                public boolean hasNext() {
                    try {
                        line = br.readLine();
                        return line != null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return false;
                }

                @Override
                public Statement next() {
                    return processLine(line);
                }
            };

        } catch (IOException e) {
            e.printStackTrace();
        }
        return iterator;
    }

    @Override
    public void forEach(Consumer<? super Statement> action) {

    }

    @Override
    public Spliterator<Statement> spliterator() {
        return null;
    }

    public static void main(String args[]) {
        QueryBotWorkloadParser queryBotWorkloadParser = new QueryBotWorkloadParser(
                "/home/peeyush/Downloads/perpetual-db/caching/src/main/resources/query-bot-5000.sample");
        queryBotWorkloadParser.parseFileInMemory();
    }


}
