package br.ifsp.photos.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.ifsp.photos.R
import br.ifsp.photos.adapter.PhotosAdapter
import br.ifsp.photos.databinding.ActivityMainBinding
import br.ifsp.photos.model.PhotosItem

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
        }
    }

//    private fun retrievePhotos() =






}