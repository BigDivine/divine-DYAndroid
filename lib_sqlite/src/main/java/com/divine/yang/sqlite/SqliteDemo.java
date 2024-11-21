//package com.divine.yang.sqlite;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.util.Log;
//
//public class SqliteDemo {
//    private Context mContext;
//    private SQLiteUtil mSQLiteUtil;
//    private SQLiteHelper sqliteHelper;
//    private String tableName = "test_table";
//
//    public SqliteDemo(Context mContext) {
//        this.mContext = mContext;
//        String createTableSql = "create table if not exists " + tableName + "(_id integer primary key autoincrement not null,username varchar(100),age integer,password varchar(20))";
//        mSQLiteUtil = SQLiteUtil.init(mContext, "test_database.db", null, 1, new String[]{createTableSql});
//        sqliteHelper = mSQLiteUtil.getSQLiteHelper();
//
//    }
//
//    protected void onDestroy() {
//        sqliteHelper.close();
//    }
//
//    public void insert(String username, String age, String password) {
//        try {
//            //            String sql = String.format("insert into %s(username,age,password) values('%s',%s,'%s')",
//            //                                       tableName, username, age, password);
//            //            sqliteHelper.insertRow(sql, "", "", null);
//            ContentValues values = new ContentValues();
//            values.put("username", username);
//            values.put("age", age);
//            values.put("password", password);
//            sqliteHelper.insertRow("", tableName, "", values);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void delete(String username, String age, String password) {
//        try {
//
////            String whereValuesSql = String.format("username='%s' or age=%s or password='%s'",
////                                                  username, age, password);
////            String sql = String.format("delete from %s where %s", tableName, whereValuesSql);
//            //            sqliteHelper.deleteRow(sql, tableName, null, null);
//
//            String whereClause = "username=? or age=? or password=?";
//            String[] whereValues = new String[]{username, age, password};
//            sqliteHelper.deleteRow("", tableName, whereClause, whereValues);
//            //        delete from 表名 where 条件;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void update(String username, String age, String password) {
//        try {
//
////            String sql = String.format("update %s set username='%s',age=%s,password='%s' where username='%s'",
////                                       tableName, username, age, password, "123");
////            sqliteHelper.updateRow(sql, tableName, null, null, null);
//
//            ContentValues values = new ContentValues();
//            values.put("username", username);
//            values.put("age", age);
//            values.put("password", password);
//            sqliteHelper.updateRow(null, tableName, values, "username=?", new String[]{"1234"});
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void query() {
//        try {
//            String sql = "select * from test_table";
//            Cursor cursor = sqliteHelper.queryRows(sql, tableName, null,
//                                                   null, null,
//                                                   null, null, null, null);
//            while (cursor.moveToNext()) {
//                String userName = cursor.getString(cursor.getColumnIndex("username"));
//                String age = cursor.getString(cursor.getColumnIndex("age"));
//                String password = cursor.getString(cursor.getColumnIndex("password"));
//                Log.e("yzl", userName + ";" + age + ";" + password);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//
//        }
//    }
//}
