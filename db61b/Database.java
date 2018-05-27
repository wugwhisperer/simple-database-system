package db61b;
import java.util.HashMap;

/** A collection of Tables, indexed by name.
 *  @author Lily Vittayarukskul */
class Database {
    /** An empty database. */
    public Database() {
        _numtables = 0;
    }

    /** Return the Table whose name is NAME stored in this database, or null
     *  if there is no such table. */
    public Table get(String name) {
        return (Table) tablehashmap.get(name);
    }

    /** Set or replace the table named NAME in THIS to TABLE.  TABLE and
     *  NAME must not be null, and NAME must be a valid name for a table. */
    public void put(String name, Table table) {
        if (name == null || table == null) {
            throw new IllegalArgumentException("null argument");
        } else {
            tablehashmap.put(name, table);
        }
    }

    /** Number of Tables. */
    private int _numtables;
    /** Database to store key as table names and values as corresponding
     * Table objects. */
    private HashMap tablehashmap = new HashMap();
}
