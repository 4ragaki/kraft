package `fun`.aragaki.kraft.ui.widget

import `fun`.aragaki.kraft.Kraft
import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.TypedValue
import android.widget.RemoteViews
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import androidx.core.graphics.scale
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.glance.*
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.updateAll
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxSize
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.Text
import coil.Coil
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.util.CoilUtils
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.instance
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.net.URL


class PopularWidget : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = GlanceWidget()


    class GlanceWidget : GlanceAppWidget() {
        override val stateDefinition = GlanceState()

        @Composable
        override fun Content() {

            Image(
                provider = ImageProvider(
                    BitmapFactory.decodeStream(
                        URL("https://files.yande.re/jpeg/4511a32f61a6051b7a373ccd4c1d7378/yande.re%20687592%20dress%20hashimoto_sana%20hiten%20penguin.jpg").openConnection()
                            .getInputStream()
                    )
                ),
                contentScale = ContentScale.Crop,
                modifier = GlanceModifier.fillMaxSize()
                    .cornerRadius(16.dp),
                contentDescription = ""
            )
        }

    }

    class GlanceState : GlanceStateDefinition<PopularConfiguration> {
        override suspend fun getDataStore(
            context: Context,
            fileKey: String
        ) = DataStoreFactory.create(object : Serializer<PopularConfiguration> {
            val boorus by Kraft.app.instance<List<BooruWrapper>>()
            override val defaultValue: PopularConfiguration = PopularConfiguration(boorus.first())

            //            TODO
            override suspend fun readFrom(input: InputStream): PopularConfiguration {
                return defaultValue
            }

            //            TODO
            override suspend fun writeTo(t: PopularConfiguration, output: OutputStream) {

            }

        }) { getLocation(context, fileKey) }


        override fun getLocation(context: Context, fileKey: String) =
            File(context.filesDir, fileKey)

    }
}