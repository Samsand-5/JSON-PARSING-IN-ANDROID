import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
 
class MainActivity : AppCompatActivity() {
 
    // on below line we are creating variables for
      // our text view, image view and progress bar
    lateinit var courseNameTV: TextView
    lateinit var courseDescTV: TextView
    lateinit var courseReqTV: TextView
    lateinit var courseIV: ImageView
    lateinit var visitCourseBtn: Button
    lateinit var loadingPB: ProgressBar
     
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         
        // on below line we are initializing our variable with their ids.
        courseNameTV = findViewById(R.id.idTVCourseName)
        courseDescTV = findViewById(R.id.idTVDesc)
        courseReqTV = findViewById(R.id.idTVPreq)
        courseIV = findViewById(R.id.idIVCourse)
        visitCourseBtn = findViewById(R.id.idBtnVisitCourse)
        loadingPB = findViewById(R.id.idLoadingPB)
         
        // on below line we are creating a method
        // to get data from api using retrofit.
        getData()
 
    }
 
    private fun getData() {
        // on below line we are creating a retrofit
        // builder and passing our base url
        // on below line we are creating a retrofit
        // builder and passing our base url
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonkeeper.com/b/")
 
            // on below line we are calling add Converter
            // factory as GSON converter factory.
            // at last we are building our retrofit builder.
            .addConverterFactory(GsonConverterFactory.create())
            .build()
 
        // below line is to create an instance for our retrofit api class.
        // below line is to create an instance for our retrofit api class.
        val retrofitAPI = retrofit.create(RetrofitAPI::class.java)
 
        val call: Call<CourseDataModal?>? = retrofitAPI.getCourse()
 
        // on below line we are making a call.
        call!!.enqueue(object : Callback<CourseDataModal?> {
            override fun onResponse(
                call: Call<CourseDataModal?>?,
                response: Response<CourseDataModal?>
            ) {
                if (response.isSuccessful()) {
                    // inside the on response method.
                    // we are hiding our progress bar.
                    loadingPB.visibility = View.GONE
 
                    // on below line we are getting data from our response
                    // and setting it in variables.
                    val courseName: String = response.body()!!.courseName
                    val courseLink: String = response.body()!!.courseLink
                    val courseImg: String = response.body()!!.courseimg
                    val courseDesc: String = response.body()!!.courseDesc
                    val coursePreq: String = response.body()!!.Prerequisites
 
                    // on below line we are setting our data
                    // to our text view and image view.
                    courseReqTV.text = coursePreq
                    courseDescTV.text = courseDesc
                    courseNameTV.text = courseName
 
                    // on below line we are setting image view from image url.
                    Picasso.get().load(courseImg).into(courseIV)
 
                    // on below line we are changing visibility for our button.
                    visitCourseBtn.visibility = View.VISIBLE
 
                    // on below line we are adding click listener for our button.
                    visitCourseBtn.setOnClickListener {
                        // on below line we are opening a intent to view the url.
                        val i = Intent(Intent.ACTION_VIEW)
                        i.setData(Uri.parse(courseLink))
                        startActivity(i)
                    }
                }
            }
 
            override fun onFailure(call: Call<CourseDataModal?>?, t: Throwable?) {
                // displaying an error message in toast
                Toast.makeText(this@MainActivity, "Fail to get the data..", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}
