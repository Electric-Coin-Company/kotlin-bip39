pluginManager.withPlugin("org.jetbrains.kotlinx.kover") {
    extensions.findByType<kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension>()?.apply {
        if (!project.property("BIP39_IS_COVERAGE_ENABLED").toString().toBoolean()) {
            disable()
        }
    }
    extensions.findByType<kotlinx.kover.gradle.plugin.dsl.KoverReportExtension>()?.apply {
        defaults {
            html {
                onCheck = true
                setReportDir(layout.buildDirectory.dir("reports/kover/html"))
            }
            xml {
                onCheck = true
                setReportFile(layout.buildDirectory.file("reports/kover/xml/report.xml"))
            }
        }
    }
}
