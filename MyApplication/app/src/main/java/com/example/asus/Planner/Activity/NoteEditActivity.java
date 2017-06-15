package com.example.asus.Planner.Activity;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.asus.Planner.Adapter.NotesAdapter;
import com.example.asus.Planner.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteEditActivity extends Activity {
    public static int numTitle = 1;
    public static String curDate = "";
    public static String curText = "";

    public static class LineEditText extends AppCompatEditText {
        // we need this constructor for LayoutInflater
        public LineEditText(Context context, AttributeSet attrs) {
            super(context, attrs);
            mRect = new Rect();
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mPaint.setColor(Color.BLUE);
        }

        private Rect mRect;
        private Paint mPaint;

        @Override
        protected void onDraw(Canvas canvas) {

            int height = getHeight();
            int line_height = getLineHeight();
            int count = height / line_height;
            if (getLineCount() > count)
                count = getLineCount();
            Rect r = mRect;
            Paint paint = mPaint;
            int baseline = getLineBounds(0, r);

            for (int i = 0; i < count; i++) {
                canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
                baseline += getLineHeight();
                super.onDraw(canvas);
            }
        }
    }

    private EditText mTitleText;
    private EditText mBodyText;
    private TextView mDateText;
    private Long mRowId;
    private NotesAdapter mDbHelper;
    private Cursor note;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mDbHelper = new NotesAdapter(this);
            mDbHelper.open();
            setContentView(R.layout.note_edit);
            setTitle(R.string.app_name);

            mTitleText = (EditText) findViewById(R.id.title);
            mBodyText = (EditText) findViewById(R.id.body);
            mDateText = (TextView) findViewById(R.id.notelist_date);

            long msTime = System.currentTimeMillis();
            Date curDateTime = new Date(msTime);

            SimpleDateFormat formatter = new SimpleDateFormat("d'/'M'/'y");
            curDate = formatter.format(curDateTime);

            mDateText.setText("" + curDate);

            mRowId = (savedInstanceState == null) ? null :
                    (Long) savedInstanceState.getSerializable(NotesAdapter.KEY_ROWID);
            if (mRowId == null) {
                Bundle extras = getIntent().getExtras();
                mRowId = extras != null ? extras.getLong(NotesAdapter.KEY_ROWID)
                        : null;
            }
            populateFields();
        }

        @Override
        protected void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            saveState();
            outState.putSerializable(NotesAdapter.KEY_ROWID, mRowId);
        }

        @Override
        protected void onPause() {
            super.onPause();
            saveState();
        }

        @Override
        protected void onResume() {
            super.onResume();
            populateFields();
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.noteedit_menu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_delete:
                    if (note != null) {
                        note.close();
                        note = null;
                    }
                    if (mRowId != null) {
                        mDbHelper.deleteNote(mRowId);
                    }
                    finish();

                    return true;
                case R.id.menu_save:
                    saveState();
                    finish();
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        private void saveState() {
            String title = mTitleText.getText().toString();
            String body = mBodyText.getText().toString();

            if (mRowId == null) {
                long id = mDbHelper.createNote(title, body, curDate);
                if (id > 0) {
                    mRowId = id;
                }
            } else {
                mDbHelper.updateNote(mRowId, title, body, curDate);
            }
        }

        private void populateFields() {
            if (mRowId != null) {
                note = mDbHelper.fetchNote(mRowId);
                startManagingCursor(note);
                mTitleText.setText(note.getString(
                        note.getColumnIndexOrThrow(NotesAdapter.KEY_TITLE)));
                mBodyText.setText(note.getString(
                        note.getColumnIndexOrThrow(NotesAdapter.KEY_BODY)));
                curText = note.getString(
                        note.getColumnIndexOrThrow(NotesAdapter.KEY_BODY));
            }
        }
    }

