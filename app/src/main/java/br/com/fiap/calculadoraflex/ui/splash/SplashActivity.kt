package br.com.fiap.calculadoraflex.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import br.com.fiap.calculadoraflex.R
import br.com.fiap.calculadoraflex.ui.form.FormActivity
import br.com.fiap.calculadoraflex.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private val TEMPO_AGUARDO_SPLASHSCREEN: Long = 3500L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        carregar()
    }

    private fun carregar() {
//Carrega a animacao
        val anim = AnimationUtils.loadAnimation(this, R.anim.animacao_splash)
        anim.reset()
        ivLogo.clearAnimation()
//Roda a animacao
        ivLogo.startAnimation(anim)
//Chama a próxima tela após 3,5 segundos definido na SPLASH_DISPLAY_LENGTH
        Handler().postDelayed({
            val proximaTela = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(proximaTela)
            finish()
        }, TEMPO_AGUARDO_SPLASHSCREEN)
    }

}
