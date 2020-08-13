/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.databinding.twowaysample.ui

import android.content.Context
import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.InverseMethod
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.android.databinding.twowaysample.R

/*
    (실행순서)
    numberOfSets get --> 레이아웃 변수 반환 in ViewModel
    setArrayToString --> String으로 컨버트
    setListener      --> 리스너 등록한 뒤 (언제 변경되는지 알려줌, getNumberOfSets에 반응)
    setNumberOfSets  --> 값을 레이아웃에 반영

    (값 변경 시)
    getNumberOfSets  --> 값을 레이아웃에서 가지고 오고
    stringToSetArray --> SetArray로 컨버트
    stringToSetArray
    numberOfSets set --> 레이아웃 변수 변경 in ViewModel
    numberOfSets get --> 레이아웃 변수 반환 in ViewModel
    numberOfSets get
    numberOfSets get
    numberOfSets get
    setArrayToString --> String으로 컨버트
    setNumberOfSets  --> 값을 레이아웃에 반영
 */

/**
 * The [EditText] that controls the number of sets is using two-way Data Binding. Applying a
 * 2-way expression to the `android:text` attribute of the EditText triggers an update on every
 * keystroke. This is an alternative implementation that uses a [View.OnFocusChangeListener]
 * instead.
 *
 * `numberOfSetsAttrChanged` creates a listener that triggers when the focus is lost
 *
 * `hideKeyboardOnInputDone` (in a different file) will clear focus when the `Done` action on
 * the keyboard is dispatched, triggering `numberOfSetsAttrChanged`.
 */
object NumberOfSetsBindingAdapters {

    val TAG = NumberOfSetsBindingAdapters::class.java.simpleName

    /**
     * Needs to be used with [NumberOfSetsConverters.setArrayToString].
     */
    /*
        1.
        @BindingAdapter를 사용하여 값이 변경될 때 초깃값을 설정하고 업데이트하는 메서드를 만든다.
     */
    @BindingAdapter("numberOfSets")
    @JvmStatic fun setNumberOfSets(view: EditText, value: String) {
        Log.d(NumberOfSetsConverters.TAG, "* * * setNumberOfSets")
        view.setText(value)
    }

    /**
     * Called when the [InverseBindingListener] of the `numberOfSetsAttrChanged` binding adapter
     * is notified of a change.
     *
     * Used with the inverse method of [NumberOfSetsConverters.setArrayToString], which is
     * [NumberOfSetsConverters.stringToSetArray].
     *
     * (번역)
     * `numberOfSetsAttrChanged` 바인딩 어댑터의 [InverseBindingListener]에 변경 알림이있을 때 호출됩니다.
     *
     * [NumberOfSetsConverters.setArrayToString]의 역 메서드 인
     * [NumberOfSetsConverters.stringToSetArray]와 함께 사용됩니다.
     */
    /*
        (p240, 사용자 정의 속성을 사용하는 양방향 바인딩)
        데이터 바인딩 라이브러리는 양방향 바인딩이 자주 사용되는 양방향 바인딩 속성에 대한 구현체를 이미 제공하지만,
        사용자가 직접 정의한 속성에 대해 양방향 바인딩을 사용하려면
        @InverseBindingAdapter와 @InverseBindingMethod 애노테이션을 사용해야 한다.

        2. 끝
        @InverseBindingAdapter를 사용하여 뷰에서 값을 읽는 메서드에 주석을 표시한다.

        ======

        (p241)
        @BindingAdapter : 데이터가 변경되었을 때 수행하고 싶은 작업을 작성
        @InverseBindingAdapter : 역으로 레이아웃의 사용자 정의 속성값이 변경되었을 때
                        뷰 모델 등과 같은 레이아웃 변수에 변경 사항을 전달하여 양방향 바인딩이 구현될 수 있게 한다.
     */
    @InverseBindingAdapter(attribute = "numberOfSets")
    @JvmStatic fun getNumberOfSets(editText: EditText): String {
        Log.d(NumberOfSetsConverters.TAG, "* * * getNumberOfSets")
        return editText.text.toString()
    }

    /**
     * That this Binding Adapter is not defined in the XML. "AttrChanged" is a special
     * suffix that lets you manage changes in the value, using two-way Data Binding.
     *
     * Note that setting a [View.OnFocusChangeListener] overrides other listeners that might be set
     * with `android:onFocusChangeListener`. Consider supporting both in the same binding adapter
     * with `requireAll = false`. See [android.databinding.adapters.CompoundButtonBindingAdapter]
     * for an example.
     */
    /*
    p242
    InverseBindingListener를 사용하여 데이터 바인딩 클래스 구현체에 속성의 변경사항을 알릴 수 있다.
    InverseBindingListener는 데이터 바인딩 클래스 내부의 ExecuteBinding()이 호출될 때 등록된다.

    모든 양방향 바인딩은 합성 이벤트 속성을 생성하는데
    이 속성의 이름은 기본 속성 이름과 같지만 접미사 "AttrChanged"를 붙인다.
     */
    @BindingAdapter("numberOfSetsAttrChanged")
    @JvmStatic fun setListener(view: EditText, listener: InverseBindingListener?) {
        Log.d(NumberOfSetsConverters.TAG, "* * * setListener")
        view.onFocusChangeListener = View.OnFocusChangeListener { focusedView, hasFocus ->
            val textView = focusedView as TextView
            if (hasFocus) {
                // Delete contents of the EditText if the focus entered.
                textView.text = ""
            } else {
                // If the focus left, update the listener
                listener?.onChange()
            }
        }
    }

    /* This sample showcases the NumberOfSetsConverters below, but note that they could be used
    also like: */

    @BindingAdapter("numberOfSets_alternative")
    @JvmStatic fun setNumberOfSets_alternative(view: EditText, value: Array<Int>) {
        view.setText(String.format(
                view.resources.getString(R.string.sets_format,
                        value[0] + 1,
                        value[1])))
    }

    @InverseBindingAdapter(attribute = "numberOfSets_alternative")
    @JvmStatic fun getNumberOfSets_alternative(editText: EditText): Array<Int> {
        if (editText.text.isEmpty()) {
            return arrayOf(0, 0)
        }

        return try {
            arrayOf(0, editText.text.toString().toInt()) // First item is not passed
        } catch (e: NumberFormatException) {
            arrayOf(0, 0)
        }
    }
}

/**
 * Converters for the number of sets attribute.
 */
/*
    p242
    양방향 바인딩에서 컨버터 사용하기
     --> interval_timer.xml
 */
object NumberOfSetsConverters {
    val TAG = NumberOfSetsConverters::class.java.simpleName
    /**
     * Used with `numberOfSets` to convert from array to String.
     */
//    @InverseMethod("stringToSetArray")
    @JvmStatic fun setArrayToString(context: Context, value: Array<Int>): String {
        Log.d(TAG, "* * * setArrayToString")
        return context.getString(R.string.sets_format, value[0] + 1, value[1])
    }

    /**
     * This is the Inverse Method used in `numberOfSets`, to convert from String to array.
     *
     * Note that Context is passed
     */
    /*
        InverseMethod는 위 혹은 아래, 둘 중 하나만 있으면 됨
     */
    @InverseMethod("setArrayToString")
    @JvmStatic fun stringToSetArray(unused: Context, value: String): Array<Int> {
        Log.d(TAG, "* * * stringToSetArray")
        // Converts String to long
        if (value.isEmpty()) {
            return arrayOf(0, 0)
        }

        return try {
            arrayOf(0, value.toInt()) // First item is not passed
        } catch (e: NumberFormatException) {
            arrayOf(0, 0)
        }
    }
}
