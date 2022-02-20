package com.example.jetpackapp

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.example.jetpackapp.ui.theme.JetpackAppTheme
import com.google.gson.annotations.SerializedName
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url
import java.io.Serializable


data class PerreteResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val imgUrl: String
    )


interface APIService {

    @GET
    suspend fun getRandomDog(@Url url: String): Response<PerreteResponse>

    companion object {

        var apiService: APIService? = null

        fun getInstance(): APIService {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl( "https://dog.ceo/api/breeds/" )
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(APIService::class.java)
            }
            return apiService!!
        }
    }
}

data class Perrete(
    val nombre: String,
    val descripcion: String,
    var img: String = "https://dog.ceo/api/breeds/image/random"
): Serializable


class MainActivity : ComponentActivity() {

    companion object {

        lateinit var mainContext: Context

        val puppies: List<Perrete> = listOf(
            Perrete("Perrete ", "Descripción perrete"),
            Perrete("Perrete 2", "Descripción perrete"),
            Perrete("Perrete 3", "Descripción perrete"),
            Perrete("Perrete 4", "Descripción perrete"),
            Perrete("Perrete 5", "Descripción perrete"),
            Perrete("Perrete 6", "Descripción perrete"),
            Perrete("Perrete 7", "Descripción perrete"),
            Perrete("Perrete 8", "Descripción perrete"),
            Perrete("Perrete 9", "Descripción perrete"),
            Perrete("Perrete 10", "Descripción perrete"),
            Perrete("Perrete 11", "Descripción perrete"),
            Perrete("Perrete 12", "Descripción perrete"),
            Perrete("Perrete 13", "Descripción perrete"),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Actualizar imagenes de los perretes haciendo una petición por cada uno
        requestImages()

        // Damos tiempo para que se resuelvan antes de iniciar el contenido
        Thread.sleep( 1000 )

        mainContext = this

        setContent {
            JetpackAppTheme() {
                Perretes( true, puppies )
            }
        }

    }
}

private fun requestImages(){

    CoroutineScope(Dispatchers.IO).launch {

        MainActivity.puppies.forEach { perrete ->
            val call = APIService.getInstance().getRandomDog("image/random")
            val response  = call.body()
            if( call.isSuccessful ) {
                perrete.img = response?.imgUrl!!
            }
        }
    }
}

@Composable
fun MyImage( loadImg: Boolean, img: String ){


    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentSize(Alignment.Center)
    ){

        if( loadImg ){
            GlideImage(
                imageModel = img,
                // Crop, Fit, Inside, FillHeight, FillWidth, None
                contentScale = ContentScale.Crop,
                // shows a placeholder while loading the image.
                placeHolder = ImageVector.vectorResource(R.drawable.ic_launcher_foreground),
                // shows an error ImageBitmap when the request failed.
                error = ImageVector.vectorResource(R.drawable.ic_launcher_foreground),
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primary)
                    .size(128.dp)
            )
        }
        else{

            Image(
                painterResource( R.drawable.ic_launcher_foreground ),
                "imagen de prueba",
                modifier = Modifier
                    .clip(CircleShape)
                    .size( 128.dp )
                    .background(MaterialTheme.colors.primary)

            )
        }
    }
}

@Composable
fun MyComponent( loadImg: Boolean, puppy: Perrete ){
    Column(
        modifier = Modifier
        .padding(8.dp)
//        .background(MaterialTheme.colors.background)
    ) {

        Button(onClick = {

            val navigate = Intent( MainActivity.mainContext, DetailActivity::class.java)
            navigate.putExtra("puppy", puppy)
            startActivity( MainActivity.mainContext, navigate, null )

        }){

            MyImage( loadImg, puppy.img )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Perretes( loadImg: Boolean, messages: List<Perrete> ){

    LazyVerticalGrid(cells = GridCells.Fixed( 2 ), content = {
        itemsIndexed( messages ) { row, item ->
            MyComponent( loadImg, item )
        }
    })

//    LazyColumn{
//        items( messages ) { message ->
//        }
//    }
}

@Preview(showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewTexts(){
    JetpackAppTheme {
        Perretes( false, MainActivity.puppies )
    }
}