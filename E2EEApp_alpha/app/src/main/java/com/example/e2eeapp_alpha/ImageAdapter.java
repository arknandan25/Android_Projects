package com.example.e2eeapp_alpha;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context mContext;
    private List<UploadImage> muploads;

    public ImageAdapter(Context context, List<UploadImage> uploads){
        mContext = context;
        muploads = uploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return  new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Log.i("positionAd", String.valueOf(position));
        UploadImage uploadCurrent = muploads.get(position);
//        holder.textViewName.setText(uploadCurrent.getName());
        holder.textViewName.setText(uploadCurrent.getName());

        Picasso.get().load(uploadCurrent.getURI()).into(holder.imageView);
        Log.i("here the data:",uploadCurrent.getName() + ":::"+  uploadCurrent.getURI());
//        Picasso.with(mContext)
//                .load(uploadCurrent.getImageUrl())
//                .fit()
//                .centerCrop()
//                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        Log.i("Size of mUploadAD", String.valueOf(muploads.size()));

        return muploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewName;
        public ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_image_name);
            imageView = itemView.findViewById(R.id.image_view_upload);
        }
    }
}
