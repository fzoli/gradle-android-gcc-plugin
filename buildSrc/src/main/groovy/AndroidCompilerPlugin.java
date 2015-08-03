import org.gradle.api.Incubating;
import org.gradle.api.NamedDomainObjectFactory;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.internal.operations.BuildOperationProcessor;
import org.gradle.internal.os.OperatingSystem;
import org.gradle.internal.reflect.Instantiator;
import org.gradle.internal.service.ServiceRegistry;
import org.gradle.model.Defaults;
import org.gradle.model.RuleSource;
import org.gradle.nativeplatform.plugins.NativeComponentPlugin;
import org.gradle.nativeplatform.toolchain.Clang;
import org.gradle.nativeplatform.toolchain.internal.NativeToolChainRegistryInternal;
import org.gradle.nativeplatform.toolchain.internal.clang.ClangToolChain;
import org.gradle.nativeplatform.toolchain.internal.gcc.version.CompilerMetaDataProviderFactory;
import org.gradle.process.internal.ExecActionFactory;

@Incubating
public class AndroidCompilerPlugin implements Plugin<Project> {

    private static final String EXT_ANDROID_COMPILER = "androidCompiler";

    private static AndroidCompilerPluginExtension extension;

    public static final AndroidCompilerPluginExtension getExtension() {
        return extension;
    }

    public void apply(Project project) {
        project.getExtensions().create(EXT_ANDROID_COMPILER, AndroidCompilerPluginExtension.class);
        project.getPluginManager().apply(NativeComponentPlugin.class);
        extension = (AndroidCompilerPluginExtension) project.getExtensions().findByName(EXT_ANDROID_COMPILER);
    }

    static class Rules extends RuleSource {
        @Defaults
        public static void addToolChain(NativeToolChainRegistryInternal toolChainRegistry, ServiceRegistry serviceRegistry) {
            final FileResolver fileResolver = serviceRegistry.get(FileResolver.class);
            final ExecActionFactory execActionFactory = serviceRegistry.get(ExecActionFactory.class);
            final Instantiator instantiator = serviceRegistry.get(Instantiator.class);
            final BuildOperationProcessor buildOperationProcessor = serviceRegistry.get(BuildOperationProcessor.class);
            final CompilerMetaDataProviderFactory metaDataProviderFactory = serviceRegistry.get(CompilerMetaDataProviderFactory.class);

            toolChainRegistry.registerFactory(AndroidArmClang.class, new NamedDomainObjectFactory<AndroidArmClang>() {
                public AndroidArmClang create(String name) {
                    return instantiator.newInstance(AndroidArmClangToolChain.class, name, buildOperationProcessor, OperatingSystem.current(), fileResolver, execActionFactory, metaDataProviderFactory, instantiator);
                }
            });
            toolChainRegistry.registerDefaultToolChain(AndroidArmClangToolChain.DEFAULT_NAME, AndroidArmClang.class);

            toolChainRegistry.registerFactory(AndroidArm64v8aGcc.class, new NamedDomainObjectFactory<AndroidArm64v8aGcc>() {
                public AndroidArm64v8aGcc create(String name) {
                    return instantiator.newInstance(AndroidArm64v8aGccToolChain.class, name, buildOperationProcessor, OperatingSystem.current(), fileResolver, execActionFactory, metaDataProviderFactory, instantiator);
                }
            });
            toolChainRegistry.registerDefaultToolChain(AndroidArm64v8aGccToolChain.DEFAULT_NAME, AndroidArm64v8aGcc.class);
        }

    }
}
