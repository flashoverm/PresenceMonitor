package de.thral.presencemonitor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import de.thral.presencemonitor.person.Person;
import de.thral.presencemonitor.person.PersonAdapter;

public class PresenceActivity extends AppCompatActivity {

    private StorageLayer storage;
    private ListView personListView;
    private PersonAdapter personAdapter;

    private AdapterView.OnItemClickListener clickListener;
    private AdapterView.OnItemLongClickListener longClickListener;
    private Toast nothingSelected;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presence);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        storage = new StorageLayer(getApplicationContext());

        clickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Person clicked = (Person) adapterView.getAdapter().getItem(i);
                storage.tooglePresence(clicked);
                updateList();
            }
        };

        longClickListener = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Person clicked = (Person) adapterView.getAdapter().getItem(i);
                storage.removePerson(clicked);
                updateList();
                return true;
            }
        };

        nothingSelected = Toast.makeText(
                this.getApplicationContext(), R.string.not_selected, Toast.LENGTH_LONG
        );

        personListView = (ListView) findViewById(R.id.list_persons);
        personAdapter = new PersonAdapter(
                this.getBaseContext(), R.layout.listitem_person, storage
        );
        personListView.setAdapter(personAdapter);
        personListView.setOnItemClickListener(clickListener);
        personListView.setOnItemLongClickListener(longClickListener);

        updateList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_presence, menu);
        this.menu = menu;
        menu.findItem(R.id.menu_entered).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_addPerson) {
            updateList();
            showAddPersonDialog();
            return true;
        }
        else if(id == R.id.menu_chosen_modify) {
            if(!storage.isFiltered()){
                if(storage.filterForPresent()){
                    item.setTitle(R.string.action_modify);
                    menu.findItem(R.id.menu_entered).setVisible(true);
                    menu.findItem(R.id.menu_addPerson).setVisible(false);
                    personListView.setOnItemClickListener(null);
                    personListView.setOnItemLongClickListener(null);
                } else {
                    nothingSelected.show();
                }
            } else {
                storage.removeFilter();
                item.setTitle(R.string.action_chosen);
                menu.findItem(R.id.menu_entered).setVisible(false);
                menu.findItem(R.id.menu_addPerson).setVisible(true);
                personListView.setOnItemClickListener(clickListener);
                personListView.setOnItemLongClickListener(longClickListener);
            }
            updateList();
            return true;
        }
        else if(id == R.id.menu_entered) {
            menu.findItem(R.id.menu_chosen_modify).setTitle(R.string.action_chosen);
            item.setVisible(false);
            menu.findItem(R.id.menu_addPerson).setVisible(true);
            personListView.setOnItemClickListener(clickListener);
            personListView.setOnItemLongClickListener(longClickListener);

            storage.tooglePresentRemoveFilter();
            updateList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateList(){
        if(personAdapter != null){
            personAdapter.notifyDataSetChanged();
        }
    }

    public void showAddPersonDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_person_add, null);
        builder.setView(dialogView)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText firstName = (EditText)dialogView.findViewById(R.id.dialog_firstname);
                        EditText lastName = (EditText)dialogView.findViewById(R.id.dialog_lastname);

                        storage.addPerson(new Person(
                                        firstName.getText().toString(),
                                        lastName.getText().toString())
                        );
                        updateList();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setTitle(R.string.action_addPerson);
        builder.create().show();
    }
}
