package studentapp.schoolsafety.com;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.SurveyViewHolder> {

    private List<SurveyItem> surveyList;
    private List<SurveyItem> surveyListFull;

    @NonNull
    @Override
    public SurveyAdapter.SurveyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.questions,parent,false);//yahn konsi layout ati h
        return new SurveyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull SurveyAdapter.SurveyViewHolder holder, int position) {

        final SurveyItem currentItem = surveyList.get(position);
        holder.questionText.setText(currentItem.getQuestion());

        if(currentItem.getSelectOption() == 0)
        {
            holder.radioButton1.setChecked(false);
            holder.radioButton2.setChecked(false);
            holder.radioButton3.setChecked(false);
            holder.radioButton4.setChecked(false);
        }

        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                if(i == R.id.radioButton1)
                {
                    currentItem.setSelectOption(1);
                }
                else if(i == R.id.radioButton2 )
                {
                    currentItem.setSelectOption(2);
                }
                else if(i == R.id.radioButton3 )
                {
                    currentItem.setSelectOption(3);
                }
                else if(i == R.id.radioButton4 )
                {
                    currentItem.setSelectOption(4);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return surveyList.size();
    }


    class SurveyViewHolder extends RecyclerView.ViewHolder
    {

        TextView questionText;
        RadioButton radioButton1;
        RadioButton radioButton2;
        RadioButton radioButton3;
        RadioButton radioButton4;
        RadioGroup radioGroup;

        public SurveyViewHolder(@NonNull View itemView) {
            super(itemView);

            questionText = itemView.findViewById(R.id.question);
            radioGroup = itemView.findViewById(R.id.radio_group);
            radioButton1 = itemView.findViewById(R.id.radioButton1);
            radioButton2= itemView.findViewById(R.id.radioButton2);
            radioButton3 = itemView.findViewById(R.id.radioButton3);
            radioButton4 = itemView.findViewById(R.id.radioButton4);


        }
    }

    SurveyAdapter(List<SurveyItem> surveyList)
    {
        this.surveyList = surveyList;
        this.surveyListFull = new ArrayList<>(surveyList);
    }
}


