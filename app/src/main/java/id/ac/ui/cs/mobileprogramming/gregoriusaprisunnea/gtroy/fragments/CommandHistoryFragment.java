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

import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.models.CommandHistory;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.databinding.CommandHistoryLayoutBinding;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.viewModels.CommandHistoryViewModel;

public class CommandHistoryFragment extends Fragment {
    CommandHistoryViewModel commandHistoryViewModel;
    private CommandHistoryLayoutBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        commandHistoryViewModel = CommandHistoryViewModel.getInstance(getActivity().getApplicationContext());

        commandHistoryViewModel.getCommandHistory().observe(getViewLifecycleOwner(), new Observer<ArrayList<CommandHistory>>() {
            @Override
            public void onChanged(ArrayList<CommandHistory> commandHistories) {
                ArrayList<String> histories = new ArrayList<>();
                for (int i=0; i < commandHistories.size(); i++) {
                    CommandHistory cmd = commandHistories.get(i);
                    histories.add(cmd.command + " - " + cmd.access_date);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, (List<String>) histories);

                binding.commandHistoryListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = CommandHistoryLayoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
