package com.energizedwork.hipsteroid.filter

import java.awt.*
import groovy.transform.*
import groovy.util.logging.Log
import org.im4java.core.*
import static java.lang.Math.round

@CompileStatic
@TupleConstructor
@Log
abstract class Filter {

	final String name

	protected abstract Collection<Operation> createOperations(Dimension imageSize)

	void execute(File input, File output, Dimension resizeTo = null) {
		def imageInfo = new Info(input.absolutePath, true)
		def originalSize = new Dimension(imageInfo.imageWidth, imageInfo.imageHeight)

		def ops = []
		if (resizeTo) {
			ops << resize((int) resizeTo.@width, (int) resizeTo.@height)
		}
		ops.addAll createOperations(resizeTo ?: originalSize)

		def command = new ConvertCmd()
		ops.inject(input) { File file, Operation operation ->
			log.fine "Executing...\n$operation [$file, $output]"
			command.run(operation, file.absolutePath, output.absolutePath)
			output
		}
	}

	public static final Filter NONE = new Filter('none') {
		@Override
		protected Collection<Operation> createOperations(Dimension imageSize) {
			[]
		}
	}

	public static final Filter GOTHAM = new Filter('gotham') {
		@Override
		protected Collection<Operation> createOperations(Dimension imageSize) {
			def op = new IMOperation()
			op.addImage(Operation.IMG_PLACEHOLDER)
			op.modulate 120d, 10d, 100d
			op.fill '#222b6d'
			op.colorize 20
			op.gamma 0.5d
			op.contrast()
			op.contrast()
			op.addOperation border('black', round([imageSize.@width, imageSize.@height].max() * 0.03f).intValue())
			op.addImage(Operation.IMG_PLACEHOLDER)
			[op]
		}
	}

	public static final Filter TOASTER = new Filter('toaster') {
		@Override
		protected Collection<Operation> createOperations(Dimension imageSize) {
			def op = new IMOperation()
			op.addImage(Operation.IMG_PLACEHOLDER)
			op.addOperation colortone('#330000', 100)
			op.modulate 150d, 80d, 100d
			op.gamma 1.2d
			op.contrast()
			op.contrast()
			op.addOperation vignette(imageSize, 'none', 'LavenderBlush3')
			op.addOperation vignette(imageSize, '#ff9966', 'none')
			op.addOperation border('white', round([imageSize.@width, imageSize.@height].max() * 0.03f).intValue())
			op.addImage(Operation.IMG_PLACEHOLDER)
			[op]
		}
	}

	public static final Filter NASHVILLE = new Filter('nashville') {
		@Override
		protected Collection<Operation> createOperations(Dimension imageSize) {
			def filterOp = new IMOperation()
			filterOp.addImage(Operation.IMG_PLACEHOLDER)
			filterOp.addOperation(colortone('#222b6d', 100))
			filterOp.addOperation(colortone('#f7daae', 100, false))
			filterOp.contrast()
			filterOp.modulate 100d, 150d, 100d
			filterOp.addRawArgs('-auto-gamma')
			filterOp.addImage(Operation.IMG_PLACEHOLDER)

			def frameOp = new IMOperation()
			frameOp.addImage(Operation.IMG_PLACEHOLDER)
			frameOp.addOperation frame(getResourceAsFile('/nashville.png'), imageSize)
			frameOp.addImage(Operation.IMG_PLACEHOLDER)

			[filterOp, frameOp]
		}
	}

	public static final Filter LOMO = new Filter('lomo') {
		@Override
		protected Collection<Operation> createOperations(Dimension imageSize) {
			def op = new IMOperation()
			op.addImage(Operation.IMG_PLACEHOLDER)
			op.channel('R')
			op.level(33d)
			op.channel('G')
			op.level(33d)
			op.addOperation(vignette(imageSize))
			op.addImage(Operation.IMG_PLACEHOLDER)
			[op]
		}
	}

	public static final Filter KELVIN = new Filter('kelvin') {
		@Override
		protected Collection<Operation> createOperations(Dimension imageSize) {
			def filterOp = new IMOperation()
			filterOp.addImage(Operation.IMG_PLACEHOLDER)

			filterOp.openOperation()
			filterOp.addRawArgs '-auto-gamma'
			filterOp.modulate 120d, 50d, 100d
			filterOp.closeOperation()

			filterOp.openOperation()
			filterOp.size((int) imageSize.@width, (int) imageSize.@height)
			filterOp.fill 'rgba(255, 153, 0, 0.5)'
			filterOp.draw "rectangle 0,0 ${imageSize.@width},${imageSize.@height}"
			filterOp.closeOperation()

			filterOp.compose 'multiply'
			filterOp.addImage(Operation.IMG_PLACEHOLDER)

			def frameOp = new IMOperation()
			frameOp.addImage(Operation.IMG_PLACEHOLDER)
			frameOp.addOperation frame(getResourceAsFile('/kelvin.png'), imageSize)
			frameOp.addImage(Operation.IMG_PLACEHOLDER)

			[filterOp, frameOp]
		}
	}

	public static final Collection<Filter> ALL = [NONE, GOTHAM, TOASTER, NASHVILLE, LOMO, KELVIN].asImmutable()

	protected static Operation resize(int width, int height) {
		def op = new IMOperation()
		op.addImage(Operation.IMG_PLACEHOLDER)
		op.resize width, height, '^'
		op.gravity 'center'
		op.crop width, height, 0, 0
		op.addImage(Operation.IMG_PLACEHOLDER)
		op
	}

	protected static Operation colortone(String color, int level, boolean negate = true) {
		def op = new IMOperation()

		op.openOperation()
		op.clone 0
		op.fill color
		op.colorize 100
		op.closeOperation()

		op.openOperation()
		op.clone 0
		op.colorspace 'gray'
		if (negate) op.negate()
		op.closeOperation()

		op.compose 'blend'
		op.define "compose:args=$level,${100 - level}"
		op.composite()

		op
	}

	protected static Operation border(String color = 'black', int width = 20) {
		def op = new IMOperation()
		op.shave width, width
		op.bordercolor color
		op.border width, width
		op
	}

	protected static Operation frame(File frame, Dimension imageSize) {
		def op = new IMOperation()
		op.openOperation()
		op.addImage frame.absolutePath
		op.resize((int) imageSize.@width, (int) imageSize.@height, '!')
		op.unsharp 1.5d, 1.0d, 1.5d, 0.02d
		op.closeOperation()
		op.flatten()
		op
	}

	protected static Operation vignette(Dimension imageSize, String color1 = 'none', String color2 = 'black', double cropFactor = 1.5) {
		int cropX = (int) Math.floor(imageSize.@width * cropFactor)
		int cropY = (int) Math.floor(imageSize.@height * cropFactor)

		def op = new IMOperation()
		op.openOperation()
		op.size(cropX, cropY)
		op.addRawArgs("radial-gradient:$color1-$color2")
		op.gravity('center')
		op.crop((int) imageSize.@width, (int) imageSize.@height, 0, 0)
		op.addRawArgs('+repage')
		op.closeOperation()

		op.compose('multiply')
		op.flatten()

		op
	}

	protected static File getResourceAsFile(String path) {
		def tempFile = File.createTempFile(path, '.png')
		tempFile.deleteOnExit()
		tempFile << Filter.getResource(path).bytes
		tempFile
	}

	@Override
	final String toString() {
		name
	}
}
