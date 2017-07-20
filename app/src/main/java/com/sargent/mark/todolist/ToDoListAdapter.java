package com.sargent.mark.todolist;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sargent.mark.todolist.data.Contract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mark on 7/4/17.
 */
public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ItemHolder> {

   private Cursor cursor;
   private ItemClickListener listener;
   private String TAG = "todolistadapter";

   @Override
   public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

      Context context = parent.getContext();
      LayoutInflater inflater = LayoutInflater.from(context);

      View view = inflater.inflate(R.layout.item, parent, false);
      ItemHolder holder = new ItemHolder(view);
      return holder;
   }

   @Override public void onBindViewHolder(ItemHolder holder, int position) {
      holder.bind(holder, position);
   }

   @Override public int getItemCount() {
      return cursor.getCount();
   }

   public interface ItemClickListener {
      void onItemClick(int pos, String description, String duedate, String category, long id);
   }


   public ToDoListAdapter(Cursor cursor, ItemClickListener listener) {
      this.cursor = cursor;
      this.listener = listener;
   }

   public void swapCursor(Cursor newCursor) {
      if (cursor != null) {
         cursor.close();
      }
      cursor = newCursor;
      if (newCursor != null) {
         // Force the RecyclerView to refresh
         this.notifyDataSetChanged();
      }
   }

   class ItemHolder extends RecyclerView.ViewHolder
         implements View.OnClickListener{
      TextView categ;
      TextView descr;
      TextView due;
      String duedate;
      String description;
      String category;
      long id;


      ItemHolder(View view) {
         super(view);
         descr = (TextView) view.findViewById(R.id.description);
         due = (TextView) view.findViewById(R.id.dueDate);
         categ = (TextView) view.findViewById(R.id.category);
         view.setOnClickListener(this);
      }

      public void bind(ItemHolder holder, int pos) {
         cursor.moveToPosition(pos);

         id = cursor.getLong(cursor.getColumnIndex(Contract.TABLE_TODO._ID));
         Log.d(TAG, "deleting id: " + id);

         duedate = cursor.getString(
               cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_DUE_DATE));
         description = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_DESCRIPTION));
         category = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_CATEGORY));

         descr.setText(description);
         due.setText(DateFormatter(duedate));
         categ.setText(category);
         holder.itemView.setTag(id);
      }

      private String DateFormatter(String dateString) {
         try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = simpleDateFormat.parse(dateString);
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("EEE, MMM d, yyyy");
            return simpleDateFormat1.format(date);
         } catch (ParseException e) {
            Log.e(TAG, "Error occured: " + dateString, e);
            return dateString;
         }
      }

      @Override public void onClick(View v) {
         int pos = getAdapterPosition();
         listener.onItemClick(pos, description, duedate, category, id);
      }
   }
}
