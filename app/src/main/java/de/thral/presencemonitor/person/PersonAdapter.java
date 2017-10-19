package de.thral.presencemonitor.person;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import de.thral.presencemonitor.R;

/**
 * Created by Markus Thral on 18.10.2017.
 */

public class PersonAdapter extends ArrayAdapter<Person> {
    private Context context;

    public PersonAdapter(Context context, int resource, List<Person> persons) {
        super(context, resource, persons);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.listitem_person, parent, false);

        TextView firstName = (TextView) rowView.findViewById(R.id.firstName);
        TextView lastName = (TextView) rowView.findViewById(R.id.lastName);

        Person person = getItem(position);

        firstName.setText(person.getFirstName());
        lastName.setText(person.getLastName());

        if(person.isPresent()){
            rowView.setBackgroundColor(Color.argb(125,75,236,90));
        } else {
            rowView.setBackgroundColor(Color.argb(0,0,0,0));
        }

        return rowView;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}