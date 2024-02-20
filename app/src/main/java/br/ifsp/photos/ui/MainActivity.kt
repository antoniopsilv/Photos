package br.ifsp.photos.ui

import android.graphics.BitmapFactory
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
                    // Avaliar a questão da limpeza das imagens
                    // Sem tratar como lista

                    //val size = photoslist.size
                    //photosImageList.clear()
                    //photosImageAdapter.notifyItemRangeRemoved(0, size)
                    //retrieveProductImage(photoslist[position])
                    downlooadPhotosImage(photoslist[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // N/A
                }

            }
        }
//        amb.photosUrlIv.apply {
            //layoutManager = LinearLayoutManager(this@MainActivity)
            //adapter = photosImageAdapter
//        }
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


    // Faltando Refatorar esse método para tirar a chamada da MainActivity
    // E melhor com uso da biblioteca Volley
    private fun downlooadPhotosImage(photosItem: PhotosItem) = Thread {

        val photosThumbnailUrlConnection = URL(photosItem.url).openConnection() as HttpURLConnection
        val photosConnection = URL(photosItem.url).openConnection() as HttpURLConnection

        try {
            if (photosConnection.responseCode == HTTP_OK) {
                runOnUiThread {
                    Toast.makeText(this, " CHEGOU DENTRO DO HTTP_OK", Toast.LENGTH_SHORT).show()
                    val bitmap = BitmapFactory.decodeStream(photosConnection.inputStream)

                    amb.photosUrlIv.apply {
                        // Implementar o novo Adapter
                        setImageBitmap(bitmap)
                    }
                }
            } else {
                runOnUiThread{
                    Toast.makeText(this, " não CHEGOU !!!", Toast.LENGTH_SHORT).show()
                }
            }

            if (photosThumbnailUrlConnection.responseCode == HTTP_OK) {
                runOnUiThread {
                    BufferedInputStream(photosThumbnailUrlConnection.inputStream).let {
                        val imageBitmapThumbnai = BitmapFactory.decodeStream(it)
                        runOnUiThread {
                            amb.photosThumbnailIv.apply {
                                // Implementar o novo Adapter
                                setImageBitmap(imageBitmapThumbnai)
                            }
                        }
                    }
                }
            } else {
                runOnUiThread{
                    Toast.makeText(this, " não CHEGOU !!!", Toast.LENGTH_SHORT).show()
                }
            }
        } catch ( e: Exception)  {
                e.printStackTrace()
        } finally {
            photosConnection.disconnect()
        }
    }.start()
}
