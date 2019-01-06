package de.kuschku.quasseldroid.util.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import butterknife.BindView
import butterknife.ButterKnife
import de.kuschku.quasseldroid.R
import de.kuschku.quasseldroid.util.helper.use

class BannerView : FrameLayout {
  @BindView(R.id.icon)
  lateinit var icon: AppCompatImageView

  @BindView(R.id.text)
  lateinit var text: TextView

  @BindView(R.id.button)
  lateinit var button: TextView

  constructor(context: Context) :
    this(context, null)

  constructor(context: Context, attrs: AttributeSet?) :
    this(context, attrs, 0)

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    super(context, attrs, defStyleAttr) {

    LayoutInflater.from(context).inflate(R.layout.widget_banner, this, true)
    ButterKnife.bind(this)

    context.theme.obtainStyledAttributes(attrs, R.styleable.BannerView, 0, 0).use {
      if (it.hasValue(R.styleable.BannerView_icon))
        icon.setImageResource(it.getResourceId(R.styleable.BannerView_icon, 0))

      if (it.hasValue(R.styleable.BannerView_text))
        text.text = it.getString(R.styleable.BannerView_text)

      if (it.hasValue(R.styleable.BannerView_buttonText))
        button.text = it.getString(R.styleable.BannerView_buttonText)
    }
  }

  fun setText(content: String) {
    text.text = content
  }

  fun setText(@StringRes content: Int) {
    text.setText(content)
  }
}