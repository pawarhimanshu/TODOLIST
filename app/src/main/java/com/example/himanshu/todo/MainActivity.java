package com.example.himanshu.todo;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;



import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    ArrayList<todo> todos = new ArrayList<>();
    // adapter;
    todoAdapter adapter;

    public static final int ADD_EXPENSE_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.listview);
        todoOpenHepler openHelper = todoOpenHepler.getInstance(getApplicationContext());
        SQLiteDatabase database = openHelper.getReadableDatabase();
        // int amountGreaterThan = 0;
        // String[] selectionArgument = {amountGreaterThan + "",};
        String[] columns = {Contract.Todo.COLUMN_NAME,Contract.Todo.COLUMN_DAY,Contract.Todo.COLUMN_ID,Contract.Todo.COLUMN_DATE,Contract.Todo.COLUMN_TIME};
        Cursor cursor = database.query(Contract.Todo.TABLE_NAME,columns,  null,null,null,null,null);
        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex(Contract.Todo.COLUMN_NAME));
            String day = cursor.getString(cursor.getColumnIndex(Contract.Todo.COLUMN_DAY));
            long id = cursor.getLong(cursor.getColumnIndex(Contract.Todo.COLUMN_ID));
            String date= cursor.getString(cursor.getColumnIndex(Contract.Todo.COLUMN_DATE));
            String time= cursor.getString(cursor.getColumnIndex(Contract.Todo.COLUMN_TIME));

            todo todo = new todo(name,day,date,time);
            todo.setId(id);
            todos.add(todo);
        }
        cursor.close();

        adapter = new todoAdapter(this, todos);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                todo todo = todos.get(i);
                String n=todo.getName();
                String d=todo.getDay();
                String date=todo.getDate();
                String time=todo.getTime();
                showInputBox(n,d,date,time,i);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final todo todo = todos.get(i);


                final int position = i;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Confirm Delete");
                builder.setCancelable(false);
                builder.setMessage("Do you really want to delete " + todo.getName() + "?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Toast.makeText(MainActivity.this,"Ok Presses",Toast.LENGTH_LONG).show();
                        todoOpenHepler openHelper= todoOpenHepler.getInstance(getApplicationContext());
                        SQLiteDatabase database = openHelper.getWritableDatabase();

                        long id = todo.getId();
                        String[] selectionArgs = {id + ""};

                        database.delete(Contract.Todo.TABLE_NAME,Contract.Todo.COLUMN_ID + " = ?",selectionArgs);
                        todos.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
//
                //Toast.makeText(this,expense.getName() + " " + expense.getAmount(),Toast.LENGTH_LONG).show();
                return true;
            }
        });

        View view = new View(this);
    }
    public void showInputBox(String name,  String day, String date,String time,final int index){
        final int position=index;
        final Dialog dialog=new Dialog(MainActivity.this);
        dialog.setTitle("Input Box");
        dialog.setContentView(R.layout.input_box);
        TextView textView=(TextView)dialog.findViewById(R.id.txtmsz);
        textView.setText("Update Item");
        textView.setTextColor(Color.parseColor("#ff2222"));
        final EditText editText=dialog.findViewById(R.id.todoname);
        editText.setText(name);
        final  EditText editText1=dialog.findViewById(R.id.daytxt);
        editText1.setText(day);
        final  EditText editText2=dialog.findViewById(R.id.datetxt);
        editText2.setText(date);
        final  EditText editText3=dialog.findViewById(R.id.timetxt);
        editText3.setText(time);
        Button bt=dialog.findViewById(R.id.btnset);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todo todo = todos.get(index);
                todo.setName(editText.getText().toString());
                todo.setDay(editText1.getText().toString());
                todo.setDate(editText2.getText().toString());
                todo.setTime(editText3.getText().toString());
                todoOpenHepler openHelper= todoOpenHepler.getInstance(getApplicationContext());
                SQLiteDatabase database = openHelper.getWritableDatabase();

                long id = todo.getId();
                String[] selectionArgs = {id + ""};
                ContentValues cv = new ContentValues();
                cv.put(Contract.Todo.COLUMN_NAME,todo.getName());
                cv.put(Contract.Todo.COLUMN_DAY,todo.getDay());
                cv.put(Contract.Todo.COLUMN_DATE,todo.getDate());
                cv.put(Contract.Todo.COLUMN_TIME,todo.getTime());

                database.update(Contract.Todo.TABLE_NAME,cv ,Contract.Todo.COLUMN_ID + " = ?",selectionArgs);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addtodo) {
            Bundle bundle = new Bundle();
            bundle.putString("title", "abc");
            bundle.putString("day", "monday");
            bundle.putString("date","01/07/2018");
            bundle.putString("time","09:06 a");
            Intent intent = new Intent(this, AddTodoActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, ADD_EXPENSE_REQUEST_CODE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("MainActivity", "Activity Result called");
        if (requestCode == ADD_EXPENSE_REQUEST_CODE) {
            if (resultCode == AddTodoActivity.ADD_RESULT_CODE) {
                String title = data.getStringExtra(AddTodoActivity.TITLE_KEY);
                String day = data.getStringExtra(AddTodoActivity.DAY_KEY);
                String date= data.getStringExtra(AddTodoActivity.DATE_KEY);
                String time= data.getStringExtra(AddTodoActivity.TIME_KEY);
                todo todo = new todo(title, day,date,time);
                todoOpenHepler openHelper = todoOpenHepler.getInstance(this);
                SQLiteDatabase database = openHelper.getWritableDatabase();

                ContentValues contentValues = new ContentValues();
                contentValues.put(Contract.Todo.COLUMN_NAME,todo.getName());
                contentValues.put(Contract.Todo.COLUMN_DAY,todo.getDay());
                contentValues.put(Contract.Todo.COLUMN_DATE,todo.getDate());
                contentValues.put(Contract.Todo.COLUMN_TIME,todo.getTime());


                long id = database.insert(Contract.Todo.TABLE_NAME,null,contentValues);
                if (id > -1L) {
                    todo.setId(id);
                    todos.add(todo);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}