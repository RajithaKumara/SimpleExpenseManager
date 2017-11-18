package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

/**
 * Created by rajitha on 11/12/17.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class PersistentExpenseManager extends ExpenseManager {
    private DataBaseConnector dataBaseConnector;

    public PersistentExpenseManager(Context context) {
        setup(context);
    }

    @Override
    public void setup(Context context) {
        //*** Begin generating dummy data for In-Memory implementation ***//*
        dataBaseConnector=new DataBaseConnector(context);

        // dummy data
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);

        dataBaseConnector.insertAccount(dummyAcct1);
        dataBaseConnector.insertAccount(dummyAcct2);

        TransactionDAO inMemoryTransactionDAO = new InMemoryTransactionDAO(context);
        setTransactionsDAO(inMemoryTransactionDAO);

        AccountDAO inMemoryAccountDAO = new InMemoryAccountDAO(context);
        setAccountsDAO(inMemoryAccountDAO);

        //*** End ***//*
    }

    @Override
    public void setupDemo() throws ExpenseManagerException {

    }

}
