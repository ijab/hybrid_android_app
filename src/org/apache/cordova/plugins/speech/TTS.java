/*
 * PhoneGap is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 *
 * Copyright (c) 2011, IBM Corporation
 *
 * Modified by Murray Macdonald (murray@workgroup.ca) on 2012/05/30 to add support for stop(), pitch(), speed() and interrupt();
 *
 */

package org.apache.cordova.plugins.speech;

import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;

public class TTS extends CordovaPlugin implements OnInitListener {

    private static final String LOG_TAG = "TTS";
    private static final int STOPPED = 0;
    private static final int INITIALIZING = 1;
    private static final int STARTED = 2;
    private TextToSpeech mTts = null;
    private int state = STOPPED;

    private CallbackContext callbackContext = null;

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action
     *            The action to execute.
     * @param args
     *            JSONArry of arguments for the plugin.
     * @param callbackContext
     *            The callback context used when calling back into JavaScript.
     * @return True if the action was valid, false otherwise.
     */
    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        // Dispatcher
        PluginResult.Status status = PluginResult.Status.OK;
        String result = "";

        try {
            if (action.equals("speak")) {
                String text = args.getString(0);
                if (isReady()) {
                	Log.d(LOG_TAG, "Speak " + text + ".");
                    HashMap<String, String> map = null;
                    map = new HashMap<String, String>();
                    map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, callbackContext.getCallbackId());
                    mTts.speak(text, TextToSpeech.QUEUE_ADD, map);
                    PluginResult pr = new PluginResult(PluginResult.Status.NO_RESULT);
                    pr.setKeepCallback(true);
                    callbackContext.sendPluginResult(pr);
                } else {
                	Log.d(LOG_TAG, "TTS service is still initialzing.");
                    JSONObject error = new JSONObject();
                    error.put("message","TTS service is still initialzing.");
                    error.put("code", TTS.INITIALIZING);
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, error));
                }
            } else if (action.equals("interrupt")) {
                String text = args.getString(0);
                if (isReady()) {
                    HashMap<String, String> map = null;
                    map = new HashMap<String, String>();
                    map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, callbackContext.getCallbackId());
                    mTts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
                    PluginResult pr = new PluginResult(PluginResult.Status.NO_RESULT);
                    pr.setKeepCallback(true);
                    callbackContext.sendPluginResult(pr);
                } else {
                    JSONObject error = new JSONObject();
                    error.put("message","TTS service is still initialzing.");
                    error.put("code", TTS.INITIALIZING);
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, error));
                }
            } else if (action.equals("stop")) {
                if (isReady()) {
                    mTts.stop();
                    callbackContext.sendPluginResult(new PluginResult(status, result));
                } else {
                    JSONObject error = new JSONObject();
                    error.put("message","TTS service is still initialzing.");
                    error.put("code", TTS.INITIALIZING);
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, error));
                }
            } else if (action.equals("silence")) {
                if (isReady()) {
                    mTts.playSilence(args.getLong(0), TextToSpeech.QUEUE_ADD, null);
                    callbackContext.sendPluginResult(new PluginResult(status, result));
                } else {
                    JSONObject error = new JSONObject();
                    error.put("message","TTS service is still initialzing.");
                    error.put("code", TTS.INITIALIZING);
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, error));
                }
            } else if (action.equals("speed")) {
                if (isReady()) {
                    float speed= (float) (args.optLong(0, 100)) /(float) 100.0;
                    mTts.setSpeechRate(speed);
                    callbackContext.sendPluginResult(new PluginResult(status, result));
                } else {
                    JSONObject error = new JSONObject();
                    error.put("message","TTS service is still initialzing.");
                    error.put("code", TTS.INITIALIZING);
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, error));
                }
            } else if (action.equals("pitch")) {
                if (isReady()) {
                    float pitch= (float) (args.optLong(0, 100)) /(float) 100.0;
                    mTts.setPitch(pitch);
                    callbackContext.sendPluginResult(new PluginResult(status, result));
                } else {
                    JSONObject error = new JSONObject();
                    error.put("message","TTS service is still initialzing.");
                    error.put("code", TTS.INITIALIZING);
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, error));
                }
            } else if (action.equals("startup")) {
                if (mTts == null) {
                    this.callbackContext = callbackContext;
                    state = TTS.INITIALIZING;
                    mTts = new TextToSpeech(cordova.getActivity().getApplicationContext(), this);
                }
                PluginResult pluginResult = new PluginResult(status, TTS.INITIALIZING);
                pluginResult.setKeepCallback(true);
                callbackContext.sendPluginResult(pluginResult);
            }
            else if (action.equals("shutdown")) {
                if (mTts != null) {
                    mTts.shutdown();
                }
                callbackContext.sendPluginResult(new PluginResult(status, result));
            }
            else if (action.equals("getLanguage")) {
                if (mTts != null) {
                    result = mTts.getLanguage().toString();
                    callbackContext.sendPluginResult(new PluginResult(status, result));
                }
            }
            else if (action.equals("isLanguageAvailable")) {
                if (mTts != null) {
                    Locale loc = new Locale(args.getString(0));
                    int available = mTts.isLanguageAvailable(loc);
                    result = (available < 0) ? "false" : "true";
                    callbackContext.sendPluginResult(new PluginResult(status, result));
                }
            }
            else if (action.equals("setLanguage")) {
                if (mTts != null) {
                    Locale loc = new Locale(args.getString(0));
                    int available = mTts.setLanguage(loc);
                    result = (available < 0) ? "false" : "true";
                    callbackContext.sendPluginResult(new PluginResult(status, result));
                }
            }
            else
            {
            	String res = "Unknown action: " + action;
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION, res));
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION));
        }
        
        return true;
    }

    /**
     * Is the TTS service ready to play yet?
     *
     * @return
     */
    private boolean isReady() {
        return (state == TTS.STARTED) ? true : false;
    }

    /**
     * Called when the TTS service is initialized.
     *
     * @param status
     */
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
        	Log.d(LOG_TAG, "TextToSpeech init succeeded");
            
            state = TTS.STARTED;
            PluginResult result = new PluginResult(PluginResult.Status.OK, TTS.STARTED);
            result.setKeepCallback(false);
            this.callbackContext.sendPluginResult(result);
            
            // Default Lang US
            mTts.setLanguage(Locale.US);
            
            if (Build.VERSION.SDK_INT >= 15)
            {
                mTts.setOnUtteranceProgressListener(new TTSProgressListener(this.callbackContext));
            }
            else
            {
                mTts.setOnUtteranceCompletedListener(new TTSCompletedListener(this.callbackContext));
            }
        }
        else if (status == TextToSpeech.ERROR) {
        	Log.d(LOG_TAG, "TextToSpeech init error");
            state = TTS.STOPPED;
            PluginResult result = new PluginResult(PluginResult.Status.ERROR, TTS.STOPPED);
            result.setKeepCallback(false);
            this.callbackContext.sendPluginResult(result);
        }
    }

    /**
     * Clean up the TTS resources
     */
    public void onDestroy() {
        if (mTts != null) {
            mTts.shutdown();
        }
    }
}