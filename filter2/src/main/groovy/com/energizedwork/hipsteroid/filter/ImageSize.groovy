package com.energizedwork.hipsteroid.filter

import java.awt.*
import groovy.transform.CompileStatic

@CompileStatic
enum ImageSize {
	thumb(100), full(640)

	private final int size

	final Dimension getDimension() {
		new Dimension(size, size)
	}

	private ImageSize(int size) {
		this.size = size
	}
}