package com.kazakago.cueue.model

import com.ibm.icu.text.Transliterator
import org.apache.lucene.analysis.CharArraySet
import org.apache.lucene.analysis.ja.JapaneseAnalyzer
import org.apache.lucene.analysis.ja.JapaneseTokenizer
import org.apache.lucene.analysis.ja.tokenattributes.ReadingAttribute
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute

class Furigana(originalText: String) {

    private val stoptags = setOf(
        "記号-一般", "記号-読点", "記号-句点", "記号-空白", "記号-括弧開", "記号-括弧閉", "記号-アルファベット"
    )

    val hiragana: String
    val katakana: String

    init {
        val kana = kanjiToKana(originalText)
        hiragana = kanaToHiragana(kana)
        katakana = kanaToKatakana(kana)
    }

    private fun kanjiToKana(kanjiText: String): String {
        val analyzer = JapaneseAnalyzer(null, JapaneseTokenizer.Mode.NORMAL, CharArraySet.EMPTY_SET, stoptags)
        val stringBuilder = StringBuilder()
        analyzer.tokenStream("", kanjiText).use { stream ->
            stream.reset()
            while (stream.incrementToken()) {
                val readingAttribute = stream.getAttribute(ReadingAttribute::class.java)
                val charTermAttribute = stream.getAttribute(CharTermAttribute::class.java)
                var kana = readingAttribute.reading
                if (kana == null) {
                    kana = charTermAttribute.toString()
                }
                stringBuilder.append(kana)
            }
        }
        return stringBuilder.toString()
    }

    private fun kanaToHiragana(kana: String): String {
        val transliterator = Transliterator.getInstance("Katakana-Hiragana")
        val hiraganaList = kana.split("ー").map { transliterator.transliterate(it) }
        return hiraganaList.joinToString("ー")
    }

    private fun kanaToKatakana(kana: String): String {
        val transliterator = Transliterator.getInstance("Hiragana-Katakana")
        return transliterator.transliterate(kana)
    }
}
