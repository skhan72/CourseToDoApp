package org.khan.coursetodoapp;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.activity.OnBackPressedCallback;

public class EditActivity extends AppCompatActivity {

    private EditText etCourse, etTitle, etText;
    private ToDo original;
    private int index = -1;

    private void updateSubtitle() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("CourseToDoApp");
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etCourse = findViewById(R.id.etCourse);
        etTitle = findViewById(R.id.etTitle);
        etText = findViewById(R.id.etText);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        if (i != null && i.hasExtra("todo")) {
            original = (ToDo) i.getSerializableExtra("todo");
            index = i.getIntExtra("index", -1);

            if (original != null) {
                etCourse.setText(original.getCourseId());
                etTitle.setText(original.getTitle());
                etText.setText(original.getText());
            }
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Register a callback for back press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                String c = etCourse.getText().toString().trim();
                String t = etTitle.getText().toString().trim();
                String txt = etText.getText().toString().trim();

                if (c.isEmpty() && t.isEmpty() && txt.isEmpty()) {
                    Toast.makeText(EditActivity.this, "Empty item not saved", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                if (original != null) {
                    boolean changed =
                            !c.equals(original.getCourseId() == null ? "" : original.getCourseId()) ||
                                    !t.equals(original.getTitle() == null ? "" : original.getTitle()) ||
                                    !txt.equals(original.getText() == null ? "" : original.getText());
                    if (!changed) {
                        Toast.makeText(EditActivity.this, "No changes made", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                }

                new AlertDialog.Builder(EditActivity.this)
                        .setTitle("Save changes?")
                        .setMessage("Do you want to save before exiting?")
                        .setPositiveButton("Save", (dialog, which) -> attemptSaveAndFinish())
                        .setNegativeButton("Don't Save", (dialog, which) -> finish())
                        .show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            attemptSaveAndFinish();
            return true;
        } else if (id == android.R.id.home) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void attemptSaveAndFinish() {
        String c = etCourse.getText().toString().trim();
        String t = etTitle.getText().toString().trim();
        String txt = etText.getText().toString().trim();

        if (c.isEmpty() || t.isEmpty()) {
            Toast.makeText(this, "Cannot save item without course id and title",
                    Toast.LENGTH_LONG).show();
            return;
        }

        ToDo out = new ToDo(c, t, txt);
        Intent res = new Intent();
        res.putExtra("todo", out);
        res.putExtra("index", index);
        setResult(RESULT_OK, res);
        finish();
    }
}