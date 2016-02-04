package com.cs442.project.team13;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseImageView;

import java.util.List;

/**
 * Created by Priyanka on 11/4/2015.
 */
public class SearchBookListAdapter extends ArrayAdapter<Book> {

    private Context context;
    private List<Book> books;

    public SearchBookListAdapter(Context context, List<Book> objects) {
        super(context, R.layout.book_search_row_item, objects);
        this.context = context;
        this.books = objects;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        if(convertView == null){
            LayoutInflater mLayoutInflater = LayoutInflater.from(context);
            convertView = mLayoutInflater.inflate(R.layout.book_search_row_item, null);
        }

        final Book book = books.get(position);

        TextView srno = (TextView) convertView.findViewById(R.id.number);
        srno.setText(position+1+"");

        TextView isbn = (TextView) convertView.findViewById(R.id.isbn);
        isbn.setText(book.getIsbn());

        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText(book.getTitle());

        TextView author = (TextView) convertView.findViewById(R.id.author);
        author.setText(book.getAuthor());


        return convertView;
    }


}
