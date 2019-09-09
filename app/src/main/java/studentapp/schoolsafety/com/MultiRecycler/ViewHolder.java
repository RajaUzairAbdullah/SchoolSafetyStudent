package studentapp.schoolsafety.com.MultiRecycler;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import studentapp.schoolsafety.com.R;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView nametext;

     public ViewHolder(View itemView) {
        super(itemView);

        nametext = (TextView) itemView.findViewById(R.id.question);
    }
}
