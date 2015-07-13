package com.msu_software_factory.thesortingguide;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.app.Fragment;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.widget.EditText;
import android.text.InputType;
import android.widget.AdapterView.OnItemSelectedListener;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private static int method;

    private Fragment about = new AboutFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        PreferenceManager.setDefaultValues(this, R.xml.preferences, true);

    }
    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }
        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(3);

        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, replaceSelectedTab(position + 1))
                .commit();
    }

    public Fragment replaceSelectedTab(int pos){
        if(pos == 1){
            return about;
        }else if (pos == 3) {
            return new PrefsFragment();
        }else{
            return PlaceholderFragment.newInstance(pos);
        }
    }

    // Selection Options
    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = "About";
                break;
            case 2:
                mTitle = "Sort";
                break;
            case 3:
                mTitle = "Settings";
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            Spinner spinner = (Spinner) rootView.findViewById(R.id.sortChoice);
                   // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                    R.array.sort_choice, android.R.layout.simple_spinner_item);
                        // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // Apply the adapter to the spinner
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new SortSpinner());
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
    public void enter(View view) {
        int[] toSort;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        System.out.println("!!!!!!!!! " + prefs.getBoolean("random_numbers", false));
        if (prefs.getBoolean("random_numbers", false)){
            toSort = Sorting.randList(10);
            toSort = sort(toSort, method);
            TextView resultBox = (TextView) findViewById(R.id.result_text);
            resultBox.setText(Sorting.toString(toSort));
        }else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Enter randomly assorted numbers");
            alertDialog.setMessage("Enter any amount of randomly assorted numbers (the lesser the faster) ranging between and including 0 and 99, with each number separated by commas.");
            final EditText input = new EditText(this); //  INPUT VARIABLE
            input.setHint("For example: 99, 2, 34, 56, 68");
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            alertDialog.setView(input);
            alertDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    System.out.println(input.getText().toString());
                    int[] toSort = parseArray(input.getText().toString());
                    toSort = sort(toSort, method);
                    TextView resultBox = (TextView) findViewById(R.id.result_text);
                    resultBox.setText(Sorting.toString(toSort));
                    dialog.dismiss();
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            alertDialog.show();

        }
    }

    public int[] parseArray(String in) {
        String[] bits = in.split(",");
        int[] arr = new int[bits.length];
        try {
            for (int i = 0; i < bits.length; i++) {
                String bit = bits[i].trim();
                arr[i] = Integer.parseInt(bit);
            }
        } catch (Exception e) {
            return null;
        }
        return arr;
    }

    public int[] sort(int[] theSort, int method){
        int[] returnThis;
        switch (method){
            case 0:
                returnThis = Sorting.bubbleSort(theSort);
                break;
            case 1:
                returnThis = Sorting.selectionSort(theSort);
                break;
            default:
                returnThis = Sorting.insertionSort(theSort);
                break;
        }
        return returnThis;
    }

    public static class SortSpinner extends Activity implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            method = pos;
        }
        public void onNothingSelected(AdapterView<?> parent) {}
    }
}

