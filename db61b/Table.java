package db61b;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static db61b.Utils.*;

/** A single table in a database.
 *  @author P. N. Hilfinger and Lily Vittayarukskul
 */
public class Table {
    /** A new Table whose columns are given by COLUMNTITLES, which may
     *  not contain duplicate names. */
    Table(String[] columnTitles) {

        if (columnTitles.length == 0) {
            throw error("table must have at least one column");
        }
        _size = 0;
        _rowSize = columnTitles.length;

        for (int i = columnTitles.length - 1; i >= 1; i -= 1) {
            for (int j = i - 1; j >= 0; j -= 1) {
                if (columnTitles[i].equals(columnTitles[j])) {
                    throw error("duplicate column name: %s",
                                columnTitles[i]);
                }
            }
        }
        _columns = new ValueList[columnTitles.length];
        for (int colindex = 0; colindex < _rowSize; colindex++) {
            ValueList column = new ValueList();
            _columns[colindex] = column;
        }
        _titles = columnTitles;
    }

    /** A new Table whose columns are give by COLUMNTITLES. */
    Table(List<String> columnTitles) {
        this(columnTitles.toArray(new String[columnTitles.size()]));
    }

    /** Return the number of columns in this table. */
    public int columns() {
        return _rowSize;
    }

    /** Return the title of the Kth column.  Requires 0 <= K < columns(). */
    public String getTitle(int k) {
        return _titles[k];
    }

    /** Return the number of the column whose title is TITLE, or -1 if
     *  there isn't one. */
    public int findColumn(String title) {
        int counter = 0;
        for (String coltitle: _titles) {
            if (coltitle.equals(title)) {
                return counter;
            }
            counter++;
        }
        return -1;
    }

    /** Return the number of rows in this table. */
    public int size() {
        return _size;
    }

    /** Return the value of column number COL (0 <= COL < columns())
     *  of record number ROW (0 <= ROW < size()). */
    public String get(int row, int col) {
        try {
            return _columns[col].get(row);
        } catch (IndexOutOfBoundsException e) {
            throw error("invalid row or column");
        }
    }

    /** Add a new row whose column values are VALUES to me if no equal
     *  row already exists.  Return true if anything was added,
     *  false otherwise. */
    public boolean add(String[] values) {
        int counter;
        for (int i = 0; i < _size; i++) {
            counter = 0;
            for (int j = 0; j < _rowSize; j++) {
                if (values[j].equals(_columns[j].get(i))) {
                    counter++;
                }
            }
            if (counter == _rowSize) {
                return false;
            }
        }
        for (int k = 0; k < _rowSize; k++) {
            _columns[k].add(values[k]);
        }
        _size++;
        int newIndex = _size - 1;
        for (int l = 0; l < _index.size(); l++) {
            if ((compareRows(newIndex, l) < 0) || (_index.size() == 0)) {
                _index.add(l, newIndex);
                break;
            }
        }
        if (_index.size() == _size - 1) {
            _index.add(newIndex, newIndex);
        }
        return true;
    }


    /** Add a new row whose column values are extracted by COLUMNS from
     *  the rows indexed by ROWS, if no equal row already exists.
     *  Return true if anything was added, false otherwise. See
     *  Column.getFrom(Integer...) for a description of how Columns
     *  extract values. */
    public boolean add(List<Column> columns, Integer... rows) {
        List<String> values = new ArrayList<String>();
        for (Column col:columns) {
            values.add(col.getFrom(rows));
        }
        String[] valList = values.toArray(new String[values.size()]);
        boolean added = this.add(valList);
        return added;
    }

    /** Read the contents of the file NAME.db, and return as a Table.
     *  Format errors in the .db file cause a DBException. */
    public static Table readTable(String name) {
        BufferedReader input;
        Table table;
        input = null;
        try {
            input = new BufferedReader(new FileReader(name + ".db"));
            String header = input.readLine();
            if (header == null) {
                throw error("missing header in DB file");
            }
            String[] columnNames = header.split(",");
            table = new Table(columnNames);
            String row = null;
            while ((row = input.readLine()) != null) {
                String[] rowsplit = row.split(",");
                table.add(rowsplit);
            }

        } catch (FileNotFoundException e) {
            throw error("could not find %s.db", name);
        } catch (IOException e) {
            throw error("problem reading from %s.db", name);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    throw error("%s.db didn't close properly.", name);
                }
            }
        }
        return table;
    }

    /** Write the contents of TABLE into the file NAME.db. Any I/O errors
     *  cause a DBException. */
    public void writeTable(String name) {
        PrintStream output;
        output = null;
        try {
            String sep;
            sep = ",";
            output = new PrintStream(name + ".db");
            for (int i = 0; i < _titles.length; i++) {
                output.print(_titles[i]);
                if (i != _rowSize - 1) {
                    output.print(sep);
                }
            }
            output.println();
            for (int j = 0; j < _size; j++) {
                for (int k = 0; k < _rowSize; k++) {
                    output.print(_columns[k].get(_index.get(j)));
                    if (k != _rowSize - 1) {
                        output.print(sep);
                    }
                }
                output.println();
            }
        } catch (IOException e) {
            throw error("trouble writing to %s.db", name);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    /** Print my contents on the standard output, separated by spaces
     *  and indented by two spaces. */
    public void print() {
        for (int i = 0; i < _size; i++) {
            System.out.print("  ");
            for (int j = 0; j < _rowSize; j++) {
                if (j == _rowSize - 1) {
                    System.out.println(_columns[j].get(_index.get(i)));
                } else {
                    System.out.print(_columns[j].get(_index.get(i)) + ' ');
                }
            }
        }
    }

    /** Return a new Table whose columns are COLUMNNAMES, selected from
     *  rows of this table that satisfy CONDITIONS. */
    public Table select(List<String> columnNames, List<Condition> conditions) {
        boolean passed;
        Table result = new Table(columnNames);
        List<Column> columns = new ArrayList<>();
        for (int h = 0; h < columnNames.size(); h++) {
            Column column = new Column(columnNames.get(h), this);
            columns.add(column);
        }
        for (int i = 0; i < _size; i++) {
            if (conditions == null) {
                result.add(columns, i);
            }
            passed = Condition.test(conditions, i);
            if (passed) {
                result.add(columns, i);
            }
        }
        return result;
    }

    /**Return a list of columns found in this table and T2.*/
    public List<String> dupColList(Table t2) {
        List<String> dupcollist = new ArrayList<String>();
        for (String col : this._titles) {
            if (t2.findColumn(col) != -1) {
                dupcollist.add(col);
            }
        }
        return dupcollist;
    }

    /** Return a new Table whose columns are COLUMNNAMES, selected
     *  from pairs of rows from this table and from T2 that match
     *  on all columns with identical names and satisfy CONDITIONS. */
    public Table select(Table t2, List<String> columnNames,
                 List<Condition> conditions) {
        Table result = new Table(columnNames);
        boolean passed;
        List<Column> columns = new ArrayList<Column>();
        for (int h = 0; h < columnNames.size(); h++) {
            Column column = new Column(columnNames.get(h), this, t2);
            columns.add(column);
        }
        List<Column> colList1 = new ArrayList<>();
        List<Column> colList2 = new ArrayList<>();
        for (String colname: dupColList(t2)) {
            Column col1 = new Column(_titles[this.findColumn(colname)], this);
            Column col2 = new Column(t2._titles[t2.findColumn(colname)], t2);
            colList1.add(col1);
            colList2.add(col2);
        }
        int dupcols = colList1.size();
        for (int i = 0; i < this._size; i++) {
            for (int j = 0; j < t2.size(); j++) {
                if (dupcols != 0) {
                    if (equijoin(colList1, colList2, i, j)) {
                        passed = Condition.test(conditions, i, j);
                        if (passed) {
                            result.add(columns, i, j);
                        }
                    }
                } else {
                    passed = Condition.test(conditions, i, j);
                    if (passed) {
                        result.add(columns, i, j);
                    }
                }
            }
        }
        return result;
    }

    /** Return <0, 0, or >0 depending on whether the row formed from
     *  the elements _columns[0].get(K0), _columns[1].get(K0), ...
     *  is less than, equal to, or greater than that formed from elememts
     *  _columns[0].get(K1), _columns[1].get(K1), ....  This method ignores
     *  the _index. */
    private int compareRows(int k0, int k1) {
        for (int i = 0; i < _columns.length; i += 1) {
            int c = _columns[i].get(k0).compareTo(
                    _columns[i].get(_index.get(k1)));
            if (c != 0) {
                return c;
            }
        }
        return 0;
    }

    /** Return true if the columns COMMON1 from ROW1 and COMMON2 from
     *  ROW2 all have identical values.  Assumes that COMMON1 and
     *  COMMON2 have the same number of elements and the same names,
     *  that the columns in COMMON1 apply to this table, those in
     *  COMMON2 to another, and that ROW1 and ROW2 are indices, respectively,
     *  into those tables. */
    private static boolean equijoin(List<Column> common1, List<Column> common2,
                                    int row1, int row2) {
        boolean passed = true;
        String val1;
        String val2;
        for (int i = 0; i < common1.size(); i++) {
            val1 = common1.get(i).getFrom(row1);
            val2 = common2.get(i).getFrom(row2);
            if (!val1.equals(val2)) {
                passed = false;
            }
        }
        return passed;
    }

    /** A class that is essentially ArrayList<String>.  For technical reasons,
     *  we need to encapsulate ArrayList<String> like this because the
     *  underlying design of Java does not properly distinguish between
     *  different kinds of ArrayList at runtime (e.g., if you have a
     *  variable of type Object that was created from an ArrayList, there is
     *  no way to determine in general whether it is an ArrayList<String>,
     *  ArrayList<Integer>, or ArrayList<Object>).  This leads to annoying
     *  compiler warnings.  The trick of defining a new type avoids this
     *  issue. */
    private static class ValueList extends ArrayList<String> {
    }

    /** My column titles. */
    private final String[] _titles;
    /** My columns. Row i consists of _columns[k].get(i) for all k. */
    private final ValueList[] _columns;

    /** Rows in the database are supposed to be sorted. To do so, we
     *  have a list whose kth element is the index in each column
     *  of the value of that column for the kth row in lexicographic order.
     *  That is, the first row (smallest in lexicographic order)
     *  is at position _index.get(0) in _columns[0], _columns[1], ...
     *  and the kth row in lexicographic order in at position _index.get(k).
     *  When a new row is inserted, insert its index at the appropriate
     *  place in this list.
     *  (Alternatively, we could simply keep each column in the proper order
     *  so that we would not need _index.  But that would mean that inserting
     *  a new row would require rearranging _rowSize lists (each list in
     *  _columns) rather than just one. */
    private final ArrayList<Integer> _index = new ArrayList<>();

    /** My number of rows (redundant, but convenient). */
    private int _size;
    /** My number of columns (redundant, but convenient). */
    private final int _rowSize;

}
