package studentapp.schoolsafety.com.MultiRecycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import studentapp.schoolsafety.com.R;

public class Adapter extends RecyclerView.Adapter<ViewHolder> {

    Context c;
    ArrayList<String> questions;

    public Adapter (Context c,ArrayList<String> questions){
        this.c = c;
        this.questions = questions;
    }


    //Initializing View Holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.questions,parent,false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Bind DATA
        holder.nametext.setText(questions.get(position));
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
}
