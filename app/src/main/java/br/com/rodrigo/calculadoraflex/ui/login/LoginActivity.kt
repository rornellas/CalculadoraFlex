package br.com.rodrigo.calculadoraflex.ui.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.rodrigo.calculadoraflex.R
import br.com.rodrigo.calculadoraflex.ui.form.FormActivity
import br.com.rodrigo.calculadoraflex.ui.signup.SignUpActivity
import br.com.rodrigo.calculadoraflex.utils.DatabaseUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class LoginActivity : AppCompatActivity() {

    private final val NEW_USER_ACTIVITY: Int = 1
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        mAuth.currentUser?.reload()

        if(mAuth.currentUser != null) {
            goToHome()
        }

        btLogin.setOnClickListener {
            val email = inputLoginEmail.text.toString()
            val pass = inputLoginPassword.text.toString()

            mAuth.signInWithEmailAndPassword(email, pass).addOnSuccessListener {
                goToHome()
            }.addOnFailureListener {
                Toast.makeText(this, "Login ou senha inválidos", Toast.LENGTH_LONG).show()
            }
        }

        btSignup.setOnClickListener {
            val criarConta = Intent(this@LoginActivity, SignUpActivity::class.java)

            startActivityForResult(criarConta, NEW_USER_ACTIVITY)
        }
    }

    private fun goToHome() {
        val formy = Intent(this@LoginActivity, FormActivity::class.java)

        startActivity(formy)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this) { instanceIdResult ->
            val newToken = instanceIdResult.token
            DatabaseUtil.saveToken(newToken)
        }
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            NEW_USER_ACTIVITY -> {
                when(resultCode) {
                    Activity.RESULT_OK -> {
                        inputLoginEmail
                            .setText(data ?. getStringExtra ("email"))
                    }
                }

            }
        }

    }
}
