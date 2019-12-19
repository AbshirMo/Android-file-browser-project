package com.example.myapplication;

import android.content.Context;
//import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private List<String>  mValues;
    private int[] mIcons;

    public CustomAdapter(Context context, List<String> values, int[] icons) {
        super(context, R.layout.row);
        this.mValues = values;
        this.mIcons = icons;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mValues.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.row, parent, false);
            mViewHolder.mFlag = (ImageView) convertView.findViewById(R.id.imageView);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.mName.setText(mValues.get(position));

        String a = mValues.get(position);

        File p = mContext.getExternalFilesDir(a);
        //File f = new File(path);

        if (p.isDirectory()) {
            mViewHolder.mFlag.setImageResource(mIcons[0]);
        }else if(p.isFile()) {
            mViewHolder.mFlag.setImageResource(mIcons[1]);
        }
        else{
            mViewHolder.mFlag.setImageResource(mIcons[2]);
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView mFlag;
        TextView mName;
    }
}
