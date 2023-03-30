package com.example.unscramble

import android.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
//import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.example.unscramble.databinding.FragmentGameBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GameFragment : androidx.fragment.app.Fragment() {



    private val viewModel: GameViewModel by viewModels()

    private var _binding: FragmentGameBinding?=null
    private val binding get()=_binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentGameBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

//orientation changes are resulting in 1 increase in wordcount and change in shuffled word.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.lifecycleOwner = viewLifecycleOwner
    binding.submit.setOnClickListener {
        onSubmitWord()
    }
    binding.skip.setOnClickListener { onSkipWord() }
    viewModel.currentScrambledWord.observe(viewLifecycleOwner, { newWord ->
        binding.textViewUnscrambledWord.text = newWord
    })
    viewModel.score.observe(
        viewLifecycleOwner,
        { newScore -> binding.score.text = getString(R.string.score, newScore) })

    viewModel.wordCount.observe(viewLifecycleOwner,
        {newWC-> binding.wordCount.text=getString(R.string.word_count, newWC, 10)})

}

    private fun startGame(){
        viewModel.getNextWord()
    }
    private fun showAlertDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.congratulations)
            .setMessage(getString(R.string.you_scored, viewModel.score.value))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.exit)){_,_ -> exitGame()}
            .setPositiveButton(getString(R.string.play_again)){_,_-> restartGame()}
            .show()


    }
    private fun onSubmitWord() {
        val input: String? =binding.textinput.text.toString()
        if(viewModel.isCorrect(input)){
            setErrorTextField(false)
            if(!viewModel.nextIsValid()) showAlertDialog()
        }
        else setErrorTextField(true)
    }

    /*
     * Skips the current word without changing the score.
     * Increases the word count.
     */
    private fun onSkipWord() {
        if(viewModel.nextIsValid()) ///updateNextWordOnScreen()
        else showAlertDialog()
    }
    private fun restartGame() {
        viewModel.newgame()
        setErrorTextField(false)
        //updateNextWordOnScreen()
    }
    private fun exitGame() {
        activity?.finish()
    }
    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error=getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            binding.textinput.text=null
        }
    }
//    private fun updateNextWordOnScreen() {
//        binding.textViewUnscrambledWord.text = viewModel.currentWord
//        binding.score.text=getString(R.string.score, viewModel.score)
//        binding.wordCount.text=getString(R.string.word_count, viewModel.wordCount,10)
//    }

}