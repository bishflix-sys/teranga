package com.example.data.nfc

import android.content.Context
import android.nfc.NfcAdapter

class NfcPassRechargeService(context: Context) {
    private val adapter = NfcAdapter.getDefaultAdapter(context)

    val isAvailable: Boolean
        get() = adapter != null

    val isEnabled: Boolean
        get() = adapter?.isEnabled == true
}
