package com.prangbi.android.lottobox.model.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.prangbi.android.lottobox.R
import com.prangbi.android.lottobox.helper.Util
import com.prangbi.android.lottobox.model.PLottoInfo
import java.util.*

/**
 * Created by Prangbi on 2017. 8. 21..
 */
class PLottoWinResultAdapter: BaseAdapter() {
    // Variable
    private var items: MutableList<Array<PLottoInfo.WinResult>> = ArrayList()

    // Implements
    override fun getCount(): Int {
        return items.count()
    }

    override fun getItem(position: Int): Array<PLottoInfo.WinResult> {
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
            convertView = inflater.inflate(R.layout.plotto_winresult_item, parent, false)
        }

        val pLottoTitleRountTextView = convertView!!.findViewById(R.id.pLottoTitleRountTextView) as TextView
        val pLottoTitleTextView = convertView!!.findViewById(R.id.pLottoTitleTextView) as TextView
        val pLottoTitleDateTextView = convertView!!.findViewById(R.id.pLottoTitleDateTextView) as TextView
        val pLottoResultTextView = convertView.findViewById(R.id.pLottoResultTextView) as TextView
        val pLottoResultAmountTextView = convertView.findViewById(R.id.pLottoResultAmountTextView) as TextView
        val pLottoGroup1TextView = convertView.findViewById(R.id.pLottoGroup1TextView) as TextView
        val pLottoNum1_1TextView = convertView.findViewById(R.id.pLottoNum1_1TextView) as TextView
        val pLottoNum1_2TextView = convertView.findViewById(R.id.pLottoNum1_2TextView) as TextView
        val pLottoNum1_3TextView = convertView.findViewById(R.id.pLottoNum1_3TextView) as TextView
        val pLottoNum1_4TextView = convertView.findViewById(R.id.pLottoNum1_4TextView) as TextView
        val pLottoNum1_5TextView = convertView.findViewById(R.id.pLottoNum1_5TextView) as TextView
        val pLottoNum1_6TextView = convertView.findViewById(R.id.pLottoNum1_6TextView) as TextView
        val pLottoGroup2TextView = convertView.findViewById(R.id.pLottoGroup2TextView) as TextView
        val pLottoNum2_1TextView = convertView.findViewById(R.id.pLottoNum2_1TextView) as TextView
        val pLottoNum2_2TextView = convertView.findViewById(R.id.pLottoNum2_2TextView) as TextView
        val pLottoNum2_3TextView = convertView.findViewById(R.id.pLottoNum2_3TextView) as TextView
        val pLottoNum2_4TextView = convertView.findViewById(R.id.pLottoNum2_4TextView) as TextView
        val pLottoNum2_5TextView = convertView.findViewById(R.id.pLottoNum2_5TextView) as TextView
        val pLottoNum2_6TextView = convertView.findViewById(R.id.pLottoNum2_6TextView) as TextView

        val winResultArray = items[position]
        if (0 < winResultArray.count()) {
            val winResult = winResultArray[0]
            val rankClass = winResult.rankClass?.toIntOrNull(10)

            pLottoTitleRountTextView.text = winResult.round
            pLottoTitleTextView.text = "연금복권520"
            pLottoTitleDateTextView.text = "(" + winResult.pensionDrawDate + ")"
            pLottoResultTextView.text = "1등 " + winResultArray.count().toString(10) + "게임"
            pLottoResultAmountTextView.text = "월 500만원 X 20년"
            pLottoGroup1TextView.text = winResult.rankClass + "조"

            pLottoNum1_1TextView.text = winResult.rankNo?.getOrNull(0).toString()
            pLottoNum1_2TextView.text = winResult.rankNo?.getOrNull(1).toString()
            pLottoNum1_3TextView.text = winResult.rankNo?.getOrNull(2).toString()
            pLottoNum1_4TextView.text = winResult.rankNo?.getOrNull(3).toString()
            pLottoNum1_5TextView.text = winResult.rankNo?.getOrNull(4).toString()
            pLottoNum1_6TextView.text = winResult.rankNo?.getOrNull(5).toString()

            pLottoGroup1TextView.background = Util.getPLottoGroupBackground(context, if (null != rankClass) { rankClass } else 0)
        }
        if (2 <= winResultArray.count()) {
            val winResult = winResultArray[1]
            val rankClass = winResult.rankClass?.toIntOrNull(10)

            pLottoGroup2TextView.text = winResult.rankClass + "조"
            pLottoNum2_1TextView.text = winResult.rankNo?.getOrNull(0).toString()
            pLottoNum2_2TextView.text = winResult.rankNo?.getOrNull(1).toString()
            pLottoNum2_3TextView.text = winResult.rankNo?.getOrNull(2).toString()
            pLottoNum2_4TextView.text = winResult.rankNo?.getOrNull(3).toString()
            pLottoNum2_5TextView.text = winResult.rankNo?.getOrNull(4).toString()
            pLottoNum2_6TextView.text = winResult.rankNo?.getOrNull(5).toString()

            pLottoGroup2TextView.background = Util.getPLottoGroupBackground(context, if (null != rankClass) { rankClass } else 0)
        }
        return convertView
    }

    // Function
    fun addItem(winResultArray: Array<PLottoInfo.WinResult>) {
        items.add(winResultArray)
    }

    fun addItems(winResultArrayList: List<Array<PLottoInfo.WinResult>>) {
        items.addAll(winResultArrayList)
    }

    fun removeAll() {
        items.removeAll(items)
    }
}
