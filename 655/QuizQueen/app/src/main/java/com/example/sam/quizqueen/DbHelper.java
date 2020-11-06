package com.example.sam.quizqueen;


        import java.lang.reflect.Array;
        import java.util.ArrayList;
        import java.util.List;
        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "qq.db";
    // tasks table name
    private static final String TABLE_QUEST = "quest";

    // tasks Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_QUES = "question";
    private static final String KEY_ANSWER = "answer"; //correct option
    private static final String KEY_OPTA= "opta"; //option a
    private static final String KEY_OPTB= "optb"; //option b
    private static final String KEY_OPTC= "optc"; //option c
    private SQLiteDatabase dbase;

    private static final String TABLE_USERS = "users";
    public static final String startForQM = "QM starts here", startForQT = "QT starts here";




    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        dbase=db;
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_QUEST + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_QUES
                + " TEXT, " + KEY_ANSWER+ " TEXT, "+KEY_OPTA +" TEXT, "
                +KEY_OPTB +" TEXT, "+KEY_OPTC+" TEXT)";

        String sql2 = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, users TEXT,password TEXT)";
        String sql3 = "CREATE TABLE IF NOT EXISTS question (id INTEGER PRIMARY KEY AUTOINCREMENT, question TEXT, opta TEXT,optb TEXT,optc TEXT,optd TEXT,answer TEXT)";
        db.execSQL(sql);
        db.execSQL(sql2);
        db.execSQL(sql3);
        //addQuestions();
        //db.close();

        //String sql1 = "CREATE TABLE IF NOT EXISTS" + TABLE_QUEST +
    }
    /*private void addQuestions()
    {
        Question q1=new Question("What is JP?","Jalur Pesawat", "Jack sParrow", "Jasa Programmer", "Jasa Programmer");
        this.addQuestion(q1);
        Question q2=new Question("where the JP place?", "Monas, Jakarta", "Gelondong, Bangun Tapan, bantul", "Gelondong, Bangun Tapan, bandul", "Gelondong, Bangun Tapan, bantul");
        this.addQuestion(q2);
        Question q3=new Question("who is CEO of the JP?","Usman and Jack", "Jack and Rully","Rully and Usman", "Rully and Usman" );
        this.addQuestion(q3);
        Question q4=new Question("what do you know about JP?", "JP is programmer home", "JP also realigy home", "all answer is true","all answer is true");
        this.addQuestion(q4);
        Question q5=new Question("what do you learn in JP?","Realigy","Programming","all answer is true","all answer is true");
        this.addQuestion(q5);
    } */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUEST);
        // Create tables again
        onCreate(db);
    }
    // Adding new question
    /*
    public void addQuestion(Question quest) {
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_QUES, quest.getQUESTION());
        values.put(KEY_ANSWER, quest.getANSWER());
        values.put(KEY_OPTA, quest.getOPTA());
        values.put(KEY_OPTB, quest.getOPTB());
        values.put(KEY_OPTC, quest.getOPTC());
        // Inserting Row
        dbase.insert(TABLE_QUEST, null, values);
    }*/

    public List<Question> getAllQuestions() {
        List<Question> quesList = new ArrayList<Question>();
        // Select All Query
        String selectQuery = "SELECT  * FROM question " ;
        dbase=this.getReadableDatabase();
        Cursor cursor = dbase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Question quest = new Question();
                quest.setID(cursor.getInt(0));
                quest.setQUESTION(cursor.getString(1));
                quest.setOPTA(cursor.getString(2));
                quest.setOPTB(cursor.getString(3));
                quest.setOPTC(cursor.getString(4));
                quest.setOPTD(cursor.getString(5));
                quest.setANSWER(cursor.getString(6));

                quesList.add(quest);
            } while (cursor.moveToNext());
        }
        // return quest list
        dbase.close();
        return quesList;

    }

    public String[] getAllUsers(){
        String selectUserQuery= "SELECT * FROM " + TABLE_USERS;
        dbase=this.getReadableDatabase();
        Cursor cursor = dbase.rawQuery(selectUserQuery,null);
        int userNumber = cursor.getCount();
        String[] userName =new String[userNumber];
        //userName[0] = startForQT;
        int i=0;

        if (cursor.moveToFirst()) {
            do {
                userName[i]=cursor.getString(1);
                i = i+1;
            }while (cursor.moveToNext());
        }
        cursor.close();
        dbase.close();
        return userName;
    }
    public String[] getAllQuestion(){
        String selectAllQuestion = "SELECT * FROM question";
        dbase = this.getReadableDatabase();
        Cursor cursor = dbase.rawQuery(selectAllQuestion,null);
        int questionNumber = cursor.getCount();
        String[] allQuestion = new String[questionNumber];

        int i=0;
        if (cursor.moveToFirst()){
            do{
                allQuestion[i] = cursor.getString(1);
                i = i +1;
            }while (cursor.moveToNext());
        }
        cursor.close();
        dbase.close();
        return allQuestion;
    }
    public String[] getAllQuestionId(){
        String selectAllQuestion = "SELECT * FROM question";
        dbase = this.getReadableDatabase();
        Cursor cursor = dbase.rawQuery(selectAllQuestion,null);
        int questionNumber = cursor.getCount();
        String[] allQuestionId = new String[questionNumber];

        int i=0;
        if (cursor.moveToFirst()){
            do{
                allQuestionId[i] = cursor.getString(0);
                i = i +1;
            }while (cursor.moveToNext());
        }
        cursor.close();
        dbase.close();
        return allQuestionId;
    }
    public String[] getQuestionById(String Id){
        String selectQuestionById = "SELECT * FROM question WHERE ID = '"+Id+"'";
        dbase = this.getReadableDatabase();
        Cursor cursor = dbase.rawQuery(selectQuestionById, null);
        String[] questionField = new String[8];
        cursor.moveToFirst();

        for (int i=0;i<8;i++){
            questionField[i] = cursor.getString(i);
        }
        cursor.close();
        dbase.close();
        return questionField;
    }


    public String[] getAllUserPassword(){


        String selectUserQuery= "SELECT * FROM " + TABLE_USERS;
        dbase=this.getReadableDatabase();
        Cursor cursor = dbase.rawQuery(selectUserQuery, null);
        int userNumber = cursor.getCount();
        String[] userPassword =new String[userNumber];
        //userName[0] = startForQT;
        int i=0;

        if (cursor.moveToFirst()) {
            do {
                userPassword[i]=cursor.getString(2);

                i = i+1;
            }while (cursor.moveToNext());
        }
        cursor.close();
        dbase.close();
        return userPassword;
    }

    public String[] getDatabaseID(String table){
        String selectUserQuery= "SELECT * FROM " + table;
        dbase=this.getReadableDatabase();
        Cursor cursor = dbase.rawQuery(selectUserQuery, null);
        int userNumber = cursor.getCount();
        String[] databaseID =new String[userNumber];
        //userName[0] = startForQT;
        int i=0;

        if (cursor.moveToFirst()) {
            do {
                databaseID[i]=cursor.getString(0);

                i = i+1;
            }while (cursor.moveToNext());
        }
        cursor.close();
        dbase.close();
        return databaseID;
    }

    public String[] getStatsFieldForUser(String userName,String field){
        String selectQuery = "SELECT "+field+" FROM STATS WHERE User = '"+userName+"';";
        dbase = this.getReadableDatabase();
        Cursor cursor = dbase.rawQuery(selectQuery,null);
        int entryNum = cursor.getCount();
        String[] fieldResult = new String [entryNum];
        int i=0;
        if (cursor.moveToFirst()){
            do{
                fieldResult[i]=cursor.getString(0);
                i=i+1;
            }while (cursor.moveToNext());
        }
        cursor.close();
        dbase.close();
        return fieldResult;
    }

    public void addNewQuestion(String questionTitle,String optA,String optB,String optC, String optD, String answer,String timer){
        dbase = this.getWritableDatabase();
        String insertNewQuestion = "INSERT INTO QUESTION(question,opta,optb,optc,optd,answer,timer) VALUES ('" + questionTitle +"' , '" + optA +"' , '"+optB +"' , '"+optC  +"' , '"+ optD +"' , '"+answer + "' , '"+timer+"')" ;
        dbase.execSQL(insertNewQuestion);
        dbase.close();
    }

    public void addNewUser(String userName,String passWord){
        dbase = this.getWritableDatabase();
        String insertNewUser = "insert into users(name,password) values ('" +userName + "' , '"+ passWord +"')";
        dbase.execSQL(insertNewUser);
        dbase.close();
    }
    public void deleteDbById(String tableName,String databaseID){
        dbase = this.getWritableDatabase();
        String insertNewUser = "DELETE FROM "+ tableName +" WHERE ID ='" + databaseID + "';";
        dbase.execSQL(insertNewUser);
        dbase.close();
    }
    public void delUserStats(String userName){
        dbase = this.getWritableDatabase();
        String delUserStats = "DELETE FROM STATS WHERE USER= '"+userName+"';";
        dbase.execSQL(delUserStats);
        dbase.close();

    }

    public void updateUserInfo(String userName,String passWord){
        dbase = this.getWritableDatabase();
        String sqlUpdataUser = "UPDATE USERS SET PASSWORD='"+ passWord +"' WHERE NAME = '"+userName + "';";
        dbase.execSQL(sqlUpdataUser);
        dbase.close();
    }
    public void  updateQuestion(String id,String questionTitle,String optA,String optB,String optC, String optD, String answer,String timer){
        dbase = this.getWritableDatabase();
        String sqlUpdQuestion = "UPDATE QUESTION SET QUESTION = '"+ questionTitle +"',OPTA = '"+ optA +"',OPTB= '"+ optB +"',OPTC= '"+ optC +"',OPTD= '"+ optD +"',ANSWER = '"+ answer +"', Timer= '"+ timer+"' WHERE ID = '"+ id +"';";
        dbase.execSQL(sqlUpdQuestion);
        dbase.close();
    }
    public void insertStats(String user,Integer correctAnswer,String q1,String q2,String q3,String q4,String q5) {
        dbase = this.getWritableDatabase();
        String sqlInstStats = "INSERT INTO STATS(USER,CORRECTANSWER,Q1,Q2,Q3,Q4,Q5) VALUES ('" + user + "','" + correctAnswer + "','" + q1 + "','" + q2 + "','" + q3 + "','" + q4 + "','" + q5 + "')";
        dbase.execSQL(sqlInstStats);
        dbase.close();
    }

    public int rowcount() {
        int row=0;
        String selectQuery = "SELECT  * FROM " + TABLE_QUEST;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        row=cursor.getCount();
        return row;
    }


}
