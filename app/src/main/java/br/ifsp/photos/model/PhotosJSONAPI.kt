package br.ifsp.photos.model

import android.content.Context
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.Response.success
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import java.net.HttpURLConnection.HTTP_NOT_MODIFIED
import java.net.HttpURLConnection.HTTP_OK

class PhotosJSONAPI(context: Context) {

    companion object {

        const val PHOTOS_ENDPOINT = "https://jsonplaceholder.typicode.com/photos"

        @Volatile
        private var INSTANCE: PhotosJSONAPI? = null

        fun getInstance(context: Context) = INSTANCE ?: kotlin.synchronized(this) {
            INSTANCE ?: PhotosJSONAPI(context).also {
                INSTANCE = it
            }
        }
    }

    // Fila para atender todas requisições
    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    // Adiciona requisições na fila
    fun <T> addToRequestQueue(request: Request<T>) {
        requestQueue.add(request)
    }
    class PhotosListRequest(
        private val responseListener: Response.Listener<Photos>,
        errorListener: Response.ErrorListener
    ): Request<Photos>(Method.GET, PHOTOS_ENDPOINT, errorListener) {

        override fun parseNetworkResponse(response: NetworkResponse?): Response<Photos> =
            if(response?.statusCode == HTTP_OK || response?.statusCode == HTTP_NOT_MODIFIED) {
                String(response.data).run {
                    success(
                        Gson().fromJson(this, Photos::class.java),
                        HttpHeaderParser.parseCacheHeaders(response)
                    )
                }
            } else {
                Response.error(VolleyError())
            }

        override fun deliverResponse(response: Photos?) {
            responseListener.onResponse(response)
        }
    }

}