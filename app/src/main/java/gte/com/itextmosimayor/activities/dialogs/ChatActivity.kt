package gte.com.itextmosimayor.activities.dialogs

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import gte.com.itextmosimayor.activities.AccountInfo
import gte.com.itextmosimayor.chat_components.FirestoreUtil
import gte.com.itextmosimayor.chat_components.ImageMessage
import gte.com.itextmosimayor.chat_components.StorageUtil
import gte.com.itextmosimayor.chat_components.TextMessage
import gte.com.itextmosimayor.constant.Constants
import gte.com.itextmosimayor.database.DatabaseInfo
import gte.com.itextmosimayor.modules.Preference
import gte.com.itextmosimayor.modules.SingletonRequest
import kotlinx.android.synthetic.main.activity_chat_messaging.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


private const val RC_SELECT_IMAGE = 2

class ChatActivity : AppCompatActivity() {

    private lateinit var currentChannelId: String
    private lateinit var otherUserId: String
    private lateinit var otherUserName: String
    private lateinit var otherUserImg: String
    private var fullName = Preference.getInstance(this).getPrefString(DatabaseInfo.DBInfo.FIRSTNAME) + " " + Preference.getInstance(this).getPrefString(DatabaseInfo.DBInfo.LASTNAME)
    private lateinit var messagesListenerRegistration: ListenerRegistration
    private var shouldInitRecyclerView = true
    private lateinit var messagesSection: Section


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(gte.com.itextmosimayor.R.layout.activity_chat_messaging)

        otherUserId = intent.getStringExtra("fid")
        otherUserName = intent.getStringExtra("name")
        otherUserImg = intent.getStringExtra("img")


        txtName.text = otherUserName
        btnBack.setOnClickListener { finish() }
        btnInfo.setOnClickListener {
            val intent = Intent(this, AccountInfo::class.java)
            intent.putExtra("fid", otherUserId)
            startActivity(intent) }
        Picasso.get().load(otherUserImg).fit().centerInside().placeholder(gte.com.itextmosimayor.R.drawable.ic_user).into(imgPP)

        FirestoreUtil.getOrCreateChatChannel(otherUserId) { channelId ->
            currentChannelId = channelId

            messagesListenerRegistration =
                    FirestoreUtil.addChatMessagesListener(channelId, this, this::updateRecyclerView)

            btnSendMessage.setOnClickListener {
                if (!editSendMessage.text.isNullOrEmpty()) {
                    val url = Constants.URL + Constants.GETSERVERTIME
                    val request = StringRequest(Request.Method.GET, url, Response.Listener { response ->

                        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                        val date = sdf.parse(response)
//                        val cal = Calendar.getInstance()
//                        cal.time = date

                        val messageToSend =
                                TextMessage(editSendMessage.text.toString(), date,
                                        FirebaseAuth.getInstance().currentUser!!.uid,
                                        otherUserId, fullName)
                        editSendMessage.setText("")
                        FirestoreUtil.sendMessage(messageToSend, channelId)

                    }, Response.ErrorListener { error -> Log.e("unassignedRepoError", error.message) })
                    SingletonRequest.getInstance(application).addToRequestQueue(request)
                }
            }

            btnSelectImage.setOnClickListener {
                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                }
                startActivityForResult(Intent.createChooser(intent, "Select Image"), RC_SELECT_IMAGE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK &&
                data != null && data.data != null) {
            val selectedImagePath = data.data

            val selectedImageBmp = MediaStore.Images.Media.getBitmap(contentResolver, selectedImagePath)

            val outputStream = ByteArrayOutputStream()

            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            val selectedImageBytes = outputStream.toByteArray()

            StorageUtil.uploadMessageImage(selectedImageBytes) { imagePath ->
                val messageToSend =
                        ImageMessage(imagePath, Calendar.getInstance().time,
                                FirebaseAuth.getInstance().currentUser!!.uid,
                                otherUserId, fullName)
                FirestoreUtil.sendMessage(messageToSend, currentChannelId)
            }
        }
    }

    private fun updateRecyclerView(messages: List<Item>) {
        fun init() {
            view_message.apply {
                layoutManager = LinearLayoutManager(this@ChatActivity)
                adapter = GroupAdapter<ViewHolder>().apply {
                    messagesSection = Section(messages)
                    this.add(messagesSection)
                }
            }
            shouldInitRecyclerView = false
        }

        fun updateItems() = messagesSection.update(messages)

        if (shouldInitRecyclerView)
            init()
        else
            updateItems()

        view_message.scrollToPosition(view_message.adapter!!.itemCount - 1)
    }

}