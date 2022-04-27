package com.android.base_project.base

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputConnection
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.base_project.R
import com.android.base_project.base.action.ItemMenuAction
import com.android.base_project.databinding.KeyboardViewBinding

@BindingMethods(
    BindingMethod(
        type = Keyboard::class,
        attribute = "app:setEnterListenter",
        method = "setEnterListener"
    ),
    BindingMethod(
        type = Keyboard::class,
        attribute = "app:setKeyboardListener",
        method = "setKeyboardListener"
    ),
    BindingMethod(
        type = Keyboard::class,
        attribute = "app:keyboardType",
        method = "keyboardType"
    ),
    BindingMethod(
        type = Keyboard::class,
        attribute = "app:setFilterCharacters",
        method = "setFilterCharacters"
    ),
    BindingMethod(
        type = Keyboard::class,
        attribute = "app:setMaxLenght",
        method = "setMaxLength"
    ),
    BindingMethod(
        type = Keyboard::class,
        attribute = "app:spaceEnableClick",
        method = "spaceEnableClick"
    )
)

class Keyboard(context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context) {
    val keyBoardBinding =
        KeyboardViewBinding.inflate(LayoutInflater.from(context), this, true)

    var inputConnection: InputConnection? = null

    private var textChange: String = ""

    val FX_VOLUME = -1.0f

    private lateinit var mediaPlayer: MediaPlayer

    private var keyboardListener: OnKeyboardProperties? = null
    private var maxLength: Int? = null
    private var filter = ""

    fun setMaxLength(value: Int) {
        Log.d(TAG, "setMaxLength: $value")
        maxLength = value
    }

    fun setFilterCharacters(chr: String) {
        Log.d(TAG, "setFilterCharacters: chr = $chr")
        filter = chr
    }

    init {

        initMediaPlayer()

        keyBoardBinding.apply {
            btnEnterTextView.setText("Save")
            groupBtnNumText.referencedIds.forEach {
                (findViewById(it) as? AppCompatButton)?.setOnClickListener { button ->
                    (button as? AppCompatButton)?.let { iviButton ->
                        val string = iviButton.text.toString()
                        val result = if (isCaps) string.uppercase() else string

                        maxLength?.let {
                            if (textChange.length < it) {
                                if (!filter.contains(result)) {
                                    textChange += result
                                    inputConnection?.commitText(result, 1)
                                    keyboardListener?.onTextChange(textChange)
                                }
                            }
                        } ?: run {
                            if (!filter.contains(result)) {
                                textChange += result
                                inputConnection?.commitText(result, 1)
                                keyboardListener?.onTextChange(textChange)
                            }
                        }
                    }
                    playPressSound()
                }
            }

            btnCaps.setOnClickListener {
                isCaps = !isCaps
                playPressSound()
            }

            btnSpace.setOnClickListener {
                inputConnection?.commitText(SPACE_TEXT, 1)
                Log.d(TAG, "filter: ${filter} ")
                maxLength?.let {
                    if (textChange.length < it) {
                        if (!filter.contains(" ")) {
                            textChange += SPACE_TEXT
                            keyboardListener?.onTextChange(textChange)
                        }
                    }
                } ?: run {
                    if (!filter.contains(" ")) {
                        textChange += SPACE_TEXT
                        keyboardListener?.onTextChange(textChange)
                    }
                }
                playPressSound()
            }

            btnKeyBack.setOnClickListener {
                inputConnection?.apply {
                    val selectedText = getSelectedText(0)
                    if (TextUtils.isEmpty(selectedText)) {
                        // no selection, so delete previous charactervf
                        deleteSurroundingText(1, 0)
                    } else {
                        // delete the selection
                        commitText("", 1)
                    }
                }
                if (textChange.isNotEmpty()) {
                    textChange = textChange.substring(0, textChange.length - 1)
                    Log.d(TAG, "onTextChange 12334 $textChange")
                    keyboardListener?.onTextChange(textChange)
                }
                playPressSound()
            }


        }
    }

    fun enableSpaceButton(enable: Boolean) {
        keyBoardBinding.btnSpace.isClickable = enable
    }

    /**
     * this function is used for set text for the Enter button is 'save'/'next'
     * type = 0 => Next; type = 1 => Save
     */

    fun setKeyboardType(type: Int) {
        Log.d("Tag", "type = $type")
        when (type) {
            TypeKeyboard.NEXT.ordinal -> {
                keyBoardBinding.btnEnterTextView.text = "next"
                keyBoardBinding.imgEnter.visibility = View.GONE
            }
            else -> {
                keyBoardBinding.btnEnterTextView.setText("save")
                keyBoardBinding.imgEnter.visibility = View.VISIBLE
            }
        }
    }

    private fun initMediaPlayer() {
        val pathAudio = "android.resource://" + context.packageName + "/"
        val uri = Uri.parse(pathAudio + R.raw.type)
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(context, uri);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_SYSTEM);
        mediaPlayer.prepareAsync()
        mediaPlayer.seekTo(0)
        mediaPlayer.setVolume(FX_VOLUME, FX_VOLUME);
        mediaPlayer.setOnCompletionListener {
            Log.d(TAG, "onComplete")
        }
        mediaPlayer.setOnErrorListener { mp, _, _ ->
            Log.d(TAG, "onError")
            true
        }

        mediaPlayer.setOnPreparedListener {
            Log.d(TAG, "onPrepared")
        }
    }

    fun setEnterLister(event: Runnable) {
        keyBoardBinding.btnEnter.setOnClickListener {
            event.run()
        }
    }

    fun setKeyboardListener(keyboardListener: OnKeyboardProperties) {
        Log.d(TAG,"setKeyboardListener")
        this.keyboardListener = keyboardListener
    }

    private fun playPressSound() {
        try {
            mediaPlayer.start()
        } catch (e: Exception) {
            Log.d(TAG, "${e.localizedMessage}")
        }

    }

    companion object {
        private const val SPACE_TEXT = " "
        private const val TAG = "Keyboard"
        private const val DEFAULT_MAX_LENGTH_PASSWORD = 7
    }

    /**
     * Only use for fragment_keyboard_template
     */
    interface OnKeyboardProperties : ItemMenuAction {
        val maxLength: Int get() = 20
        val resize: Float get() = 24f
        val currentValue: LiveData<String>
            get() = MutableLiveData()

        fun onClickEnter(result: String)

        fun onTextChange(result: String) {
            Log.d(TAG, "onTextChange $result")
        }
    }
}

enum class TypeKeyboard {
    NEXT,
    DONE,
    SEARCH,
    ENTER
}