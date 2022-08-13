package com.james.mobile.dev

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton


object MyDialog {
    fun showStartDialog(context: Context, viewGroup: ViewGroup, listener: OnStartPressed) {
        val builder = AlertDialog.Builder(context)
        val dialogView: View =
            LayoutInflater.from(context).inflate(R.layout.start_dialog, viewGroup, false)
        dialogView.background = ContextCompat.getDrawable(context, R.drawable.dialog_bg)
        builder.setView(dialogView)

        val alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)

        dialogView.findViewById<MaterialButton>(R.id.start).setOnClickListener {
            alertDialog.dismiss()
            listener.onStartPressed()
        }
        alertDialog.show()
    }

    fun showEndDialog(
        context: Context,
        viewGroup: ViewGroup,
        listener: OnRestartPressed,
        remainingLives: Int,
        score: Int,
        lost: Boolean
    ) {
        val builder = AlertDialog.Builder(context)
        val dialogView: View =
            LayoutInflater.from(context).inflate(R.layout.end_dialog, viewGroup, false)
        dialogView.background = ContextCompat.getDrawable(context, R.drawable.dialog_bg)
        builder.setView(dialogView)
        if (lost) {
            dialogView.findViewById<TextView>(R.id.tvLose).visibility = VISIBLE
            dialogView.findViewById<TextView>(R.id.tvWin).visibility = GONE
        } else {
            dialogView.findViewById<TextView>(R.id.tvLose).visibility = GONE
            dialogView.findViewById<TextView>(R.id.tvWin).visibility = VISIBLE
        }

        dialogView.findViewById<TextView>(R.id.remainingLives).text = remainingLives.toString()
        dialogView.findViewById<TextView>(R.id.tvScore).text = score.toString()

        val alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)

        dialogView.findViewById<MaterialButton>(R.id.start).setOnClickListener {
            alertDialog.dismiss()
            listener.onRestartPressed()
        }

        alertDialog.show()
    }

    interface OnStartPressed {
        fun onStartPressed()
    }

    interface OnRestartPressed {
        fun onRestartPressed()
    }

}