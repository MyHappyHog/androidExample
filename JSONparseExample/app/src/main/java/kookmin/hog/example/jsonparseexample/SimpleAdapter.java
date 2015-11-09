package kookmin.hog.example.jsonparseexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sloth on 2015-11-09.
 */

public class SimpleAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private ArrayList<String> data;

    public SimpleAdapter(Context context, ArrayList<String> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override public int getCount() {
        return data.size();
    }

    @Override public String getItem(int position) {
        return data.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        // 기존에 뷰가 있으면 기존의 것을 불러옴. 재사용
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.simple_list_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        String word = getItem(position);
        String data[] = word.split("#");

        holder.temperature.setText("temperature: " + data[0]);
        holder.humidity.setText("humidity: " + data[1]);
        holder.illumination.setText("illumination: " + data[2]);
        // Note: don't actually do string concatenation like this in an adapter's getView.

        return view;
    }

    static class ViewHolder {
        @Bind(R.id.temperature) TextView temperature;
        @Bind(R.id.humidity) TextView humidity;
        @Bind(R.id.illumination) TextView illumination;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}