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
 * Use the {@link Fragment_message#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_message extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MySQLiteHelper dbHelper;

    TextView title;
    TextView class_name;
    TextView stu_name;
    TextView stu_id;
    TextView attachment;
    TextView content;

    Button exit;

    public Fragment_message() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_message.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_message newInstance(String param1, String param2) {
        Fragment_message fragment = new Fragment_message();
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
        View v = inflater.inflate(R.layout.message, container, false);

        Bundle bundle = getArguments();

        title = (TextView)v.findViewById(R.id.title);
        class_name = (TextView)v.findViewById(R.id.class_name);
        stu_name = (TextView)v.findViewById(R.id.stu_name);
        stu_id = (TextView)v.findViewById(R.id.stu_id);
        attachment = (TextView)v.findViewById(R.id.attachment);
        content = (TextView)v.findViewById(R.id.content);

        dbHelper = new MySQLiteHelper(container.getContext(), "final.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if(bundle != null)
        {
            int message_id = Fragment_teacherArgs.fromBundle(getArguments()).getMessageId();
            String sql = "SELECT * FROM message WHERE _id = " + message_id;
            Cursor c = db.rawQuery(sql,null);
            c.moveToFirst();

            title.setText("主旨：" + c.getString(3));
            class_name.setText("課程名稱：" + c.getString(2));
            stu_name.setText("姓名：" + c.getString(4));
            stu_id.setText("學號：" + c.getString(5));
            if(c.getString(9).equals("")){
                content.setText("無");
            }else{
                content.setText(c.getString(9));
            }
        }

        exit = (Button)v.findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).popBackStack();
            }
        });

        return v;
    }
}