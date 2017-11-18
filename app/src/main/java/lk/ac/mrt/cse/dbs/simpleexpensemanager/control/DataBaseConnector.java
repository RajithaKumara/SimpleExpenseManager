package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;


/**
 * Created by rajitha on 11/12/17.
 */

public class DataBaseConnector extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME_ACCOUNT = "account";
    private static final String TABLE_NAME_TRANSACTION = "account_transactions";
    private static final String ACCOUNT_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_ACCOUNT + " (account_no TEXT PRIMARY KEY, bank_name TEXT NOT NULL, account_holder_name TEXT NOT NULL, balance DOUBLE);";
    private static final String TRANSACTION_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_TRANSACTION + " (account_no TEXT PRIMARY KEY, date TEXT NOT NULL, expense_type TEXT NOT NULL, amount DOUBLE);";

    public DataBaseConnector(Context context) {
        super(context, "db_150341R", null, DATABASE_VERSION);
        //Toast.makeText(context, "Database created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ACCOUNT_TABLE_CREATE);
        db.execSQL(TRANSACTION_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertAccount(Account account){
        String account_no=account.getAccountNo();
        String bank_name=account.getBankName();
        String holder_name=account.getAccountHolderName();
        double balance=account.getBalance();

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("account_no",account_no);
        contentValues.put("bank_name",bank_name);
        contentValues.put("account_holder_name",holder_name);
        contentValues.put("balance",balance);
        database.insert(TABLE_NAME_ACCOUNT,null,contentValues);
    }


    public List<Account> getAccountDetail(){
        List<Account> accounts = new LinkedList<>();

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor resultSet=database.rawQuery("SELECT * FROM "+TABLE_NAME_ACCOUNT+"",null);
        resultSet.moveToFirst();
        while (resultSet.isAfterLast()==false){
            String account_no=(String) resultSet.getString(resultSet.getColumnIndex("account_no"));
            String bank_name=(String) resultSet.getString(resultSet.getColumnIndex("bank_name"));
            String holder_name=(String) resultSet.getString(resultSet.getColumnIndex("account_holder_name"));
            double balance=(Double) resultSet.getDouble(resultSet.getColumnIndex("balance"));
            Account account=new Account(account_no,bank_name,holder_name,10000.0);
            accounts.add(account);
            resultSet.moveToNext();
        }
        return accounts;
    }

    public void insertTransaction(Transaction transaction){
        String date= String.valueOf(transaction.getDate());
        String account_no=transaction.getAccountNo();
        String expense_type= String.valueOf(transaction.getExpenseType());
        double amount=transaction.getAmount();

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("account_no",account_no);
        contentValues.put("date",date);
        contentValues.put("expense_type",expense_type);
        contentValues.put("amount",amount);
        database.insert(TABLE_NAME_TRANSACTION,null,contentValues);
    }

    public List<Transaction> getTransactionDetail(){
        List<Transaction> transactions = new LinkedList<>();

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor resultSet=database.rawQuery("SELECT * FROM "+TABLE_NAME_TRANSACTION+"",null);
        resultSet.moveToFirst();
        while (resultSet.isAfterLast()==false){
            String date=(String) resultSet.getString(resultSet.getColumnIndex("date"));
            String account_no=(String) resultSet.getString(resultSet.getColumnIndex("account_no"));
            String expense_type=(String) resultSet.getString(resultSet.getColumnIndex("expense_type"));
            double amount=(Double) resultSet.getDouble(resultSet.getColumnIndex("amount"));

            Calendar cal=Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            Date date_D=null;
            try {
                cal.setTime(simpleDateFormat.parse(date));
                date_D=cal.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }


            ExpenseType expenseType;
            if (expense_type.equals("EXPENSE")){
                expenseType=ExpenseType.EXPENSE;
            }else{
                expenseType=ExpenseType.INCOME;
            }

            Transaction transaction=new Transaction(date_D,account_no,expenseType,amount);
            transactions.add(transaction);
            resultSet.moveToNext();
        }
        return transactions;
    }

    public Account getSpecificAccount(String accountNo){
        Account account=null;

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor resultSet=database.rawQuery("SELECT * FROM "+TABLE_NAME_ACCOUNT+" where account_no='"+accountNo+"'",null);
        resultSet.moveToFirst();

        String account_no=resultSet.getString(resultSet.getColumnIndex("account_no"));
        String bank_name=resultSet.getString(resultSet.getColumnIndex("bank_name"));
        String holder_name=resultSet.getString(resultSet.getColumnIndex("account_holder_name"));
        double balance=resultSet.getDouble(resultSet.getColumnIndex("balance"));
        account=new Account(account_no,bank_name,holder_name,balance);

        return account;
    }

    public void updateAccountBalance(Account account){
        String account_no=account.getAccountNo();
        double balance=account.getBalance();

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("balance",balance);
        database.update(TABLE_NAME_ACCOUNT,contentValues,"account_no='"+account_no+"'",null);
    }
}
