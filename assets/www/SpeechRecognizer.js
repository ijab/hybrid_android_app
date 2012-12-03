/**
 *  SpeechRecognizer.js
 *  Speech Recognizer cordova plugin (Android)
 *
 *  @author Colin Turner
 *
 *  MIT Licensed
 */

/**
 * c'tor
 */
function SpeechRecognizer() {
}

SpeechRecognizer.EXEC = cordova.require('cordova/exec');

/**
 * Initialize
 *
 * @param successCallback
 * @param errorCallback
 */
SpeechRecognizer.prototype.init = function(successCallback, errorCallback) {
     return SpeechRecognizer.EXEC(successCallback, errorCallback, "SpeechRecognizer", "init", []);
};

/**
 * Recognize speech and return a list of matches
 *
 * @param successCallback
 * @param errorCallback
 * @param reqCode User-defined integer request code which will be returned when recognition is complete
 * @param maxMatches The maximum number of matches to return. 0 means the service decides how many to return.
 * @param promptString An optional string to prompt the user during recognition
 */
SpeechRecognizer.prototype.startRecognize = function(successCallback, errorCallback, reqCode, maxMatches, promptString) {
    return SpeechRecognizer.EXEC(successCallback, errorCallback, "SpeechRecognizer", "startRecognize", [reqCode, maxMatches, promptString]);
};

/**
 * Get the list of the supported languages in IETF BCP 47 format
 * 
 * @param successCallback
 * @param errorCallback
 *
 * Returns an array of codes in the success callback
 */
SpeechRecognizer.prototype.getSupportedLanguages = function(successCallback, errorCallback) {
    return SpeechRecognizer.EXEC(successCallback, errorCallback, "SpeechRecognizer", "getSupportedLanguages", []);
};

/**
 * Load
 */
if (typeof window.navigator == 'undefined') {
	window.navigator = {};
}

if (!window.navigator.speechrecognizer) {
    window.navigator.speechrecognizer = new SpeechRecognizer();
}
