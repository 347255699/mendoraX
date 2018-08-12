package org.mendoraX.container.scanner;

import java.util.List;

/**
 * Scanner for instantiation a class set blow target package path.
 * Created by kam on 2018/2/4.
 */
public interface PackageScanner {
    String FILE_SUFFIX_CLASS = ".class";
    String FILE_SUFFIX_JAR = ".jar";

    /**
     * Find target element blow package
     *
     * @param packagePath
     * @return
     */
    List<String> classNames(String packagePath);
}
