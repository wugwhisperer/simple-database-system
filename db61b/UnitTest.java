package db61b;

/** The suite of all JUnit tests for the qirkat package.
 *  @author P. N. Hilfinger and Lily Vittayarukskul
 */
public class UnitTest {

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        System.exit(ucb.junit.textui.runClasses(
                TestColumn.class, TestCondition.class,
                TestDatabase.class, TestTable.class));
    }

}
