package com.cokimutai.agrobizna.ui.login

import android.content.Intent
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.cokimutai.agrobizna.MainActivity
import com.cokimutai.agrobizna.R
import com.cokimutai.agrobizna.admin.AdminHome
import com.cokimutai.agrobizna.supports.SavedPreference
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login_screen.*

class LoginScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        userGoogleSignedIn()

        FirebaseApp.initializeApp(this)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso)

        firebaseAuth= FirebaseAuth.getInstance()



        Signin.setOnClickListener{ view: View? ->
            Toast.makeText(this,"Logging In", Toast.LENGTH_SHORT).show()
            signInGoogle()
        }

    }

    private fun isUserSignedIn(): Boolean {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        return account != null
    }

    private fun userGoogleSignedIn(){
        if (!isUserSignedIn()){

            Toast.makeText(this, "Sign out Triggered...", Toast.LENGTH_LONG).show()
        }
    }
    private  fun signInGoogle(){

        val signInIntent: Intent =mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent,Req_Code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==Req_Code){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
//            firebaseAuthWithGoogle(account!!)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount? =completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e: ApiException){
            Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun UpdateUI(account: GoogleSignInAccount){
        val credential= GoogleAuthProvider.getCredential(account.idToken,null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {task->
            if(task.isSuccessful) {
                SavedPreference.setEmail(this,account.email.toString())
                SavedPreference.setUsername(this,account.displayName.toString())
               // openUpDb()
                if (account.email == "cokimutai@gmail.com"){
                    startActivity(Intent(this, AdminHome::class.java))
                    finish()
                }else if(account.email == "corkim141@gmail.com"){
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else{
                    val toast = Toast.makeText(this,
                            "You are not authorized to use this app!!!", Toast.LENGTH_LONG)
                    val view = toast.view
                    view?.background?.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN)
                    val txt = view?.findViewById<TextView>(android.R.id.message)
                    txt?.setTextColor(Color.WHITE)
                    toast.show()
                }

            }
        }
    }

    fun openUpDb(){
        val userId = firebaseAuth.currentUser!!.uid
        databaseReference = FirebaseDatabase.getInstance().reference
                .child("admin").child(userId)
        //u8eps3lnMzOodExAlIXHWxfowub2

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    isAdmin = true
                    Log.d("Admin", "Admin Found!")

                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        val email = SavedPreference.getEmail(this)
        if (email == "cokimutai@gmail.com"){
            isAdmin = true
        }
       if(GoogleSignIn.getLastSignedInAccount(this)!=null){
           //openUpDb()
           if(isAdmin){
            startActivity(Intent(this, AdminHome::class.java))
            finish()
           }else if (SavedPreference.getEmail(this) == "corkim141@gmail.com") {
               startActivity(Intent(this, MainActivity::class.java))
               finish()
           } else{
               val toast = Toast.makeText(this,
                       "You are not authorized to use this app!!!", Toast.LENGTH_LONG)
               val view = toast.view
               view?.background?.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN)
               val txt = view?.findViewById<TextView>(android.R.id.message)
               txt?.setTextColor(Color.WHITE)
               toast.show()
//               Toast.makeText(this, "You are not authorized to use this app!!!",
//                       Toast.LENGTH_LONG).show()
           }
        }
    }
    companion object {
        lateinit var mGoogleSignInClient: GoogleSignInClient
        val Req_Code:Int=123
        private lateinit var firebaseAuth: FirebaseAuth
        var isAdmin = false
        lateinit var firebaseDatabase : FirebaseDatabase
        lateinit var databaseReference: DatabaseReference
        private const val KEY_FARM_ITEM = "items"
    }
}