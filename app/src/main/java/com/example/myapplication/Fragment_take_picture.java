package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_take_picture#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_take_picture extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static final int CAMERA_PERMISSION = 100;

    ImageView picture;
    Button take_picture;
    Button choose_picture;
    Button confirm;

    String file_path;

    Bitmap bitmap;

    private MySQLiteHelper dbHelper;

    public Fragment_take_picture() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_take_picture.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_take_picture newInstance(String param1, String param2) {
        Fragment_take_picture fragment = new Fragment_take_picture();
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
        View v = inflater.inflate(R.layout.take_picture, container, false);

        if (getActivity().checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION);

        picture = (ImageView) v.findViewById(R.id.picture);

        choose_picture = (Button) v.findViewById(R.id.choose_picture);
        choose_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 0);
            }
        });

        take_picture = (Button) v.findViewById(R.id.take_picture);
        take_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 1);
                }
            }
        });

        confirm = (Button) v.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    saveScreenShot(bitmap);
                    dbHelper = new MySQLiteHelper(container.getContext(), "final.db", null, 1);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    String sql = Fragment_send_leaveArgs.fromBundle(getArguments()).getSql();
                    sql += "'"+file_path+"')";
                    db.execSQL(sql);
                    Toast.makeText(getContext(), "成功送出申請", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(getView()).popBackStack();
                }catch (Exception e){

                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setMessage("未提供證明文件，是否送出？");
                    alert.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int i) {
                            dbHelper = new MySQLiteHelper(container.getContext(), "final.db", null, 1);
                            SQLiteDatabase db = dbHelper.getWritableDatabase();

                            String sql = Fragment_send_leaveArgs.fromBundle(getArguments()).getSql();
                            sql += "'無')";
                            db.execSQL(sql);
                            Toast.makeText(getContext(), "成功送出申請", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(getView()).popBackStack();
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
    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == 0){
                Uri uri = data.getData();
                ContentResolver cr = getContext().getContentResolver();
                try {
                    bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    picture.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                }
            }
            if (requestCode == 1) {
                Bundle extras = data.getExtras();
                bitmap = (Bitmap) extras.get("data");
                bitmap = rotateBimap(getContext(), 90, bitmap);
                picture.setImageBitmap(bitmap);
            }
        }
    }
    private void saveScreenShot(Bitmap bitmap) {
        if (bitmap.getByteCount() != 0){
            getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            String extStorageDirectory = "/storage/emulated/0/Android/data/com.example.myapplication/files/Pictures/";
            OutputStream outStream = null;
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMddHHmmss");
            String file_name = sdf.format(date)+ ".jpg";
            file_path = extStorageDirectory + file_name;
            File file = new File (extStorageDirectory, file_name);
            try {
                outStream = new FileOutputStream(file); // create an input stream
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.close();
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
            } catch(Exception e) {
                Toast.makeText(getContext(), "exception:" + e,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    private Bitmap rotateBimap(Context context, float degree, Bitmap srcBitmap) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degree);
        Bitmap bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight()
                , matrix, true);
        return bitmap;
    }
}