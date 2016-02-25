package dongs.lab1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Debug;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("mysum"); // Load native library at runtime (mysum.so)
    }
    // Declare the native method mysum() that accepts a and b, and returns the sum
    private native int mysum(int vertices, int[][] adj_mat);
    private static final int MY_PERMISSIONS_REQUEST_WRITE_SD = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, MY_PERMISSIONS_REQUEST_WRITE_SD);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_WRITE_SD: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    int vertices = 1000;

                    int[][] adj_mat = intAdjArray(vertices);

                    Debug.startMethodTracing("cycle_noreorder");
                    int i = mysum(vertices, adj_mat);
                    Debug.stopMethodTracing();

                    TextView txtView = (TextView) findViewById(R.id.text_id);
                    if (i == 1) {
                        txtView.setText("HOLY SHIT IT HAS A CYCLE");
                    } else {
                        txtView.setText("HOLY SHIT IT DOESNT HAVE A CYCLE");
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Generates an int adjacency array.
     * @param vertices the number of vertices to give the array
     * @return a 2 dimensional int array.
     */
    public static int[][] intAdjArray(int vertices){

        int[][] adjMatrix = new int[vertices][vertices];

        for(int i = 0; i < vertices; i++){
            for(int j = 0; j < vertices; j++){
                adjMatrix[i][j] = 0;
            }
        }

        return adjMatrix;
    }
}
