package edu.uci.ics.perpetual.language;

import edu.uci.ics.perpetual.JSQLParserException;
import edu.uci.ics.perpetual.parser.CCJSqlParserManager;
import edu.uci.ics.perpetual.parser.ParseException;
import edu.uci.ics.perpetual.statement.Statement;
import edu.uci.ics.perpetual.statement.add.AddAcquisitionFunction;
import edu.uci.ics.perpetual.statement.add.AddDataSource;
import edu.uci.ics.perpetual.statement.add.AddRequest;
import edu.uci.ics.perpetual.statement.add.AddTag;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestAdd {

    private static CCJSqlParserManager parser;

    @BeforeClass
    public static void setUp(){
        parser = new CCJSqlParserManager();
    }

    @Test
    public void testAddAcquisitionFunction() throws ParseException, JSQLParserException {

            String create = "ADD FOR CameraFeed ACQUISITION FUNCTION('CameraSource', '.../func.jar')";
        Statement stmt = parser.parse(create);


        Assert.assertTrue(stmt instanceof AddAcquisitionFunction);

    }

    @Test
    public void testAddDataSource() throws ParseException, JSQLParserException {

        String create = "ADD FOR CameraFeed DATASOURCE(1, 'Hall Camera', 'CameraSource', '{ip:\"127.1.1.1\", port:1111}')";
        Statement stmt = parser.parse(create);


        Assert.assertTrue(stmt instanceof AddDataSource);

    }

    @Test
    public void testAddRequest() throws ParseException, JSQLParserException {

        String create = "ADD Request(1, 1, '06/13/2019 11:19:00', '06/13/2019 11:21:00', 20, 'pull')";
        Statement stmt = parser.parse(create);


        Assert.assertTrue(stmt instanceof AddRequest);

    }

    @Test
    public void testAddTag() throws ParseException, JSQLParserException {

        String create = "ADD FOR Image TAG (objects char)";
        Statement statement = parser.parse(create);


        Assert.assertTrue(statement instanceof AddTag);
    }

}
