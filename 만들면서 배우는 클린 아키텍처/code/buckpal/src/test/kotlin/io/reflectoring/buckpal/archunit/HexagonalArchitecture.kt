package io.reflectoring.buckpal.archunit

import com.tngtech.archunit.core.domain.JavaClasses

class HexagonalArchitecture(basePackage: String) : ArchitectureElement(basePackage) {

    private lateinit var adapters: Adapters
    private lateinit var applicationLayer: ApplicationLayer
    private var configurationPackage: String? = null
    private val domainPackages: MutableList<String> = mutableListOf()

    companion object {
        fun boundedContext(basePackage: String): HexagonalArchitecture {
            return HexagonalArchitecture(basePackage)
        }
    }

    fun withAdaptersLayer(adaptersPackage: String): Adapters {
        adapters = Adapters(this, fullQualifiedPackage(adaptersPackage))
        return adapters
    }

    fun withDomainLayer(domainPackage: String): HexagonalArchitecture {
        domainPackages.add(fullQualifiedPackage(domainPackage))
        return this
    }

    fun withApplicationLayer(applicationPackage: String): ApplicationLayer {
        applicationLayer = ApplicationLayer(fullQualifiedPackage(applicationPackage), this)
        return applicationLayer
    }

    fun withConfiguration(packageName: String): HexagonalArchitecture {
        configurationPackage = fullQualifiedPackage(packageName)
        return this
    }

    private fun domainDoesNotDependOnOtherPackages(classes: JavaClasses) {
        denyAnyDependency(domainPackages, listOfNotNull(adapters.basePackage), classes)
        denyAnyDependency(domainPackages, listOfNotNull(applicationLayer.basePackage), classes)
    }

    fun check(classes: JavaClasses) {
        adapters.doesNotContainEmptyPackages()
        adapters.dontDependOnEachOther(classes)
        adapters.doesNotDependOn(configurationPackage, classes)
        applicationLayer.doesNotContainEmptyPackages()
        applicationLayer.doesNotDependOn(adapters.basePackage, classes)
        configurationPackage?.let { applicationLayer.doesNotDependOn(it, classes) }
        domainDoesNotDependOnOtherPackages(classes)
    }

}
