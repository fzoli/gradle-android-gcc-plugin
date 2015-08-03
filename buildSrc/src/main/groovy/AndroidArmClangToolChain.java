import org.gradle.api.internal.file.FileResolver;
import org.gradle.internal.operations.BuildOperationProcessor;
import org.gradle.internal.os.OperatingSystem;
import org.gradle.internal.reflect.Instantiator;
import org.gradle.nativeplatform.toolchain.Clang;
import org.gradle.nativeplatform.toolchain.internal.gcc.AbstractGccCompatibleToolChain;
import org.gradle.nativeplatform.toolchain.internal.gcc.DefaultGccPlatformToolChain;
import org.gradle.nativeplatform.toolchain.internal.gcc.version.CompilerMetaDataProviderFactory;
import org.gradle.process.internal.ExecActionFactory;

import org.gradle.nativeplatform.toolchain.internal.clang.ClangToolChain;

import java.util.List;
import org.gradle.api.Action;
import org.gradle.nativeplatform.toolchain.internal.gcc.TargetPlatformConfiguration;
import org.gradle.nativeplatform.platform.internal.NativePlatformInternal;
import org.gradle.nativeplatform.toolchain.internal.gcc.DefaultGccPlatformToolChain;

import java.lang.reflect.*;

public class AndroidArmClangToolChain extends ClangToolChain implements AndroidArmClang {

    public static final String DEFAULT_NAME = "androidArmClang";

    public AndroidArmClangToolChain(String name, BuildOperationProcessor buildOperationProcessor, OperatingSystem operatingSystem, FileResolver fileResolver, ExecActionFactory execActionFactory, CompilerMetaDataProviderFactory metaDataProviderFactory, Instantiator instantiator) {
        super(name, buildOperationProcessor, operatingSystem, fileResolver, execActionFactory, metaDataProviderFactory, instantiator);
	publicTarget(new ArmArchitecture());
    }

    public void publicTarget(TargetPlatformConfiguration configuration) {
        try {
            Method m = org.gradle.nativeplatform.toolchain.internal.gcc.AbstractGccCompatibleToolChain.class.getDeclaredMethod("target", TargetPlatformConfiguration.class);
            m.setAccessible(true);
            m.invoke(this, configuration);
        }
        catch (Exception e) {
            System.err.println("AndroidArmClangToolChain target exception: " + e.toString());
            e.printStackTrace();
            throw new RuntimeException("AndroidArmClangToolChain target exception", e);
        }
    }

    @Override
    protected void configureDefaultTools(DefaultGccPlatformToolChain toolChain) {
        toolChain.getLinker().setExecutable("arm-linux-androideabi-clang++");
        toolChain.getcCompiler().setExecutable("arm-linux-androideabi-clang");
        toolChain.getCppCompiler().setExecutable("arm-linux-androideabi-clang++");
        toolChain.getObjcCompiler().setExecutable("arm-linux-androideabi-clang");
        toolChain.getObjcppCompiler().setExecutable("arm-linux-androideabi-clang++");
        toolChain.getAssembler().setExecutable("arm-linux-androideabi-as");
    }

    @Override
    protected String getTypeName() {
        return "AndroidArmClang";
    }

    private class ArmArchitecture implements TargetPlatformConfiguration {
        public boolean supportsPlatform(NativePlatformInternal targetPlatform) {
            return targetPlatform.getOperatingSystem().isLinux()
                && targetPlatform.getName().toLowerCase().contains("android")
                && targetPlatform.getArchitecture().isArm();
        }

        public void apply(DefaultGccPlatformToolChain gccToolChain) {
            Action<List<String>> args = new Action<List<String>>() {
                public void execute(List<String> args) {
                    
                }
            };
            gccToolChain.getCppCompiler().withArguments(args);
            gccToolChain.getcCompiler().withArguments(args);
            gccToolChain.getObjcCompiler().withArguments(args);
            gccToolChain.getObjcppCompiler().withArguments(args);
            gccToolChain.getLinker().withArguments(args);
            gccToolChain.getAssembler().withArguments(args);
        }
    }

}
