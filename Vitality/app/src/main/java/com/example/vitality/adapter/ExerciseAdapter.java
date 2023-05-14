package com.example.vitality.adapter;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.vitality.R;
import com.example.vitality.database.DietDatabase;
import com.example.vitality.entity.ExerciseEntity;
import com.example.vitality.utils.DateTimeUtils;

public class ExerciseAdapter extends BaseQuickAdapter<ExerciseEntity, BaseViewHolder> {
    public ExerciseAdapter() {
        super(R.layout.item_exercise);
        addChildClickViewIds(R.id.btnEdit);
    }
    //Each item sets the data to add and delete click events
    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ExerciseEntity exerciseEntity) {
        baseViewHolder.setText(R.id.tvTime, DateTimeUtils.timeFormat(exerciseEntity.selTime));
        baseViewHolder.setText(R.id.tvCalorie, exerciseEntity.calc);
        baseViewHolder.setText(R.id.tvType, exerciseEntity.type);
        baseViewHolder.setText(R.id.tvDuration, ""+exerciseEntity.duration);

        Button btnDel = baseViewHolder.getView(R.id.btnDel);


        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DietDatabase.databaseWriteExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        DietDatabase.getInstance(getContext()).exerciseDao().delete(exerciseEntity);
                    }
                });
            }
        });
    }
}

