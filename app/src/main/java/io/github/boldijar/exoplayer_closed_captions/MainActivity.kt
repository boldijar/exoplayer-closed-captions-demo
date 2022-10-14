package io.github.boldijar.exoplayer_closed_captions

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Tracks
import com.google.android.exoplayer2.ui.StyledPlayerView


class MainActivity : AppCompatActivity(), Player.Listener,
    AdapterView.OnItemClickListener {

    companion object {
        const val STREAM =
            "https://stream.mux.com/HDGj01zK01esWsWf9WJj5t5yuXQZJFF6bo.m3u8"
    }

    private lateinit var player: ExoPlayer
    private lateinit var playerView: StyledPlayerView
    private lateinit var languagesListView: ListView

    private var languages: List<Language> = mutableListOf()
    private var selectedLanguageCode: String? = null

    private fun prepare() {
        playerView.player = null
        playerView.player = player
        playerView.keepScreenOn = true
        player.setMediaItem(MediaItem.fromUri(STREAM))
        player.playWhenReady = true
        player.prepare()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        player = ExoPlayer.Builder(this).build()
        playerView = findViewById(R.id.exo_player)
        languagesListView = findViewById(R.id.list_view)
        languagesListView.choiceMode = ListView.CHOICE_MODE_SINGLE
        languagesListView.onItemClickListener = this
        player.addListener(this)
    }

    override fun onPause() {
        super.onPause()
        player.release()
    }

    override fun onResume() {
        super.onResume()
        prepare()
    }

    private fun refreshLanguages() {
        languages = ExoUtils.getLanguagesFromTrackInfo(player.currentTracks)
        val listViewItems =
            languages.map { it.name }
                .toTypedArray()
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_single_choice,
                listViewItems
            )
        languagesListView.adapter = adapter
        val selectedIndex = languages.indexOfFirst { it.code == selectedLanguageCode }
        languagesListView.setItemChecked(selectedIndex, true)
    }

    override fun onTracksChanged(tracks: Tracks) {
        super.onTracksChanged(tracks)
        selectedLanguageCode = player.trackSelectionParameters.preferredTextLanguages.firstOrNull()
        refreshLanguages()
    }


    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val selection = languages.getOrNull(p2)
        selectedLanguageCode = if (selection?.code == selectedLanguageCode) {
            null
        } else {
            selection?.code
        }
        player.trackSelectionParameters = player.trackSelectionParameters
            .buildUpon()
            .setPreferredTextLanguage(selectedLanguageCode)
            .build()
        refreshLanguages()
    }


}