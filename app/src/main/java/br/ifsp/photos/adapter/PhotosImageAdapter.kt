package br.ifsp.photos.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import br.ifsp.photos.databinding.TilePhotosBinding


class PhotosImageAdapter(
        private val activityContext: Context,
        private val photoImageList: MutableList<Bitmap>):
    RecyclerView.Adapter<PhotosImageAdapter.PhotosImageViewHolder>() {

    inner class PhotosImageViewHolder(tpb: TilePhotosBinding): RecyclerView.ViewHolder(tpb.photosImageView) {
        val photosIv: ImageView = tpb.photosImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosImageViewHolder =
        PhotosImageViewHolder(TilePhotosBinding.inflate( LayoutInflater.from(activityContext), parent, false))

    override fun onBindViewHolder(holder: PhotosImageViewHolder, position: Int) =
        holder.photosIv.setImageBitmap(photoImageList[position])

    override fun getItemCount(): Int = photoImageList.size
}