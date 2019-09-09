package studentapp.schoolsafety.com;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PackageViewHolder>  {

    private List<PackageItem> packageItemList;
    private List<PackageItem> packageItemListfull;

    @NonNull
    @Override
    public PackageAdapter.PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.packageitem,parent,false);
        return new PackageViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PackageAdapter.PackageViewHolder holder, int position) {

        final PackageItem currentItem = packageItemList.get(position);

        holder.btnPackages.setText(currentItem.getTxtBtnPackage());
//        holder.linkbtn.setText(currentItem.getLink());
        holder.btnPackages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),currentItem.getLink(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), PDF_Viewer.class);
                intent.putExtra("pdf_url",currentItem.getLink());
                view.getContext().startActivity(intent);



            }
        });

    }

    @Override
    public int getItemCount() {
        return packageItemList.size();
    }

    class  PackageViewHolder extends RecyclerView.ViewHolder
    {

        Button btnPackages,linkbtn;


        public PackageViewHolder(@NonNull View itemView) {
            super(itemView);
            btnPackages = itemView.findViewById(R.id.packagebtn);
//            linkbtn = itemView.findViewById(R.id.link);
        }
    }

    PackageAdapter(List<PackageItem> packageItemList)
    {
        this.packageItemList = packageItemList;
        packageItemListfull = new ArrayList<>(packageItemList);
    }

}
