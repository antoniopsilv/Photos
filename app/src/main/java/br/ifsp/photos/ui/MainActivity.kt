package br.ifsp.photos.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import br.ifsp.photos.R
import br.ifsp.photos.adapter.PhotosAdapter
import br.ifsp.photos.databinding.ActivityMainBinding
import br.ifsp.photos.model.PhotosItem
import br.ifsp.photos.model.PhotosJSONAPI

class MainActivity : AppCompatActivity() {

    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val photoslist: MutableList<PhotosItem> = mutableListOf()
    private val photosAdapter: PhotosAdapter by lazy {
        PhotosAdapter(this, photoslist)
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
                    // N/A TEMPORARY
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // N/A
                }

            }
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

}