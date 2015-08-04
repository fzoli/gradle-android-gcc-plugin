import org.gradle.api.Nullable;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.internal.operations.BuildOperationProcessor;
import org.gradle.internal.os.OperatingSystem;
import org.gradle.internal.reflect.Instantiator;
import org.gradle.nativeplatform.toolchain.internal.gcc.DefaultGccPlatformToolChain;
import org.gradle.nativeplatform.toolchain.internal.gcc.version.CompilerMetaDataProviderFactory;
import org.gradle.process.internal.ExecActionFactory;

import org.gradle.api.Action;
import org.gradle.nativeplatform.toolchain.internal.gcc.TargetPlatformConfiguration;
import org.gradle.nativeplatform.platform.internal.NativePlatformInternal;
import org.gradle.nativeplatform.toolchain.internal.gcc.GccToolChain;
import org.gradle.nativeplatform.toolchain.internal.CommandLineToolInvocation;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.*;

import java.io.File;

import org.gradle.internal.operations.BuildOperationWorker;
import org.gradle.internal.operations.BuildOperation;
import org.gradle.internal.operations.BuildOperationQueue;
import org.gradle.nativeplatform.toolchain.internal.DefaultCommandLineToolInvocationWorker;

import org.gradle.internal.operations.MultipleBuildOperationFailures;
import org.gradle.nativeplatform.toolchain.internal.DefaultMutableCommandLineToolContext;

class BuildOperationProcessorDecorated implements BuildOperationProcessor {

    private final BuildOperationProcessor delegate;
    private final ExecActionFactory execActionFactory;

    BuildOperationProcessorDecorated(BuildOperationProcessor delegate, ExecActionFactory execActionFactory) {
        this.delegate = delegate;
        this.execActionFactory = execActionFactory;
    }

    @Override
    public <T extends BuildOperation> BuildOperationQueue<T> newQueue(BuildOperationWorker<T> worker, @Nullable String logLocation) {
        if (worker.toString().contains("Linker")) {
            BuildOperationQueue<T> queue = delegate.newQueue(worker, logLocation);
            return new BuildOperationQueueDecorated<T>(queue, execActionFactory);
        }
        return delegate.newQueue(worker, logLocation);
    }

}

class BuildOperationQueueDecorated<T extends BuildOperation> implements BuildOperationQueue<T> {

    private final BuildOperationQueue<T> delegate;
    private final ExecActionFactory execActionFactory;

    BuildOperationQueueDecorated(BuildOperationQueue<T> delegate, ExecActionFactory execActionFactory) {
        this.delegate = delegate;
        this.execActionFactory = execActionFactory;
    }

    private int addCount = 0;

    @Override
    public void add(T operation) {
        delegate.add(operation);
        addCount++;
        if (addCount == 1) {

            delegate.waitForCompletion();
            System.out.println("linker finished: " + operation.getDescription() + " " + operation.getClass());
            // LinkerSpec#getOutputFile()

            // TODO: strip
            CommandLineToolInvocation invocation = new DefaultMutableCommandLineToolContext().createInvocation(
                    "touch salala", new ArrayList<String>(){{
                        add("/mnt/ntfs/salllllllala");
                    }}, new org.gradle.internal.operations.logging.DefaultBuildOperationLoggerFactory().newOperationLogger("touch logger", new File("/mnt/ntfs/hihihihi")));
            new DefaultCommandLineToolInvocationWorker("touch test", new File("/bin/touch"), execActionFactory).execute(invocation);

        }
    }

    @Override
    public void waitForCompletion() throws MultipleBuildOperationFailures {
        delegate.waitForCompletion();
    }

}

abstract class AndroidGccToolChain extends GccToolChain {

    public AndroidGccToolChain(String name, BuildOperationProcessor buildOperationProcessor, OperatingSystem operatingSystem, FileResolver fileResolver, ExecActionFactory execActionFactory, CompilerMetaDataProviderFactory metaDataProviderFactory, Instantiator instantiator) {
        super(instantiator, name, new BuildOperationProcessorDecorated(buildOperationProcessor, execActionFactory), operatingSystem, fileResolver, execActionFactory, metaDataProviderFactory);
        publicTarget(new AndroidArchitecture());
    }

    protected abstract File getToolChainDirectory(String rootPath);

    @Override
    protected abstract String getTypeName();

    protected abstract String getCppCompiler();

    protected abstract String getCCompiler();

    protected abstract String getAssembler();

    protected abstract boolean supportsPlatform(NativePlatformInternal targetPlatform, String rawName);

    protected Action<List<String>> getCArguments(final String rootPath) {
        return new Action<List<String>>() {
                public void execute(List<String> args) {
                    
                }
            };
    }

    protected Action<List<String>> getCppArguments(final String rootPath) {
        return new Action<List<String>>() {
                public void execute(List<String> args) {
                    
                }
            };
    }

    protected Action<List<String>> getLinkerArguments(final String rootPath) {
        return new Action<List<String>>() {
                public void execute(List<String> args) {
                    
                }
            };
    }

    protected Action<List<String>> getAssemblerArguments(final String rootPath) {
        return new Action<List<String>>() {
                public void execute(List<String> args) {
                    
                }
            };
    }

    @Override
    protected void configureDefaultTools(DefaultGccPlatformToolChain toolChain) {
        String prefix = getToolChainPrefix();
        String cppCompiler = prefix + getCppCompiler();
        String cCompiler = prefix + getCCompiler();
        String assembler = prefix + getAssembler();
        toolChain.getLinker().setExecutable(cppCompiler);
        toolChain.getcCompiler().setExecutable(cCompiler);
        toolChain.getCppCompiler().setExecutable(cppCompiler);
        toolChain.getObjcCompiler().setExecutable(cCompiler);
        toolChain.getObjcppCompiler().setExecutable(cppCompiler);
        toolChain.getAssembler().setExecutable(assembler);
    }

    private String getNdkRootPath() {
        return AndroidCompilerPlugin.getExtension().getNdkRootPath();
    }

    protected String getToolChainPrefix() {
        String rootPath = getNdkRootPath();
        if (rootPath.isEmpty()) {
            return "";
        }
        File dir = getToolChainDirectory(rootPath);
        if (!dir.isDirectory()) {
            return "";
        }
        return dir.getAbsolutePath() + "/";
    }

    public void publicTarget(TargetPlatformConfiguration configuration) {
        try {
            Method m = org.gradle.nativeplatform.toolchain.internal.gcc.AbstractGccCompatibleToolChain.class.getDeclaredMethod("target", TargetPlatformConfiguration.class);
            m.setAccessible(true);
            m.invoke(this, configuration);
        }
        catch (Exception e) {
            System.err.println("AndroidGccToolChain target exception: " + e.toString());
            e.printStackTrace();
            throw new RuntimeException("AndroidGccToolChain target exception", e);
        }
    }

    private class AndroidArchitecture implements TargetPlatformConfiguration {

        public boolean supportsPlatform(NativePlatformInternal targetPlatform) {
            String name = targetPlatform.getName().toLowerCase().replaceAll("-*", "");
            return targetPlatform.getOperatingSystem().isLinux()
                && name.contains("android")
                && AndroidGccToolChain.this.supportsPlatform(targetPlatform, name);
        }

        public void apply(DefaultGccPlatformToolChain gccToolChain) {
            String rootPath = getNdkRootPath();
            Action<List<String>> cArgs = getCArguments(rootPath);
            Action<List<String>> cppArgs = getCppArguments(rootPath);
            Action<List<String>> lnArgs = getLinkerArguments(rootPath);
            Action<List<String>> asArgs = getAssemblerArguments(rootPath);

            gccToolChain.getCppCompiler().withArguments(cppArgs);
            gccToolChain.getcCompiler().withArguments(cArgs);
            gccToolChain.getObjcCompiler().withArguments(cArgs);
            gccToolChain.getObjcppCompiler().withArguments(cppArgs);
            gccToolChain.getLinker().withArguments(lnArgs);
            gccToolChain.getAssembler().withArguments(asArgs);

        }

    }
    
}
