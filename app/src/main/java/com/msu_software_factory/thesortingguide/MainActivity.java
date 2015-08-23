package com.msu_software_factory.thesortingguide;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.Image;
import android.graphics.Rect;
import android.os.SystemClock;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.widget.EditText;
import android.text.InputType;
import android.widget.AdapterView.OnItemSelectedListener;
import java.util.ArrayList;
import java.util.LinkedList;

import android.os.Handler;


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
    public static int[] toSort;
    public int offsetCount = 0;
    public Handler handler;
    public TextView resultbox;

    private Fragment about = new AboutFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();
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
            ((MainActivity) activity).onSectionAttached(4);

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

    public Fragment replaceSelectedTab(int pos) {
        if (pos == 1) {
            return about;
        } else if (pos == 4) {
            return new PrefsFragment();
        } else if (pos == 3){
            return new Descriptions();
        } else {
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
                mTitle = "Descriptions";
                break;
            case 4:
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
//        retrieves the shared preferences for the randomizer, which is true or false
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getBoolean("random_numbers", false)){
            this.toSort = Sorting.randList(10);
            TextView resultBox = (TextView) findViewById(R.id.result_text);
            toSort = sort(toSort, method);

            this.resultbox = resultBox;
            resultBox.setText(Sorting.toString(toSort));
            SortView.setToSort(Sorting.sortSteps.steps);
            SortView.setActivity(this);

            LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View sort_view = vi.inflate(R.layout.sort_view, null);
            ViewGroup insertPoint = (ViewGroup)findViewById(R.id.sort_space);
            insertPoint.addView(sort_view, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


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
                    String temp = input.getText().toString();
                    int[] toSort;
                    if (input != null) {
                        toSort = parseArray(input.getText().toString());
                    } else {
                        toSort = new int[0];
                    }
                    if ( toSort != null && toSort.length == 6) {
                        toSort = sort(toSort, method);
                        TextView resultBox = (TextView) findViewById(R.id.result_text);
                        resultBox.setText(Sorting.toString(toSort));
                        dialog.dismiss();

//                    Passes the sorting list to a static variable
                        SortView.setToSort(Sorting.sortSteps.steps);

                        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View v = vi.inflate(R.layout.sort_view, null);
                        ViewGroup insertPoint = (ViewGroup) findViewById(R.id.sort_space);
                        insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    } else {
                        Context context = getApplicationContext();
                        CharSequence text = "The number of the inputs shall be 6, and only 6. 5 will not cut it, unless you then proceed to input another number, making 6";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
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

    public int[] sort(int[] theSort, int method) {
        int[] returnThis;
        switch (method) {
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

    public void AnimateControl(final View sort_view, LinkedList toSort, int[] unsorted) {

        // reset timing counter
        offsetCount = 0;
        for (int i = 0; i < toSort.size(); i++){
            Step step = (Step) toSort.get(i);
            if (step.type == "swap") {
                step.toString();
                AnimateMove(sort_view, unsorted, SortView.sortedUnits, step.start, step.end);
            }
            else {
                step.toString();
            }
        }
    }

    public void AnimateMove(final View sort_view, final int[] unsorted, final Rect[] rex, final int start, final int end){

        int delayTime = 5;  //millis

        Runnable mR1 = new Runnable() {
            @Override
            public void run() {
                rex[start].offset(0, -1);
                sort_view.invalidate();
            }
        };

        Runnable mR2 = new Runnable() {
            @Override
            public void run(){
                if (start < end) {
                    rex[start].offset(1,0);
                }else {
                    rex[start].offset(-1, 0);
                }
                sort_view.invalidate();
            }
        };

        Runnable mR3 = new Runnable() {
            @Override
            public void run(){
                if (start < end) {

                        rex[end].offset(-1, 0);
                } else {
                        rex[end].offset(1, 0);
                }
                sort_view.postInvalidate();
            }
        };

        Runnable mR4 = new Runnable() {
            @Override
            public void run(){
                rex[start].offset(0,1);
                sort_view.invalidate();
            }
        };

        Runnable mR5 = new Runnable() {
            @Override
            public void run(){
                Rect temp = rex[start];
                int temp2 = unsorted[start];

                rex[start] = rex[end];
                unsorted[start] = unsorted[end];

                rex[end] = temp;
                unsorted[end] = temp2;
            }
        };

        // move square upwards
        for (int i = 0; i < (SortView.rHeight + 20); i++) {
            sort_view.postDelayed(mR1, delayTime * (offsetCount + 1));
            offsetCount++;
        }

        // move square horizontally above its correct spot
        if (start < end) {
            for (int i = rex[start].left; i < rex[end].left; i++) {
                sort_view.postDelayed(mR2, delayTime * (offsetCount + 1));
                offsetCount++;
            }
        } else {
            for (int i = rex[start].left; i > rex[end].left; i--) {
                sort_view.postDelayed(mR2, delayTime * (offsetCount + 1));
                offsetCount++;
            }
        }

        // shift all other units over
        int shiftlen = Math.abs(start - end);
        for (int i = 0; i < (SortView.rWidth + 20) * shiftlen; i++){
            sort_view.postDelayed(mR3, delayTime * (offsetCount + 1));
            offsetCount++;
        }

        // move square back into line
        for (int i = 0; i < SortView.rHeight + 20; i++){
            sort_view.postDelayed(mR4, delayTime * (offsetCount + 1));
            offsetCount++;
        }

        sort_view.postDelayed(mR5, delayTime * (offsetCount + 1));

    }

    public static class SortSpinner extends Activity implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            method = pos;
        }

        public void onNothingSelected(AdapterView<?> parent) {
        }
    }
}

