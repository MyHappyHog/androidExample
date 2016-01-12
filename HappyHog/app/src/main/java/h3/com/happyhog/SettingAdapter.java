package h3.com.happyhog;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by ngh1 on 2016-01-04.
 */
public class SettingAdapter extends BaseExpandableListAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<String> groupList;
    private Intent intent;
    // private ArrayList<ArrayList<String>> childList;
    private Animal animal;

    public SettingAdapter(Context context, Animal animal, Intent intent) {
        this.context = context;
        // this.groupList = groupList;
        // this.childList = childList; not needed...
        this.layoutInflater = LayoutInflater.from(context);
        this.animal = animal;
        this.intent = intent;

        groupList = new ArrayList<String>();
        groupList.add("Profile");
        //groupList.add("Sensor Data");
        //groupList.add("Sensor Configuration");
        //groupList.add("Scheduler");
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
        // return childList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
        // return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_group_layout, null);
        }

        TextView header = (TextView) convertView.findViewById(R.id.list_group_text);
        header.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        switch (groupPosition) {
            case 0:
                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.list_child_profile_layout, null);
                }

                ImageButton animalImg = (ImageButton) convertView.findViewById(R.id.list_child_profile_img);
                final EditText editName = (EditText) convertView.findViewById(R.id.list_child_profile_name);
                EditText editMemo = (EditText) convertView.findViewById(R.id.list_child_profile_memo);
                EditText editDevice = (EditText) convertView.findViewById(R.id.list_child_profile_device);
                Button saveBtn = (Button) convertView.findViewById(R.id.list_child_profile_save_btn);

                animalImg.setImageResource(animal.img);
                editName.setText(animal.name);
                editMemo.setText(animal.memo);
                editDevice.setText(animal.device);

                editName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!s.toString().trim().equals("")) {
                            animal.name = s.toString().trim();
                        }
                    }
                });

                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "changed ? : " + animal.name, Toast.LENGTH_LONG).show();
                        intent.putExtra(Define.ANIMAL_NAME, animal.name);
                    }
                });
                return convertView;

            case 1:
            case 2:
            case 3:

            default:

        }

        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
