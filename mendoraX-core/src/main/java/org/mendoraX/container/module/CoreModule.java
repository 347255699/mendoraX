package org.mendoraX.container.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mendoraX.container.scanner.PackageScanner;
import org.mendoraX.container.scanner.PackageScannerImpl;
import org.mendoraX.initData.Const;
import org.mendoraX.initData.vo.MicroService;
import org.mendoraX.initData.vo.config.DataSource;
import org.mendoraX.initData.vo.config.SysConfig;
import org.mendoraX.initData.vo.config.WebConfig;

/**
 * @auther menfre
 * @date 2018/8/11
 * version: 1.0
 * desc: 核心Module
 */
@RequiredArgsConstructor
public class CoreModule extends AbstractModule {
    @NonNull
    private ClassLoader cl;
    @NonNull
    private MicroService microService;

    @Override
    protected void configure() {
        if (microService == MicroService.WEB)
            bind(WebConfig.class).toInstance(WebConfig.loading());
        if (microService == MicroService.REAR)
            bind(DataSource.class).toInstance(DataSource.loading());
        SysConfig sysConfig = SysConfig.loading();
        bind(SysConfig.class).toInstance(sysConfig);
        bind(String.class).annotatedWith(Names.named(Const.TAG_BASE_PACKAGE)).toInstance(sysConfig.getBasePackage());
        bind(PackageScanner.class).to(PackageScannerImpl.class);
    }

    @Provides
    public ClassLoader provideClassLoader() {
        return cl;
    }

    @Provides
    public MicroService provideMicroService() {
        return microService;
    }
}
