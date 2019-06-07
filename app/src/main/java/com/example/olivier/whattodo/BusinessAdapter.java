package com.example.olivier.whattodo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.ViewHolder> {
    public List<Business> bList;
    private int pos;

     public BusinessAdapter( List<Business> bList)
     {
        this.bList=bList;
     }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.business_list,parent,false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                // item clicked
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        holder.bText.setText(bList.get(position).getBname());
      holder.descText.setText(bList.get(position).getDescription());
        pos=position;




    }

    @Override
    public int getItemCount() {
        return bList.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{

         View mView;
         public TextView bText;
        public TextView descText;
        public ViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            bText= (TextView) mView.findViewById(R.id.textView6);
            descText= (TextView) mView.findViewById(R.id.textView7);

            mView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int g=getAdapterPosition();
                    final String text=bList.get(g).getBname();
                    Context context = mView.getContext();
                    Intent i = new Intent(context,BusinessLanding.class);
                    i.putExtra("key",text);
                    context.startActivity(i);
                }
            });


        }
    }
}
