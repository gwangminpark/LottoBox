package com.prangbi.android.lottobox.controller

import android.app.Activity
import android.app.Dialog
import android.app.Fragment
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import com.prangbi.android.lottobox.R
import com.prangbi.android.lottobox.base.Definition
import com.prangbi.android.lottobox.helper.Util
import com.prangbi.android.lottobox.model.NLottoInfo
import com.prangbi.android.lottobox.model.NLottoModel
import com.prangbi.android.lottobox.model.adapter.NLottoWinResultAdapter
import kotlinx.android.synthetic.main.nlotto_winresult_fragment.*

/**
 * Created by Prangbi on 2017. 10. 1..
 */
class NLottoWinResultFragment: Fragment() {
    /**
     * Companion
     */
    companion object {
        private const val WIN_RESULT_WEB_URL = Definition.SERVER_URL + "/gameResult.do?method=byWin&drwNo="
    }

    /**
     * Variable
     */
    private lateinit var mContext: Activity
    private val winResultAdapter = NLottoWinResultAdapter()
    private var broadcastReceiver: BroadcastReceiver? = null
    private lateinit var progressDialog: Dialog
    private var nLottoModel = NLottoModel()
    private var canConnect = false
    private var nextDrwNo = 0
    private var isLoading = false
    private var autoRequestCount = 0

    /**
     * Lifecycle
     */
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.nlotto_winresult_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mContext = activity

        initRecommendationLayout()
        initWinResultListView()
        initBroadcastReciever()

        progressDialog = Util.makeLoadingDialog(mContext)
        canConnect = Util.canConnectToInternet(mContext)
        isLoading = true
        nextDrwNo = 0
        autoRequestCount = 0
        showProgressDialog()
        getLatestWinResult()
        getRecommendationNumbers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        broadcastReceiver?.let {
            mContext?.unregisterReceiver(broadcastReceiver)
        }
    }

    /**
     * Function
     */
    private fun initRecommendationLayout() {
        recommendLayout.setOnClickListener(View.OnClickListener {
            getRecommendationNumbers()
        })
    }

    private fun initBroadcastReciever() {
        broadcastReceiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent?.getAction()
                if ("com.prangbi.android.lottopop.CONNECTIVITY_CHANGE" == action) {
                    canConnect = Util.canConnectToInternet(mContext)
                }
            }
        }

        val intentFilter = IntentFilter()
        intentFilter.addAction("com.prangbi.android.lottopop.CONNECTIVITY_CHANGE")
        broadcastReceiver?.let {
            mContext.registerReceiver(broadcastReceiver, intentFilter)
        }
    }

    private fun initWinResultListView() {
        winResultListView.adapter = winResultAdapter
        winResultListView.setOnItemClickListener { parent, view, position, id ->
            val item = parent.adapter.getItem(position) as NLottoInfo.WinResult
            val url = NLottoWinResultFragment.WIN_RESULT_WEB_URL + item.drwNo.toString(10)
            Util.moveToWebActivity(mContext, "당첨결과", url)
        }
        winResultListView.setOnScrollListener(object: AbsListView.OnScrollListener {
            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                if (0 >= nextDrwNo || true == isLoading || false == canConnect) {
                    return
                }

                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    isLoading = true
                    autoRequestCount = 0
                    showProgressDialog()
                    getWinResult()
                }
            }

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {

            }
        })

        swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            isLoading = true
            getLatestWinResult()
        })
    }

    private fun showProgressDialog() {
        progressDialog.show();
    }

    private fun hideProgressDialog() {
        progressDialog.dismiss()
    }

    private fun getRecommendationNumbers() {
        val numbers = nLottoModel.getRecommendationNumbers()

        recommendNumber1TextView.text = numbers[0].toString()
        recommendNumber2TextView.text = numbers[1].toString()
        recommendNumber3TextView.text = numbers[2].toString()
        recommendNumber4TextView.text = numbers[3].toString()
        recommendNumber5TextView.text = numbers[4].toString()
        recommendNumber6TextView.text = numbers[5].toString()

        recommendNumber1TextView.background = Util.getNLottoNumberBackground(mContext, numbers[0])
        recommendNumber2TextView.background = Util.getNLottoNumberBackground(mContext, numbers[1])
        recommendNumber3TextView.background = Util.getNLottoNumberBackground(mContext, numbers[2])
        recommendNumber4TextView.background = Util.getNLottoNumberBackground(mContext, numbers[3])
        recommendNumber5TextView.background = Util.getNLottoNumberBackground(mContext, numbers[4])
        recommendNumber6TextView.background = Util.getNLottoNumberBackground(mContext, numbers[5])
    }

    private fun commonCompletion() {
        winResultAdapter.notifyDataSetChanged()
        swipeRefreshLayout.isRefreshing = false
        hideProgressDialog()
        isLoading = false
    }

    private fun getWinResult() {
        nLottoModel.getWinResult(mContext, nextDrwNo, { winResult, error ->
            if (null == error) {
                var shouldRequestMore = false
                if (null != winResult) {
                    winResultAdapter.addItem(winResult)
                    nextDrwNo = winResult.drwNo - 1

                    if (Definition.COUNT_PER_PAGE > ++autoRequestCount && 0 < nextDrwNo) {
                        shouldRequestMore = true
                    }
                }

                if (true == shouldRequestMore) {
                    getWinResult()
                } else {
                    commonCompletion()
                }
            } else {
                commonCompletion()
                Util.showToast(mContext, error.localizedMessage)
            }
        })
    }

    private fun getLatestWinResult() {
        nLottoModel.getLatestWinResult(mContext, { winResult, error ->
            if (null == error) {
                winResultAdapter.removeAll()
                if (null != winResult) {
                    winResultAdapter.addItem(winResult)
                    nextDrwNo = winResult.drwNo - 1
                    autoRequestCount += 1
                    getWinResult()
                } else {
                    commonCompletion()
                }
            } else {
                commonCompletion()
                Util.showToast(mContext, error.localizedMessage)
            }
        })
    }
}
