package vn.vhn.vhscode.chromebrowser.webclient

import android.util.Log
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient

class VSCodeWebClient : WebViewClient() {
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url);
        view!!.evaluateJavascript(
            """(function(){
	if(!window.vscodeOrigKeyboardEventDescriptorCode) window.vscodeOrigKeyboardEventDescriptorCode = Object.getOwnPropertyDescriptor(KeyboardEvent.prototype, 'code');
	var codeGetter = window.vscodeOrigKeyboardEventDescriptorCode.get;
	// if(!window.vscodeOrigKeyboardEventDescriptorShift) window.vscodeOrigKeyboardEventDescriptorShift = Object.getOwnPropertyDescriptor(KeyboardEvent.prototype, 'shiftKey');
	// var shiftGetter = window.vscodeOrigKeyboardEventDescriptorShift.get;
	var customMapper = {
		27: 'Escape'
	};
    var customKeyRemapper = {
        'Backspace': 'Backspace',
        'Control': 'ControlLeft',
        'Alt': 'AltLeft',
    };
	var prepareCustomProps = function() {
		if(this._vscode_modded) return;
		this._vscode_modded = true;
		var orig = codeGetter.apply(this);
		if (this.which in customMapper) {
			this._vscodeCode = customMapper[this.which];
		} else if (orig === "" && typeof this.key === "string" && this.key.length) {
            if (this.key in customKeyRemapper) this._vscodeCode = customKeyRemapper[this.key];
            else if(this.key>="a" && this.key<="z") this._vscodeCode = "Key" + this.key.toUpperCase();
        } else this._vscodeCode = orig;
	}
	Object.defineProperty(KeyboardEvent.prototype, 'code', {
		get(){
			prepareCustomProps.apply(this);
			return this._vscodeCode;
		}
	});
})()""", null
        );
    }
}