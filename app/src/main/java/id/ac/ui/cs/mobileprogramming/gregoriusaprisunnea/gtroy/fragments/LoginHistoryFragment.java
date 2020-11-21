package id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.models.LoginHistory;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.databinding.LoginHistoryLayoutBinding;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.viewModels.LoginHistoryViewModel;

public class LoginHistoryFragment extends Fragment {
    LoginHistoryViewModel loginHistoryViewModel;
    LoginHistoryLayoutBinding binding;
    @Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginHistoryViewModel = LoginHistoryViewModel.getInstance(getActivity().getApplicationContext());

        loginHistoryViewModel.getLoginHistory().observe(getViewLifecycleOwner(), new Observer<ArrayList<LoginHistory>>() {
            @Override
            public void onChanged(ArrayList<LoginHistory> loginHistories) {
                ArrayList<String> histories = new ArrayList<>();
                for (int i=0; i < loginHistories.size(); i++) {
                    histories.add(loginHistories.get(i).access_date);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, (List<String>) histories);
                binding.loginHistoryListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = LoginHistoryLayoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
