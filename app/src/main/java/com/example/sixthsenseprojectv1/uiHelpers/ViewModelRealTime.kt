package com.example.sixthsenseprojectv1.uiHelpers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sixthsenseprojectv1.*
import com.example.sixthsenseprojectv1.database.TimedDataPoint
import com.himanshoe.charty.line.model.LineData

class ViewModelRealTime : ViewModel() {

    private val _tempSensorName: MutableLiveData<String> = MutableLiveData("")
    private val _tempSensorData: MutableLiveData<String> = MutableLiveData("")
    private val _tempWarningSign: MutableLiveData<Int> = MutableLiveData()
    val tempSensorName: LiveData<String> = _tempSensorName
    val tempSensorData: LiveData<String> = _tempSensorData
    val tempWarningSign: LiveData<Int> = _tempWarningSign

    private val _tempValues: MutableLiveData<MutableList<TimedDataPoint>> = MutableLiveData()
    private val _tempValuesTen: MutableLiveData<MutableList<LineData>> = MutableLiveData()

    val tempValues: LiveData<MutableList<TimedDataPoint>> = _tempValues
    val tempValuesTen: LiveData<MutableList<LineData>> = _tempValuesTen

    private val _tempSwitchPanelsMe: MutableLiveData<Int> = MutableLiveData()
    val tempSwitchPanelsMe: LiveData<Int> = _tempSwitchPanelsMe
    private val _tempSwitchPanelsTeammate: MutableLiveData<Int> = MutableLiveData()
    val tempSwitchPanelsTeammate: LiveData<Int> = _tempSwitchPanelsTeammate
    private val _tempSwitchTabsMe: MutableLiveData<Int> = MutableLiveData()
    val tempSwitchTabsMe: LiveData<Int> = _tempSwitchTabsMe
    private val _tempSwitchTabsTeammate: MutableLiveData<Int> = MutableLiveData()
    val tempSwitchTabsTeammate: LiveData<Int> = _tempSwitchTabsTeammate
    private val _tempSwitchTeammate: MutableLiveData<String> = MutableLiveData("")
    val tempSwitchTeammate: LiveData<String> = _tempSwitchTeammate
    private val _tempSwitchView: MutableLiveData<Int> = MutableLiveData()
    val tempSwitchView: LiveData<Int> = _tempSwitchView
    private val _tempSplitHomeMe: MutableLiveData<Int> = MutableLiveData()
    val tempSplitHomeMe: LiveData<Int> = _tempSplitHomeMe
    private val _tempSplitHomeTeammate: MutableLiveData<Int> = MutableLiveData()
    val tempSplitHomeTeammate: LiveData<Int> = _tempSplitHomeTeammate

    fun onGraphChange(values: MutableList<TimedDataPoint>) {
        _tempValues.value = values
    }

    fun onGraphChangeTen(values: MutableList<LineData>) {
        _tempValuesTen.value = values
    }

    fun onDataChange(sensorName: String, sensorData: String, warningSign: Int) {
        _tempSensorName.value = sensorName
        _tempSensorData.value = sensorData
        _tempWarningSign.value = warningSign
    }

    /**
     * To make no change to a value, pass in the value null.
     */
    fun onUiViewChange(
        panelMe: Int?,
        panelTeammate: Int?,
        tabMe: Int?,
        tabTeammate: Int?,
        member: String?,
        view: Int?,
        homeMe: Int?,
        homeTeammate: Int?
    ) {
        if (panelMe != null) {
            _tempSwitchPanelsMe.value = panelMe
            tspm = panelMe
        }
        if (panelTeammate != null) {
            _tempSwitchPanelsTeammate.value = panelTeammate
            tspt = panelTeammate
        }
        if (tabMe != null) {
            _tempSwitchTabsMe.value = tabMe
            tstm = tabMe
        }
        if (tabTeammate != null) {
            _tempSwitchTabsTeammate.value = tabTeammate
            tstt = tabTeammate
        }
        if (member != null) {
            _tempSwitchTeammate.value = member
            tst = member
        }
        if (view != null) {
            _tempSwitchView.value = view
            tsv = view
        }
        if (homeMe != null) {
            _tempSplitHomeMe.value = homeMe
        }
        if (homeTeammate != null) {
            _tempSplitHomeTeammate.value = homeTeammate
        }
    }

}