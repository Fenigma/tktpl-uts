package id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.models.PermissionDisplayString;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.databinding.ActivityPermissionDescriptionBinding;

public class PermissionDescriptionActivity extends AppCompatActivity {

    private ActivityPermissionDescriptionBinding binding;


    static {
        System.loadLibrary("permission-description");
    }

    public native PermissionDisplayString getPermissionDescription(String description);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPermissionDescriptionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        setUp();
    }

    private void setUp() {
        PermissionDisplayString description = getPermissionDescription(
                "We need Contact Permission to make sure that the service is running properly..."
        );
        binding.permissionDescriptionTextView.setText(String.valueOf(description.getPermissionDisplayString()));

    }


}
