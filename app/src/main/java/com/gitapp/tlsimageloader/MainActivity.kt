package com.gitapp.tlsimageloader

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Downloader
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        try {
            val p = Picasso.Builder(this)
                .downloader(OkHttp3Downloader(finalConvert()))
                .build()

            p.load(
                "https://sdo.gsfc.nasa.gov/assets/img/latest/latest_2048_HMIIC.jpg")
                .fit().centerCrop()
                .placeholder(android.R.drawable.sym_contact_card)
                .error(android.R.drawable.sym_def_app_icon).into(img_pics)
        } catch (e: Exception) {
            Toast.makeText(this," error as a result of " + e.localizedMessage,Toast.LENGTH_LONG).show()
        }


    }

    //retunrns a X509TrustManager

    private fun provideX509TrustManager(): X509TrustManager? {
        try {
            //its a staic class calling the getInstance method which takes a params of DefaultAlgorithm
            val factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            //this is casting null as Keystore
            factory.init(null as KeyStore?)
            //referencing a variable inside TrustManagerFactory
            val trustManagers = factory.trustManagers
            //returns the first element in trustManager and cast as X509TrustManager
            return trustManagers[0] as X509TrustManager
        } catch (exception: NoSuchAlgorithmException) {
            Log.e(javaClass.simpleName, "not trust manager available", exception)
        } catch (exception: KeyStoreException) {
            Log.e(javaClass.simpleName, "not trust manager available", exception)
        }
        return null
    }



    //returns OKHttpClient
    fun finalConvert() : OkHttpClient {
        //gets an sslcontext

        val sslContext: SSLContext = SSLContext.getInstance("TLS")

        sslContext.init(null, null, null)
        val noSSLv3Factory: SSLSocketFactory

        noSSLv3Factory = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            Tls12SocketFactory(sslContext.socketFactory)
        } else {
            sslContext.socketFactory
        }

        //OkHttpClient is built
        val okb = OkHttpClient.Builder()
            .sslSocketFactory(noSSLv3Factory, provideX509TrustManager()!!)

        return okb.build()
    }










}
