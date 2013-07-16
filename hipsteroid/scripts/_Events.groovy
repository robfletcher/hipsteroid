eventCompileStart = {

	ant.copy(todir: resourcesDirPath) {
		fileset dir: 'test/resources'
	}

	event 'StatusUpdate', ['Compiling front-end resources']
	def process = 'grunt'.execute()
	def returnCode = process.waitFor()
	switch (returnCode) {
		case 0:
			event 'StatusUpdate', ['Compiled front-end resources']
			break
		case 2:
			event 'StatusError', ['Grunt is not installed. Front-end resources were NOT compiled.']
			break
		default:
			event 'StatusError', ["Failed to compile front-end resources: ${process.errorStream.text}"]
	}



}