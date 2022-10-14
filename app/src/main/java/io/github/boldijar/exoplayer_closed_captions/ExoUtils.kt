package io.github.boldijar.exoplayer_closed_captions

import com.google.android.exoplayer2.C.TRACK_TYPE_TEXT
import com.google.android.exoplayer2.Tracks
import java.util.*

object ExoUtils {
    fun getLanguagesFromTrackInfo(track: Tracks): List<Language> {
        return track.groups.asSequence().mapNotNull {
            if (it.type != TRACK_TYPE_TEXT) {
                return@mapNotNull null
            }
            val group = it
            val formats = (0 until group.length).map { index -> group.getTrackFormat(index) }
            formats.mapNotNull { format ->
                format.language
            }
        }
            .flatten()
            .toSet()
            .map {
                val locale = Locale(it)
                val languageName = locale.getDisplayLanguage(locale)
                Language(code = it, name = languageName)
            }.toList()
    }
}