import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.dicoding.eventdicoding.R
import com.dicoding.eventdicoding.data.database.Event

class FavoriteAdapter(
    context: Context,
    private val events: List<Event>,
    private val onDeleteClick: (Event) -> Unit // Callback untuk tombol hapus
) : ArrayAdapter<Event>(context, 0, events) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_event, parent, false)

        val event = getItem(position)

        val nameTextView = view.findViewById<TextView>(R.id.event_name)
        val deleteButton = view.findViewById<Button>(R.id.button_delete)

        // Set data ke TextView
        nameTextView.text = event?.name

        // Set listener untuk tombol hapus
        deleteButton.setOnClickListener {
            event?.let { onDeleteClick(it) }
        }

        return view
    }
}