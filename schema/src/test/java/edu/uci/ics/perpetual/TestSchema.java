package edu.uci.ics.perpetual;

import edu.uci.ics.perpetual.parser.CCJSqlParserManager;
import edu.uci.ics.perpetual.parser.ParseException;
import edu.uci.ics.perpetual.statement.Statement;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestSchema {
    private static SchemaManager schema;
    private static CCJSqlParserManager parser;

    @BeforeClass
    public static void setUp(){
        schema = SchemaManager.getInstance();
        parser = new CCJSqlParserManager();
    }

    @Test
    public void test() throws ParseException, JSQLParserException {
        schema.accept(parser.parse("CREATE RAW TYPE Image(id INT, image BLOB , reqID INT );"));
//        String create = "CREATE FUNCTION images_to_objects(Images, person, face) RETURNS Objects COST 50";
        String create = "ADD FOR Image TAG (objects char)";

        Statement stmt = parser.parse(create);

        schema.accept(stmt);
    }
}
