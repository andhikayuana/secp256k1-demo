import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.internal.os.OperatingSystem

val localProperties = gradleLocalProperties(rootDir, providers)
val currentOs = OperatingSystem.current()
val bash = "bash"

val buildSecp256k1 by tasks.creating { group = "build" }

val buildSecp256k1Host by tasks.creating(Exec::class) {
    group = "build"
    buildSecp256k1.dependsOn(this)

    val target = when {
        currentOs.isLinux -> "linux"
        currentOs.isMacOsX -> "darwin"
        currentOs.isWindows -> "mingw"
        else -> error("Unsupported OS $currentOs")
    }

    inputs.files(projectDir.resolve("build.sh"))
    outputs.dir(projectDir.resolve("build/$target"))

    workingDir = projectDir
    environment("TARGET", target)
    commandLine(bash, "-l", "build.sh")
}

val buildSecp256k1Android by tasks.creating {
    group = "build"
    buildSecp256k1.dependsOn(this)
}

fun creatingBuildSecp256k1Android(arch: String) = tasks.creating(Exec::class) {
    group = "build"
    buildSecp256k1Android.dependsOn(this)

    inputs.files(projectDir.resolve("build-android.sh"))
    outputs.dir(projectDir.resolve("build/android/$arch"))

    workingDir = projectDir

    val toolchain = when {
        currentOs.isLinux -> "linux-x86_64"
        currentOs.isMacOsX -> "darwin-x86_64"
        currentOs.isWindows -> "windows-x86_64"
        else -> error("No Android toolchain defined for this OS: $currentOs")
    }
    environment("TOOLCHAIN", toolchain)
    environment("ARCH", arch)
    environment("ANDROID_NDK", "${localProperties.getProperty("sdk.dir")}/ndk/25.1.8937393")
    commandLine(bash, "build-android.sh")
}

val buildSecp256k1AndroidX86_64 by creatingBuildSecp256k1Android("x86_64")
val buildSecp256k1AndroidX86 by creatingBuildSecp256k1Android("x86")
val buildSecp256k1AndroidArm64v8a by creatingBuildSecp256k1Android("arm64-v8a")
val buildSecp256k1AndroidArmeabiv7a by creatingBuildSecp256k1Android("armeabi-v7a")

val clean by tasks.creating {
    group = "build"
    doLast {
        delete(projectDir.resolve("build"))
    }
}