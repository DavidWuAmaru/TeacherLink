package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_choose_identity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_choose_identity extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MySQLiteHelper dbHelper;

    Button stu;
    Button teacher;

    public Fragment_choose_identity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_choose_identity newInstance(String param1, String param2) {
        Fragment_choose_identity fragment = new Fragment_choose_identity();
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

        View v = inflater.inflate(R.layout.choose_identity, container, false);
        stu = (Button)v.findViewById(R.id.stu);
        teacher = (Button)v.findViewById(R.id.teacher);

        dbHelper = new MySQLiteHelper(container.getContext(), "final.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql = "SELECT * FROM tem WHERE _id = 1";
        Cursor c = db.rawQuery(sql,null);
        c.moveToFirst();

        String account_name = c.getString(1);

        String sql2 = "SELECT * FROM member WHERE account_name = '"+account_name+"'";
        Cursor cursor = db.rawQuery(sql2,null);
        cursor.moveToFirst();
        if(cursor.getCount() == 0){
            stu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sql3 = "INSERT INTO member (account_name,identity) VALUES ('"+account_name+"','stu')";
                    db.execSQL(sql3);
                    Navigation.findNavController(container).navigate(R.id.action_choose_identity_to_stu);
                }
            });
            teacher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sql3 = "INSERT INTO member (account_name,identity) VALUES ('"+account_name+"','teacher')";
                    db.execSQL(sql3);
                    Navigation.findNavController(container).navigate(R.id.action_choose_identity_to_teacher);
                }
            });
        }else if(cursor.getString(2).equals("")){
            stu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sqlstr = "update member set identity = 'stu' where account_name = '" + account_name + "'";
                    db.execSQL(sqlstr);
                    Navigation.findNavController(container).navigate(R.id.action_choose_identity_to_stu);
                }
            });
            teacher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sqlstr = "update member set identity = 'teacher' where account_name = '" + account_name + "'";
                    db.execSQL(sqlstr);
                    Navigation.findNavController(container).navigate(R.id.action_choose_identity_to_teacher);
                }
            });
        }else {
            if (cursor.getString(2).equals("stu")){
                Navigation.findNavController(container).navigate(R.id.action_choose_identity_to_stu);
            }else if(cursor.getString(2).equals("teacher")){
                Navigation.findNavController(container).navigate(R.id.action_choose_identity_to_teacher);
            }
        }
        return v;
    }
}