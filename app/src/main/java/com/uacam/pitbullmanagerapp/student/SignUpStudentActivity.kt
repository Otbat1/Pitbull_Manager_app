package com.uacam.pitbullmanagerapp.student

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

import com.uacam.pitbullmanagerapp.databinding.ActivitySignUpStudentBinding
import java.util.*
import java.util.regex.Pattern

class SignUpStudentActivity : AppCompatActivity(){
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpStudentBinding
    private lateinit var currentUser: FirebaseUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.signUpButton.setOnClickListener {

            val mEmail = binding.emailEditText.text.toString()
            val mPassword = binding.passwordEditText.text.toString()
            val mRepeatPassword = binding.repeatPasswordEditText.text.toString()
            val mfirst_name = binding.firstNameEditText.text.toString()
            val mlast_name = binding.lastNameEditText.text.toString()
            val passwordRegex = Pattern.compile("^" +
                    "(?=.*[-@#$%^&+=])" +     // Al menos 1 carácter especial
                    ".{6,}" +                // Al menos 4 caracteres
                    "$")

            if(mEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
                Toast.makeText(this, "Ingrese un email valido.",
                    Toast.LENGTH_SHORT).show()
            } else if (mPassword.isEmpty() || !passwordRegex.matcher(mPassword).matches()){
                Toast.makeText(this, "La contraseña es debil.",
                    Toast.LENGTH_SHORT).show()
            } else if (mPassword != mRepeatPassword){
                Toast.makeText(this, "Confirma la contraseña.",
                    Toast.LENGTH_SHORT).show()
            } else {
                createAccount(mEmail, mPassword,mfirst_name,mlast_name)
            }
        }

        binding.backImageView.setOnClickListener {
            val intent = Intent(this, SignInStudentActivity::class.java)
            this.startActivity(intent)
        }
    }
    /**
    var email: String? = null,
    var first_name: String? = null,
    var imagen: String? = null,
    var last_name: String? = null,
     **/

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            if(currentUser.isEmailVerified){
                val intent = Intent(this, StudentSessionActivity::class.java)
                this.startActivity(intent)
            } else {
                val intent = Intent(this, CheckEmailStudentActivity::class.java)
                this.startActivity(intent)
            }
        }
    }
    private fun createAccount(email: String, password: String, first_name:String, last_name:String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, CheckEmailStudentActivity::class.java)

                    this.startActivity(intent)
                } else {
                    Toast.makeText(this, "No se pudo crear la cuenta. Vuelva a intertarlo",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
/**
    private fun registerUser(
        email: String,
        firstname: String,
        lastname: String,
        phonenumber: String,
        password: String
    ) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){

            if (it.isSuccessful) {
                Objects.requireNonNull(auth.currentUser)!!.sendEmailVerification()
                    .addOnCompleteListener { task ->

                        if (it.isSuccessful) {

                            currentUser = FirebaseAuth.getInstance().currentUser!!
                            assert(currentUser != null)
                            val uid: String = currentUser.getUid()

                            val mDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(uid)

                            val mUserDatabase = FirebaseDatabase.getInstance().reference.child("Users")

                            val deviceToken: Task<String> = FirebaseMessaging.getInstance().getToken()

                            val userMap = HashMap<String, String>()
                            userMap["email"] = email
                            userMap["first_name"] = firstname
                            userMap["last_name"] = lastname
                            userMap["phone_number"] = phonenumber
                            userMap["password"] = password
                            userMap["device_token"] = deviceToken!!.toString()

                            mDatabase.setValue(userMap).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "El registro fue exitoso", Toast.LENGTH_LONG).show()

                                    val current_user_id = Objects.requireNonNull(auth.currentUser)?.uid
                                    val deviceToken = FirebaseMessaging.getInstance().token

                                    mUserDatabase.child(current_user_id!!).child("device_token")
                                        .setValue(deviceToken).addOnSuccessListener {
                                            Intent(this, CheckEmailStudentActivity::class.java).also {
                                                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                startActivity(it)
                                            }
                                        }
                                }
                            }

                        } else {
                            Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "No se puede registrar, por favor revise el formulario y vuelva a intentarlo.", Toast.LENGTH_SHORT).show()
            }
        }
    }
 */
}
