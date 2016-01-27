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
        if (groupPosition < 0 && groupPosition > 4) {
            return null;
        }

        switch (groupPosition) {
            case 0:
                convertView = changeProfile(convertView, parent); break;
            case 1:
                convertView = changeSensorData(convertView, parent); break;
            case 2:
                convertView = changeSensorConfiguration(convertView, parent); break;
            case 3:
                convertView = changeScheduler(convertView, parent); break;
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    private View changeProfile(View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_child_profile_layout, null);
        }

        final String beforeName = animal.getName();

        ImageButton animalImg = (ImageButton) convertView.findViewById(R.id.list_child_profile_img);
        final EditText editName = (EditText) convertView.findViewById(R.id.list_child_profile_name);
        EditText editMemo = (EditText) convertView.findViewById(R.id.list_child_profile_memo);
        EditText editDevice = (EditText) convertView.findViewById(R.id.list_child_profile_device);
        Button saveBtn = (Button) convertView.findViewById(R.id.list_child_profile_save_btn);

        animalImg.setImageResource(animal.getImg());
        editName.setText(animal.getName());
        editMemo.setText(animal.getMemo());
        editDevice.setText(animal.getDevice());

        editName.addTextChangedListener(new AnimalTextWatcher(AnimalTextWatcher.NAME_CHANGE));
        editMemo.addTextChangedListener(new AnimalTextWatcher(AnimalTextWatcher.MEMO_CHANGE));
        editDevice.addTextChangedListener(new AnimalTextWatcher(AnimalTextWatcher.DEVICE_CHANGE));

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "changed!", Toast.LENGTH_LONG).show();
                AnimalDatabase.getInstance(context).removeAnimal(beforeName);
                intent.putExtra(Define.INTENT_ANIMAL, animal);
            }
        });

        return convertView;
    }

    private View changeSensorData(View convertView, ViewGroup parent) {

        return convertView;
    }

    private View changeSensorConfiguration(View convertView, ViewGroup parent) {

        return convertView;
    }

    private View changeScheduler(View convertView, ViewGroup parent) {

        return convertView;
    }


    class AnimalTextWatcher implements TextWatcher {
        public static final int NAME_CHANGE = 1;
        public static final int MEMO_CHANGE = NAME_CHANGE + 1;
        public static final int DEVICE_CHANGE = MEMO_CHANGE + 1;

        private int flag;

        AnimalTextWatcher(int flag) {
            this.flag = flag;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) {
            if (!s.toString().trim().equals("")) {
                switch (flag) {
                    case NAME_CHANGE:
                        animal.setName(s.toString().trim());
                        break;

                    case MEMO_CHANGE:
                        animal.setMemo(s.toString().trim());
                        break;

                    case DEVICE_CHANGE:
                        animal.setDevice(s.toString().trim());
                        break;

                    default:
                        // throw exception : unexpected flag
                }
            }
        }
    }
}
