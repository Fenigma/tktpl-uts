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

import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.models.KnownHost;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.databinding.KnownHostLayoutBinding;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.viewModels.KnownHostViewModel;

public class KnownHostFragment  extends Fragment {
    KnownHostViewModel knownHostViewModel;
    KnownHostLayoutBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        knownHostViewModel = KnownHostViewModel.getInstance(getActivity().getApplicationContext());

        knownHostViewModel.getKnownHosts().observe(getViewLifecycleOwner(), new Observer<ArrayList<KnownHost>>() {
            @Override
            public void onChanged(ArrayList<KnownHost> knownHosts) {
                ArrayList<String> histories = new ArrayList<>();
                for (int i=0; i < knownHosts.size(); i++) {
                    KnownHost host = knownHosts.get(i);
                    histories.add(host.ip + " : " + host.port);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, (List<String>) histories);
                binding.knownHostListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = KnownHostLayoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
