package co.freeside.hipsteroid

import java.awt.Dimension
import groovy.transform.TupleConstructor
import org.im4java.core.*
import static java.lang.Math.round

@TupleConstructor
abstract class Filter {

	final String name

	protected abstract Iterable<Operation> createOperations(Dimension imageSize)

	void execute(File input, File output, Dimension resizeTo = null) {
		def imageInfo = new Info(input.absolutePath, true)
		def originalSize = new Dimension(imageInfo.imageWidth, imageInfo.imageHeight)

		def ops = []
		if (resizeTo) {
			ops << resize(resizeTo.@width, resizeTo.@height)
		}
		ops.addAll createOperations(resizeTo ?: originalSize)

		def command = new ConvertCmd()
		ops.inject(input) { File file, Operation operation ->
			println "$operation [$file, $output]"
			command.run(operation, file.absolutePath, output.absolutePath)
			output
		}
	}

	public static final Filter GOTHAM = new Filter('gotham') {
		@Override
		protected Iterable<Operation> createOperations(Dimension imageSize) {
			def op = new IMOperation()
			op.addImage()
			op.modulate 120, 10, 100
			op.fill '#222b6d'
			op.colorize 20
			op.gamma 0.5d
			op.contrast()
			op.contrast()
			op.addOperation border('black', round([imageSize.@width, imageSize.@height].max() * 0.03f).intValue())
			op.addImage()
			[op]
		}
	}

	public static final Filter TOASTER = new Filter('toaster') {
		@Override
		protected Iterable<Operation> createOperations(Dimension imageSize) {
			def op = new IMOperation()
			op.addImage()
			op.addOperation colortone('#330000', 100)
			op.modulate 150, 80, 100
			op.gamma 1.2
			op.contrast()
			op.contrast()
			op.addOperation vignette(imageSize, 'none', 'LavenderBlush3')
			op.addOperation vignette(imageSize, '#ff9966', 'none')
			op.addOperation border('white', round([imageSize.@width, imageSize.@height].max() * 0.03f).intValue())
			op.addImage()
			[op]
		}
	}

	public static final Filter NASHVILLE = new Filter('nashville') {
		@Override
		protected Iterable<Operation> createOperations(Dimension imageSize) {
			def filterOp = new IMOperation()
			filterOp.addImage()
			filterOp.addOperation(colortone('#222b6d', 100))
			filterOp.addOperation(colortone('#f7daae', 100, false))
			filterOp.contrast()
			filterOp.modulate(100, 150, 100)
			filterOp.addRawArgs('-auto-gamma')
			filterOp.addImage()

			def frameOp = new IMOperation()
			frameOp.addImage()
			frameOp.addOperation frame(getResourceAsFile('/nashville.png'), imageSize)
			frameOp.addImage()

			[filterOp, frameOp]
		}
	}

	public static final Filter LOMO = new Filter('lomo') {
		@Override
		protected Iterable<Operation> createOperations(Dimension imageSize) {
			def op = new IMOperation()
			op.addImage()
			op.channel('R')
			op.level(33)
			op.channel('G')
			op.level(33)
			op.addOperation(vignette(imageSize))
			op.addImage()
			[op]
		}
	}

	public static final Filter KELVIN = new Filter('kelvin') {
		@Override
		protected Iterable<Operation> createOperations(Dimension imageSize) {
			def filterOp = new IMOperation()
			filterOp.addImage()

			filterOp.openOperation()
			filterOp.addRawArgs '-auto-gamma'
			filterOp.modulate 120, 50, 100
			filterOp.closeOperation()

			filterOp.openOperation()
			filterOp.size imageSize.@width, imageSize.@height
			filterOp.fill 'rgba(255, 153, 0, 0.5)'
			filterOp.draw "rectangle 0,0 ${imageSize.@width},${imageSize.@height}"
			filterOp.closeOperation()

			filterOp.compose 'multiply'
			filterOp.addImage()

			def frameOp = new IMOperation()
			frameOp.addImage()
			frameOp.addOperation frame(getResourceAsFile('/kelvin.png'), imageSize)
			frameOp.addImage()

			[filterOp, frameOp]
		}
	}

	public static final Collection<Filter> ALL = [GOTHAM, TOASTER, NASHVILLE, LOMO, KELVIN].asImmutable()

	protected static Operation resize(int width, int height) {
		def op = new IMOperation()
		op.addImage()
		op.resize width, height, '^'
		op.gravity 'center'
		op.crop width, height, 0, 0
		op.addImage()
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
		op.resize imageSize.@width, imageSize.@height, '!'
		op.unsharp 1.5, 1.0, 1.5, 0.02
		op.closeOperation()
		op.flatten()
		op
	}

	protected static Operation vignette(Dimension imageSize, String color1 = 'none', String color2 = 'black', Double cropFactor = 1.5) {
		int cropX = Math.floor(imageSize.@width * cropFactor)
		int cropY = Math.floor(imageSize.@height * cropFactor)

		def op = new IMOperation()
		op.openOperation()
		op.size(cropX, cropY)
		op.addRawArgs("radial-gradient:$color1-$color2")
		op.gravity('center')
		op.crop(imageSize.@width, imageSize.@height, 0, 0)
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
