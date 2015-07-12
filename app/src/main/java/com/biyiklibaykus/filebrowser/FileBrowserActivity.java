package com.biyiklibaykus.filebrowser;

import android.os.Bundle;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class FileBrowserActivity extends ListActivity
{
    public static final String EXTRA_FILE_PATH = "filebrowser";

    private static final String TAG = "FileBrowserActivity";

    private ArrayAdapter<File> mAdapter;
    private File mCurrentDir;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);



        mAdapter = new ArrayAdapter<File>(this,android.R.layout.simple_list_item_1)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View v = super.getView(position, convertView, parent);
                TextView textView = (TextView) v.findViewById(android.R.id.text1);
                File f = getItem(position);
                textView.setText(f.getName());
                if(f.isDirectory())
                {
                    textView.setTextColor(Color.parseColor("#8d2800"));
                }else
                {
                    textView.setTextColor(Color.BLACK);
                }

                return v;
            }
        };
        setListAdapter(mAdapter);

        mCurrentDir = Environment.getExternalStorageDirectory();
        Log.d(TAG, "Start Path" + mCurrentDir.getPath());
        updatePath(mCurrentDir);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        File file = mAdapter.getItem(position);
        updatePath(file);

        super.onListItemClick(l, v, position, id);
    }

    private boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        String message = "hohey";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        back();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 1000);
    }


    private void back()
    {
        // when pressed back button id possible
        // change dir to parent dir
        File parentDir = mCurrentDir.getParentFile();
        if(parentDir != null)
        {
            updatePath(parentDir);

        }
    }

    private void updatePath(File file)
    {
        if(file.isDirectory())
        {
            // if selected file is a directory open.
            mCurrentDir = file;
            File[] files = file.listFiles();
            mAdapter.clear();
            for (File f: files)
            {
                mAdapter.add(f);
            }
        }else
        {
            // id selected file is a file return file path as intent's result
            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_FILE_PATH,file.getAbsolutePath());
            setResult(RESULT_OK, resultIntent);
            finish();

        }
    }




}