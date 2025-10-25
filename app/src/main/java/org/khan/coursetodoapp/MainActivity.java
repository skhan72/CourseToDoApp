package org.khan.coursetodoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ToDoAdapter.OnItemClickListener {
    private static final String FILENAME = "todos.json";
    private static final int REQ_EDIT = 1001;

    private List<ToDo> todos = new ArrayList<>();
    private ToDoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        RecyclerView rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ToDoAdapter(todos, this);
        rv.setAdapter(adapter);

        // Load JSON file
        String raw = FileUtil.readFile(this, FILENAME);
        if (raw != null) {
            try {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<ToDo>>() {}.getType();
                List<ToDo> loaded = gson.fromJson(raw, listType);
                if (loaded != null) todos.addAll(loaded);
            } catch (Exception e) { e.printStackTrace(); }
        }

        sortAndRefresh();
    }


    private void sortAndRefresh() {
        Collections.sort(todos, (a, b) ->
                Long.compare(b.getLastSaveTime(), a.getLastSaveTime()));
        adapter.notifyDataSetChanged();
        updateSubtitle();
        saveToFile();
    }

    private void updateSubtitle() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("CourseToDoApp");
            getSupportActionBar().setSubtitle("To Do:" + todos.size() );
        }
    }

    private void saveToFile() {
        Gson gson = new Gson();
        FileUtil.writeFile(this, FILENAME, gson.toJson(todos));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            Toast.makeText(this, "Add clicked", Toast.LENGTH_SHORT).show();
            startActivityForResult(new Intent(this, EditActivity.class), REQ_EDIT);
            return true;
        } else if (id == R.id.action_info) {
            Toast.makeText(this, "About clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_EDIT && resultCode == RESULT_OK && data != null) {
            ToDo todo = (ToDo) data.getSerializableExtra("todo");
            int index = data.getIntExtra("index", -1);
            if (index >= 0 && index < todos.size())
                todos.set(index, todo);
            else
                todos.add(todo);
            sortAndRefresh();
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent i = new Intent(this, EditActivity.class);
        i.putExtra("todo", todos.get(position));
        i.putExtra("index", position);
        startActivityForResult(i, REQ_EDIT);
    }

    @Override
    public void onItemLongClick(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete item")
                .setMessage("Delete this to-do?")
                .setPositiveButton("Delete", (d, w) -> {
                    todos.remove(position);
                    sortAndRefresh();
                    Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
