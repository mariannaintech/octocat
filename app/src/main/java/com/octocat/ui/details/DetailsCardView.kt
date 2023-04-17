package com.octocat.ui.details

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import com.octocat.R
import com.octocat.databinding.DetailsCardContentBinding

private const val DEFAULT_TITLE_TEXT_SIZE = 18f
private const val DEFAULT_BODY_TEXT_SIZE = 16f

class DetailsCardView : CardView {

    private val binding = DetailsCardContentBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        R.attr.detailsCardViewStyle
    )

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.DetailsCardView, defStyleAttr, 0)
        val titleText = a.getText(R.styleable.DetailsCardView_titleText)
        val titleTextSize = a.getDimension(R.styleable.DetailsCardView_titleTextSize, DEFAULT_TITLE_TEXT_SIZE)
        val titleTextColor = a.getColor(R.styleable.DetailsCardView_titleTextColor, 0)
        binding.title.apply {
            text = titleText
            setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize)
            setTextColor(titleTextColor)
        }

        val bodyText = a.getText(R.styleable.DetailsCardView_bodyText)
        val bodyTextSize = a.getDimension(R.styleable.DetailsCardView_bodyTextSize, DEFAULT_BODY_TEXT_SIZE)
        val bodyTextColor = a.getColor(R.styleable.DetailsCardView_bodyTextColor, 0)
        binding.body.apply {
            text = bodyText
            setTextSize(TypedValue.COMPLEX_UNIT_PX, bodyTextSize)
            setTextColor(bodyTextColor)
        }

        val imageDrawable = a.getDrawable(R.styleable.DetailsCardView_imageSrc)
        if (imageDrawable != null) {
            binding.image.setImageDrawable(imageDrawable)
        }

        a.recycle()
    }

    var bodyText: CharSequence
        get() = binding.body.text
        set(value) {
            binding.body.text = value
        }

}