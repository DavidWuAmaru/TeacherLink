package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

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
 * Use the {@link Fragment_send_leave#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_send_leave extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView title;
    TextView content;
    TextView stu_name;
    TextView stu_id;

    Spinner leave;

    Button send;

    private Fragment mfragment_send_leave = null;

    private MySQLiteHelper dbHelper;

    public Fragment_send_leave() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_send_leave.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_send_leave newInstance(String param1, String param2) {
        Fragment_send_leave fragment = new Fragment_send_leave();
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
        View v = inflater.inflate(R.layout.send_leave, container, false);

        Bundle bundle = getArguments();

        title = (TextView)v.findViewById(R.id.title);
        content = (TextView)v.findViewById(R.id.content);
        stu_name = (TextView)v.findViewById(R.id.stu_name);
        stu_id = (TextView)v.findViewById(R.id.stu_id);

        String[] leave_type = {"事假(婚、喪)", "病假", "公假", "產假、陪產假", "其他(需留言註明原因)"};
        ArrayAdapter<String> leave_type_list = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                leave_type);

        leave = (Spinner)v.findViewById(R.id.leave);
        leave.setAdapter(leave_type_list);

        send = (Button)v.findViewById(R.id.send);

        dbHelper = new MySQLiteHelper(container.getContext(), "final.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(title.getText().toString().equals("") || stu_name.getText().toString().equals("") || stu_id.getText().toString().equals("")){
                    Toast.makeText(getContext(), "❌請輸入完整資訊", Toast.LENGTH_SHORT).show();
                }else if(bundle != null) {
                    int class_id = Fragment_stuArgs.fromBundle(getArguments()).getClassId();
                    String sql = "SELECT * FROM class WHERE _id = " + class_id;
                    Cursor c = db.rawQuery(sql,null);
                    c.moveToFirst();

                    String sql2 = "SELECT * FROM tem WHERE _id = 1";
                    Cursor c2 = db.rawQuery(sql2,null);
                    c2.moveToFirst();

                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setMessage("課程名稱："+c.getString(2)+
                            "\n主旨："+title.getText()+
                            "\n姓名："+stu_name.getText()+
                            "\n學號："+stu_id.getText()+
                            "\n請假類型："+leave.getSelectedItem()+
                            "\n留言："+content.getText()+
                            "\n\n是否進入下一頁？");
                    alert.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int i) {
                            String sql3 = "INSERT INTO message (account_name,class_name,title,stu_name,stu_id,type,leave,content,stu_account_name,attachment) VALUES ('"+c.getString(1)+"','"+c.getString(2)+"','"+title.getText()+"','"+stu_name.getText()+"','"+stu_id.getText()+"','請假','"+leave.getSelectedItem()+"','"+content.getText()+"','"+c2.getString(1)+"',";
                            Bundle bundle = new Fragment_send_leaveArgs.Builder().setSql(sql3).build().toBundle();
                            title.setText("");
                            stu_name.setText("");
                            stu_id.setText("");
                            leave.setSelection(0);
                            content.setText("");
                            Navigation.findNavController(container).navigate(R.id.action_send_leave_to_take_picture,bundle);
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