package br.com.rodrigo.calculadoraflex.ui.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import br.com.rodrigo.calculadoraflex.BuildConfig
import br.com.rodrigo.calculadoraflex.R
import br.com.rodrigo.calculadoraflex.ui.login.LoginActivity
import br.com.rodrigo.calculadoraflex.utils.RemoteConfig
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private val TEMPO_AGUARDO_SPLASHSCREEN: Long = 3500L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        RemoteConfig.remoteConfigFetch()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    RemoteConfig.getFirebaseRemoteConfig().activateFetched()
                    val minVersionApp = RemoteConfig.getFirebaseRemoteConfig()
                        .getLong("min_version_app")
                        .toInt()
                    if (minVersionApp <= BuildConfig.VERSION_CODE)
                        carregar()
                    else
                        showAlertMinVersion()
                } else
                    carregar()
            }

    }

    private fun carregar() {
        val sharedPreferences = getSharedPreferences("user_preferences", MODE_PRIVATE)

        val firstOpen = sharedPreferences.getBoolean("first_time", true)

        if(firstOpen) {
            splash()
        } else {
            nextActitity()
        }
    }

    private fun splash() {
        //Carrega a animacao
        val anim = AnimationUtils.loadAnimation(this, R.anim.animacao_splash)
        anim.reset()
        ivLogo.clearAnimation()
        //Roda a animacao
        ivLogo.startAnimation(anim)
        //Chama a próxima tela após 3,5 segundos definido na SPLASH_DISPLAY_LENGTH
        Handler().postDelayed({
            markAppAlreadyOpen()
            nextActitity()
        }, TEMPO_AGUARDO_SPLASHSCREEN)
    }

    private fun nextActitity() {
        val proximaTela = Intent(this@SplashActivity, LoginActivity::class.java)
        startActivity(proximaTela)
        finish()
    }

    private fun showAlertMinVersion() {
        AlertDialog.Builder(this)
            .setTitle("Ops")
            .setMessage("Você esta utilizando uma versão muito antiga do aplicativo. Deseja atualizar?")
            .setPositiveButton("Sim") { dialog, which ->
                var intent: Intent
                try {
                    intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
                    startActivity(intent)
                } catch (e: android.content.ActivityNotFoundException) {
                    intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                    )
                    startActivity(intent)
                }
            }
            .setNegativeButton("Não") { dialog, which ->
                finish()
            }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    private fun continueApp() {
        val preferences = getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
        val isFirstOpen = preferences.getBoolean("first_time", true)
        if (!isFirstOpen) {
            nextActitity()
        } else {
            markAppAlreadyOpen()
            splash()
        }
    }

    private fun markAppAlreadyOpen() {
        val sharedPreferences = getSharedPreferences("user_preferences", MODE_PRIVATE).edit()
        sharedPreferences.putBoolean("first_time", false)
        sharedPreferences.apply()
    }

}