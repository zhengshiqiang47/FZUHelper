package com.helper.west2ol.fzuhelper.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.helper.west2ol.fzuhelper.bean.CourseBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "COURSE_BEAN".
*/
public class CourseBeanDao extends AbstractDao<CourseBean, Long> {

    public static final String TABLENAME = "COURSE_BEAN";

    /**
     * Properties of entity CourseBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property CourseId = new Property(0, Long.class, "courseId", true, "_id");
        public final static Property KcName = new Property(1, String.class, "kcName", false, "KC_NAME");
        public final static Property KcLocation = new Property(2, String.class, "kcLocation", false, "KC_LOCATION");
        public final static Property KcStartTime = new Property(3, int.class, "kcStartTime", false, "KC_START_TIME");
        public final static Property KcEndTime = new Property(4, int.class, "kcEndTime", false, "KC_END_TIME");
        public final static Property KcStartWeek = new Property(5, int.class, "kcStartWeek", false, "KC_START_WEEK");
        public final static Property KcEndWeek = new Property(6, int.class, "kcEndWeek", false, "KC_END_WEEK");
        public final static Property KcIsDouble = new Property(7, boolean.class, "kcIsDouble", false, "KC_IS_DOUBLE");
        public final static Property KcIsSingle = new Property(8, boolean.class, "kcIsSingle", false, "KC_IS_SINGLE");
        public final static Property KcWeekend = new Property(9, int.class, "kcWeekend", false, "KC_WEEKEND");
        public final static Property KcYear = new Property(10, int.class, "kcYear", false, "KC_YEAR");
        public final static Property KcXuenian = new Property(11, int.class, "kcXuenian", false, "KC_XUENIAN");
        public final static Property KcNote = new Property(12, String.class, "kcNote", false, "KC_NOTE");
        public final static Property KcBackgroundId = new Property(13, int.class, "kcBackgroundId", false, "KC_BACKGROUND_ID");
        public final static Property Unique = new Property(14, String.class, "unique", false, "UNIQUE");
    }


    public CourseBeanDao(DaoConfig config) {
        super(config);
    }
    
    public CourseBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"COURSE_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: courseId
                "\"KC_NAME\" TEXT," + // 1: kcName
                "\"KC_LOCATION\" TEXT," + // 2: kcLocation
                "\"KC_START_TIME\" INTEGER NOT NULL ," + // 3: kcStartTime
                "\"KC_END_TIME\" INTEGER NOT NULL ," + // 4: kcEndTime
                "\"KC_START_WEEK\" INTEGER NOT NULL ," + // 5: kcStartWeek
                "\"KC_END_WEEK\" INTEGER NOT NULL ," + // 6: kcEndWeek
                "\"KC_IS_DOUBLE\" INTEGER NOT NULL ," + // 7: kcIsDouble
                "\"KC_IS_SINGLE\" INTEGER NOT NULL ," + // 8: kcIsSingle
                "\"KC_WEEKEND\" INTEGER NOT NULL ," + // 9: kcWeekend
                "\"KC_YEAR\" INTEGER NOT NULL ," + // 10: kcYear
                "\"KC_XUENIAN\" INTEGER NOT NULL ," + // 11: kcXuenian
                "\"KC_NOTE\" TEXT," + // 12: kcNote
                "\"KC_BACKGROUND_ID\" INTEGER NOT NULL ," + // 13: kcBackgroundId
                "\"UNIQUE\" TEXT UNIQUE );"); // 14: unique
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"COURSE_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CourseBean entity) {
        stmt.clearBindings();
 
        Long courseId = entity.getCourseId();
        if (courseId != null) {
            stmt.bindLong(1, courseId);
        }
 
        String kcName = entity.getKcName();
        if (kcName != null) {
            stmt.bindString(2, kcName);
        }
 
        String kcLocation = entity.getKcLocation();
        if (kcLocation != null) {
            stmt.bindString(3, kcLocation);
        }
        stmt.bindLong(4, entity.getKcStartTime());
        stmt.bindLong(5, entity.getKcEndTime());
        stmt.bindLong(6, entity.getKcStartWeek());
        stmt.bindLong(7, entity.getKcEndWeek());
        stmt.bindLong(8, entity.getKcIsDouble() ? 1L: 0L);
        stmt.bindLong(9, entity.getKcIsSingle() ? 1L: 0L);
        stmt.bindLong(10, entity.getKcWeekend());
        stmt.bindLong(11, entity.getKcYear());
        stmt.bindLong(12, entity.getKcXuenian());
 
        String kcNote = entity.getKcNote();
        if (kcNote != null) {
            stmt.bindString(13, kcNote);
        }
        stmt.bindLong(14, entity.getKcBackgroundId());
 
        String unique = entity.getUnique();
        if (unique != null) {
            stmt.bindString(15, unique);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CourseBean entity) {
        stmt.clearBindings();
 
        Long courseId = entity.getCourseId();
        if (courseId != null) {
            stmt.bindLong(1, courseId);
        }
 
        String kcName = entity.getKcName();
        if (kcName != null) {
            stmt.bindString(2, kcName);
        }
 
        String kcLocation = entity.getKcLocation();
        if (kcLocation != null) {
            stmt.bindString(3, kcLocation);
        }
        stmt.bindLong(4, entity.getKcStartTime());
        stmt.bindLong(5, entity.getKcEndTime());
        stmt.bindLong(6, entity.getKcStartWeek());
        stmt.bindLong(7, entity.getKcEndWeek());
        stmt.bindLong(8, entity.getKcIsDouble() ? 1L: 0L);
        stmt.bindLong(9, entity.getKcIsSingle() ? 1L: 0L);
        stmt.bindLong(10, entity.getKcWeekend());
        stmt.bindLong(11, entity.getKcYear());
        stmt.bindLong(12, entity.getKcXuenian());
 
        String kcNote = entity.getKcNote();
        if (kcNote != null) {
            stmt.bindString(13, kcNote);
        }
        stmt.bindLong(14, entity.getKcBackgroundId());
 
        String unique = entity.getUnique();
        if (unique != null) {
            stmt.bindString(15, unique);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public CourseBean readEntity(Cursor cursor, int offset) {
        CourseBean entity = new CourseBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // courseId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // kcName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // kcLocation
            cursor.getInt(offset + 3), // kcStartTime
            cursor.getInt(offset + 4), // kcEndTime
            cursor.getInt(offset + 5), // kcStartWeek
            cursor.getInt(offset + 6), // kcEndWeek
            cursor.getShort(offset + 7) != 0, // kcIsDouble
            cursor.getShort(offset + 8) != 0, // kcIsSingle
            cursor.getInt(offset + 9), // kcWeekend
            cursor.getInt(offset + 10), // kcYear
            cursor.getInt(offset + 11), // kcXuenian
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // kcNote
            cursor.getInt(offset + 13), // kcBackgroundId
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14) // unique
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, CourseBean entity, int offset) {
        entity.setCourseId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setKcName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setKcLocation(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setKcStartTime(cursor.getInt(offset + 3));
        entity.setKcEndTime(cursor.getInt(offset + 4));
        entity.setKcStartWeek(cursor.getInt(offset + 5));
        entity.setKcEndWeek(cursor.getInt(offset + 6));
        entity.setKcIsDouble(cursor.getShort(offset + 7) != 0);
        entity.setKcIsSingle(cursor.getShort(offset + 8) != 0);
        entity.setKcWeekend(cursor.getInt(offset + 9));
        entity.setKcYear(cursor.getInt(offset + 10));
        entity.setKcXuenian(cursor.getInt(offset + 11));
        entity.setKcNote(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setKcBackgroundId(cursor.getInt(offset + 13));
        entity.setUnique(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(CourseBean entity, long rowId) {
        entity.setCourseId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(CourseBean entity) {
        if(entity != null) {
            return entity.getCourseId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(CourseBean entity) {
        return entity.getCourseId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
