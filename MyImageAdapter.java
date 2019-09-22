package com.cuna.firebaselogin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import modelosdatos.Upload;

public class MyImageAdapter extends RecyclerView.Adapter<MyImageAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Upload> mUploads;

    public MyImageAdapter(Context mContext, List<Upload> mUploads){

        this.mContext=mContext;
        this.mUploads=mUploads;

    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v=LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        Upload uploadCurrent= mUploads.get(position);
        holder.lblViewName.setText(uploadCurrent.getName());

        //valor de la imagen
        Picasso.with(mContext).load(uploadCurrent.getImageUrl()).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(holder.imageView);

    }

    @Override
    public int getItemCount() {

        return mUploads.size();

    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        public TextView lblViewName;
        public ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            lblViewName=itemView.findViewById(R.id.lblTitleCardView);
            imageView=itemView.findViewById(R.id.imageCardView);

        }
    }

}
