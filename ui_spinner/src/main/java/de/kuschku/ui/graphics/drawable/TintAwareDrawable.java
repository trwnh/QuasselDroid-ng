/*
 * Copyright (C) 2016 The Android Open Source Project
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

package de.kuschku.ui.graphics.drawable;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;

import androidx.annotation.ColorInt;

/**
 * Interface which allows a {@link android.graphics.drawable.Drawable} to receive tinting calls
 * from {@code DrawableCompat}.
 */
public interface TintAwareDrawable {
  void setTint(@ColorInt int tint);

  void setTintList(ColorStateList tint);

  void setTintMode(PorterDuff.Mode tintMode);
}