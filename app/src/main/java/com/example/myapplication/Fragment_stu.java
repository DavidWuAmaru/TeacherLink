package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_stu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_stu extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView mRecyclerView;
    mAdapter mAdapter;
    private MySQLiteHelper dbHelper;

    public Fragment_stu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment0.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_stu newInstance(String param1, String param2) {
        Fragment_stu fragment = new Fragment_stu();
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
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                getActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.stu, container, false);

        mRecyclerView = v.findViewById(R.id.class_list);
        dbHelper = new MySQLiteHelper(container.getContext(), "final.db", null, 1);

        mAdapter = new mAdapter(dbHelper.getAllClass(),container);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql = "SELECT * FROM tem WHERE _id = 1";
        Cursor c = db.rawQuery(sql,null);
        c.moveToFirst();

        String sql2 = "SELECT * FROM message WHERE stu_account_name = '" + c.getString(1)+"'";
        Cursor c2 = db.rawQuery(sql2,null);

        int i =0;
        while (c2.moveToNext()) {
            if(c2.getInt(12) == 0 && c2.getString(10) != null && c2.getString(6).equals("請假")){
                NotificationHelper notificationHelper = new NotificationHelper(getActivity());
                String notice = "您對 " + c2.getString(2) + " 提出的請假申請以被教師 " + c2.getString(10);
                NotificationCompat.Builder nb = notificationHelper.notificationChannelBuild(notice);
                notificationHelper.getManager().notify(i,nb.build());

                String sqlstr = "update message set Notice = 1 where stu_account_name = '" + c.getString(1)+"'";
                db.execSQL(sqlstr);
                i+=1;
            }
        }
        return v;
    }
    public class mAdapter extends RecyclerView.Adapter<mAdapter.ViewHolder>{

        private ArrayList<HashMap<String, String>> data;
        private ViewGroup container;

        public mAdapter (ArrayList<HashMap<String, String>> data, ViewGroup container){
            this.data = data;
            this.container = container;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            private TextView class_name,class_time,teacher_name;
            private View mView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                class_name = (TextView)itemView.findViewById(R.id.title);
                class_time = (TextView)itemView.findViewById(R.id.class_time);
                teacher_name = (TextView)itemView.findViewById(R.id.content);
                mView  = itemView;
            }
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_view,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.class_name.setText("課程名稱：" + data.get(position).get("class_name"));
            holder.class_time.setText("上課時間：" + data.get(position).get("classtime"));
            holder.teacher_name.setText("授課教師：" + data.get(position).get("teacher_name"));

            holder.mView.setOnClickListener((v)->{
                Bundle bundle = new Fragment_stuArgs.Builder().setClassId(Integer.parseInt(data.get(position).get("class_id"))).build().toBundle();
                Navigation.findNavController(container).navigate(R.id.action_stu_to_class_info, bundle);
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}