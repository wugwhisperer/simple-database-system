package db61b;

import org.junit.Assert;
import org.junit.Test;

/** A collection of unit tests for Condition class.
 *  @author Lily Vittayarukskul */
public class TestCondition {
    public TestCondition() {
    }

    /** Test column relation literal conditions. */
    @Test
    public void testColumnRelationLiteral() {
        Table t1 = Table.readTable("../testing/enrolled");
        String colName = "SID";
        Column column1 = new Column(colName, new Table[]{t1});
        String literal = "103";
        Condition condition = new Condition(column1, ">", literal);
        Boolean[] actual = new Boolean[t1.size()];
        for (int rowIndex = 0; rowIndex < t1.size(); rowIndex++) {
            boolean passed = condition.test(rowIndex);
            actual[rowIndex] = passed;
        }
        Boolean[] expected = new Boolean[]{
            false, false, false, false, false, false,
            false, false, false, false, true, true,
            true, true, true, true, true, true, true};
        Assert.assertArrayEquals(expected, actual);
    }

    /** Test column relation column conditions. */
    @Test
    public void testColumnRelationColumn() {
        Table t1 = Table.readTable("../testing/enrolled");
        Table t2 = Table.readTable("../testing/students");
        String colName = "SID";
        Column column1 = new Column(colName, new Table[]{t1});
        Column column2 = new Column(colName, new Table[]{t2});
        Condition condition = new Condition(column1, "=", column2);
        Boolean[] actual = new Boolean[t2.size()];
        for (int rowIndex = 0; rowIndex < t2.size(); rowIndex++) {
            boolean passed = condition.test(rowIndex);
            actual[rowIndex] = passed;
        }
        Boolean[] expected = new Boolean[]{true, false,
            false, false, false, false};
        Assert.assertArrayEquals(expected, actual);
    }

    /** Run all unit test in this class. */
    public static void main(String[] ignored) {
        System.exit(ucb.junit.textui.runClasses(TestCondition.class));
    }
}

