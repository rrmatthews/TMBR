package teamomega.cs.brandeis.edu.tmber;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Edwin on 11/13/2016.
 */

public class OptionsPreference extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.options_pref);
    }
}