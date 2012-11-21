eventCompileStart = {
	ant.copy(todir: resourcesDirPath) {
		fileset dir: 'test/resources'
	}
}