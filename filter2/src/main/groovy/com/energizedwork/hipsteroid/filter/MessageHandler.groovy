package com.energizedwork.hipsteroid.filter

import groovy.transform.CompileStatic

@CompileStatic
interface MessageHandler {

	byte[] onMessage(byte[] message)

}