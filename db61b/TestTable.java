package db61b;

import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/** A collection of unit tests for Table class.
 *  @author Lily Vittayarukskul */
public class TestTable {
    public TestTable() {
    }

    /** Test reading blank table. */
    @Test
    public void testReadTable() {
        Table t1 = Table.readTable("testing/blank");
        String[] expected = new String[]{"First", "Second", "Third"};
        String[] actual = new String[t1.columns()];
        for (int i = 0; i < t1.columns(); ++i) {
            actual[i] = t1.getTitle(i);
        }
        Assert.assertArrayEquals(expected, actual);
    }

    /** Test reading existing table. */
    @Test
    public void testreadTable2() {
        Table t1 = Table.readTable("testing/enrolled");
        String[] expected = new String[]{"SID", "CCN", "Grade"};
        String[] actual = new String[t1.columns()];
        for (int i = 0; i < t1.columns(); ++i) {
            actual[i] = t1.getTitle(i);
        }
        Assert.assertArrayEquals(expected, actual);
        String[] vals = new String[t1.size() * t1.columns()];
        int listindex = 0;

        for (int row = 0; row < t1.size(); ++row) {
            for (int col = 0; col < t1.columns(); ++col) {
                vals[listindex] = t1.get(row, col);
                ++listindex;
            }
        }
        String[] expectedvals = ("101,21228,B,101,21105,B+,"
                + "101,21232,A-,101,21001,B,102,21231,A,102,"
                + "21105,A-,102,21229,A,102,21001,B+,103,21105,"
                + "B+,103,21005,B+,104,21228,A-,104,21229,B+,104,"
                + "21105,A-,104,21005,A-,105,21228,A,105,21001,B+,"
                + "106,21103,A,106,21001,B,106,21231,A").split(",");
        Assert.assertArrayEquals(expectedvals, vals);
    }

    /** Test printing existing table. */
    @Test
    public void testprint() {
        Table t1 = Table.readTable("testing/enrolled");
        String expected = "  101 21001 B\n"
                + "  101 21105 B+\n"
                + "  101 21228 B\n"
                + "  101 21232 A-\n"
                + "  102 21001 B+\n"
                + "  102 21105 A-\n"
                + "  102 21229 A\n"
                + "  102 21231 A\n"
                + "  103 21005 B+\n"
                + "  103 21105 B+\n"
                + "  104 21005 A-\n"
                + "  104 21105 A-\n"
                + "  104 21228 A-\n"
                + "  104 21229 B+\n"
                + "  105 21001 B+\n"
                + "  105 21228 A\n"
                + "  106 21001 B\n"
                + "  106 21103 A\n"
                + "  106 21231 A\n";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);
        t1.print();
        String actual = baos.toString();
        System.out.flush();
        System.setOut(old);
        Assert.assertEquals(expected, actual);
    }

    /** Test select for single table. */
    @Test
    public void testSelect1() {
        Table t1 = Table.readTable("testing/enrolled");
        Table t2 = Table.readTable("testing/students");
        List<String> columnNames = new ArrayList<>();
        columnNames.add("SID");
        Column column1 = new Column("CCN", new Table[]{t1});
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Condition(column1, "=", "21228"));

        String expected = "  101\n" + "  104\n" + "  105\n";
        Table actualTable = t1.select(columnNames, conditions);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);

        actualTable.print();
        String actual = baos.toString();
        System.out.flush();
        System.setOut(old);

        Assert.assertEquals(expected, actual);
    }

    /** Test select for two tables. */
    @Test
    public void testSelect2() {
        Table t1 = Table.readTable("testing/enrolled");
        Table t2 = Table.readTable("testing/students");
        List<String> columnNames = new ArrayList<>();
        columnNames.add("CCN");
        String columnCompared = "SID";
        Column column1 = new Column(columnCompared, new Table[]{t1});
        Column column2 = new Column(columnCompared, new Table[]{t1, t2});
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Condition(column1, "=", column2));
        String expected = "  21001\n"
                + "  21005\n"
                + "  21103\n"
                + "  21105\n"
                + "  21228\n"
                + "  21229\n"
                + "  21231\n"
                + "  21232\n";
        Table actualTable = t1.select(t2, columnNames, conditions);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);

        actualTable.print();
        String actual = baos.toString();
        System.out.flush();
        System.setOut(old);

        Assert.assertEquals(expected, actual);
    }

    /** Run all unit test in this class. */
//    public static void main(String[] ignored) {
//        System.exit(ucb.junit.textui.runClasses(TestTable.class));
//    }
}

