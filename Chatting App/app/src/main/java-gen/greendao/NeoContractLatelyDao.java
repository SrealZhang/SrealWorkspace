package greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "NEO_CONTRACT_LATELY".
*/
public class NeoContractLatelyDao extends AbstractDao<NeoContractLately, String> {

    public static final String TABLENAME = "NEO_CONTRACT_LATELY";

    /**
     * Properties of entity NeoContractLately.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property FriendJID = new Property(0, String.class, "friendJID", true, "FRIEND_JID");
        public final static Property FriendName = new Property(1, String.class, "friendName", false, "FRIEND_NAME");
        public final static Property MyJID = new Property(2, String.class, "myJID", false, "MY_JID");
        public final static Property Num = new Property(3, Integer.class, "num", false, "NUM");
        public final static Property Time = new Property(4, long.class, "time", false, "TIME");
        public final static Property Body = new Property(5, String.class, "body", false, "BODY");
    };


    public NeoContractLatelyDao(DaoConfig config) {
        super(config);
    }
    
    public NeoContractLatelyDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"NEO_CONTRACT_LATELY\" (" + //
                "\"FRIEND_JID\" TEXT PRIMARY KEY NOT NULL ," + // 0: friendJID
                "\"FRIEND_NAME\" TEXT NOT NULL ," + // 1: friendName
                "\"MY_JID\" TEXT NOT NULL ," + // 2: myJID
                "\"NUM\" INTEGER," + // 3: num
                "\"TIME\" INTEGER NOT NULL ," + // 4: time
                "\"BODY\" TEXT NOT NULL );"); // 5: body
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"NEO_CONTRACT_LATELY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, NeoContractLately entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getFriendJID());
        stmt.bindString(2, entity.getFriendName());
        stmt.bindString(3, entity.getMyJID());
 
        Integer num = entity.getNum();
        if (num != null) {
            stmt.bindLong(4, num);
        }
        stmt.bindLong(5, entity.getTime());
        stmt.bindString(6, entity.getBody());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, NeoContractLately entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getFriendJID());
        stmt.bindString(2, entity.getFriendName());
        stmt.bindString(3, entity.getMyJID());
 
        Integer num = entity.getNum();
        if (num != null) {
            stmt.bindLong(4, num);
        }
        stmt.bindLong(5, entity.getTime());
        stmt.bindString(6, entity.getBody());
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.getString(offset + 0);
    }    

    @Override
    public NeoContractLately readEntity(Cursor cursor, int offset) {
        NeoContractLately entity = new NeoContractLately( //
            cursor.getString(offset + 0), // friendJID
            cursor.getString(offset + 1), // friendName
            cursor.getString(offset + 2), // myJID
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // num
            cursor.getLong(offset + 4), // time
            cursor.getString(offset + 5) // body
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, NeoContractLately entity, int offset) {
        entity.setFriendJID(cursor.getString(offset + 0));
        entity.setFriendName(cursor.getString(offset + 1));
        entity.setMyJID(cursor.getString(offset + 2));
        entity.setNum(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setTime(cursor.getLong(offset + 4));
        entity.setBody(cursor.getString(offset + 5));
     }
    
    @Override
    protected final String updateKeyAfterInsert(NeoContractLately entity, long rowId) {
        return entity.getFriendJID();
    }
    
    @Override
    public String getKey(NeoContractLately entity) {
        if(entity != null) {
            return entity.getFriendJID();
        } else {
            return null;
        }
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
