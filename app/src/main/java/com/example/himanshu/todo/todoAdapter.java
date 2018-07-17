package com.example.himanshu.todo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class todoAdapter extends ArrayAdapter{
    ArrayList<todo> items;
    LayoutInflater inflater;

    int inflateCount = 0;

    public todoAdapter(@NonNull Context context, ArrayList<todo> items) {
        super(context, 0,  items);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View output = convertView;
        if(output == null){
            inflateCount++;
            Log.d("ExpenseAdapter","Inflate Count:" + inflateCount);
            output = inflater.inflate(R.layout.todorow_layout,parent,false);
            TextView nameTextView = output.findViewById(R.id.todotask);
            TextView dayview = output.findViewById(R.id.day);
            TextView dateview=output.findViewById(R.id.datetext);
            TextView timeview=output.findViewById(R.id.timetext);
            todoholder viewHolder = new todoholder();
            viewHolder.title =nameTextView;
            viewHolder.day =dayview ;
            viewHolder.date=dateview;
            viewHolder.time=timeview;
            output.setTag(viewHolder);
        }
        todoholder viewHolder = (todoholder) output.getTag();
        todo todos= items.get(position);
        viewHolder.title.setText(todos.getName());
        viewHolder.day.setText(todos.getDay() );
        viewHolder.date.setText(todos.getDate());
        viewHolder.time.setText(todos.getTime());
        return output;
    }
}

