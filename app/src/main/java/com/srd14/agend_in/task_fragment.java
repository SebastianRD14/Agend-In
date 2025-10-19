package com.srd14.agend_in;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class task_fragment extends Fragment {

    public task_fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.task_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button buttonEdit = view.findViewById(R.id.botonEditTask);
        Button buttonDetails = view.findViewById(R.id.botonViewDetails);

        buttonEdit.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new edit_task_fragment())
                    .addToBackStack(null)
                    .commit();
        });

        buttonDetails.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new task_detail_fragment())
                    .addToBackStack(null)
                    .commit();
        });
    }
}
