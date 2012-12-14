eventCompileStart = {

	ant.copy(todir: resourcesDirPath) {
		fileset dir: 'test/resources'
	}

	event 'StatusUpdate', ['compiling front-end resources']
	ant.exec executable: 'grunt', failonerror: true

}