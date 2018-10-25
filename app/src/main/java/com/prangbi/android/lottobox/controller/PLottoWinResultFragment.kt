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
import com.prangbi.android.lottobox.model.PLottoInfo
import com.prangbi.android.lottobox.model.PLottoModel
import com.prangbi.android.lottobox.model.adapter.PLottoWinResultAdapter
import kotlinx.android.synthetic.main.plotto_winresult_fragment.*

/**
 * Created by Prangbi on 2017. 10. 1..
 */
class PLottoWinResultFragment: Fragment() {
    /**
     * Companion
     */
    companion object {
        private val WIN_RESULT_WEB_URL = Definition.SERVER_URL + "/gameResult.do?method=win520&Round="
    }

    /**
     * Variable
     */
    private lateinit var mContext: Activity
    private val winResultAdapter = PLottoWinResultAdapter()
    private var broadcastReceiver: BroadcastReceiver? = null
    private lateinit var progressDialog: Dialog
    private var pLottoModel = PLottoModel()
    private var canConnect = false
    private var nextRound = 0
    private var isLoading = false
    private var autoRequestCount = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.plotto_winresult_fragment, container, false)
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
        nextRound = 0
        autoRequestCount = 0
        showProgressDialog()
        getLatestWinResult()
        getRecommendationGroups()
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
            getRecommendationGroups()
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
            val item = parent.adapter.getItem(position) as Array<PLottoInfo.WinResult>
            if(0 < item.count()) {
                val url = PLottoWinResultFragment.WIN_RESULT_WEB_URL + item[0].round
                Util.moveToWebActivity(mContext, "당첨결과", url)
            }

        }
        winResultListView.setOnScrollListener(object: AbsListView.OnScrollListener {
            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                if (0 >= nextRound || true == isLoading || false == canConnect) {
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

    private fun getRecommendationGroups() {
        val groups = pLottoModel.getRecommendationGroups()

        recommendGroup1TextView.text = groups[0].toString()
        recommendGroup2TextView.text = groups[1].toString()

        recommendGroup1TextView.background = Util.getPLottoGroupBackground(mContext, groups[0])
        recommendGroup2TextView.background = Util.getPLottoGroupBackground(mContext, groups[1])
    }

    private fun commonCompletion() {
        winResultAdapter.notifyDataSetChanged()
        swipeRefreshLayout.isRefreshing = false
        hideProgressDialog()
        isLoading = false
    }

    private fun getWinResult() {
        pLottoModel.getWinResult(mContext, nextRound, { winResultArray, error ->
            if (null == error) {
                var shouldRequestMore = false
                if (null != winResultArray && 0 < winResultArray.count()) {
                    winResultAdapter.addItem(winResultArray)
                    nextRound = winResultArray[0].round!!.toInt() - 1

                    if (Definition.COUNT_PER_PAGE > ++autoRequestCount && 0 < nextRound) {
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
        pLottoModel.getLatestWinResult(mContext, { winResultArr, error ->
            if (null == error) {
                winResultAdapter.removeAll()
                if (null != winResultArr && 0 < winResultArr.size) {
                    winResultAdapter.addItem(winResultArr)
                    nextRound = winResultArr[0].round!!.toInt() - 1
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
