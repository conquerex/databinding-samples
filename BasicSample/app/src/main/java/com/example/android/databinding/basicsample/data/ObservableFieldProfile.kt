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

import androidx.databinding.ObservableInt

/**
 * Used as a layout variable to provide static properties (name and lastName) and an observable
 * one (likes).
 */
data class ObservableFieldProfile(
        val name: String,
        val lastName: String,
        // p208
        // Observable을 접두어로 갖는 타입은 컴파일 타임에 따로 계산되어 반영
        val likes: ObservableInt
)
