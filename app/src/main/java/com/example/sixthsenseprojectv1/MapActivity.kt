package com.example.sixthsenseprojectv1

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sixthsenseprojectv1.database.DBHandler
import com.example.sixthsenseprojectv1.databinding.ActivityMapBinding
import com.example.sixthsenseprojectv1.teams.Teammates
import com.example.sixthsenseprojectv1.ui.theme.SixthSenseProjectV1Theme
import com.example.sixthsenseprojectv1.ui.theme.customfont
import com.example.sixthsenseprojectv1.uiHelpers.*
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager

class MapActivity : ComponentActivity() {

    private lateinit var offlineManager: OfflineRegionManager
    private lateinit var offlineRegion: OfflineRegion
    private var mapView: MapView? = null
    private lateinit var binding: ActivityMapBinding

    private val regionObserver: OfflineRegionObserver = object : OfflineRegionObserver {
        override fun mapboxTileCountLimitExceeded(limit: Long) {
            logE(TAG, "Mapbox tile count max (= $limit) has exceeded!")
        }

        override fun statusChanged(status: OfflineRegionStatus) {
            logD(
                TAG,
                "${status.completedResourceCount}/${status.requiredResourceCount} resources; ${status.completedResourceSize} bytes downloaded."
            )
            if (status.downloadState == OfflineRegionDownloadState.INACTIVE) {
                downloadComplete()
                return
            }
        }

        override fun responseError(error: ResponseError) {
            logE(TAG, "onError: ${error.reason}, ${error.message}")
            offlineRegion.setOfflineRegionDownloadState(OfflineRegionDownloadState.INACTIVE)
        }
    }

    private val callback: OfflineRegionCreateCallback = OfflineRegionCreateCallback { expected ->
        if (expected.isValue) {
            expected.value?.let {
                offlineRegion = it
                it.setOfflineRegionObserver(regionObserver)
                it.setOfflineRegionDownloadState(OfflineRegionDownloadState.ACTIVE)
            }
        } else {
            logE(TAG, expected.error!!)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        offlineManager =
            OfflineRegionManager(MapInitOptions.getDefaultResourceOptions(this@MapActivity))
        offlineManager.createOfflineRegion(
            OfflineRegionGeometryDefinition.Builder()
                .geometry(point)
                .pixelRatio(2f)
                .minZoom(zoom - 2)
                .maxZoom(zoom + 2)
                .styleURL(styleUrl)
                .glyphsRasterizationMode(GlyphsRasterizationMode.NO_GLYPHS_RASTERIZED_LOCALLY)
                .build(),
            callback
        )

        setContent {
            SixthSenseProjectV1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Row {
                        Column {
                            getMap()
                        }
                    }
                }
            }
        }
    }

    private fun downloadComplete() {
        binding.downloadProgress.visibility = View.GONE
        binding.showMapButton.visibility = View.VISIBLE
        binding.showMapButton.setOnClickListener {
            it.visibility = View.GONE
            // create mapView
            mapView = MapView(this@MapActivity).also { mapview ->
                val mapboxMap = mapview.getMapboxMap()
                mapboxMap.setCamera(CameraOptions.Builder().zoom(zoom).center(point).build())
                mapboxMap.loadStyleUri(styleUrl)
            }
            setContentView(mapView)

            mapView?.onStart()
        }
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        offlineRegion.invalidate { }
        mapView?.onDestroy()
    }

    companion object {
        private const val TAG = "Offline"
        var zoom = 4.0
        var point: Point = Point.fromLngLat(-69.141054, 45.757005)
        private const val styleUrl = Style.DARK
    }

    fun selectMemberOnMapActivity(
        memberIndex: Int,
        viewModel: ViewModelRealTime,
        context: Context
    ) {
        selectedMember = teammateName[memberIndex]
        if (selectedMember == "Me") {
            viewModel.onUiViewChange(
                0,
                0,
                0,
                0,
                "Me",
                null,
                null,
                null
            )

            try {
                val i = teammateName.indexOf("Me")
                val wholeLongitude = myLongitude.getFromDatabase(context, teammateID[i])
                val wholeLatitude = myLatitude.getFromDatabase(context, teammateID[i])
                val delDate = "°"
                val longitudeArray = wholeLongitude.split(delDate)
                val latitudeArray = wholeLatitude.split(delDate)
                val longitude = longitudeArray[0].toDouble()
                val latitude = latitudeArray[0].toDouble()

                zoom = 6.0
                point = Point.fromLngLat(longitude, latitude)
            } catch (e: Exception) {
                //handle exception
            }
            val navigate = Intent(this@MapActivity, HomeActivity::class.java)
            startActivity(navigate)
        } else {
            viewModel.onUiViewChange(
                null,
                1,
                null,
                0,
                selectedMember,
                null,
                null,
                null
            )

            try {
                val i = teammateName.indexOf(selectedMember)
                val wholeLongitude = myLongitude.getFromDatabase(context, teammateID[i])
                val wholeLatitude = myLatitude.getFromDatabase(context, teammateID[i])
                val delDate = "°"
                val longitudeArray = wholeLongitude.split(delDate)
                val latitudeArray = wholeLatitude.split(delDate)
                val longitude = longitudeArray[0].toDouble()
                val latitude = latitudeArray[0].toDouble()

                zoom = 6.0
                point = Point.fromLngLat(longitude, latitude)
            } catch (e: Exception) {
                //handle exception
            }
            val navigate = Intent(this@MapActivity, HomeActivity::class.java)
            startActivity(navigate)
        }
    }

    /* This Method create the scaffold with floating action button and combing it with Health sub-screen */
    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun getMap() {
        Scaffold(modifier = Modifier
            .background(Color.Transparent)
            .clip(RoundedCornerShape(10.dp)),
            topBar = {
                //create the TopAppBar
                TopAppBar(modifier = Modifier
                    .fillMaxHeight(0.11f)
                    .padding(top = 3.dp, start = 3.dp, end = 3.dp, bottom = 3.dp)
                    .graphicsLayer {
                        shape = RoundedCornerShape(12.dp)
                        clip = true
                    }, elevation = 10.dp, title = {
                    Text(
                        "Map",
                        color = Color.White,
                        fontFamily = customfont,
                        overflow = TextOverflow.Ellipsis
                    )
                }, actions = {

                    //create the row layout to arrange the action button and dropdownMenu
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(.85f)
                            .fillMaxHeight()
                            .padding(1.dp)
                            .background(Color.Transparent),
                        horizontalArrangement = Arrangement.End,

                        ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(.80f)
                                .padding(2.dp)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(10.dp)),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            LazyHorizontalGrid(
                                GridCells.Fixed(1), Modifier

                                    .fillMaxSize(), horizontalArrangement = Arrangement.Start
                            ) {
                                items(teammateName.size) { index ->
                                    IconButton(modifier = Modifier
                                        .size(95.dp, 45.dp)
                                        .padding(1.dp)
                                        .background(Color.Transparent),
                                        onClick = {
                                            selectedMember = teammateName[index]
                                            if (selectedMember == "Me") {
                                                tspm = 0
                                                tspt = 0
                                                tstm = 0
                                                tstt = 0
                                                tst = "Me"
                                                tsv = 0

                                                try {
                                                    val i = teammateName.indexOf("Me")
                                                    val wholeLongitude =
                                                        myLongitude.getFromDatabase(
                                                            this@MapActivity,
                                                            teammateID[i]
                                                        )
                                                    val wholeLatitude = myLatitude.getFromDatabase(
                                                        this@MapActivity,
                                                        teammateID[i]
                                                    )
                                                    val delDate = "°"
                                                    val longitudeArray =
                                                        wholeLongitude.split(delDate)
                                                    val latitudeArray = wholeLatitude.split(delDate)
                                                    val longitude = longitudeArray[0].toDouble()
                                                    val latitude = latitudeArray[0].toDouble()

                                                    zoom = 6.0
                                                    point = Point.fromLngLat(longitude, latitude)
                                                } catch (e: Exception) {
                                                    //handle exception
                                                }
                                            } else {
                                                tspm = 0
                                                tspt = 1
                                                tstm = 0
                                                tstt = 0
                                                tst = selectedMember
                                                tsv = 0

                                                try {
                                                    val i = teammateName.indexOf(selectedMember)
                                                    val wholeLongitude =
                                                        myLongitude.getFromDatabase(
                                                            this@MapActivity,
                                                            teammateID[i]
                                                        )
                                                    val wholeLatitude = myLatitude.getFromDatabase(
                                                        this@MapActivity,
                                                        teammateID[i]
                                                    )
                                                    val delDate = "°"
                                                    val longitudeArray =
                                                        wholeLongitude.split(delDate)
                                                    val latitudeArray = wholeLatitude.split(delDate)
                                                    val longitude = longitudeArray[0].toDouble()
                                                    val latitude = latitudeArray[0].toDouble()

                                                    zoom = 6.0
                                                    point = Point.fromLngLat(longitude, latitude)
                                                } catch (e: Exception) {
                                                    //handle exception
                                                }
                                            }
                                            val navigate = Intent(
                                                this@MapActivity,
                                                HomeActivity::class.java
                                            )
                                            startActivity(navigate)
                                        }
                                    ) {
                                        Row {
                                            val pin = Teammates().returnPins(
                                                teammateName[index],
                                                this@MapActivity
                                            )
                                            val color =
                                                Teammates().returnPinsColor(teammateName[index])
                                            Icon(
                                                painter = painterResource(pin),
                                                "Pins",
                                                modifier = Modifier
                                                    .padding(1.dp)
                                                    .size(20.dp),
                                                tint = color
                                            )

                                            //create the space for size of 3.Dp
                                            Spacer(modifier = Modifier.size(3.dp))

                                            Text(
                                                teammateName[index], color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        //create the space for size of 3.Dp
                        Spacer(modifier = Modifier.size(3.dp))

                        // Health Action button
                        IconButton(modifier = Modifier
                            .padding(2.dp)
                            .size(55.dp), onClick = {
                            tspm = 0
                            tspt = 0
                            tstm = 0
                            tstt = 0
                            tst = "Me"
                            tsv = 0

                            val navigate = Intent(this@MapActivity, HomeActivity::class.java)
                            startActivity(navigate)
                        }) {


                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home",
                                tint = Color.White
                            )

                        }

                        //create the space for size of 3.Dp
                        Spacer(modifier = Modifier.size(3.dp))
                        val showConsentDialog = remember { mutableStateOf(false) }
                        if (showConsentDialog.value) {
                            DialogAlert().ConsentDialog(
                                onYesClick = {
                                    val dbHandler = DBHandler(this@MapActivity)
                                    dbHandler.exportToCSV(this@MapActivity)
                                    showConsentDialog.value = false
                                },
                                onNoClick = { showConsentDialog.value = false }
                            )
                        }
                        //Icons Button-4
                        IconButton(modifier = Modifier
                            .size(55.dp)
                            .padding(1.dp)
                            .background(Color.Transparent), onClick = {
                            showConsentDialog.value = true
                        }) {
                            Icon(

                                painter = painterResource(R.drawable.export),
                                "Export",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(4.dp),
                                tint = Color.White
                            )
                        }
                        //create the space for size of 2.Dp
                        Spacer(modifier = Modifier.size(2.dp))
                    }
                })
            }) {
            Column {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(3.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black), verticalArrangement = Arrangement.Center
                ) {

                    MapboxScreen()
                }
            }
        }
    }

    @Composable
    fun MapboxScreen() {
        val viewModel: ViewModelRealTime = viewModel()
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
                .testTag(stringResource(id = R.string.MBScreen))
        ) {
            AndroidView(
                modifier = Modifier,
                factory = { context ->
                    ResourceOptionsManager.getDefault(
                        context,
                        context.getString(R.string.MAPBOX_DOWNLOADS_TOKEN)
                    )
                    //create map
                    val initialCameraOptions = CameraOptions.Builder()
                        .center(point)
                        .zoom(zoom)
                        .build()
                    val mapInitOptions = MapInitOptions(
                        context,
                        MapInitOptions.getDefaultResourceOptions(context),
                        MapInitOptions.getDefaultMapOptions(context),
                        MapInitOptions.defaultPluginList,
                        initialCameraOptions,
                        true
                    )
                    val mapView = MapView(context, mapInitOptions)
                    mapView.apply {
                        getMapboxMap().loadStyleUri(Style.DARK)
                        for (i in teammateName.indices) {
                            addAnnotationToMap(mapView, i, onPinClick = {
                                selectMemberOnMapActivity(i, viewModel, this@MapActivity)
                            })
                        }
                    }
                }
            )
        }
    }

    fun addAnnotationToMap(mapView: MapView, i: Int, onPinClick: () -> Unit): View {
        // Create an instance of the Annotation API and get the PointAnnotationManager.

        try {
            val pin = Teammates().returnPins(teammateName[i], this@MapActivity)
            val wholeLongitude = myLongitude.getFromDatabase(this@MapActivity, teammateID[i])
            val wholeLatitude = myLatitude.getFromDatabase(this@MapActivity, teammateID[i])
            val delDate = "°"
            val longitudeArray = wholeLongitude.split(delDate)
            val latitudeArray = wholeLatitude.split(delDate)
            val longitude = longitudeArray[0].toDouble()
            val latitude = latitudeArray[0].toDouble()

            val location: Point = Point.fromLngLat(longitude, latitude)

            bitmapFromDrawableRes(this@MapActivity, pin)?.let {
                val annotationApi = mapView.annotations
                val pointAnnotationManager = annotationApi.createPointAnnotationManager()
                // Set options for the resulting symbol layer.
                val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                    // Define a geographic coordinate.
                    .withPoint(location)
                    // Specify the bitmap you assigned to the point annotation
                    // The bitmap will be added to map style automatically.
                    .withIconImage(it)
                // Add a click listener to the annotation
                pointAnnotationManager.addClickListener {
                    // Handle click event here
//                    Toast.makeText(this@MapActivity, "${teammateName[i]}", Toast.LENGTH_SHORT).show()
                    onPinClick()  // :PinSelectMember:
                    true
                }
                // Add the resulting pointAnnotation to the map.
                pointAnnotationManager.create(pointAnnotationOptions)
            }
        } catch (e: Exception) {
            //handle exception
        }

        return mapView
    }

    fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
        convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

    fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
            // copying drawable object to not manipulate on the same reference
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }

}