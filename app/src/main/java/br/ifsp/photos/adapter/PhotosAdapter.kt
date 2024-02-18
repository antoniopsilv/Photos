package br.ifsp.photos.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.ifsp.photos.model.Photos
import br.ifsp.photos.model.PhotosItem

class PhotosAdapter (
    private val activityContext: Context,
    private val photosList: MutableList<PhotosItem>
): ArrayAdapter<PhotosItem>(activityContext, android.R.layout.simple_list_item_1, photosList) {

    private data class PhotosHolder(val photosTitleTv: TextView)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val photosView = convertView ?: LayoutInflater.from(activityContext)
            .inflate(android.R.layout.simple_list_item_1,parent,false).apply {
                tag = PhotosHolder(findViewById(android.R.id.text1))
            }

        (photosView.tag as PhotosHolder).photosTitleTv.text = photosList[position].title
        return photosView
    }

}



