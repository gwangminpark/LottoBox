package com.prangbi.android.lottobox.model.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.prangbi.android.lottobox.R
import com.prangbi.android.lottobox.helper.Util
import com.prangbi.android.lottobox.model.NLottoInfo
import java.util.*

/**
 * Created by Prangbi on 9/9/16.
 */
class NLottoWinResultAdapter: BaseAdapter() {
    // Variable
    private var items: MutableList<NLottoInfo.WinResult> = ArrayList()

    // Implements
    override fun getCount(): Int {
        return items.count()
    }

    override fun getItem(position: Int): NLottoInfo.WinResult {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val context = parent.context
        if (null == convertView) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.nlotto_winresult_item, parent, false)
        }

        val nLottoTitleDrwNoTextView = convertView!!.findViewById(R.id.nLottoTitleDrwNoTextView) as TextView
        val nLottoTitleTextView = convertView.findViewById(R.id.nLottoTitleTextView) as TextView
        val nLottoTitleDateTextView = convertView.findViewById(R.id.nLottoTitleDateTextView) as TextView
        val nLottoResultTextView = convertView.findViewById(R.id.nLottoResultTextView) as TextView
        val nLottoResultAmountTextView = convertView.findViewById(R.id.nLottoResultAmountTextView) as TextView
        val nLottoNumber1TextView = convertView.findViewById(R.id.nLottoNumber1TextView) as TextView
        val nLottoNumber2TextView = convertView.findViewById(R.id.nLottoNumber2TextView) as TextView
        val nLottoNumber3TextView = convertView.findViewById(R.id.nLottoNumber3TextView) as TextView
        val nLottoNumber4TextView = convertView.findViewById(R.id.nLottoNumber4TextView) as TextView
        val nLottoNumber5TextView = convertView.findViewById(R.id.nLottoNumber5TextView) as TextView
        val nLottoNumber6TextView = convertView.findViewById(R.id.nLottoNumber6TextView) as TextView
        val nLottoBonusNumberTextView = convertView.findViewById(R.id.nLottoBonusNumberTextView) as TextView

        val winResult = items[position]
        nLottoTitleDrwNoTextView.text = winResult.drwNo.toString(10)
        nLottoTitleTextView.text = "나눔로또6/45"
        nLottoTitleDateTextView.text = "(" + winResult.drwNoDate + ")"
        nLottoResultTextView.text = "1등 " + winResult.firstPrzwnerCo.toString(10) + "게임"
        nLottoResultAmountTextView.text = "게임당 " + Util.amountString(winResult.firstWinamnt) + "원"

        nLottoNumber1TextView.text = winResult.drwtNo1.toString(10)
        nLottoNumber2TextView.text = winResult.drwtNo2.toString(10)
        nLottoNumber3TextView.text = winResult.drwtNo3.toString(10)
        nLottoNumber4TextView.text = winResult.drwtNo4.toString(10)
        nLottoNumber5TextView.text = winResult.drwtNo5.toString(10)
        nLottoNumber6TextView.text = winResult.drwtNo6.toString(10)
        nLottoBonusNumberTextView.text = winResult.bnusNo.toString(10)

        nLottoNumber1TextView.background = Util.getNLottoNumberBackground(context, winResult.drwtNo1.toInt())
        nLottoNumber2TextView.background = Util.getNLottoNumberBackground(context, winResult.drwtNo2.toInt())
        nLottoNumber3TextView.background = Util.getNLottoNumberBackground(context, winResult.drwtNo3.toInt())
        nLottoNumber4TextView.background = Util.getNLottoNumberBackground(context, winResult.drwtNo4.toInt())
        nLottoNumber5TextView.background = Util.getNLottoNumberBackground(context, winResult.drwtNo5.toInt())
        nLottoNumber6TextView.background = Util.getNLottoNumberBackground(context, winResult.drwtNo6.toInt())
        nLottoBonusNumberTextView.background = Util.getNLottoNumberBackground(context, winResult.bnusNo.toInt())

        return convertView
    }

    // Function
    fun addItem(winResult: NLottoInfo.WinResult) {
        items.add(winResult)
    }

    fun addItems(winResultList: List<NLottoInfo.WinResult>) {
        items.addAll(winResultList)
    }

    fun removeAll() {
        items.removeAll(items)
    }
}
