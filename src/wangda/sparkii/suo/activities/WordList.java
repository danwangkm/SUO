package wangda.sparkii.suo.activities;

import java.util.ArrayList;
import java.util.HashMap;

import wangda.sparkii.suo.R;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import wangda.sparkii.suo.db.DictionaryAdapter;
import wangda.sparkii.suo.db.DictionaryDatabase;
import wangda.sparkii.suo.view.login.Login;

public class WordList extends ListActivity {
	private Button myButton = null;
	private AutoCompleteTextView myWord = null;
	
	String username;
	
	DictionaryDatabase dbHelper = null;
	SQLiteDatabase db = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wordlist);

		// initiate the layout items
		myWord = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);
		myWord.setThreshold(1);
		myButton = (Button) findViewById(R.id.button2);
		myButton.setOnClickListener(new MyButtonListener());

		// create the dictionary
		DictionaryDatabase dbHelper = new DictionaryDatabase(WordList.this,
				"dictionary");
		final SQLiteDatabase db = dbHelper.openDatabase(WordList.this);
		
		myWord.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				Cursor cursor_autoText = db
						.rawQuery(
								"select distinct word as _id from dictionary where word like ? limit 6",
								new String[] { s.toString() + "%" });
				DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(
						WordList.this, cursor_autoText, true);
				myWord.setAdapter(dictionaryAdapter);
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}
		});

		// get word that is searched
		Intent intent = getIntent();
		String word = intent.getStringExtra("word").toUpperCase();
		username = intent.getStringExtra("username");

		myWord.setText(word);// initiate the search box message

		if (!word.equals("")) {
			// search in the dictionary and print result as a list
			ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

			Cursor cursor = db.query("dictionary", new String[] {
					"english", "expUser" }, "word = ?", new String[] { word },
					null, null, null);
			//System.out.println("WordList: "+db.getPath());
			while (cursor.moveToNext()) {
				HashMap<String, String> map = new HashMap<String, String>();
				String exp = cursor.getString(cursor
						.getColumnIndex("english"));
				map.put("exp", exp);
				String user = cursor.getString(cursor.getColumnIndex("expUser"));
				map.put("user", user);
				System.out.println("query----->" + exp + " ------by " + user);
				list.add(map);
			}
			cursor.close();
			// user could add their explanation
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("exp", "答案不满意，我要补充...");
			list.add(map);

			SimpleAdapter listAdapter = new SimpleAdapter(this, list,
					R.layout.oneword, new String[] { "exp", "user" },
					new int[] { R.id.textView1, R.id.textView2 });
			setListAdapter(listAdapter);
		}

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		if (position == l.getLastVisiblePosition()) {
			// user add their explanation
			Intent intent = getIntent();
			String word = intent.getStringExtra("word").toUpperCase();
			intent = new Intent();
			intent.putExtra("word", word);
			intent.putExtra("username", username);
			intent.setClass(WordList.this, NewWord.class);
			WordList.this.startActivity(intent);
		} else {
			// user choose preferred explanation
			// Toast.makeText(WordList.this, "you choose " + id,
			// Toast.LENGTH_SHORT).show();
		}
	}

	class MyButtonListener implements android.view.View.OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub

			// search a new word
			if (!myWord.getText().toString().equals("")) {
				Intent intent = new Intent();
				intent.putExtra("word", myWord.getText().toString());
				intent.setClass(WordList.this, WordList.class);
				WordList.this.startActivity(intent);
			}
		}
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login:
            	Intent intent = new Intent();
    			intent.setClass(this, Login.class);
    			this.startActivity(intent);
    			finish();
            	break;
            case R.id.exit:
                finish();
                break;
        }
        return true;
    }

}
