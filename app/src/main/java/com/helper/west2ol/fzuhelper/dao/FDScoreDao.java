package com.helper.west2ol.fzuhelper.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.helper.west2ol.fzuhelper.bean.FDScore;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "FDSCORE".
*/
public class FDScoreDao extends AbstractDao<FDScore, Long> {

    public static final String TABLENAME = "FDSCORE";

    /**
     * Properties of entity FDScore.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property FdScoreId = new Property(0, Long.class, "fdScoreId", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Jidian = new Property(2, String.class, "jidian", false, "JIDIAN");
        public final static Property Xuefen = new Property(3, String.class, "xuefen", false, "XUEFEN");
        public final static Property Score = new Property(4, String.class, "score", false, "SCORE");
        public final static Property Year = new Property(5, int.class, "year", false, "YEAR");
        public final static Property Xuenian = new Property(6, int.class, "xuenian", false, "XUENIAN");
    }


    public FDScoreDao(DaoConfig config) {
        super(config);
    }
    
    public FDScoreDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"FDSCORE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: fdScoreId
                "\"NAME\" TEXT," + // 1: name
                "\"JIDIAN\" TEXT," + // 2: jidian
                "\"XUEFEN\" TEXT," + // 3: xuefen
                "\"SCORE\" TEXT," + // 4: score
                "\"YEAR\" INTEGER NOT NULL ," + // 5: year
                "\"XUENIAN\" INTEGER NOT NULL );"); // 6: xuenian
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"FDSCORE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, FDScore entity) {
        stmt.clearBindings();
 
        Long fdScoreId = entity.getFdScoreId();
        if (fdScoreId != null) {
            stmt.bindLong(1, fdScoreId);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String jidian = entity.getJidian();
        if (jidian != null) {
            stmt.bindString(3, jidian);
        }
 
        String xuefen = entity.getXuefen();
        if (xuefen != null) {
            stmt.bindString(4, xuefen);
        }
 
        String score = entity.getScore();
        if (score != null) {
            stmt.bindString(5, score);
        }
        stmt.bindLong(6, entity.getYear());
        stmt.bindLong(7, entity.getXuenian());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, FDScore entity) {
        stmt.clearBindings();
 
        Long fdScoreId = entity.getFdScoreId();
        if (fdScoreId != null) {
            stmt.bindLong(1, fdScoreId);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String jidian = entity.getJidian();
        if (jidian != null) {
            stmt.bindString(3, jidian);
        }
 
        String xuefen = entity.getXuefen();
        if (xuefen != null) {
            stmt.bindString(4, xuefen);
        }
 
        String score = entity.getScore();
        if (score != null) {
            stmt.bindString(5, score);
        }
        stmt.bindLong(6, entity.getYear());
        stmt.bindLong(7, entity.getXuenian());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public FDScore readEntity(Cursor cursor, int offset) {
        FDScore entity = new FDScore( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // fdScoreId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // jidian
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // xuefen
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // score
            cursor.getInt(offset + 5), // year
            cursor.getInt(offset + 6) // xuenian
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, FDScore entity, int offset) {
        entity.setFdScoreId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setJidian(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setXuefen(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setScore(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setYear(cursor.getInt(offset + 5));
        entity.setXuenian(cursor.getInt(offset + 6));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(FDScore entity, long rowId) {
        entity.setFdScoreId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(FDScore entity) {
        if(entity != null) {
            return entity.getFdScoreId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(FDScore entity) {
        return entity.getFdScoreId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}