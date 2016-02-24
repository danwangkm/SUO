package wangda.sparkii.suo.db;

import wangda.sparkii.suo.R;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class DictionaryAdapter extends CursorAdapter {

	private LayoutInflater layoutInflater;

	public CharSequence convertToString(Cursor cursor) {

		return cursor == null ? "" : cursor.getString(cursor
				.getColumnIndex("_id"));

	}// 用于将_id字段（也就是word字段）的值设置TextView组件的文本

	// view参数表示用于显示列表项的TextView组件

	private void setView(View view, Cursor cursor) {

		TextView tvWordItem = (TextView) view;
		tvWordItem.setText(cursor.getString(cursor.getColumnIndex("_id")));

	}

	public void bindView(View view, Context context, Cursor cursor) {

		setView(view, cursor);

	}

	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		// generate a new view, that is a new textView
		View view = layoutInflater.inflate(R.layout.list_item, null);

		setView(view, cursor);

		return view;

	}

	public DictionaryAdapter(Context context, Cursor c, boolean autoRequery) {

		super(context, c, autoRequery);

		// 通过系统服务获得LayoutInflater对象

		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

}
