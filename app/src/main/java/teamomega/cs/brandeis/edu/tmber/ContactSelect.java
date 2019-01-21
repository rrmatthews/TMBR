package teamomega.cs.brandeis.edu.tmber;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.app.Activity.RESULT_OK;

public class ContactSelect extends Fragment implements View.OnClickListener {

    View vi;
    Button btn;
    TextView name;
    TextView num;
    private DataBaseHelper mDbHelper;
    private static final String TAG = ContactSelect.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;     // contacts unique ID

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        vi = inflater.inflate(R.layout.buddy_select, container, false);

        btn = (Button) vi.findViewById(R.id.buddy);
        btn.setOnClickListener(this);

        name = (TextView) vi.findViewById(R.id.name);
        num = (TextView) vi.findViewById(R.id.num);

        mDbHelper = new DataBaseHelper(this.getActivity());

        Cursor res = mDbHelper.getAllData();

        if(res.getCount() != 0) {
            while(res.moveToNext()) {
                name.setText(res.getString(1));
                num.setText(res.getString(2));
            }
        }

        // Inflate the layout for this fragment
        return vi;
    }

    public void onClick(final View v) {

        // using native contacts selection
        // Intent.ACTION_PICK = Pick an item from the data, returning what was selected.
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();

            retrieveContactName();
            retrieveContactNumber();

            mDbHelper = new DataBaseHelper(this.getActivity());

            Cursor res = mDbHelper.getAllData();

            if(res.getCount() != 0) {
                while(res.moveToNext()) {
                    mDbHelper.deleteData(res.getString(0));
                }
            }

            mDbHelper.insertData(name.getText().toString(), num.getText().toString());

        }
    }

    private void retrieveContactNumber() {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = this.getActivity().getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d(TAG, "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = this.getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();

        num = (TextView) this.getActivity().findViewById(R.id.num);
        num.setText(contactNumber);

    }

    private void retrieveContactName() {

        String contactName = null;

        // querying contact data store
        Cursor cursor = this.getActivity().getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();

        name = (TextView) this.getActivity().findViewById(R.id.name);
        name.setText(contactName);

    }
}
