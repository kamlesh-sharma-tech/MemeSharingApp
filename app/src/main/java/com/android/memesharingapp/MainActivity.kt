package com.android.memesharingapp

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import android.content.DialogInterface

import android.app.AlertDialog


class MainActivity : AppCompatActivity() {
    var memeUrl : String ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
    }

    //create a function to load memes
    fun loadMeme(){
        val loading : ProgressBar = findViewById(R.id.progress)
        loading.visibility = View.VISIBLE

        val queue = Volley.newRequestQueue(this);
        val url = "https://meme-api.herokuapp.com/gimme"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,url,null,
            Response.Listener {
                memeUrl = it.getString("url")
                var image:ImageView = findViewById(R.id.memeImage)
                Glide.with(this).load(memeUrl).listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        loading.visibility = View.VISIBLE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        loading.visibility = View.GONE
                        return false
                    }

                }).into(image)
            },
            Response.ErrorListener {

            }
        )
        queue.add(jsonObjectRequest)

    }

    fun nextMeme(view: View) {
        loadMeme()
    }
    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"See this meme \n$memeUrl")
        val chooser = Intent.createChooser(intent,"Share Meme")
        startActivity(chooser)
    }

    override fun onBackPressed() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Exit Application")
        builder.setMessage("Are you sure you want to exit ?")
        builder.setCancelable(false)
        builder.setIcon(R.drawable.ic_exit)
        builder.setNegativeButton("No",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.setPositiveButton("Yes",
            DialogInterface.OnClickListener { dialog, which -> finish() })
        val alertDialog: AlertDialog = builder.create()
        builder.show()
    }

}