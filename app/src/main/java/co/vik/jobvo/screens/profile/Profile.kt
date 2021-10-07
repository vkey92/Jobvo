package co.vik.jobvo.screens.profile

import android.Manifest
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import co.vik.jobvo.R
import co.vik.jobvo.common.Common
import co.vik.jobvo.common.FilePath
import co.vik.jobvo.common.SpinnerAdapter
import co.vik.jobvo.databinding.ActivityProfileBinding
import co.vik.jobvo.pojo.ProfileModel
import co.vik.jobvo.screens.dashboard.Dashboard
import co.vik.jobvo.screens.database.AppDatabase
import co.vik.jobvo.screens.database.entity.ProfileData
import co.vik.jobvo.screens.signup.SignupViewModel
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.*
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import es.dmoral.toasty.Toasty
import id.zelory.compressor.Compressor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*

class Profile : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityProfileBinding
    private var adapter: SpinnerAdapter? = null
    private var editcheck = false
    private lateinit var dialog: Dialog
    private var userChoosenTask = ""
    private lateinit var imageUri: Uri
    private val REQUEST_CAMERA = 1
    private var SELECT_PICTURE = 100
    private var filepath = ""
    private var name = ""
    private var phone = ""
    private var email = ""
    private var gender = ""
    private var dob = ""
    private var address = ""
    private var state = ""
    private var district = ""
    private var profileurl = ""
    private var city = ""
    private var pincode = ""
    private var userid = ""
    private lateinit var database : AppDatabase
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var progressDialog: Dialog
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.navigationBarColor = resources.getColor(R.color.belowcolor)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        var listOfMindOrks = listOf("mindorks.com", "blog.mindorks.com", "afteracademy.com")
        var count = 0
        listOfMindOrks.forEach {

            count++;

        }
        init()

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.editbtn -> {
                editcheck = true
                //Common.hideViews(binding.noneditlayout);
                //Common.hideViews(binding.noneditlayout);
                binding.noneditlayout.visibility = View.GONE
                binding.editlayout.visibility = View.VISIBLE
                /*  ViewAnimator
                    .animate(binding.editlayout) //.scale(0, 1)
                    .bounce()
                    .start()*/
            }

            R.id.photoeditbtn -> {
                selectImage()
            }

            R.id.editsavebtn -> {
                name = binding.editname.text.toString().trim()
                phone = binding.editphoneno.text.toString().trim()
                email = binding.editemail.text.toString().trim()
                gender = binding.editgenderspin.selectedItem.toString().trim()
                if (gender.equals("gender", ignoreCase = true))
                    gender = ""
                dob = binding.editdob.text.toString().trim()
                address = binding.editaddress.text.toString().trim()
                state = binding.editstate.text.toString().trim()
                district = binding.editdistrict.text.toString().trim()
                city = binding.editcity.text.toString().trim()
                profileurl = binding.editprofileurl.text.toString().trim()
                pincode = binding.editpincode.text.toString().trim()
                if (!email.isEmpty() && !Common.emailValidator(email)) binding.editemail.error =
                    "Please enter valid email" else if (!pincode.isEmpty() && pincode.length < 6) binding.editpincode.error =
                    "Please enter valid pincode" else {
                    binding.editemail.error = null
                    binding.editpincode.error = null
                    getProfile()
                }

            }
        }
    }

    fun init() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
            overridePendingTransition(R.anim.enter1, R.anim.exit1)
        }
       database = AppDatabase.getDataBase(this)
        binding.editsavebtn.setOnClickListener(this)
        binding.photoeditbtn.setOnClickListener(this)
        binding.editbtn.setOnClickListener(this)
        userid = Common.getPreferences(applicationContext, "user_id").toString()
        val genderlist = ArrayList<String>()
        genderlist.clear()
        genderlist.add("Male")
        genderlist.add("Female")
        adapter = SpinnerAdapter(applicationContext, R.layout.chk1)
        adapter!!.addAll(genderlist)
        adapter!!.add("Gender")
        binding.editgenderspin.adapter = adapter
        binding.editgenderspin.setSelection(adapter!!.getCount())
        binding.profileusername.isSelected = true

        profileViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(ProfileViewModel::class.java)

        profileViewModel.getProfileData().observe(this, Observer<ProfileModel?> { profilemodel ->
            if (profilemodel != null) {
                Log.e("otp response = ", profilemodel.response.toString())
                if (profilemodel.status.equals("1", ignoreCase = true)) {
                    binding.profilemodel = profilemodel
                    val (user_id, dob) = profilemodel
                    Log.e("user id = ", println(user_id).toString())
                    Log.e("dob = ", dob.toString())
                    println(user_id)
                    println(dob)

                        Common.SetPreferences(this@Profile, "imageurl", profilemodel.data?.image)

                        val gendervalue = profilemodel.data?.gender
                        if (!gendervalue.equals("null", ignoreCase = true)) {
                            val spinnerPosition = adapter!!.getPosition(gendervalue)
                            binding.editgenderspin.setSelection(spinnerPosition)
                        }
                        Glide.with(applicationContext).load(profilemodel.data?.image)
                            .placeholder(R.mipmap.ic_launcher)
                            .dontAnimate().into(Dashboard.headerMainBinding.profileImage)

                        Glide.with(applicationContext).load(profilemodel.data?.image)
                            .placeholder(R.mipmap.ic_launcher)
                            .dontAnimate().into(binding.profileImage)

                        Glide.with(applicationContext).load(profilemodel.data?.image)
                            .placeholder(R.mipmap.ic_launcher)
                            .dontAnimate().into(binding.editprofileimage)


                        Dashboard.headerMainBinding.phoneno.setText(profilemodel.data?.name)
                        Dashboard.headerMainBinding.userName.setText(profilemodel.data?.mobile)
                    var model = ProfileData(0,
                        profilemodel.data?.image!!,profilemodel.data?.gender!!,profilemodel.data?.profileUrl!!,
                     profilemodel.data?.city!!,profilemodel.data?.mobile!!,profilemodel.data?.distict!!,profilemodel.data?.userId!!,profilemodel.data?.dob!!,
                    profilemodel.data?.name!!,profilemodel.data?.state!!,profilemodel.data?.email!!)
                       GlobalScope.launch {
                           database.jobvoDao().deleteProfile(model)
                           database.jobvoDao().insertProfile(model)
                       }
                    if (editcheck) {
                        editcheck = false
                        //Common.collapse(binding.editlayout);
                        //Common.expand(binding.noneditlayout);
                        //Common.hideViews(binding.editlayout);
                        binding.editlayout.visibility = View.GONE
                        binding.noneditlayout.visibility = View.VISIBLE
                        /* ViewAnimator
                            .animate(binding.noneditlayout) //.scale(0, 1)
                            .bounce()
                            .start()*/
                        progressDialog.dismiss()
                        getProfile()
                    }


                } else
                    Toasty.error(
                        applicationContext,
                        "" + profilemodel.response,
                        Toasty.LENGTH_SHORT
                    ).show()
            }
            progressDialog.dismiss()
        })
        database.jobvoDao().getProfile().observe(this, Observer {
            Log.e("JobvoDB = ",it.toString())
        })
        getProfile()


    }

    private fun getProfile() {
        progressDialog = Common.showProgressDialog(this@Profile)!!
        var part: MultipartBody.Part? = null
        if (!filepath.isEmpty()) {
            val sourceFile = File(filepath)
            var finalImage: File? = null
            try {
                finalImage = Compressor(this@Profile).compressToFile(sourceFile)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            Log.e("proper file path is", sourceFile.toString())
            val fileReqBody = RequestBody.create(
                "image/*".toMediaTypeOrNull(),
                finalImage!!
            )
            part = MultipartBody.Part.Companion.createFormData(
                "image",
                finalImage!!.name,
                fileReqBody
            )
        }
        val useridpart = MultipartBody.Part.Companion.createFormData("user_id", userid)
        val namepart = MultipartBody.Part.Companion.createFormData("name", name)
        val dobpart = MultipartBody.Part.Companion.createFormData("dob", dob)
        val genderpart = MultipartBody.Part.Companion.createFormData("gender", gender)
        val phonepart = MultipartBody.Part.Companion.createFormData("mobile", phone)
        val emailpart = MultipartBody.Part.Companion.createFormData("email", email)
        val citypart = MultipartBody.Part.Companion.createFormData("city", city)
        val statepart = MultipartBody.Part.Companion.createFormData("state", state)
        val distpart = MultipartBody.Part.Companion.createFormData("distict", district)
        val addpart = MultipartBody.Part.Companion.createFormData("address", address)
        val pinpart = MultipartBody.Part.Companion.createFormData("pincode", pincode)
        val profilepart = MultipartBody.Part.Companion.createFormData("profile_url", profileurl)
        if (part == null)
            part = useridpart

        profileViewModel.getProfileData(
            part, useridpart, namepart, dobpart, genderpart, phonepart, emailpart, citypart,
            statepart, distpart, addpart, pinpart, profilepart
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            var mBitmap: Bitmap? = null
            var bm: Bitmap? = null
            Log.e("request code = ", "" + requestCode)
            if (requestCode == SELECT_PICTURE) {
                dialog.dismiss()
                val selectedImageUri = data!!.data
                Log.e("image uri is =", selectedImageUri.toString())
                val thumbnail: Bitmap? = null
                if (data != null) {
                    //thumbnail = (Bitmap) data.getExtras().get("data");
                }
                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    filepath = FilePath.getPath(this@Profile, selectedImageUri).toString()
                    try {
                        try {
                            mBitmap = MediaStore.Images.Media.getBitmap(
                                this@Profile.contentResolver,
                                selectedImageUri
                            )
                            if (mBitmap != null) {
                                Log.e("bitmap is not", "=null")
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                        //  bm = Common.rotatebitmap(filepath, mBitmap);
                        binding.editprofileimage.setImageURI(selectedImageUri)
                        binding.profileImage.setImageURI(selectedImageUri)
                    } catch (ex: Throwable) {
                        ex.printStackTrace()
                        binding.editprofileimage.setImageBitmap(thumbnail)
                        binding.profileImage.setImageBitmap(thumbnail)
                    }
                }
            } else if (requestCode == REQUEST_CAMERA) {
                //onCaptureImageResult(data);
                dialog.dismiss()
                filepath = FilePath.getPath(this@Profile, imageUri).toString()
                try {
                    mBitmap =
                        MediaStore.Images.Media.getBitmap(this@Profile.contentResolver, imageUri)
                    if (mBitmap != null) {
                        Log.e("bitmap is not", "=null")
                    }
                    bm = Common.rotatebitmap(filepath, mBitmap)
                    binding.editprofileimage.setImageBitmap(bm)
                    binding.profileImage.setImageBitmap(bm)
                } catch (e: Throwable) {
                    e.printStackTrace()
                    binding.editprofileimage.setImageURI(imageUri)
                    binding.profileImage.setImageURI(imageUri)
                }
            }
        }
    }

    private fun selectImage() {
        dialog = Dialog(this@Profile)
        dialog.getWindow()?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.activity_image_select)
        dialog.setCancelable(true)
        val gallerylayout = dialog.findViewById<View>(R.id.LL_img_upload) as LinearLayout
        val cameralayout = dialog.findViewById<View>(R.id.LL_take_photo) as LinearLayout
        val camiamge: ImageView = dialog.findViewById(R.id.cameraimage)
        val galimage: ImageView = dialog.findViewById(R.id.galleryimage)
        cameralayout.setOnClickListener {
            userChoosenTask = "Take Photo"
            requestCameraPermission()
        }
        gallerylayout.setOnClickListener {
            userChoosenTask = "Choose from Library"
            requestStoragePermission()
        }
        dialog.show()
    }

    private fun requestCameraPermission() {
        Dexter.withActivity(this@Profile)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    // permission is granted
                    openCamera()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    // check for permanent denial of permission
                    if (response.isPermanentlyDenied) {
                        Common.showSettingsDialog(this@Profile)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private fun requestStoragePermission() {
        Dexter.withActivity(this@Profile)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        //  Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        openGallery()
                    }

                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) {
                        // show alert dialog navigating to Settings
                        Common.showSettingsDialog(this@Profile)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).withErrorListener {
                Toast.makeText(this@Profile, "Error occurred! ", Toast.LENGTH_SHORT).show()
            }
            .onSameThread()
            .check()
    }

    fun openCamera() {
        try {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "New Picture")
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
            imageUri = this@Profile.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            )!!
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, REQUEST_CAMERA)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT //
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_PICTURE)
    }


}