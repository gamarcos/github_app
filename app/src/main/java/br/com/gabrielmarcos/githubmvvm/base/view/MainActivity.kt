package br.com.gabrielmarcos.githubmvvm.base.view

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import br.com.gabrielmarcos.githubmvvm.R
import br.com.gabrielmarcos.githubmvvm.util.InternetUtil
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        checkNetworkConnection()
        setUpToolbar()
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_arrow_left_outline_white_24dp)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun checkNetworkConnection() {
        InternetUtil.observe(this, Observer { status ->
            takeIf { !status }?.run {
                setUpSnakMessage(R.string.error_connection_unavailable, R.color.purple_attention)
            } ?: setUpSnakMessage(R.string.error_connection_available, R.color.green_success)
        })
    }

    private fun setUpSnakMessage(@StringRes message: Int, @DrawableRes color: Int) {
        val snackBar = Snackbar.make(activityContent, message, Snackbar.LENGTH_LONG)
        snackBar.view.setBackgroundResource(color)
        snackBar.show()
    }
}
