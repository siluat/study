package io.reflectoring.buckpal.archunit

import com.tngtech.archunit.core.domain.JavaClasses

class ApplicationLayer(
    basePackage: String,
    private val parentContext: HexagonalArchitecture
) : ArchitectureElement(basePackage) {

    private val incomingPortsPackages = mutableListOf<String>()
    private val outgoingPortsPackages = mutableListOf<String>()
    private val servicePackages = mutableListOf<String>()

    fun incomingPorts(packageName: String): ApplicationLayer {
        incomingPortsPackages.add(fullQualifiedPackage(packageName))
        return this
    }

    fun outgoingPorts(packageName: String): ApplicationLayer {
        outgoingPortsPackages.add(fullQualifiedPackage(packageName))
        return this
    }

    fun services(packageName: String): ApplicationLayer {
        servicePackages.add(fullQualifiedPackage(packageName))
        return this
    }

    fun and(): HexagonalArchitecture = parentContext

    fun doesNotDependOn(packageName: String, classes: JavaClasses) {
        denyDependency(basePackage, packageName, classes)
    }

    fun incomingAndOutgoingPortsDoNotDependOnEachOther(classes: JavaClasses) {
        denyAnyDependency(incomingPortsPackages, outgoingPortsPackages, classes)
        denyAnyDependency(outgoingPortsPackages, incomingPortsPackages, classes)
    }


    private fun allPackages(): List<String> =
        incomingPortsPackages + outgoingPortsPackages + servicePackages

    fun doesNotContainEmptyPackages() {
        denyEmptyPackages(allPackages())
    }

}
