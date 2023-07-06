import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.kosa.gather_e.R
import android.view.WindowManager.LayoutParams
import android.widget.Button
import com.google.android.material.internal.ViewUtils.dpToPx
import com.kosa.gather_e.model.entity.gather.GatherEntity
import com.kosa.gather_e.model.entity.map.PastMeetingGatherEntity
import com.kosa.gather_e.ui.home.PostDetailActivity

class GatherInfoDialogFragment(private val gather: GatherEntity) : DialogFragment() {
    constructor(gather: PastMeetingGatherEntity) : this(gather as GatherEntity)


    @SuppressLint("RestrictedApi")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = AlertDialog.Builder(it, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
            val inflater = it.layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_gather_info, null)

            val titleText: TextView = dialogView.findViewById(R.id.info_title)
            val locationText: TextView = dialogView.findViewById(R.id.info_location)
            titleText.text = gather.gatherTitle
            locationText.text = gather.gatherLocationName


            val dialog = builder.setView(dialogView).create()
            dialog.show()


            val window: Window? = dialog.window
            val params: LayoutParams? = window?.attributes
            window?.attributes = params

            val goGatherBtn: Button = dialogView.findViewById(R.id.go_gather_btn)
            goGatherBtn.setOnClickListener {
                Intent(it.context, PostDetailActivity::class.java).apply {
                    putExtra("gather", gather)
                }.run { it.context.startActivity(this) }
            }


            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
