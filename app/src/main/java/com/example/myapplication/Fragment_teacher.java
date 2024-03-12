package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_teacher#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_teacher extends Fragment {

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

    Button creat_class;

    public Fragment_teacher() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment teacher.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_teacher newInstance(String param1, String param2) {
        Fragment_teacher fragment = new Fragment_teacher();
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
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
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
        View v = inflater.inflate(R.layout.teacher, container, false);

        mRecyclerView = v.findViewById(R.id.message);
        dbHelper = new MySQLiteHelper(container.getContext(), "final.db", null, 1);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "SELECT * FROM tem WHERE _id = 1";
        Cursor c = db.rawQuery(sql,null);
        c.moveToFirst();

        mAdapter = new mAdapter(dbHelper.getMessageByName(c.getString(1)),container);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        creat_class = (Button)v.findViewById(R.id.creat_class);
        creat_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(container).navigate(R.id.action_teacher_to_add_class);
            }
        });

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
            private TextView title,stu_name,stu_id,type;
            private View mView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                title = (TextView)itemView.findViewById(R.id.title);
                stu_name = (TextView)itemView.findViewById(R.id.stu_name);
                stu_id = (TextView)itemView.findViewById(R.id.stu_id);
                type  = (TextView)itemView.findViewById(R.id.type);
                mView  = itemView;
            }
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_view,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.title.setText("主旨：" + data.get(position).get("title"));
            holder.stu_name.setText("姓名：" + data.get(position).get("stu_name"));
            holder.stu_id.setText("學號：" + data.get(position).get("stu_id"));
            holder.type.setText(data.get(position).get("type"));

            holder.mView.setOnClickListener((v)->{
//                Toast.makeText(v.getContext(),data.get(position).get("message_id"),Toast.LENGTH_SHORT).show();
                Bundle bundle = new Fragment_teacherArgs.Builder().setMessageId(Integer.parseInt(data.get(position).get("message_id"))).build().toBundle();
                if(data.get(position).get("type").equals("請假")){
                    Navigation.findNavController(container).navigate(R.id.action_teacher_to_leave, bundle);
                }else if(data.get(position).get("type").equals("訊息")){
                    Navigation.findNavController(container).navigate(R.id.action_teacher_to_message, bundle);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}