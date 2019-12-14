package br.com.rodrigo.calculadoraflex.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.rodrigo.calculadoraflex.R
import br.com.rodrigo.calculadoraflex.model.User
import br.com.rodrigo.calculadoraflex.utils.DatabaseUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()

        btCreate.setOnClickListener {
            val email = inputEmail.text.toString()
            val pass = inputPassword.text.toString()

            mAuth.createUserWithEmailAndPassword(
                email, pass
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    saveInRealTimeDatabase()
                } else {
                    Toast.makeText(
                        this@SignUpActivity, it.exception?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
    }

    private fun saveInRealTimeDatabase() {
        val user = User(
            inputName.text.toString(), inputEmail.text.toString(),
            inputPhone.text.toString()
        )
        DatabaseUtil.getDatabase().getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .setValue(user)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(
                        this, "Usuário criado com sucesso",
                        Toast.LENGTH_SHORT
                    ).show()
                    val returnIntent = Intent()
                    returnIntent.putExtra("email", inputEmail.text.toString())
                    setResult(RESULT_OK, returnIntent)
                    finish()
                } else {
                    Toast.makeText(this, "Erro ao criar o usuário", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
