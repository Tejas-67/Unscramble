package com.example.unscramble

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    private val _score=MutableLiveData(0)
    public val score:LiveData<Int>
        get()=_score

    private val _currentScrambledWord= MutableLiveData<String>()
    val currentScrambledWord: LiveData<String>
        get() = _currentScrambledWord

    private var _wordCount=MutableLiveData(0)
    public val wordCount:LiveData<Int>
        get()=_wordCount


    private lateinit var _currentWord:String



    //public val currentWord: String
    //get()=_currentScrambledWord


    private var wordList:MutableList<String> = mutableListOf()
    init{
        getNextWord()
    }
    fun getNextWord(){
        _currentWord= allWordsList.random()
        val temp=_currentWord.toCharArray()
        temp.shuffle()
        while(String(temp).equals(temp)) temp.shuffle()
        if(wordList.contains(_currentWord)) getNextWord()
        else{
            _currentScrambledWord.value=String(temp)
            _wordCount.value=(_wordCount.value)?.inc()
            wordList.add(_currentWord)
        }


    }
    fun newgame(){
        wordList.clear()
        _score.value=0
        _wordCount.value=0
        getNextWord()
    }
    public fun isCorrect(value: String?):Boolean{
        if(value.equals(_currentWord, true)){
            updateScore()
            return true
        }
        return false
    }
    private fun updateScore(){
        _score.value=(_score.value)?.plus(SCORE_INCREASE)
    }
    public fun nextIsValid():Boolean{
        if(_wordCount.value!!< MAX_NO_OF_WORDS){
            getNextWord()
            return true
        }
        return false
    }

}