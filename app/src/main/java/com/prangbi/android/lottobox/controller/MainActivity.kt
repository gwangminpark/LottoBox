package com.prangbi.android.lottobox.controller

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import com.prangbi.android.lottobox.R
import com.prangbi.android.lottobox.base.Definition
import com.prangbi.android.lottobox.helper.Util
import kotlinx.android.synthetic.main.main_activity.*


/**
 * Created by Prangbi on 2017. 7. 12..
 */
class MainActivity: AppCompatActivity() {
    /**
     * Variable
     */
    private lateinit var mContext: Activity
    private val nLottoWinResultFragment = NLottoWinResultFragment()
    private val pLottoWinResultFragment = PLottoWinResultFragment()

    /**
     * Lifecycle
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        mContext = this
        setContentView(R.layout.main_activity)

        termsTextView.setOnClickListener(View.OnClickListener {
            Util.moveToWebActivity(mContext, "개인정보취급방침", Definition.TERMS_URL)
        })
        bottomNLottoButtonLayout.setOnClickListener(View.OnClickListener {
            replaceToNLottoWinResultFragment()
        })
        bottomPLottoButtonLayout.setOnClickListener(View.OnClickListener {
           /* startActivity(Intent(baseContext, TestActivity::class.java))
            finish()*/
            replaceToPLottoWinResultFragment()
        })
        bottomNLottoButtonLayout.performClick()
    }

    /**
     * Event
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_actionbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.qrCode -> {
                scanQrCode()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (null != result) {
            if (null != result.contents) {
                val intent = Intent(mContext, WebActivity::class.java)
                intent.putExtra("title", "당첨결과")
                intent.putExtra("url", result.contents)
                startActivity(intent)
            } else {
                Toast.makeText(mContext, "바코드를 읽지 못했습니다.", Toast.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    /**
     * Function
     */
    private fun replaceToNLottoWinResultFragment() {
        if(false == nLottoWinResultFragment.isVisible) {
            bottomNLottoImageView.setColorFilter(Color.parseColor("#FFFFFF"))
            bottomPLottoImageView.setColorFilter(Color.parseColor("#808080"))
            bottomNLottoTextView.setTextColor(Color.parseColor("#FFFFFF"))
            bottomPLottoTextView.setTextColor(Color.parseColor("#808080"))

            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentLayout, nLottoWinResultFragment)
            transaction.commit()
        }
    }

    private fun replaceToPLottoWinResultFragment() {
        if(false == pLottoWinResultFragment.isVisible) {
            bottomNLottoImageView.setColorFilter(Color.parseColor("#808080"))
            bottomPLottoImageView.setColorFilter(Color.parseColor("#FFFFFF"))
            bottomNLottoTextView.setTextColor(Color.parseColor("#808080"))
            bottomPLottoTextView.setTextColor(Color.parseColor("#FFFFFF"))

            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentLayout, pLottoWinResultFragment)
            transaction.commit()
        }
    }

    private fun scanQrCode() {
        IntentIntegrator(mContext).initiateScan()
    }
}
