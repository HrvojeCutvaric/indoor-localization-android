import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension

fun Project.configureEnvFlavorsForCommonAndroidDsl() {
    val devUrl = (findProperty("DEV_BASE_URL_BACKEND") as String?)
        ?: error("Missing DEV_BASE_URL_BACKEND in gradle.properties or command line")
    val prodUrl = (findProperty("PROD_BASE_URL_BACKEND") as String?)
        ?: error("Missing PROD_BASE_URL_BACKEND in gradle.properties or command line")

    plugins.withId("com.android.application") {
        extensions.configure<ApplicationExtension> {
            buildFeatures { buildConfig = true }

            flavorDimensions += "env"
            productFlavors {
                create("dev") {
                    dimension = "env"
                    buildConfigField("String", "BASE_URL_BACKEND", devUrl)
                    applicationIdSuffix = ".dev"
                    versionNameSuffix = "-dev"
                }
                create("prod") {
                    dimension = "env"
                    buildConfigField("String", "BASE_URL_BACKEND", prodUrl)
                }
            }
        }
    }

    plugins.withId("com.android.library") {
        extensions.configure<LibraryExtension> {
            buildFeatures { buildConfig = true }

            flavorDimensions += "env"
            productFlavors {
                create("dev") {
                    dimension = "env"
                    buildConfigField("String", "BASE_URL_BACKEND", devUrl)
                }
                create("prod") {
                    dimension = "env"
                    buildConfigField("String", "BASE_URL_BACKEND", prodUrl)
                }
            }
        }
    }
}

configureEnvFlavorsForCommonAndroidDsl()
