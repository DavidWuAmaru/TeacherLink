package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_leave#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_leave extends Fragment {

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
    TextView leave;
    TextView no_attachment;
    TextView content;

    Button allow;
    Button refuse;
    Button check;

    TextView allowed;

    public Fragment_leave() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_message_info.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_leave newInstance(String param1, String param2) {
        Fragment_leave fragment = new Fragment_leave();
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
        View v = inflater.inflate(R.layout.leave, container, false);
        Bundle bundle = getArguments();

        title = (TextView)v.findViewById(R.id.title);
        class_name = (TextView)v.findViewById(R.id.class_name);
        stu_name = (TextView)v.findViewById(R.id.stu_name);
        stu_id = (TextView)v.findViewById(R.id.stu_id);
        leave = (TextView)v.findViewById(R.id.leave);
        no_attachment = (TextView)v.findViewById(R.id.no_attachment);
        content = (TextView)v.findViewById(R.id.content);

        allow = (Button)v.findViewById(R.id.allow);
        refuse = (Button)v.findViewById(R.id.refuse);
        check = (Button)v.findViewById(R.id.check);

        allowed = (TextView)v.findViewById(R.id.allowed);

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
            leave.setText("請假類型：" + c.getString(7));
            if(c.getString(9).equals("")){
                content.setText("無");
            }else{
                content.setText(c.getString(9));
            }

            if(c.getString(8).equals("無")){
                check.setVisibility(View.GONE);
                no_attachment.setVisibility(View.VISIBLE);
            }

            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Fragment_leaveArgs.Builder().setImgPath(c.getString(8)).build().toBundle();
                    Navigation.findNavController(container).navigate(R.id.action_leave_to_attachment,bundle);
                }
            });

            allowed(c);

            allow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setMessage("是否確定允許此申請?");
                    alert.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int i) {
                            String sqlstr = "update message set leave_allow = '允許' where _id = " + message_id;
                            db.execSQL(sqlstr);
                            Cursor c = db.rawQuery(sql,null);
                            c.moveToFirst();
                            allowed(c);
                        }
                    });
                    alert.setNegativeButton("否",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int i) {
                        }
                    });
                    alert.show();
                }
            });

            refuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setMessage("是否確定拒絕此申請?");
                    alert.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int i) {
                            String sqlstr = "update message set leave_allow = '拒絕' where _id = " + message_id;
                            db.execSQL(sqlstr);
                            Cursor c = db.rawQuery(sql,null);
                            c.moveToFirst();
                            allowed(c);
                        }
                    });
                    alert.setNegativeButton("否",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int i) {
                        }
                    });
                    alert.show();
                }
            });
        }
        return v;
    }
    public void allowed(Cursor c){
        if(c.getString(10) != null){
            allow.setVisibility(View.GONE);
            refuse.setVisibility(View.GONE);
            allowed.setVisibility(View.VISIBLE);
            allowed.setText("已" + c.getString(10) + "此申請");
        }
    }
}