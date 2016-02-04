package com.cs442.project.team13;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Priyanka on 11/4/2015.
 */
public class UserBookListAdapter extends ArrayAdapter<Book> {
    private Context context;
    private List<Book> books;

    public UserBookListAdapter(Context context, List<Book> objects) {
        super(context, R.layout.book_row_item, objects);
        this.context = context;
        this.books = objects;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        if(convertView == null){
            LayoutInflater mLayoutInflater = LayoutInflater.from(context);
            convertView = mLayoutInflater.inflate(R.layout.book_row_item, null);
        }

        final Book book = books.get(position);

        final TextView infoView = (TextView) convertView.findViewById(R.id.info);

        infoView.setText("ISBN: "+book.getIsbn() + "\n" +"Title: "+ book.getTitle());

        infoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String bookId = getItem(position).getObjectId();
                if(bookId!=null && !bookId.isEmpty())
                {
                    Intent intent = new Intent(getContext(), DisplayBookInfoActivity.class);
                    intent.putExtra("id",bookId);
                    intent.putExtra("providerDetail",false);
                    getContext().startActivity(intent);
                }else{
                    Toast.makeText(getContext(), "Book information not found", Toast.LENGTH_LONG).show();
                }

            }
        });

        convertView.setTag(book);

        ImageButton delete = (ImageButton) convertView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getContext() instanceof DisplayUserBookListActivity)
                    ((DisplayUserBookListActivity)getContext()).delete(v,book);

            }
        });

        ImageButton update = (ImageButton) convertView.findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getContext() instanceof DisplayUserBookListActivity)
                    ((DisplayUserBookListActivity)getContext()).update(v,book.getObjectId());
            }
        });
        return convertView;
    }



}
