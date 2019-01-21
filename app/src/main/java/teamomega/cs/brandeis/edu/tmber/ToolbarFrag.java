package teamomega.cs.brandeis.edu.tmber;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Edwin on 11/13/2016.
 */

public class ToolbarFrag extends Fragment implements View.OnClickListener{

    View vi;
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;

    MainFragment main = new MainFragment();
    FirstPreference fpref = new FirstPreference();
    ContactSelect spref = new ContactSelect();
    OptionsPreference tpref = new OptionsPreference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getFragmentManager().findFragmentById(R.id.main_container) == null) {
            getFragmentManager().beginTransaction().add(R.id.main_container, main).commit();
        }

        vi = inflater.inflate(R.layout.toolbar_fragment, container, false);
        btn1 = (Button) vi.findViewById(R.id.home);
        btn1.setOnClickListener(this);
        btn2 = (Button) vi.findViewById(R.id.toggle_pref);
        btn2.setOnClickListener(this);
        btn3 = (Button) vi.findViewById(R.id.contact_pref);
        btn3.setOnClickListener(this);
        btn4 = (Button) vi.findViewById(R.id.pref);
        btn4.setOnClickListener(this);

        // Inflate the layout for this fragment
        return vi;
    }

    public void onClick(final View v) {
        switch(v.getId()) {
            case R.id.home:
                getFragmentManager().beginTransaction().replace(R.id.main_container, main).commit();
                getFragmentManager().beginTransaction().addToBackStack(null).commit();
                break;
            case R.id.toggle_pref:
                getFragmentManager().beginTransaction().replace(R.id.main_container, fpref).commit();
                getFragmentManager().beginTransaction().addToBackStack(null).commit();
                break;
            case R.id.contact_pref:
                getFragmentManager().beginTransaction().replace(R.id.main_container, spref).commit();
                getFragmentManager().beginTransaction().addToBackStack(null).commit();
                break;
            case R.id.pref:
                getFragmentManager().beginTransaction().replace(R.id.main_container, tpref).commit();
                getFragmentManager().beginTransaction().addToBackStack(null).commit();
                break;
        }
    }
}