package org.apache.cordova.plugins.speech;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.PluginResult;

import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;


public class TTSCompletedListener implements OnUtteranceCompletedListener{
	private CallbackContext callbackContext = null;
	
	
    public TTSCompletedListener(CallbackContext cbContext){
    	this.callbackContext = cbContext;
    }
 
    /**
     * Once the utterance has completely been played call the speak's success callback
     */
    public void onUtteranceCompleted(String utteranceId) {
        PluginResult result = new PluginResult(PluginResult.Status.OK, utteranceId);
        result.setKeepCallback(false);
        this.callbackContext.sendPluginResult(result);
    } 
}