package co.vik.jobvo.common

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import co.vik.jobvo.R
import java.io.ByteArrayOutputStream
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

@SuppressLint("SimpleDateFormat")
class Common {
    // dialog for login again after session expired.
    var dialogA: Dialog? = null


    companion object {
        // this dialog used for progress dialog
        var rslt = ""
        var idvalue: String? = null
        var sharedpreferences: SharedPreferences? = null
        private var dialog: Dialog? = null
        var Format_ddMMyyyy = "yyyy-MM-dd"
        var formatStrings = arrayOf("dd/MM/yyyy", "dd-MMM-yy", "M-d-y")
        var mainurl = "http://jobvoo.com/api/userapi/"
        //var mainurl = "  http://jobvoo.com/"

        var weburl = "http://jobvoo.com/"
        var SELECT_PICTURE = 100

        //public static String mainurl = "http://mindsekhelo.com/ricplatinum";
        /************************************************** check internet connection  */
        fun isInternetOn(con: Context): Boolean {
            val connectivityManager: ConnectivityManager
            var wifiInfo: NetworkInfo
            var mobileInfo: NetworkInfo
            var connected = false
            try {
                connectivityManager = con
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (connectivityManager != null) {
                    val info = connectivityManager.allNetworkInfo
                    if (info != null) for (i in info.indices) if (info[i].state == NetworkInfo.State.CONNECTED) {
                        connected = true
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return connected
        }

        /*************************************End check internet connection */
        fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
            val bytes = ByteArrayOutputStream()
            inImage.compress(Bitmap.CompressFormat.JPEG, 80, bytes)
            val path = MediaStore.Images.Media.insertImage(
                inContext.contentResolver, inImage, "IMG_" + System.currentTimeMillis(), null
            )
            Log.e("my uri is", path)
            return Uri.parse(path)
        }

        fun getRealPathFromUri(context: Context, contentUri: Uri?): String? {
            var cursor: Cursor? = null
            return try {
                val proj = arrayOf(MediaStore.Images.Media.DATA)
                cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
                val column_index: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor!!.moveToFirst()
                cursor.getString(column_index)
            } finally {
                if (cursor != null) {
                    cursor.close()
                }
            }
        }


        /** */
        /***************************************************** set shared preferences  */
        fun SetPreferences(con: Context, key: String?, value: String?) {
            // save the data
            val preferences = con.getSharedPreferences(
                "hyd", 0
            )
            val editor = preferences.edit()
            editor.putString(key, value)
            editor.commit()
        }

        /**************************************************** get shared preferences  */
        fun getPreferences(con: Context, key: String?): String? {
            val sharedPreferences = con.getSharedPreferences(
                "hyd", 0
            )
            return sharedPreferences.getString(key, "0")
        }

        /**************************************************** show toast msg  */
        fun showToast(cont: Context?, msg: String?, duration: Int) {
            Toast.makeText(cont, msg, duration).show()
        }

        /******************************************************************************************************************************
         * used to set activity at bottom (activity set bottom when we used activity
         * as dialog box and for set activity as dialog we set theme in activity in
         * manifest file )
         */
        fun setActivityAtBottom(act: Activity) {
            val winlayp = act.window.attributes
            winlayp.gravity = Gravity.BOTTOM
        }// String cuurent_date = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss")
        //  Date date=Calendar.getInstance().getTime();
        /***************************************** Get Current Days */
        fun getcurrentdate(format: String): String {

            // String cuurent_date = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss")
            val cuurent_date = SimpleDateFormat(format)
                .format(Calendar.getInstance().time)
            //  Date date=Calendar.getInstance().getTime();
            println("cuurent_date$cuurent_date")
            return cuurent_date
        }

        fun getcurrentmont(): String {

            // String cuurent_date = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss")
            val cuurent_date = SimpleDateFormat("MMM")
                .format(Calendar.getInstance().time)
            //  Date date=Calendar.getInstance().getTime();
            println("cuurent_date$cuurent_date")
            return cuurent_date
        }
        /** */
        /*********************** string convert to date  */
        /**************************************** Get Cover Days Data  */
        fun StringConvertDate(con: Context?, str: String?): java.util.Date? {
            for (formatString in formatStrings) {
                try {
                    try {
                        return SimpleDateFormat(formatString).parse(str)
                    } catch (e: ParseException) {
                        // TODO Auto-generated catch block
                        //e.printStackTrace();
                    }
                } catch (e: android.net.ParseException) {
                }
            }
            return null
        }

        /**************************************** Get Cover Days Data  */
        fun StringToConvertDate(str: String?): java.util.Date? {
            for (formatString in formatStrings) {
                try {
                    try {
                        return SimpleDateFormat(formatString).parse(str)
                    } catch (e: ParseException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                } catch (e: android.net.ParseException) {
                }
            }
            return null
        }

        /** */
        fun StringToBool(s: String): Boolean {
            return if (s == "1" || s.equals(
                    "true",
                    ignoreCase = true
                )
            ) true else if (s == "0" || s.equals("false", ignoreCase = true)) false else false
        }
        /////////////////////////////////
        /** */
        fun BoolToString(bol: Boolean): String {
            return if (bol == true) "1" else "0"
        }

        /** */
        fun DateConvertString(dt: java.util.Date?): String {
            val reportDate: String
            reportDate = try {
                val df: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                df.format(dt)
            } catch (e: NullPointerException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
                ""
            }
            return reportDate
        }

        /*********************************************  String dtae convert date */
        fun setStringToDateformate(date_st: String?): java.util.Date? {
            val formatter = SimpleDateFormat("dd-MM-yyyy")
            var dt: java.util.Date? = null
            try {
                dt = formatter.parse(date_st)
            } catch (e: ParseException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
            return dt
        }

        /************************************************ Convert Bitmap to Byte Array  */
        fun BitmapToByteArray(bitmap: Bitmap?): ByteArray? {
            val byte_arr: ByteArray
            if (bitmap != null) {
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                byte_arr = stream.toByteArray()
                return byte_arr
            }
            return null
        }

        /************************************************ Convert Byte Array To Bitmap  */
        fun ByteArrayToBitmap(byte_array: ByteArray): Bitmap {
            return BitmapFactory.decodeByteArray(byte_array, 0, byte_array.size)
        }

        /************************************************* Bitmap to String  */
        fun BitMapToString(bitmap: Bitmap): String {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val arr = baos.toByteArray()
            return Base64.encodeToString(arr, Base64.DEFAULT)
        }

        /************************************************* String to Bitmap  */
        fun StringToBitMap(image: String?): Bitmap? {
            return try {
                val encodeByte = Base64.decode(image, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(
                    encodeByte, 0,
                    encodeByte.size
                )
            } catch (e: Exception) {
                e.message
                null
            }
        }

        /*********************************************** get current time  */
        val currentTime: String
            get() = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                Calendar
                    .getInstance().time
            )

        fun checktimings(time: String?, endtime: String?): Boolean {
            val pattern = "HH:mm"
            val sdf = SimpleDateFormat(pattern)
            try {
                val date1 = sdf.parse(time)
                val date2 = sdf.parse(endtime)
                return if (date1.before(date2)) {
                    true
                } else {
                    false
                }
            } catch (e: android.net.ParseException) {
                e.printStackTrace()
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return false
        }


        /***************************************************** hide progress dialog  */
        fun hidePreloader() {
            // hide dialog
            if (dialog != null) {
                if (dialog!!.isShowing) {
                    dialog!!.dismiss()
                }
                dialog = null
            }
        }


        /********************************************** Hide Keyboard  */
        fun hide_keyboard(view: View?, context: Context) {
            if (view != null) {
                val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        /********************************************** Is numaric not  */
        fun isNumeric(str: String): Boolean {
            try {
                val i = str.toInt()
            } catch (nfe: NumberFormatException) {
                return false
            }
            return true
        }

        /**************************************** Hide Keyboard outside edittexts  */
        fun setupUI(view: View, context: Context) {

            //Set up touch listener for non-text box views to hide keyboard.
            if (view !is EditText) {
                view.setOnTouchListener { v, event ->
                    hide_keyboard(view, context)
                    false
                }
            }

            //If a layout container, iterate over children and seed recursion.
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    val innerView = view.getChildAt(i)
                    setupUI(innerView, context)
                }
            }
        }

        fun convertDATe(uDate: java.util.Date): Date {
            return Date(uDate.time)
        }

        fun NumberFormatData(str: String): String {
            // TODO Auto-generated method stub
            return if (isNumeric(str)) {
                str
            } else "-1"
        }

        fun IntegerNullParser(str: String): String {
            // TODO Auto-generated method stub
            return if (str.equals("-1", ignoreCase = true) || str.equals("0", ignoreCase = true)) {
                ""
            } else str
        }

        /**
         * Returns the User device name
         */
        val deviceName: String
            get() {
                val manufacturer = Build.MANUFACTURER
                val model = Build.MODEL
                return if (model.startsWith(manufacturer)) {
                    capitalize(model)
                } else capitalize(manufacturer) + " " + model
            }

        private fun capitalize(str: String): String {
            if (TextUtils.isEmpty(str)) {
                return str
            }
            val arr = str.toCharArray()
            var capitalizeNext = true
            var phrase = ""
            for (c in arr) {
                if (capitalizeNext && Character.isLetter(c)) {
                    phrase += Character.toUpperCase(c)
                    capitalizeNext = false
                    continue
                } else if (Character.isWhitespace(c)) {
                    capitalizeNext = true
                }
                phrase += c
            }
            return phrase
        }
        /** */
        /*************************************************** show progress dialog  */ /*	public static void showProgressDialog(Context context_dilog, String Title) {
		// check privious dialog

		dialog= new Dialog(context_dilog);

		if (dialog != null || dialog.isShowing()) {
			dialog.dismiss();
		}

			*/
        /* dialog.setCancelable(false);
		     dialog.setCanceledOnTouchOutside(false);*/
        /*
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable((new ColorDrawable(android.graphics.Color.TRANSPARENT)));
		dialog.setContentView(R.layout.prgress_bar_layout);
		TextView title = (TextView)dialog.findViewById(R.id.tv_info);
		TextView tv_wait= (TextView)dialog.findViewById(R.id.tv_wait);

		Typeface typeFace_medium = Typeface.createFromAsset(context_dilog.getAssets(), "FREESCPT_0.TTF");
		tv_wait.setTypeface(typeFace_medium);

		title.setText(Title);
		dialog.setCancelable(true);
		dialog.show();

	}*/
        /***************************************************** hide progress dialog  */
        fun hideProgressDialog() {
            // hide dialog
            /*dialog.setCancelable(false);
	        dialog.setCanceledOnTouchOutside(false);*/
            if (dialog != null) {
                if (dialog!!.isShowing) {
                    dialog!!.dismiss()
                }
                dialog = null
            }
        }

        ////////////////////////////////// remove whitespace from email //////////////
        fun containsWhiteSpace(testCode: String?): Boolean {
            if (testCode != null) {
                for (i in 0 until testCode.length) {
                    if (Character.isWhitespace(testCode[i])) {
                        return true
                    }
                }
            }
            return false
        }

        ////////////////////////////////// Dialog One Button //////////////
        /*public static void DialogOneBtn(Context context, String title_text, String info_text) {
		final Dialog dialogOneBtn = new Dialog(context);
		dialogOneBtn.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//dialogTwoBtn.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		dialogOneBtn.getWindow().setBackgroundDrawable((new ColorDrawable(android.graphics.Color.TRANSPARENT)));
		dialogOneBtn.setContentView(R.layout.dialog_box_single_pierrofino);


		TextView title = (TextView) dialogOneBtn.findViewById(R.id.tv_title);
		TextView info = (TextView) dialogOneBtn.findViewById(R.id.tv_info);
		Button btnOK = (Button) dialogOneBtn.findViewById(R.id.ok_button);

		title.setText(""+title_text);
		info.setText(""+info_text);

		btnOK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialogOneBtn.dismiss();
			}
		});


		dialogOneBtn.show();


	}
*/
        fun hideKeyboard(ctx: Context) {
            val inputManager = ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            // check if no view has focus:
            val v = (ctx as Activity).currentFocus ?: return
            inputManager.hideSoftInputFromWindow(v.windowToken, 0)
        }

        fun roundTwoDecimals(d: Float): Float {
            //Log.e("to be round", ""+d);
            val twoDForm = DecimalFormat("#.##")
            //Log.e("twoDForm", ""+twoDForm.format(d));
            val abc = "" + twoDForm.format(d.toDouble())
            return if (abc.contains(",")) {
                java.lang.Float.valueOf(twoDForm.format(d.toDouble()).replace(",", "."))
            } else {
                java.lang.Float.valueOf(twoDForm.format(d.toDouble()))
            }
        }

        ////////////////////////////////// get Day of Year //////////////
        fun getDayOfYear(context: Context?): Int {
            val cal = Calendar.getInstance()
            return cal[Calendar.DAY_OF_YEAR]
        }

        fun getDate(timeStamp: Long): String {
            return try {
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val netDate = java.util.Date(timeStamp)
                sdf.format(netDate)
            } catch (ex: Exception) {
                "xx"
            }
        }

        fun gettimestamp(timestamp: Long): String {
            try {
                val calendar = Calendar.getInstance()
                val tz = TimeZone.getDefault()
                calendar.timeInMillis = timestamp * 1000
                calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.timeInMillis))
                val sdf = SimpleDateFormat("hh:mm a")
                val currenTimeZone = calendar.time as java.util.Date
                return sdf.format(currenTimeZone)
            } catch (e: Exception) {
            }
            return ""
        }

        fun getdatefromtimestamp(timestamp: Long, format: String?): String {
            try {
                val cal = Calendar.getInstance(Locale.ENGLISH)
                cal.timeInMillis = timestamp
                val sdf = SimpleDateFormat(format)
                val currenTimeZone = cal.time as java.util.Date
                return sdf.format(currenTimeZone)
            } catch (e: Exception) {
            }
            return ""
        }

        fun digitalclock(textView: TextView, context: Activity) {
            val thread: Thread = object : Thread() {
                override fun run() {
                    try {
                        while (!isInterrupted) {
                            sleep(1000)
                            context.runOnUiThread {
                                val date = System.currentTimeMillis()
                                val sdf = SimpleDateFormat("hh:mm:ss")
                                val datestring = sdf.format(date)
                                textView.text = datestring
                            }
                        }
                    } catch (e: InterruptedException) {
                    }
                }
            }
            thread.start()
        }

        fun Replacewhitespaces(str: String?, replace: String?): String {
            val ptn = Pattern.compile("\\s+")
            val mtch = ptn.matcher(str)
            return mtch.replaceAll(replace)
        }

        fun showfulltext(activity: Activity?, msg: String?) {
            AlertDialog.Builder(activity) //.setTitle("Nuke planet Jupiter?")
                .setMessage(msg)
                .setPositiveButton("Ok") { dialog, which -> dialog.dismiss() }
                .show()
        }

        fun formatDateTime(date: java.util.Date?, format: String?): String {
            //"dd MMM yyyy" HH:mm aa
            val formatter: DateFormat = SimpleDateFormat(format)
            return formatter.format(date)
        }


        fun stringTodate(dtStart: String?): java.util.Date? {
            var date: java.util.Date? = null
            val format = SimpleDateFormat("yyy-MM-dd HH:mm:ss")
            try {
                date = format.parse(dtStart)
                println(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return date
        }

        fun formateDateFromstring(
            inputFormat: String?,
            outputFormat: String?,
            inputDate: String?
        ): String {
            var parsed: java.util.Date? = null
            var outputDate = ""
            val df_input = SimpleDateFormat(inputFormat, Locale.getDefault())
            val df_output = SimpleDateFormat(outputFormat, Locale.getDefault())
            try {
                parsed = df_input.parse(inputDate)
                outputDate = df_output.format(parsed)
            } catch (e: Exception) {
            }
            return outputDate
        }

        fun expand(v: View) {
            v.measure(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val targtetHeight = v.measuredHeight
            v.layoutParams.height = 0
            v.visibility = View.VISIBLE
            val a: Animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                    v.layoutParams.height =
                        if (interpolatedTime == 1f) LinearLayout.LayoutParams.WRAP_CONTENT else (targtetHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }

                override fun willChangeBounds(): Boolean {
                    return true
                }
            }
            a.setDuration(
                ((targtetHeight / v.context.resources.displayMetrics.density) as Float).toLong()
            )
            v.startAnimation(a)
        }

        fun expand1(v: View) {
            v.measure(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val targtetHeight = v.measuredHeight
            v.layoutParams.height = 0
            v.visibility = View.GONE
            val a: Animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                    v.layoutParams.height =
                        if (interpolatedTime == 1f) LinearLayout.LayoutParams.WRAP_CONTENT else (targtetHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }

                override fun willChangeBounds(): Boolean {
                    return true
                }
            }
            a.setDuration(
                (targtetHeight / v.context.resources.displayMetrics.density) as Long
            )
            v.startAnimation(a)
        }

        fun collapse(v: View) {
            val initialHeight = v.measuredHeight
            val a: Animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                    if (interpolatedTime == 1f) {
                        v.visibility = View.GONE
                    } else {
                        v.layoutParams.height =
                            initialHeight - (initialHeight * interpolatedTime).toInt()
                        v.requestLayout()
                    }
                }

                override fun willChangeBounds(): Boolean {
                    return true
                }
            }
            a.setDuration(
                ((initialHeight / v.context.resources.displayMetrics.density) as Float).toLong()

            )
            v.startAnimation(a)
        }

        fun slidefromRightToLeft(view: View, main_layout: View) {
            val animate: TranslateAnimation
            animate = if (view.height == 0) {
                main_layout.height // parent layout
                TranslateAnimation(
                    (main_layout.width / 2).toFloat(),
                    0.toFloat(), 0.toFloat(), 0.toFloat()
                )
            } else {
                TranslateAnimation(
                    view.width.toFloat(),
                    0.toFloat(),
                    0.toFloat(),
                    0.toFloat()
                ) // View for animation
            }
            animate.duration = 500
            animate.fillAfter = true
            view.startAnimation(animate)
            view.visibility = View.GONE // Change visibility VISIBLE or GONE
        }

        fun slidefromRightToLeft1(view: View, main_layout: View) {
            val animate: TranslateAnimation
            animate = if (view.height == 0) {
                main_layout.height // parent layout
                TranslateAnimation(
                    (main_layout.width / 2).toFloat(),
                    0.toFloat(), 0.toFloat(), 0.toFloat()
                )
            } else {
                TranslateAnimation(
                    view.width.toFloat(),
                    0.toFloat(),
                    0.toFloat(),
                    0.toFloat()
                ) // View for animation
            }
            animate.duration = 500
            animate.fillAfter = true
            view.startAnimation(animate)
            view.visibility = View.VISIBLE // Change visibility VISIBLE or GONE
        }

        fun getPathFromURI(contentUri: Uri?, context: Context): String? {
            var res: String? = null
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
            if (cursor!!.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                res = cursor.getString(column_index)
            }
            cursor.close()
            return res
        }



        fun getRootDirPath(context: Context): String {
            return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                val file = ContextCompat.getExternalFilesDirs(
                    context.applicationContext,
                    null
                )[0]
                file.absolutePath
            } else {
                context.applicationContext.filesDir.absolutePath
            }
        }

        fun showcommondialog(parent: Activity?, title: String?, msg: String?) {
            val alertDialog = AlertDialog.Builder(parent).create()
            alertDialog.setTitle(title)
            alertDialog.setMessage(msg)
            alertDialog.setButton("OK", DialogInterface.OnClickListener { dialog, which ->
                alertDialog.dismiss()
                return@OnClickListener
            })
            alertDialog.show()
        }

        fun showcommondialog1(
            parent: Activity, title: String?, msg: String?,
            path: String?, filename: String?, extenstion: String?, checkhttp: Boolean
        ) {
            val alertDialog = AlertDialog.Builder(parent).create()
            alertDialog.setTitle(title)
            alertDialog.setMessage(msg)
            if (checkhttp) {
                alertDialog.setButton("Open Link",
                    DialogInterface.OnClickListener { dialog, which ->
                        alertDialog.dismiss()
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(msg))
                        parent.startActivity(browserIntent)
                        return@OnClickListener
                    })
            } else {
                alertDialog.setButton("OK", DialogInterface.OnClickListener { dialog, which ->
                    alertDialog.dismiss()
                    //openFolder(parent);
                    return@OnClickListener
                })
            }
            alertDialog.show()
        }

        fun emailValidator(email: String?): Boolean {
            val pattern: Pattern
            val matcher: Matcher
            val EMAIL_PATTERN =
                "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
            pattern = Pattern.compile(EMAIL_PATTERN)
            matcher = pattern.matcher(email)
            return matcher.matches()
        }




        fun setLocale(parent: Activity, localeName: String?) {
            val myLocale = Locale(localeName)
            val res = parent.resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = myLocale
            res.updateConfiguration(conf, dm)

            //Intent refresh = new Intent(parent, MainActivity.class);
            //parent.startActivity(refresh);
        }

        fun showSettingsDialog(context: Activity) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Need Permissions")
            builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
            builder.setPositiveButton("GOTO SETTINGS") { dialog, which ->
                dialog.cancel()
                openSettings(context)
            }
            builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
            builder.show()
        }

        // navigating user to app settings
        fun openSettings(context: Activity) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            context.startActivityForResult(intent, 101)
        }



        fun getonemonthagodate(format: String): String {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, -1)
            val date: Date = calendar.time
            val format = SimpleDateFormat(format)
            val dateOutput = format.format(date)
            return dateOutput
        }

        fun getpreviousdate(format: String): String {
            val dateFormat: DateFormat = SimpleDateFormat(format)
            val cal = Calendar.getInstance()
            cal.add(Calendar.DATE, -1)
            var output = dateFormat.format(cal.time)
            return output

        }

        fun getWeekStartDate(format: String): String {
            val dateFormat: DateFormat = SimpleDateFormat(format)
            val calendar = Calendar.getInstance()
            while (calendar[Calendar.DAY_OF_WEEK] !== Calendar.MONDAY) {
                calendar.add(Calendar.DATE, -1)
            }
            var output = dateFormat.format(calendar.time)
            return output
        }

        fun getWeekEndDate(format: String): String {
            val dateFormat: DateFormat = SimpleDateFormat(format)
            val calendar = Calendar.getInstance()
            while (calendar[Calendar.DAY_OF_WEEK] !== Calendar.MONDAY) {
                calendar.add(Calendar.DATE, 1)
            }
            var output = dateFormat.format(calendar.time)
            return output
        }

        fun getlastweekfirstdate(format: String, days: Int): String {
            val cal = Calendar.getInstance()
            val s = SimpleDateFormat(format)
            cal.add(Calendar.DAY_OF_YEAR, days)
            return s.format(Date(cal.timeInMillis))
        }

        fun openGallery(act: Activity) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT //
            act.startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_PICTURE)
        }

        fun printDifference(startDate: Date, endDate: Date):String {
            //milliseconds
            var different = endDate.time - startDate.time
            println("startDate : $startDate")
            println("endDate : $endDate")
            println("different : $different")
            val secondsInMilli: Long = 1000
            val minutesInMilli = secondsInMilli * 60
            val hoursInMilli = minutesInMilli * 60
            val daysInMilli = hoursInMilli * 24
            val elapsedDays = different / daysInMilli
            different = different % daysInMilli
            val elapsedHours = different / hoursInMilli
            different = different % hoursInMilli
            val elapsedMinutes = different / minutesInMilli
            different = different % minutesInMilli
            val elapsedSeconds = different / secondsInMilli
            System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds
            )
            return ""+elapsedDays+" D  "+elapsedHours+" H  "+elapsedMinutes+" M  "+elapsedSeconds+" S"
        }

        fun showProgressDialog(context: Activity?): Dialog? {
            dialog = Dialog(context!!)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.setContentView(R.layout.progress_dialog)
            dialog!!.setCancelable(false)
            dialog!!.show()
            return dialog
        }

        fun rotatebitmap(photoPath: String?, bitmap: Bitmap?): Bitmap? {
            var ei: ExifInterface? = null
            try {
                ei = ExifInterface(photoPath!!)
            } catch (e: java.lang.Exception) {
            }
            val orientation = ei!!.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )
            var rotatedBitmap: Bitmap? = null
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotatedBitmap =
                    bitmap?.let { rotateImage(it, 90) }
                ExifInterface.ORIENTATION_ROTATE_180 -> rotatedBitmap =
                    bitmap?.let { rotateImage(it, 180) }
                ExifInterface.ORIENTATION_ROTATE_270 -> rotatedBitmap =
                    bitmap?.let { rotateImage(it, 270) }
                ExifInterface.ORIENTATION_NORMAL -> rotatedBitmap = bitmap
                else -> rotatedBitmap = bitmap
            }
            return rotatedBitmap
        }

        fun rotateImage(img: Bitmap, degree: Int): Bitmap? {
            val matrix = Matrix()
            matrix.postRotate(degree.toFloat())
            /*if (img != null && !img.isRecycled()) {
                    img.recycle();
                    img = null;
                }*/return Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        }

        fun openWhatsApp(number: String,context : Activity) {
            try {
                val text = "Hello admin"
                val toNumber = "91$number"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("http://api.whatsapp.com/send?phone=$toNumber&text=$text")
                context.startActivity(intent)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }


    }


}