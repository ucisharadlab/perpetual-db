package edu.uci.ics.perpetual.language;

import edu.uci.ics.perpetual.JSQLParserException;
import edu.uci.ics.perpetual.parser.CCJSqlParserManager;
import edu.uci.ics.perpetual.parser.ParseException;
import edu.uci.ics.perpetual.statement.Statement;
import edu.uci.ics.perpetual.statement.select.Select;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestSelect {

    private static CCJSqlParserManager parser;

    @BeforeClass
    public static void setUp(){
        parser = new CCJSqlParserManager();
    }

    @Test
    public void testAddAcquisitionFunction() throws ParseException, JSQLParserException {

        String create = "SELECT image FROM Images gt WHERE gt.gender='male' AND gt.expression='Smile'";
        Statement stmt = parser.parse(create);
        Assert.assertTrue(stmt instanceof Select);

        create = "SELECT image FROM Images gt WHERE gt.gender='male' AND gt.expression='Smile'\n" +
                "WITHIN 50";
        stmt = parser.parse(create);
        Assert.assertTrue(stmt instanceof Select);

        create = "SELECT image FROM Images gt WHERE gt.gender='male' AND gt.expression='Smile'\n" +
                "EPOCH 10";
        stmt = parser.parse(create);
        Assert.assertTrue(stmt instanceof Select);

    }

}
