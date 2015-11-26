package h3.com.toolbartest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ngh1 on 2015-11-26.
 */
public class AnimalAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Animal> arrayList;
    private LayoutInflater layoutInflater;

    public AnimalAdapter(Context context, int layout, ArrayList<Animal> arrayList) {
        this.context = context;
        this.layout = layout;
        this.arrayList = arrayList;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.animalView.setImageResource((arrayList.get(position)).img);

        viewHolder.checkBox.setChecked(arrayList.get(position).isChecked);
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.checkBox.setChecked(viewHolder.checkBox.isChecked());
                arrayList.get(position).isChecked = viewHolder.checkBox.isChecked();
            }
        });

        return convertView;
    }

    static class ViewHolder {
        /////////////////////////////////////////

        @Bind(R.id.animalImage)
        AnimalView animalView;

        @Bind(R.id.animalCheckBox)
        CheckBox checkBox;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
