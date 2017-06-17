package hiker.arukami.arukamiapp.Controllers.Activities;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import hiker.arukami.arukamiapp.Controllers.Fragments.MainHikeFragment;
import hiker.arukami.arukamiapp.Models.PointModel;
import hiker.arukami.arukamiapp.R;

public class AddPointActivity extends AppCompatActivity {
    @BindView(R.id.point_bk)
    ImageView _background;
    @BindView(R.id.input_description)
    EditText _description;

    private PointModel model;
    MainHikeFragment mainHikeFragment = MainHikeFragment.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_point);
        ButterKnife.bind(this);
        model = new PointModel();
        Bitmap photo = getIntent().getParcelableExtra("Image");
        _background.setImageBitmap(photo);
        model = getIntent().getParcelableExtra("Model");
        Toolbar toolbar = (Toolbar) findViewById(R.id.addpoint_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        findViewById(R.id.add_point_submit).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addPoint(v);
            }
        });
    }

    public void addPoint(View v){
        model.setDescription(_description.getText().toString());
        mainHikeFragment.addPoint(model);
        finish();
    }
}
