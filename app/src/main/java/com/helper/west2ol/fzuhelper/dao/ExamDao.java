package com.helper.west2ol.fzuhelper.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.helper.west2ol.fzuhelper.bean.Exam;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "EXAM".
*/
public class ExamDao extends AbstractDao<Exam, Long> {

    public static final String TABLENAME = "EXAM";

    /**
     * Properties of entity Exam.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property ExamId = new Property(0, Long.class, "examId", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Xuefen = new Property(2, String.class, "xuefen", false, "XUEFEN");
        public final static Property Teacher = new Property(3, String.class, "teacher", false, "TEACHER");
        public final static Property Address = new Property(4, String.class, "address", false, "ADDRESS");
        public final static Property Zuohao = new Property(5, String.class, "zuohao", false, "ZUOHAO");
    }


    public ExamDao(DaoConfig config) {
        super(config);
    }
    
    public ExamDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"EXAM\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: examId
                "\"NAME\" TEXT," + // 1: name
                "\"XUEFEN\" TEXT," + // 2: xuefen
                "\"TEACHER\" TEXT," + // 3: teacher
                "\"ADDRESS\" TEXT," + // 4: address
                "\"ZUOHAO\" TEXT);"); // 5: zuohao
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"EXAM\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Exam entity) {
        stmt.clearBindings();
 
        Long examId = entity.getExamId();
        if (examId != null) {
            stmt.bindLong(1, examId);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String xuefen = entity.getXuefen();
        if (xuefen != null) {
            stmt.bindString(3, xuefen);
        }
 
        String teacher = entity.getTeacher();
        if (teacher != null) {
            stmt.bindString(4, teacher);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(5, address);
        }
 
        String zuohao = entity.getZuohao();
        if (zuohao != null) {
            stmt.bindString(6, zuohao);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Exam entity) {
        stmt.clearBindings();
 
        Long examId = entity.getExamId();
        if (examId != null) {
            stmt.bindLong(1, examId);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String xuefen = entity.getXuefen();
        if (xuefen != null) {
            stmt.bindString(3, xuefen);
        }
 
        String teacher = entity.getTeacher();
        if (teacher != null) {
            stmt.bindString(4, teacher);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(5, address);
        }
 
        String zuohao = entity.getZuohao();
        if (zuohao != null) {
            stmt.bindString(6, zuohao);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Exam readEntity(Cursor cursor, int offset) {
        Exam entity = new Exam( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // examId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // xuefen
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // teacher
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // address
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // zuohao
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Exam entity, int offset) {
        entity.setExamId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setXuefen(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setTeacher(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setAddress(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setZuohao(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Exam entity, long rowId) {
        entity.setExamId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Exam entity) {
        if(entity != null) {
            return entity.getExamId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Exam entity) {
        return entity.getExamId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}