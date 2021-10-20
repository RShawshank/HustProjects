package com.example.herve.Study.greendao.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.example.herve.Study.bean.User;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER".
*/
public class UserDao extends AbstractDao<User, String> {

    public static final String TABLENAME = "USER";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property UserId = new Property(0, String.class, "userId", true, "USER_ID");
        public final static Property PassWord = new Property(1, String.class, "passWord", false, "PASS_WORD");
        public final static Property UserName = new Property(2, String.class, "userName", false, "USER_NAME");
        public final static Property Age = new Property(3, String.class, "age", false, "AGE");
        public final static Property Sex = new Property(4, boolean.class, "sex", false, "SEX");
        public final static Property Identity = new Property(5, int.class, "Identity", false, "IDENTITY");
        public final static Property Duties = new Property(6, String.class, "duties", false, "DUTIES");
        public final static Property Characterization = new Property(7, String.class, "characterization", false, "CHARACTERIZATION");
    }


    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER\" (" + //
                "\"USER_ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: userId
                "\"PASS_WORD\" TEXT," + // 1: passWord
                "\"USER_NAME\" TEXT," + // 2: userName
                "\"AGE\" TEXT," + // 3: age
                "\"SEX\" INTEGER NOT NULL ," + // 4: sex
                "\"IDENTITY\" INTEGER NOT NULL ," + // 5: Identity
                "\"DUTIES\" TEXT," + // 6: duties
                "\"CHARACTERIZATION\" TEXT);"); // 7: characterization
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, User entity) {
        stmt.clearBindings();
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(1, userId);
        }
 
        String passWord = entity.getPassWord();
        if (passWord != null) {
            stmt.bindString(2, passWord);
        }
 
        String userName = entity.getUserName();
        if (userName != null) {
            stmt.bindString(3, userName);
        }
 
        String age = entity.getAge();
        if (age != null) {
            stmt.bindString(4, age);
        }
        stmt.bindLong(5, entity.getSex() ? 1L: 0L);
        stmt.bindLong(6, entity.getIdentity());
 
        String duties = entity.getDuties();
        if (duties != null) {
            stmt.bindString(7, duties);
        }
 
        String characterization = entity.getCharacterization();
        if (characterization != null) {
            stmt.bindString(8, characterization);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(1, userId);
        }
 
        String passWord = entity.getPassWord();
        if (passWord != null) {
            stmt.bindString(2, passWord);
        }
 
        String userName = entity.getUserName();
        if (userName != null) {
            stmt.bindString(3, userName);
        }
 
        String age = entity.getAge();
        if (age != null) {
            stmt.bindString(4, age);
        }
        stmt.bindLong(5, entity.getSex() ? 1L: 0L);
        stmt.bindLong(6, entity.getIdentity());
 
        String duties = entity.getDuties();
        if (duties != null) {
            stmt.bindString(7, duties);
        }
 
        String characterization = entity.getCharacterization();
        if (characterization != null) {
            stmt.bindString(8, characterization);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // userId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // passWord
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // userName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // age
            cursor.getShort(offset + 4) != 0, // sex
            cursor.getInt(offset + 5), // Identity
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // duties
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // characterization
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setUserId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setPassWord(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUserName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAge(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSex(cursor.getShort(offset + 4) != 0);
        entity.setIdentity(cursor.getInt(offset + 5));
        entity.setDuties(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setCharacterization(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    @Override
    protected final String updateKeyAfterInsert(User entity, long rowId) {
        return entity.getUserId();
    }
    
    @Override
    public String getKey(User entity) {
        if(entity != null) {
            return entity.getUserId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(User entity) {
        return entity.getUserId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
