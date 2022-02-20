package com.example.jetpackapp

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.jetpackapp.ui.theme.JetpackAppTheme
import com.skydoves.landscapist.glide.GlideImage

class DetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val puppy = intent?.getSerializableExtra("puppy") as Perrete

        setContent {
            JetpackAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    DetailComponent(puppy = puppy)
                }
            }
        }
    }
}


@Composable
fun DetailComponent( puppy: Perrete ){
    Column(
        modifier = Modifier
            .padding(8.dp)
            .background(MaterialTheme.colors.background) ) {

            DetailImage( puppy.img )
            DetailTexts( puppy )
    }
}

@Composable
fun DetailImage( img: String ){


    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentSize(Alignment.Center)
    ){

        GlideImage(
            imageModel = img,
            // Crop, Fit, Inside, FillHeight, FillWidth, None
            contentScale = ContentScale.Crop,
            // shows a placeholder while loading the image.
            placeHolder = ImageVector.vectorResource(R.drawable.ic_launcher_foreground),
            // shows an error ImageBitmap when the request failed.
            error = ImageVector.vectorResource(R.drawable.ic_launcher_foreground),
            modifier = Modifier
//                .clip(CircleShape)
                .background(MaterialTheme.colors.primary)
                .size(256.dp)
        )
    }
}


@Composable
fun DetailTexts( puppy: Perrete ){
    Row( modifier = Modifier
        .padding(top = 8.dp)
    ) {
        MyText(
            text = puppy.nombre,
            MaterialTheme.colors.primary,
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Row {
        MyText(
            text = puppy.descripcion,
            MaterialTheme.colors.onBackground
        )
    }

}

@Composable
fun MyText( text: String, color: Color){
    Text(
        text = text,
        color = color,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
    )
}