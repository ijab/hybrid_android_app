package org.apache.cordova.plugins.speech;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.PluginResult;

import android.speech.tts.UtteranceProgressListener;


public class TTSProgressListener extends UtteranceProgressListener{
	private CallbackContext callbackContext = null;
	
	
    public TTSProgressListener(CallbackContext cbContext){
    	this.callbackContext = cbContext;
    }
 
    

	@Override
	public void onDone(String utteranceId) {
		PluginResult result = new PluginResult(PluginResult.Status.OK, utteranceId);
        result.setKeepCallback(false);
        this.callbackContext.sendPluginResult(result);		
	}

	@Override
	public void onError(String utteranceId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart(String utteranceId) {
		// TODO Auto-generated method stub
		
	} 
}