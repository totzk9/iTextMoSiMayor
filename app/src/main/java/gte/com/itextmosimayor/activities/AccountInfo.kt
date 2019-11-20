package gte.com.itextmosimayor.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import gte.com.itextmosimayor.R
import gte.com.itextmosimayor.database.DatabaseHandler
import gte.com.itextmosimayor.database.DatabaseInfo
import gte.com.itextmosimayor.database.Queries
import gte.com.itextmosimayor.models.OtherUsers
import gte.com.itextmosimayor.modules.ColumnIndexCache
import kotlinx.android.synthetic.main.activity_account_info.*

class AccountInfo : AppCompatActivity() {

    private lateinit var userId: String
    private var databaseReference: DatabaseReference? = null
    private lateinit var db: SQLiteDatabase
    private lateinit var number: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)

        setContentView(R.layout.activity_account_info)

        userId = intent.getStringExtra("fid")

        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        val database = DatabaseHandler(this)
        db = database.readableDatabase

        btnBack.setOnClickListener {
            finish()
        }

        val listener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(OtherUsers::class.java)!!
                txtName.text = user.name
                txtNumber.text = user.mobileNumber
                number = user.mobileNumber

                val c = Queries.getInstance(this@AccountInfo).getDepartments(user.departmentID)
                val cache = ColumnIndexCache()
                if (c != null && c.moveToFirst()) {
                    txtDepartment.text = c.getString(cache.getColumnIndex(c, DatabaseInfo.DBInfo.DEPARTMENTNAME))
                    cache.clear()
                }

                Picasso.get()
                        .load(user.img)
                        .fit()
                        .centerInside()
                        .placeholder(R.drawable.ic_person)
                        .into(imgPP)

            }
            override fun onCancelled(p0: DatabaseError) {

            }
        }
        databaseReference!!.child(userId).addValueEventListener(listener)

        fabText.setOnClickListener {
            composeSmsMessage()
        }

        fabCall.setOnClickListener {
            checkPermission()
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.CALL_PHONE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.CALL_PHONE),
                        42)
            }
        } else {
            // Permission has already been granted
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "1122334455"))
            startActivity(intent)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 42) {
            // If request is cancelled, the result arrays are empty.
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // permission was granted, yay!
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "1122334455"))
                startActivity(intent)
            } else {
                // permission denied, boo! Disable the
                // functionality
            }
            return
        }
    }

    fun composeSmsMessage() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.putExtra("address", number)
        intent.data = Uri.parse("sms:")
        startActivity(intent)
    }
}
