package com.example.bayern.mygeo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bayern.mygeo.R;

public class GeoLocationFragment extends Fragment
{
    private Spinner mRadiusSpinner;
    private EditText mLocationNameEditText;
    private TextView mLocationNameError;
    private EditText mLocationRadiusEditText;
    private TextView mLocationRadiusError;
    private CheckBox mExperationNeverCheckbox;
    private EditText mExperationEditText;
    private TextView mExperationError;
    private Button mCreateButton;

    public static GeoLocationFragment newInstance()
    {
//        ListFragment fragmentFirst = new ListFragment();
//        Bundle args = new Bundle();
//        args.putInt("someInt", page);
//        args.putString("someTitle", title);
//        fragmentFirst.setArguments(args);
        return new GeoLocationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        page = getArguments().getInt("someInt", 0);
//        title = getArguments().getString("someTitle");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.geo_location_fragment_layout, container, false);

        mLocationNameEditText = (EditText) view.findViewById(R.id.location_name_edit_text);
        mLocationNameError = (TextView) view.findViewById(R.id.location_name_error);

        mLocationRadiusEditText = (EditText) view.findViewById(R.id.location_radius_edit_text);
        mLocationRadiusError = (TextView) view.findViewById(R.id.location_radius_error);

        mExperationNeverCheckbox = (CheckBox) view.findViewById(R.id.expiration_checkbox);
        mExperationNeverCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    mExperationEditText.setVisibility(View.GONE);
                }
                else
                {
                    mExperationEditText.setVisibility(View.VISIBLE);
                }
            }
        });
        mExperationEditText = (EditText) view.findViewById(R.id.expiration_edit_text);
        mExperationError = (TextView) view.findViewById(R.id.location_expiration_error);

        mRadiusSpinner = (Spinner) view.findViewById(R.id.radius_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.radius_spinner_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mRadiusSpinner.setAdapter(adapter);

        mCreateButton = (Button) view.findViewById(R.id.create_button);
        createButtonListener();

        return view;
    }

    private void createButtonListener()
    {
        mCreateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                cleanAllErrors();
                if (checkAllFieldsForErrors())
                {

                }
            }
        });
    }

    private void cleanAllErrors()
    {
        mLocationNameError.setVisibility(View.GONE);
        mLocationRadiusError.setVisibility(View.GONE);
        mExperationError.setVisibility(View.GONE);
    }

    private boolean checkAllFieldsForErrors()
    {
        boolean hasError = false;
        if (TextUtils.isEmpty(mLocationNameEditText.getText().toString()))
        {
            mLocationNameError.setVisibility(View.VISIBLE);
            hasError = true;
        }

        {
            int radius = 0;
            try
            {
                radius = Integer.parseInt(mLocationRadiusEditText.getText().toString());
            } catch (NumberFormatException exception)
            {
                mLocationRadiusError.setVisibility(View.VISIBLE);
                hasError = true;
            }

            if (!(radius >= 50 && radius <= 1000))
            {
                mLocationRadiusError.setVisibility(View.VISIBLE);
                hasError = true;
            }
        }

        if (!mExperationNeverCheckbox.isChecked())
        {
            int experationInDays = 0;
            try
            {
                experationInDays = Integer.parseInt(mExperationEditText.getText().toString());
            } catch (NumberFormatException exception)
            {
                mExperationError.setVisibility(View.VISIBLE);
                hasError = true;
            }

            if (!(experationInDays > 0))
            {
                mExperationError.setVisibility(View.VISIBLE);
                hasError = true;
            }
        }

        return !hasError;
    }
}
