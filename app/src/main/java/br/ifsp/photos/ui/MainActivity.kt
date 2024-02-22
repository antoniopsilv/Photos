package br.ifsp.photos.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import br.ifsp.photos.R
import br.ifsp.photos.adapter.PhotosAdapter
import br.ifsp.photos.adapter.PhotosImageAdapter
import br.ifsp.photos.databinding.ActivityMainBinding
import br.ifsp.photos.model.PhotosItem
import br.ifsp.photos.model.PhotosJSONAPI
import com.android.volley.toolbox.ImageRequest
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.HttpURLConnection.HTTP_OK
import java.net.URL

class MainActivity : AppCompatActivity() {

    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val photoslist: MutableList<PhotosItem> = mutableListOf()
    private val photosAdapter: PhotosAdapter by lazy {
        PhotosAdapter(this, photoslist)
    }

    private val photosImageList: MutableList<Bitmap> = mutableListOf()
    private val photosImageAdapter: PhotosImageAdapter by lazy {
        PhotosImageAdapter(this, photosImageList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.mainTb.apply {
            title = "Fotos"
        })

        amb.photosSp.apply {
            adapter = photosAdapter
            onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val size = photoslist.size
                    photosImageList.clear()
                    photosImageAdapter.notifyItemRangeRemoved(0, size)
                    retrieveProductImage(photoslist[position].url)
                    retrieveProductImage(photoslist[position].thumbnailUrl)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // N/A
                }
            }
        }
        amb.productImagesRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = photosImageAdapter
        }
        retrievePhotos()
    }

    private fun retrievePhotos() =
        PhotosJSONAPI.PhotosListRequest({ photoslist ->
            photoslist.also {
                photosAdapter.addAll(it)
            }
        }, {
            Toast.makeText(this, getString(R.string.request_problem), Toast.LENGTH_SHORT).show()
        }).also {
            PhotosJSONAPI.getInstance(this).addToRequestQueue(it)
        }


    private fun retrieveProductImage(url: String) =

        url.also { imageUrl ->
            ImageRequest(imageUrl, { response ->
                photosImageList.add(response)
                photosImageAdapter.notifyItemInserted(photosImageList.lastIndex)
            }, 0, 0, ImageView.ScaleType.CENTER,
                Bitmap.Config.ARGB_8888, {
                    Toast.makeText(this, getString(R.string.request_problem), Toast.LENGTH_SHORT)
                        .show()
                }).also {
                PhotosJSONAPI.getInstance(this).addToRequestQueue(it)
            }
        }

}
