package com.example.charmer.moving.utils;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.charmer.moving.R;
import com.example.charmer.moving.pojo.Exercises;

import java.util.List;

/**
 * Created by loongggdroid on 2016/5/19.
 */
public class MytryAdapter extends RecyclerView.Adapter<MytryAdapter.ViewHolder> {
    // 数据集
    private List<Exercises> exerciseList ;

    public MytryAdapter(List<Exercises> exerciseList) {
        super();
        this.exerciseList = exerciseList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        // 创建一个View，简单起见直接使用系统提供的布局，就是一个TextView

        View view = View.inflate(viewGroup.getContext(), R.layout.exemidinfo, null);

        // 创建一个ViewHolder

        ViewHolder holder = new ViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        // 绑定数据到ViewHolder上
        Exercises exercises = exerciseList.get(i);
        viewHolder.type.setText(exercises.getExerciseType());
        viewHolder.theme.setText(exercises.getExerciseTheme());
        viewHolder.place.setText(exercises.getPlace());
        viewHolder.activityTime.setText(DateUtil.dateToString(exercises.getActivityTime()).substring(0,16));
        viewHolder.cost.setText(exercises.getCost().toString());
        viewHolder.paymentMethod.setText(exercises.getPaymentMethod());
        viewHolder.currentNumber.setText(exercises.getCurrentNumber().toString());
        viewHolder.totalNumber.setText(exercises.getTotalNumber().toString());

    }

    @Override
    public int getItemCount() {

        return exerciseList.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView type ;
        TextView theme ;
        TextView place ;
        TextView activityTime ;
        TextView cost ;
        TextView paymentMethod ;
        TextView currentNumber ;
        TextView totalNumber ;

        public ViewHolder(View itemView) {

            super(itemView);

            type = ((TextView) itemView.findViewById(R.id.type));
            theme = ((TextView) itemView.findViewById(R.id.theme));
            place = ((TextView) itemView.findViewById(R.id.place));
            activityTime = ((TextView) itemView.findViewById(R.id.activityTime));
            cost = ((TextView) itemView.findViewById(R.id.cost));
            paymentMethod = ((TextView) itemView.findViewById(R.id.paymentMethod));
            currentNumber = ((TextView) itemView.findViewById(R.id.currentNumber));
            totalNumber = ((TextView) itemView.findViewById(R.id.totalNumber));

        }

    }

}



