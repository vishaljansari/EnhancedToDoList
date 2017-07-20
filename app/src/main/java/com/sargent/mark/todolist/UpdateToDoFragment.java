package com.sargent.mark.todolist;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by mark on 7/5/17.
 */

public class UpdateToDoFragment extends DialogFragment {

   private EditText toDo;
   private DatePicker dp;
   private Button add;
   private ArrayAdapter<CharSequence> spinnerAdapter;

   // ADDED SPINNER FOR USER INTERFACE TO UPDATE THE OLD VALUE
   private Spinner categorySpinner;
   private final String TAG = "updatetodofragment";
   private long id;

   public UpdateToDoFragment() {
   }

   public static UpdateToDoFragment newInstance(int year, int month, int day, String description, String category, long id) {
      UpdateToDoFragment f = new UpdateToDoFragment();

      // Supply num input as an argument.
      Bundle args = new Bundle();
      args.putInt("year", year);
      args.putInt("month", month);
      args.putInt("day", day);
      args.putLong("id", id);
      args.putString("description", description);
      // ADDED CATEGORY TO THE ARGS TO UPDATE THE CATEGORY
      args.putString("category", category);

      f.setArguments(args);

      return f;
   }

   //To have a way for the activity to get the data from the dialog
   public interface OnUpdateDialogCloseListener {
      void closeUpdateDialog(int year, int month, int day, String description, String category, long id);
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_to_do_adder, container, false);
      toDo = (EditText) view.findViewById(R.id.toDo);
      dp = (DatePicker) view.findViewById(R.id.datePicker);
      add = (Button) view.findViewById(R.id.add);

      // INITIALIZED THE SPINNER FROM THE XML LAYOUT
      categorySpinner = (Spinner) view.findViewById(R.id.categorySpinner);
      // SET THE ADAPTER FOR THE UPDATED VALUE OF SPINNER
      spinnerAdapter = ArrayAdapter.createFromResource(this.getContext(), R.array.category_array, android.R.layout.simple_spinner_item);
      spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      categorySpinner.setAdapter(spinnerAdapter);

      int year = getArguments().getInt("year");
      int month = getArguments().getInt("month");
      int day = getArguments().getInt("day");
      id = getArguments().getLong("id");
      String description = getArguments().getString("description");
      String category = getArguments().getString("category");
      dp.updateDate(year, month, day);
      toDo.setText(description);
      add.setText("Update");
      add.setOnClickListener(new View.OnClickListener() {

         @Override
         public void onClick(View v) {
            UpdateToDoFragment.OnUpdateDialogCloseListener activity = (UpdateToDoFragment.OnUpdateDialogCloseListener) getActivity();
            Log.d(TAG, "id: " + id);

            // PASSED CATEGORY VALUE TO THE LISTNER
            activity.closeUpdateDialog(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(), toDo.getText().toString(), categorySpinner.getSelectedItem().toString(), id);
            UpdateToDoFragment.this.dismiss();
         }
      });


      int selectedcategory = spinnerAdapter.getPosition(category);
      categorySpinner.setSelection(selectedcategory);

      return view;
   }
}