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
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_class_info#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_class_info extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MySQLiteHelper dbHelper;

    TextView class_name;
    TextView class_time;
    TextView teacher_name;

    Button message;
    Button leave;

    public Fragment_class_info() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_send_message.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_class_info newInstance(String param1, String param2) {
        Fragment_class_info fragment = new Fragment_class_info();
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
        View v = inflater.inflate(R.layout.class_info, container, false);

        Bundle bundle = getArguments();

        class_name = (TextView)v.findViewById(R.id.title);
        class_time = (TextView)v.findViewById(R.id.class_time);
        teacher_name = (TextView)v.findViewById(R.id.content);

        message = (Button)v.findViewById(R.id.message);
        leave = (Button)v.findViewById(R.id.leave);

        dbHelper = new MySQLiteHelper(container.getContext(), "final.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if(bundle != null)
        {
            int class_id = Fragment_stuArgs.fromBundle(getArguments()).getClassId();
            String sql = "SELECT * FROM class WHERE _id = " + class_id;
            Cursor c = db.rawQuery(sql,null);
            c.moveToFirst();

            class_name.setText("課程名稱：" + c.getString(2));
            class_time.setText("上課時間：" + c.getString(3));
            teacher_name.setText("授課教師：" + c.getString(4));
        }

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Fragment_class_infoArgs.Builder().setClassId(Fragment_stuArgs.fromBundle(getArguments()).getClassId()).build().toBundle();
                Navigation.findNavController(container).navigate(R.id.action_class_info_to_send_message,bundle);
            }
        });

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Fragment_class_infoArgs.Builder().setClassId(Fragment_stuArgs.fromBundle(getArguments()).getClassId()).build().toBundle();
                Navigation.findNavController(container).navigate(R.id.action_class_info_to_send_leave,bundle);
            }
        });

        return v;
    }
}