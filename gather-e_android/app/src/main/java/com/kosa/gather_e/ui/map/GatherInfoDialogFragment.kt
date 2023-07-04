import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.kosa.gather_e.R
import com.kosa.gather_e.model.entity.gather.GatherEntity
import android.view.WindowManager.LayoutParams

class GatherInfoDialogFragment(private val gather: GatherEntity) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
            val inflater = it.layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_gather_info, null)

            val titleText: TextView = dialogView.findViewById(R.id.info_title)
            val dateText: TextView = dialogView.findViewById(R.id.info_date)
            val locationText: TextView = dialogView.findViewById(R.id.info_location)
            titleText.text = gather.gatherTitle
            dateText.text = gather.gatherDate
            locationText.text = gather.gatherLocationName

            val dialog = builder.setView(dialogView)
                .setPositiveButton("OK") { _, _ ->
                }.create()

            val window: Window? = dialog.window
            val params: LayoutParams? = window?.attributes
            params?.gravity = Gravity.BOTTOM
            window?.attributes = params
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
