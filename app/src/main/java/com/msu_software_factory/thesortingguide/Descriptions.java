package com.msu_software_factory.thesortingguide;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class Descriptions extends Fragment {
    static TextView desc, code;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_descriptions, container, false);
        Spinner descSpinner = (Spinner) rootView.findViewById(R.id.descSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.sort_choice, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        descSpinner.setAdapter(adapter);
        descSpinner.setOnItemSelectedListener(new descSpinner());

        desc = (TextView) rootView.findViewById(R.id.descView);
        code = (TextView) rootView.findViewById(R.id.codeView);

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(3);
    }
    public static void getDesc(int position){
        switch(position){
            case 0:
                desc.setText(R.string.bubbleDesc);
                code.setText(R.string.bubbleCode);
                break;
            case 1:
                desc.setText(R.string.selectionDesc);
                code.setText(R.string.selectionCode);
                break;
            case 2:
                desc.setText(R.string.insertionDesc);
                code.setText(R.string.insertionCode);
                break;
        }
    }

    public static class descSpinner extends Activity implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            getDesc(pos);
        }

        public void onNothingSelected(AdapterView<?> parent) {
        }
    }
}