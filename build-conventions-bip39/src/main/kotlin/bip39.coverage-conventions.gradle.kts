import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension

pluginManager.withPlugin("org.jetbrains.kotlinx.kover") {
    extensions.configure<KoverProjectExtension>("kover") {
        if (!project.property("BIP39_IS_COVERAGE_ENABLED").toString().toBoolean()) {
            disable()
        }
        reports {
            total {
                html {
                    onCheck = true
                    htmlDir = layout.buildDirectory.dir("reports/kover/html")
                }
                xml {
                    onCheck = true
                    xmlFile = layout.buildDirectory.file("reports/kover/xml/report.xml")
                }
            }
        }
    }
}