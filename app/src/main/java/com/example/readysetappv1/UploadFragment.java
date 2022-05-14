package com.example.readysetappv1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadFragment extends Fragment {

    private EditText link_text;
    private Button submit_button;
    private Spinner spinner1;
    private Spinner spinner2;

    public UploadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UploadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadFragment newInstance() {
        UploadFragment fragment = new UploadFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_upload, container, false);
        link_text = v.findViewById(R.id.link_input);
        submit_button = v.findViewById(R.id.button_input);
        submit_button.setOnClickListener(this::onClickSubmit);
        spinner1 = v.findViewById(R.id.primaryTag);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getContext(),
                R.array.subject_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] array2;
                if (spinner1.getSelectedItem().toString().equals("eng")) {
                    array2 = new String[] {"ap lang", "ap lit", "eng 1", "eng 2", "eng 3", "eng 4", "jrnl", "crea"};
                } else if (spinner1.getSelectedItem().toString().equals("hist")) {
                    array2 = new String[] {"u.s.", "euro", "world", "afr", "asia", "aus", "ancient", "modern"};
                } else {
                    array2 = new String[] {"oops"};
                }
                ArrayAdapter<String> adapter2 = new ArrayAdapter(getContext(),
                        android.R.layout.simple_spinner_item, array2);
// Specify the layout to use when the list of choices appears
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
                spinner2.setAdapter(adapter2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                String[] array2 = new String[] {"ap lang", "ap lit", "eng 1", "eng 2", "eng 3", "eng 4", "jrnl", "crea"};
                ArrayAdapter<String> adapter2 = new ArrayAdapter(getContext(),
                        android.R.layout.simple_spinner_item, array2);
// Specify the layout to use when the list of choices appears
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
                spinner2.setAdapter(adapter2);
            }
        });
        spinner2 = v.findViewById(R.id.secondaryTag);
        // Create an ArrayAdapter using the string array and a default spinner layout
        // TODO: Switch cases based on spinner 1 result
        String[] array2 = new String[] {"ap lang", "ap lit", "eng 1", "eng 2", "eng 3", "eng 4", "jrnl", "crea"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter(getContext(),
                android.R.layout.simple_spinner_item, array2);
// Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);

        return v;
    }

    private void onClickSubmit(View v) {
        // TODO: Save link_text.getText() to something useful like User object(?)
        Toast.makeText(getActivity(), "We got ".concat(link_text.getText().toString()), Toast.LENGTH_LONG).show();
    }

}