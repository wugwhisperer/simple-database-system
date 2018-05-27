package db61b;

import org.junit.Assert;
import org.junit.Test;

/** A collection of unit tests for Column class.
 *  @author Lily Vittayarukskul */
public class TestColumn {

    /** Return Column name. */
    @Test
    public void testGetName() {
        Table t1 = Table.readTable("testing/enrolled");
        String colName = "SID";
        Column column = new Column(colName, new Table[]{t1});
        String actual = column.getName();
        Assert.assertEquals(colName, actual);
    }

    /** Return Column row value. */
    @Test
    public void testGetFrom() {
        Table t1 = Table.readTable("testing/enrolled");
        String colName = "SID";
        Column column = new Column(colName, new Table[]{t1});
        String actual = column.getFrom(
                new Integer[]{Integer.valueOf(1),
                        Integer.valueOf(2),
                        Integer.valueOf(3)});
        String expected = "101";
        Assert.assertEquals(expected, actual);
    }

    /** Run ALL unit tests in this class. */
//    public static void main(String[] ignored) {
//        System.exit(ucb.junit.textui.runClasses(TestColumn.class));
//    }
}
