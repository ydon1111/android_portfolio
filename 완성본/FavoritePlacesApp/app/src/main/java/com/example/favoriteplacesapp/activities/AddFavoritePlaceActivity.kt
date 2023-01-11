package com.example.favoriteplacesapp.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.webkit.PermissionRequest
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.favoriteplacesapp.R
import com.example.favoriteplacesapp.database.DatabaseHandler
import com.example.favoriteplacesapp.databinding.ActivityAddFavoritePlaceBinding
import com.example.favoriteplacesapp.models.FavoritePlaceModel
import com.example.favoriteplacesapp.utils.GetAddressFromLatLng
import com.google.android.gms.location.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
class AddHappyPlaceActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddFavoritePlaceBinding

    private var cal = Calendar.getInstance()
    private lateinit var dataSetListener: DatePickerDialog.OnDateSetListener
    private var saveImageToInternalStorage: Uri? = null
    private var mLatitude: Double = 0.0
    private var mLongitude: Double = 0.0

    private var mFavoritePlaceDetails: FavoritePlaceModel? = null

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddFavoritePlaceBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbarAddPlace)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbarAddPlace.setNavigationOnClickListener {
            onBackPressed()
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (!Places.isInitialized()) {
            Places.initialize(
                this@AddHappyPlaceActivity,
                resources.getString(R.string.google_api_key)
            )
        }

        if (intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)) {
            mFavoritePlaceDetails =
                intent.getSerializableExtra(MainActivity.EXTRA_PLACE_DETAILS) as FavoritePlaceModel?
        }

        dataSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }
        updateDateInView()

        if (mFavoritePlaceDetails != null) {
            supportActionBar?.title = "추억의장소 수정"

            binding.etTitle.setText(mFavoritePlaceDetails!!.title)
            binding.etDescription.setText(mFavoritePlaceDetails!!.description)
            binding.etDate.setText(mFavoritePlaceDetails!!.date)
            binding.etLocation.setText(mFavoritePlaceDetails!!.location)
            mLatitude = mFavoritePlaceDetails!!.latitude
            mLongitude = mFavoritePlaceDetails!!.longitude

            saveImageToInternalStorage = Uri.parse(
                mFavoritePlaceDetails!!.image
            )

            binding.ivPlaceImage.setImageURI(saveImageToInternalStorage)

            binding.btnSave.text = "수정하기"


        }

        binding.etDate.setOnClickListener(this)
        binding.tvAddImage.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
        binding.etLocation.setOnClickListener(this)
        binding.tvSelectCurrentLocation.setOnClickListener(this)
    }

    private fun isLocationEnable(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE)
                as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {

        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location? = locationResult.lastLocation
            mLatitude = mLastLocation!!.latitude
            Log.i("현재 위도", "$mLatitude")
            mLongitude = mLastLocation.longitude
            Log.i("현재 경도", "$mLongitude")

            val addressTask =
                GetAddressFromLatLng(this@AddHappyPlaceActivity, mLatitude, mLongitude)
            addressTask.setAddressListener(object : GetAddressFromLatLng.AddressListener{
                override fun onAddressFound(address:String?){
                    binding.etLocation.setText(address)
                }
                override fun onError(){
                    Log.e("Get Address::","오류발생@@@")
                }
            })
            addressTask.getAddress()
        }
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.et_date -> {
                DatePickerDialog(
                    this,
                    dataSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            R.id.tv_add_image -> {
                val pictureDialog = AlertDialog.Builder(this)
                pictureDialog.setTitle("옵션 선택")
                val pictureDialogItems = arrayOf("갤러리에서 사진 선택", "사진 찍기")
                pictureDialog.setItems(pictureDialogItems) { _, which ->
                    when (which) {
                        0 -> choosePhotoFromGallery()
                        1 -> takePhotoFromCamera()
                    }
                }
                pictureDialog.show()
            }
            R.id.btn_save -> {
                when {
                    binding.etTitle.text.isNullOrEmpty() -> {
                        Toast.makeText(this, "방문장소를 입력하세요", Toast.LENGTH_SHORT).show()
                    }
                    binding.etDescription.text.isNullOrEmpty() -> {
                        Toast.makeText(this, "상세정보를 입력하세요", Toast.LENGTH_SHORT).show()
                    }
                    binding.etLocation.text.isNullOrEmpty() -> {
                        Toast.makeText(this, "장소를 입력하세요", Toast.LENGTH_SHORT).show()
                    }
                    saveImageToInternalStorage == null -> {
                        Toast.makeText(this, "사진을 넣어주세요", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        val favoritePlaceModel = FavoritePlaceModel(
                            if (mFavoritePlaceDetails == null) 0 else mFavoritePlaceDetails!!.id,
                            binding.etTitle.text.toString(),
                            saveImageToInternalStorage.toString(),
                            binding.etDescription.text.toString(),
                            binding.etDate.text.toString(),
                            binding.etLocation.text.toString(),
                            mLatitude,
                            mLongitude
                        )
                        val dbHandler = DatabaseHandler(this)

                        if (mFavoritePlaceDetails == null) {
                            val addFavoritePlace = dbHandler.addFavoritePlace(favoritePlaceModel)
                            if (addFavoritePlace > 0) {
                                setResult(Activity.RESULT_OK)
                                finish()
                            }
                        } else {
                            val updateFavoritePlace =
                                dbHandler.updateFavoritePlace(favoritePlaceModel)
                            if (updateFavoritePlace > 0) {
                                setResult(Activity.RESULT_OK)
                                finish()
                            }
                        }
                    }
                }
            }
            R.id.et_location -> {
                try {
                    val fields = listOf(
                        Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG,
                        Place.Field.ADDRESS
                    )
                    val intent =
                        Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                            .build(this@AddHappyPlaceActivity)
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            R.id.tv_select_current_location -> {
                if (!isLocationEnable()) {
                    Toast.makeText(
                        this,
                        "위치정보 제공이 꺼져있습니다. 위치정보 제공을 켜주세요.",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                } else {
                    Dexter.withActivity(this).withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ).withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                            if (report!!.areAllPermissionsGranted()) {

                                requestNewLocationData()
                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                            token: PermissionToken?
                        ) {
                            showRationalDialogForPermissions()
                        }
                    }).onSameThread()
                        .check()
                }
            }
        }
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                if (data != null) {
                    println(data)
                    val contentURI = data.data
                    try {
                        @Suppress("DEPRECATION")
                        val selectedImageBitmap =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)

                        saveImageToInternalStorage =
                            saveImageToInternalStorage(selectedImageBitmap)
                        Log.e("saveImage", "path:: $saveImageToInternalStorage")

                        binding.ivPlaceImage.setImageBitmap(selectedImageBitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@AddHappyPlaceActivity, "이미지 불러오기 실패", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else if (requestCode == CAMERA) {
                val thumbnail: Bitmap = data!!.extras!!.get("data") as Bitmap

                saveImageToInternalStorage = saveImageToInternalStorage(thumbnail)
                Log.e("saveImage", "Path:: $saveImageToInternalStorage")

                binding.ivPlaceImage.setImageBitmap(thumbnail)
            } else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
                val place: Place = Autocomplete.getPlaceFromIntent(data!!)
                binding.etLocation.setText(place.address)
                mLatitude = place.latLng!!.latitude
                mLongitude = place.latLng!!.longitude
            }

        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("취소됨", "취소됨")
        }
    }

    private fun takePhotoFromCamera() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        val galleryIntent =
                            Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE
                            )
                        startActivityForResult(galleryIntent, CAMERA)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationalDialogForPermissions()
                }
            }).onSameThread().check()

    }


    private fun choosePhotoFromGallery() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        val galleryIntent =
                            Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                        startActivityForResult(galleryIntent, GALLERY)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationalDialogForPermissions()
                }
            }).onSameThread().check()
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this).setMessage("권한 승인이 필요합니다.")
            .setPositiveButton("권한 설정 가기") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }.setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun updateDateInView() {
        val myFormat = "yyyy.MM.dd"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        binding.etDate.setText(sdf.format(cal.time).toString())
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri {
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Uri.parse(file.absolutePath)

    }

    companion object {
        private const val GALLERY = 1
        private const val CAMERA = 2
        private const val IMAGE_DIRECTORY = "추억의 장소들"
        private const val PLACE_AUTOCOMPLETE_REQUEST_CODE = 3
    }
}
