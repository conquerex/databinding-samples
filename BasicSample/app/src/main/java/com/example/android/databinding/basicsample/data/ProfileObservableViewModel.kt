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

package com.example.android.databinding.basicsample.data

import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.databinding.basicsample.BR
import com.example.android.databinding.basicsample.util.ObservableViewModel


/**
 * This class is used as a variable in the XML layout and it's fully observable, meaning that
 * changes to any of the exposed observables automatically refresh the UI. *
 */
class ProfileLiveDataViewModel : ViewModel() {
    private val _name = MutableLiveData("Ada")
    private val _lastName = MutableLiveData("Lovelace")
    private val _likes = MutableLiveData(0)

    val name: LiveData<String> = _name
    val lastName: LiveData<String> = _lastName
    val likes: LiveData<Int> = _likes

    // popularity is exposed as LiveData using a Transformation instead of a @Bindable property.
    val popularity: LiveData<Popularity> = Transformations.map(_likes) {
        when {
            it > 9 -> Popularity.STAR
            it > 4 -> Popularity.POPULAR
            else -> Popularity.NORMAL
        }
    }

    /*
    p218, 리스너 바인딩
     이벤트가 발생할 때 실행하는 바인딩 표현식
     리스너 바인딩은 두 가지 옵션을 제공
     하나는 모든 매개 변수를 무시, 사용하지 않는 것
     하나는 매개 변수의 이름을 정하고 바인딩 표현식에서 그것들을 사용하는 것
     */
    fun onLike() {
        _likes.value = (_likes.value ?: 0) + 1
    }

    fun onReset() {
        _likes.value = 0
    }
}

/**
 * As an alternative to LiveData, you can use Observable Fields and binding properties.
 *
 * `Popularity` is exposed here as a `@Bindable` property so it's necessary to call
 * `notifyPropertyChanged` when any of the dependent properties change (`likes` in this case).
 */
class ProfileObservableViewModel : ObservableViewModel() {
    val name = ObservableField("Ada")
    val lastName = ObservableField("Lovelace")
    val likes = ObservableInt(0)

    /*
    p224, Observable 객체 사용하기
    BaseObservable을 구현한 데이터 클래스는 프로퍼티 변경 시 알림을 책임진다.
    (이 경우에는 ObservableViewModel을 구현한 ViewModel)
    이는 @Bindable 애노테이션을 getter 메서드에 적용하고 ---> fun getPopularity()
    notifyPropertyChanged() 메서드를 setter 메서드 내에서 호출하는 것으로 적용된다. ---> fun onLike()
     */
    fun onLike() {
        likes.increment()
        // You control when the @Bindable properties are updated using `notifyPropertyChanged()`.
        notifyPropertyChanged(BR.popularity)
    }

    @Bindable
    fun getPopularity(): Popularity {
        return likes.get().let {
            when {
                it > 9 -> Popularity.STAR
                it > 4 -> Popularity.POPULAR
                else -> Popularity.NORMAL
            }
        }
    }
}

enum class Popularity {
    NORMAL,
    POPULAR,
    STAR
}

private fun ObservableInt.increment() {
    set(get() + 1)
}
