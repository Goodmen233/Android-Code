package com.example.l12_chengbincai;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    EditText contactName;
    TextView detail;
    ListView listView;
    Cursor contactCursor;
    Uri contactUri;

    public void queryContacts(){
        String[] FORM_COLUNMS = {ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};
        int [] TO_IDS = {android.R.id.text1};
        String[] projection = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.LOOKUP_KEY,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        };
        String selection;
        String[] selectionArgs = {""};
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY+" DESC";
        String searchString = contactName.getText().toString();
        if(TextUtils.isEmpty(searchString)){
            selection=null;
            selectionArgs=null;
        }else{
            selection=ContactsContract.Contacts.DISPLAY_NAME_PRIMARY+" LIKE ? ";
            selectionArgs[0]="%"+searchString+"%";
        }
        contactCursor = getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
        );
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                contactCursor,
                FORM_COLUNMS,
                TO_IDS,
                0
        );
        listView.setAdapter(cursorAdapter);
    }

    public void checkPermissionAndQuery(){
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS},1);
        }else{
            queryContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode==1){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                queryContacts();
            }else{
                Toast.makeText(MainActivity.this,
                        "Read Contacts Permission is denied",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        contactCursor.moveToPosition(i);
        Long contactId = contactCursor.getLong(0);
        String contactKey = contactCursor.getString(1);
        contactUri = ContactsContract.Contacts.getLookupUri(contactId,contactKey);

        StringBuilder content = new StringBuilder();
        content.append("Name: "+contactCursor.getString(2)+"\n");

        String[] projection = {ContactsContract.CommonDataKinds.Email.ADDRESS};
        String selection = ContactsContract.CommonDataKinds.Email.CONTACT_ID+" = ? ";
        String[] selectionArgs={contactId.toString()};
        Cursor emailCursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        );
        try {
            if(emailCursor.moveToFirst()){
                int num=1;
                do{
                    content.append("Email"+num+": "+emailCursor.getString(0)+"\n");
                    num=num+1;
                }while (emailCursor.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            emailCursor.close();
        }

        String[] projectionP = { ContactsContract.CommonDataKinds.Phone.NUMBER };
        String selectionP = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? ";
        String[] selectionPArgs={ contactId.toString() };
        Cursor PhoneCursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projectionP,
                selectionP,
                selectionPArgs,
                null
        );
        try{
            if(PhoneCursor.moveToFirst()){
                int num = 1;
                do{
                    content.append("Phone" + num + ": " + PhoneCursor.getString(0) + "\n");
                    num = num + 1;
                }while (PhoneCursor.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            PhoneCursor.close();
        }

        detail.setText(content);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactName = findViewById(R.id.editTextTextPersonName);
        detail = findViewById(R.id.detail);
        listView = findViewById(R.id.listView);
        Button query = findViewById(R.id.query);
        Button modify = findViewById(R.id.modify);
        Button insert = findViewById(R.id.insert);
        listView.setOnItemClickListener(this);
        query.setOnClickListener(v -> {
            checkPermissionAndQuery();
        });
        modify.setOnClickListener(v -> {
            if(contactUri != null){
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setDataAndType(contactUri,ContactsContract.Contacts.CONTENT_ITEM_TYPE);
                startActivity(intent);
            }else{
                Toast.makeText(MainActivity.this,"Please select a contact",Toast.LENGTH_LONG).show();
            }
        });
        insert.setOnClickListener(v -> {
            Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
            intent.putExtra(ContactsContract.Intents.Insert.NAME,"LLC");
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL,"LLCSB@qq.com");
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE,ContactsContract.CommonDataKinds.Email.TYPE_WORK);
            intent.putExtra(ContactsContract.Intents.Insert.PHONE,"010110");
            intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE,ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
            startActivity(intent);
        });
    }

}