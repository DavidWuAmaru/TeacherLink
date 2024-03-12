package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_add_class#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_add_class extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView class_name;
    TextView teacher_name;

    Spinner class_start_w;
    Spinner class_start_time;
    Spinner class_end_w;
    Spinner class_end_time;

    Button creat;

    private MySQLiteHelper dbHelper;

    public Fragment_add_class() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment add_class.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_add_class newInstance(String param1, String param2) {
        Fragment_add_class fragment = new Fragment_add_class();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_class, container, false);

        String[] day = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        ArrayAdapter<String> daylist = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                day);

        String[] time = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "A", "B", "C", "D"};
        ArrayAdapter<String> timelist = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                time);

        class_start_w = (Spinner) v.findViewById(R.id.leave);
        class_start_w.setAdapter(daylist);

        class_start_time = (Spinner) v.findViewById(R.id.class_start_time);
        class_start_time.setAdapter(timelist);

        class_end_w = (Spinner) v.findViewById(R.id.class_end_w);
        class_end_w.setAdapter(daylist);

        class_end_time = (Spinner) v.findViewById(R.id.class_end_time);
        class_end_time.setAdapter(timelist);

        class_name = (TextView)v.findViewById(R.id.title);
        teacher_name = (TextView)v.findViewById(R.id.content);
        creat = (Button)v.findViewById(R.id.send);

        dbHelper = new MySQLiteHelper(container.getContext(), "final.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql = "SELECT * FROM tem WHERE _id = 1";
        Cursor c = db.rawQuery(sql,null);
        c.moveToFirst();
        String account_name = c.getString(1);

        creat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(class_name.getText().toString().equals("") || teacher_name.getText().toString().equals("")){
                    Toast.makeText(getContext(), "❌請輸入完整資訊", Toast.LENGTH_SHORT).show();
                }else{
                    String class_time = class_start_w.getSelectedItem().toString()+class_start_time.getSelectedItem().toString()+" 至 "+class_end_w.getSelectedItem().toString()+class_end_time.getSelectedItem().toString();
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setMessage("課程名稱："+class_name.getText().toString()+
                            "\n授課教師："+teacher_name.getText().toString()+
                            "\n上課時間："+class_time+
                            "\n\n確認新增此課程？");
                    alert.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int i) {
                            String sql = "INSERT INTO class (account_name,class_name,teacher_name,classtime) VALUES ('"+account_name+"','"+class_name.getText().toString()+"','"+teacher_name.getText().toString()+"','"+class_time+"')";
                            db.execSQL(sql);
                            class_name.setText("");
                            teacher_name.setText("");
                            class_start_w.setSelection(0);
                            class_start_time.setSelection(0);
                            class_end_w.setSelection(0);
                            class_end_time.setSelection(0);
                        }
                    });
                    alert.setNegativeButton("否",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int i) {
                        }
                    });
                    alert.show();
                }
            }
        });

        return v;
    }
}