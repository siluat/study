package io.reflectoring.buckpal

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import io.reflectoring.buckpal.archunit.HexagonalArchitecture
import kotlin.test.Test

class DependencyRuleTests {

    @Test
    fun `domain layer does not depend on application layer`() {
        noClasses()
            .that()
            .resideInAPackage("io.reflectoring.buckpal.account.domain..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("io.reflectoring.buckpal.account.application..")
            .check(ClassFileImporter().importPackages("io.reflectoring.buckpal.."))
    }

    @Test
    fun `validate registration context architecture`() {
        HexagonalArchitecture.boundedContext("io.reflectoring.buckpal.account")
            .withDomainLayer("domain")
            .withAdaptersLayer("adapter")
            .incoming("in.web")
            .outgoing("out.persistence")
            .and()
            .withApplicationLayer("application")
            .services("service")
            .incomingPorts("port.in")
            .outgoingPorts("port.out")
            .and()
            .withConfiguration("configuration")
            .check(ClassFileImporter().importPackages("io.reflectoring.buckpal.."))
    }

    @Test
    fun `test package dependencies`() {
        noClasses()
            .that()
            .resideInAPackage("io.reflectoring.buckpal.account.domain..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("io.reflectoring.buckpal.account.application..")
            .check(ClassFileImporter().importPackages("io.reflectoring.buckpal.."))
    }

}
