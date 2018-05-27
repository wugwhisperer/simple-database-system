package db61b;

import org.junit.Assert;
import org.junit.Test;

/** A collection of unit tests for Database class.
 *  @author Lily Vittayarukskul */
public class TestDatabase {
    public TestDatabase() {
    }

    /** Test Table retrieving aspect of database. */
    @Test
    public void testGetTable() {
        Table t1 = Table.readTable("testing/enrolled");
        Database database = new Database();
        database.put("enrolled", t1);
        Table expected = t1;
        Table actual = database.get("enrolled");
        Assert.assertEquals(expected, actual);
    }

    /** Run all unit test in this class. */
//    public static void main(String[] ignored) {
//        System.exit(ucb.junit.textui.runClasses(TestDatabase.class));
//    }
}

