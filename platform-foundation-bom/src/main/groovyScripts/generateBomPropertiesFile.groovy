def targetDir = new File(project.build.directory).absoluteFile
def effectivePom = new File(targetDir, "effective-pom.xml")

new File(targetDir, "platform-foundation-bom.properties").withWriter { writer ->
	def xml = new XmlSlurper().parseText(effectivePom.text)
	xml.project.dependencyManagement.dependencies.dependency
		.list()
		.findAll { dependency -> dependency.groupId != 'org.eclipse.jetty.websocket' }
		.sort { a, b ->
			def comparison = a.groupId.text().compareTo(b.groupId.text())
			if (!comparison) {
				comparison = a.artifactId.text().compareTo(b.artifactId.text())
			}
			comparison
		}
		.each { dependency ->
			writer.println "${dependency.groupId}\\:${dependency.artifactId}=${dependency.version}"
		}
}