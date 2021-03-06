package h3.com.happyhog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ngh1 on 2015-12-29.
 */
public class AnimalAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Animal> arrayList;
    private LayoutInflater layoutInflater;

    private View.OnClickListener onClickListener;

    public AnimalAdapter(Context context, ArrayList<Animal> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public AnimalAdapter(Context context, ArrayList<Animal> arrayList, View.OnClickListener onClickListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.layoutInflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_view_item_layout, null);
            viewHolder = new ViewHolder(convertView, position);
            //viewHolder.btnSetting.setOnClickListener(onClickListener);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // values setting
        viewHolder.animal.setImageResource((arrayList.get(position)).getImg());
        viewHolder.text_temperature.setText((arrayList.get(position)).getName() + ", " + (arrayList.get(position)).getState()); // change !!!
        viewHolder.text_humidity.setText((arrayList.get(position)).getKey()); // change !!!

        // camera btn listener
        /*
        viewHolder.btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ((View) (v.getParent())).toString(), Toast.LENGTH_SHORT).show();
            }
        });

         */


        return convertView;
    }

    public static class ViewHolder {
        int pos;

        @Bind(R.id.list_view_animal)
        ImageButton animal;

        @Bind(R.id.btnCamera)
        ImageButton btnCamera;

        @Bind(R.id.btnSetting) ImageButton btnSetting;

        @Bind(R.id.ic_temperature)
        ImageView ic_temperature;

        @Bind(R.id.text_temperature)
        TextView text_temperature;

        @Bind(R.id.text_humidity)
        TextView text_humidity;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
        public ViewHolder(View view, int pos) { ButterKnife.bind(this, view); this.pos = pos; }

    }
}
