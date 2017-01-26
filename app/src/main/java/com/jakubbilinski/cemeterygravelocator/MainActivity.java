package com.jakubbilinski.cemeterygravelocator;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final int ADD_REQUEST_ID = 92;

    private FloatingActionButton floatingActionButtonAdd;
    private RecyclerView recyclerView;
    private GravesAdapter gravesAdapter;
    private DatabaseHelper db;
    private boolean isReadyToExit = false;
    private SharedPreferences preferencesMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        preferencesMain = PreferenceManager.getDefaultSharedPreferences(this);
        setFloatingActionButton();
        setRecyclerView();
        PermissionChecker.checkForPermissions(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_searcher).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    gravesAdapter.getFilter().filter("");
                } else {
                    gravesAdapter.getFilter().filter(newText);
                }

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_settings) {
            startActivity(new Intent(this, PreferencesMainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isReadyToExit) {
            finish();
        } else {
            Toast.makeText(this, getString(R.string.press_back_button_to_exit), Toast.LENGTH_LONG).show();
            isReadyToExit = true;
        }
    }

    private void setFloatingActionButton() {
        floatingActionButtonAdd = (FloatingActionButton) findViewById(R.id.floatingActionButtonAdd);
        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddGraveActivity.class);
                startActivityForResult(intent,ADD_REQUEST_ID);
            }
        });
    }

    private void setRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        gravesAdapter = new GravesAdapter(this,this,db.getAllGraves());
        recyclerView.setAdapter(gravesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createItemTouchCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private ItemTouchHelper.Callback createItemTouchCallback() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int positonRemovedItem = viewHolder.getAdapterPosition();
                final int idItemToRemove = gravesAdapter.getGraveId(positonRemovedItem);
                final Grave removedGrave = gravesAdapter.removeItem(positonRemovedItem);

                if (preferencesMain.getBoolean("pref_snackbar",true)) {
                    Snackbar snackbar = Snackbar
                            .make(recyclerView, getString(R.string.item_removed), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.undo_upper), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    gravesAdapter.restoreItem(positonRemovedItem, removedGrave);
                                }
                            });

                    snackbar.addCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT)
                                db.removeGraveById(idItemToRemove);
                        }
                    });

                    snackbar.show();
                } else {
                    db.removeGraveById(idItemToRemove);
                }
            }
        };

        return simpleCallback;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_REQUEST_ID) {
            if (preferencesMain.getBoolean("pref_snackbar",true) && resultCode == Activity.RESULT_OK)
                Snackbar.make(recyclerView, getString(R.string.item_added), Snackbar.LENGTH_LONG).show();

            gravesAdapter.refreshData(db.getAllGraves());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
