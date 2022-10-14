package io.github.boldijar.exoplayer_closed_captions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView

class MainActivity : AppCompatActivity() {

    companion object {
        const val STREAM =
            "http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/bipbop_16x9_variant.m3u8"
    }

    private lateinit var player: ExoPlayer
    private lateinit var playerView: StyledPlayerView

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
    }

    override fun onPause() {
        super.onPause()
        player.release()
    }

    override fun onResume() {
        super.onResume()
        prepare()
    }
}