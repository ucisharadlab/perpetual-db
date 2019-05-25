package edu.uci.ics.perpetual.language;

import edu.uci.ics.perpetual.JSQLParserException;
import edu.uci.ics.perpetual.parser.CCJSqlParserManager;
import edu.uci.ics.perpetual.parser.ParseException;
import edu.uci.ics.perpetual.statement.Statement;
import edu.uci.ics.perpetual.statement.create.type.CreateDataSourceType;
import edu.uci.ics.perpetual.statement.create.type.CreateFunction;
import edu.uci.ics.perpetual.statement.create.type.CreateMetadataType;
import edu.uci.ics.perpetual.statement.create.type.CreateRawType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestCreate {

    private static CCJSqlParserManager parser;

    @BeforeClass
    public static void setUp(){
        parser = new CCJSqlParserManager();
    }

    @Test
    public void testCreateMetaDataType() throws ParseException, JSQLParserException {

        String create = "CREATE METADATA TYPE Users(id INT, name CHAR, age INT);";
        Statement stmt = parser.parse(create);

        Assert.assertTrue(stmt instanceof CreateMetadataType);

    }

    @Test
    public void testCreateRawType() throws ParseException, JSQLParserException {

        String create = "CREATE RAW TYPE Image(id INT, image BLOB , reqID INT );";
        Statement stmt = parser.parse(create);

        Assert.assertTrue(stmt instanceof CreateRawType);

    }

    @Test
    public void testCreateFunctionType() throws ParseException, JSQLParserException {

        String create = "CREATE FUNCTION images_to_objects(Images) RETURNS Objects COST 50";
        Statement stmt = parser.parse(create);

        Assert.assertTrue(stmt instanceof CreateFunction);

        create = "CREATE FUNCTION images_to_objects(Images, person) RETURNS Objects COST 50";
        stmt = parser.parse(create);

        Assert.assertTrue(stmt instanceof CreateFunction);

    }

    @Test
    public void testCreateDataSourceType() throws ParseException, JSQLParserException {

        String create = "CREATE DATASOURCE TYPE CameraFeed('{ip:\"1.1.1.1\", port:2000}') GENERATES Image";
        Statement statement = parser.parse(create);


        Assert.assertTrue(statement instanceof CreateDataSourceType);
    }

}
