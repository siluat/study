package io.reflectoring.buckpal.archunit

import com.tngtech.archunit.core.domain.JavaClasses

class Adapters(
    private val parentContext: HexagonalArchitecture,
    basePackage: String
) : ArchitectureElement(basePackage) {

    private val incomingAdapterPackages = mutableListOf<String>()
    private val outgoingAdapterPackages = mutableListOf<String>()

    fun incoming(packageName: String): Adapters {
        incomingAdapterPackages.add(fullQualifiedPackage(packageName))
        return this
    }

    fun outgoing(packageName: String): Adapters {
        outgoingAdapterPackages.add(fullQualifiedPackage(packageName))
        return this
    }

    private fun allAdapterPackages(): List<String> =
        incomingAdapterPackages + outgoingAdapterPackages

    fun and(): HexagonalArchitecture = parentContext

    fun dontDependOnEachOther(classes: JavaClasses) {
        val allAdapters = allAdapterPackages()
        allAdapters.forEach { adapter1 ->
            allAdapters.forEach { adapter2 ->
                if (adapter1 != adapter2) {
                    denyDependency(adapter1, adapter2, classes)
                }
            }
        }
    }

    fun doesNotDependOn(packageName: String?, classes: JavaClasses) {
        packageName?.let { denyDependency(basePackage, it, classes) }
    }

    fun doesNotContainEmptyPackages() {
        denyEmptyPackages(allAdapterPackages())
    }

}
