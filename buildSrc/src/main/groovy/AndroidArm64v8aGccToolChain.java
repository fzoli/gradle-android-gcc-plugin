import org.gradle.api.internal.file.FileResolver;
import org.gradle.internal.operations.BuildOperationProcessor;
import org.gradle.internal.os.OperatingSystem;
import org.gradle.internal.reflect.Instantiator;
import org.gradle.nativeplatform.toolchain.Gcc;
import org.gradle.nativeplatform.toolchain.internal.gcc.AbstractGccCompatibleToolChain;
import org.gradle.nativeplatform.toolchain.internal.gcc.DefaultGccPlatformToolChain;
import org.gradle.nativeplatform.toolchain.internal.gcc.version.CompilerMetaDataProviderFactory;
import org.gradle.process.internal.ExecActionFactory;

import org.gradle.api.Action;
import org.gradle.nativeplatform.toolchain.internal.gcc.TargetPlatformConfiguration;
import org.gradle.nativeplatform.platform.internal.NativePlatformInternal;

import org.gradle.nativeplatform.toolchain.internal.gcc.GccToolChain;
import org.gradle.nativeplatform.toolchain.internal.gcc.version.GccVersionResult;

import java.util.List;
import java.lang.reflect.*;

import java.io.File;

public class AndroidArm64v8aGccToolChain extends AndroidGccToolChain implements AndroidArm64v8aGcc {

    public static final String DEFAULT_NAME = "androidArm64v8aGcc";

    public AndroidArm64v8aGccToolChain(String name, BuildOperationProcessor buildOperationProcessor, OperatingSystem operatingSystem, FileResolver fileResolver, ExecActionFactory execActionFactory, CompilerMetaDataProviderFactory metaDataProviderFactory, Instantiator instantiator) {
        super(name, buildOperationProcessor, operatingSystem, fileResolver, execActionFactory, metaDataProviderFactory, instantiator);
    }

    @Override
    protected String getTypeName() {
        return "AndroidArm64v8aGcc";
    }

    @Override
    protected File getToolChainDirectory(String rootPath) {
        return new File(rootPath, "toolchains/aarch64-linux-android-4.9/prebuilt/linux-x86_64/bin");
    }

    @Override
    protected String getCppCompiler() {
        return "aarch64-linux-android-g++";
    }

    @Override
    protected String getCCompiler() {
        return "aarch64-linux-android-gcc";
    }

    @Override
    protected String getAssembler() {
        return "aarch64-linux-android-as";
    }

    @Override
    protected boolean supportsPlatform(NativePlatformInternal targetPlatform, String rawName) {
        return targetPlatform.getArchitecture().isArm() && rawName.contains("arm64v8a");
    }

    @Override
    protected Action<List<String>> getCArguments(final String rootPath) {
        return new Action<List<String>>() {
                public void execute(List<String> args) {
                    args.add("-MMD");
                    args.add("-MP");
                    args.add("-fpic");
                    args.add("-ffunction-sections");
                    args.add("-funwind-tables");
                    args.add("-fstack-protector");
                    args.add("-no-canonical-prefixes");
                    args.add("-O2");
                    args.add("-g");
                    args.add("-DNDEBUG");
                    args.add("-fomit-frame-pointer");
                    args.add("-fstrict-aliasing");
                    args.add("-funswitch-loops");
                    args.add("-finline-limit=300");
                    args.add("-O0");
                    args.add("-UNDEBUG");
                    args.add("-fno-omit-frame-pointer");
                    args.add("-fno-strict-aliasing");
                    if (!rootPath.isEmpty()) {
                        args.add("-I" + rootPath + "/sources/cxx-stl/llvm-libc++/libcxx/include");
                        args.add("-I" + rootPath + "/sources/cxx-stl/llvm-libc++/../llvm-libc++abi/libcxxabi/include");
                        args.add("-I" + rootPath + "/sources/cxx-stl/llvm-libc++/../../android/support/include");
                        args.add("-I" + rootPath + "/platforms/android-21/arch-arm64/usr/include");
                    }
                    args.add("-DANDROID");
                    args.add("-Wa,--noexecstack");
                    args.add("-Wformat");
                    args.add("-Werror=format-security");
                }
            };
    }

    @Override
    protected Action<List<String>> getCppArguments(final String rootPath) {
        return new Action<List<String>>() {
                public void execute(List<String> args) {
                    args.add("-MMD");
                    args.add("-MP");
                    args.add("-fpic");
                    args.add("-ffunction-sections");
                    args.add("-funwind-tables");
                    args.add("-fstack-protector");
                    args.add("-no-canonical-prefixes");
                    args.add("-fno-exceptions");
                    args.add("-fno-rtti");
                    args.add("-O2");
                    args.add("-g");
                    args.add("-DNDEBUG");
                    args.add("-fomit-frame-pointer");
                    args.add("-fstrict-aliasing");
                    args.add("-funswitch-loops");
                    args.add("-finline-limit=300");
                    args.add("-O0");
                    args.add("-UNDEBUG");
                    args.add("-fno-omit-frame-pointer");
                    args.add("-fno-strict-aliasing");
                    if (!rootPath.isEmpty()) {
                        args.add("-I" + rootPath + "/sources/cxx-stl/llvm-libc++/libcxx/include");
                        args.add("-I" + rootPath + "/sources/cxx-stl/llvm-libc++/../llvm-libc++abi/libcxxabi/include");
                        args.add("-I" + rootPath + "/sources/cxx-stl/llvm-libc++/../../android/support/include");
                        args.add("-I" + rootPath + "/platforms/android-21/arch-arm64/usr/include");
                    }
                    args.add("-DANDROID");
                    args.add("-Wa,--noexecstack");
                    args.add("-Wformat");
                    args.add("-Werror=format-security");
                    args.add("-std=c++11");
                    args.add("-fno-strict-aliasing");
                    args.add("-fexceptions");
                    args.add("-frtti");
                }
            };
    }

    @Override
    protected Action<List<String>> getLinkerArguments(final String rootPath) {
        return new Action<List<String>>() {
                public void execute(List<String> args) {
                    if (!rootPath.isEmpty()) {
                        args.add("--sysroot=" + rootPath + "/platforms/android-21/arch-arm64");
                        args.add("" + rootPath + "/sources/cxx-stl/llvm-libc++/libs/arm64-v8a/libc++_static.a");
                    }
                    args.add("-lgcc");
                    args.add("-no-canonical-prefixes");
                    args.add("-Wl,--no-undefined");
                    args.add("-Wl,-z,noexecstack");
                    args.add("-Wl,-z,relro");
                    args.add("-Wl,-z,now");
                    args.add("-lc");
                    args.add("-lm");
                }
            };
    }
    
}
