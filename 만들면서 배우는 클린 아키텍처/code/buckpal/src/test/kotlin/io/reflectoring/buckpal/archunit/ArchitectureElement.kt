package io.reflectoring.buckpal.archunit

import com.tngtech.archunit.base.DescribedPredicate.greaterThanOrEqualTo
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.conditions.ArchConditions.containNumberOfElements
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses

abstract class ArchitectureElement(
    val basePackage: String
) {

    fun fullQualifiedPackage(relativePackage: String): String {
        return "$basePackage.$relativePackage"
    }

    companion object {
        fun denyDependency(fromPackageName: String, toPackageName: String, classes: JavaClasses) {
            noClasses()
                .that()
                .resideInAPackage(matchAllClassesInPackage(fromPackageName))
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(matchAllClassesInPackage(toPackageName))
                .check(classes)
        }

        fun denyAnyDependency(fromPackages: List<String>, toPackages: List<String>, classes: JavaClasses) {
            fromPackages.forEach { fromPackage ->
                toPackages.forEach { toPackage ->
                    noClasses()
                        .that()
                        .resideInAPackage(matchAllClassesInPackage(fromPackage))
                        .should()
                        .dependOnClassesThat()
                        .resideInAnyPackage(matchAllClassesInPackage(toPackage))
                        .check(classes)
                }
            }
        }

        private fun matchAllClassesInPackage(packageName: String): String {
            return "$packageName.."
        }
    }

    fun denyEmptyPackage(packageName: String) {
        classes()
            .that()
            .resideInAPackage(matchAllClassesInPackage(packageName))
            .should(containNumberOfElements(greaterThanOrEqualTo(1)))
            .check(classesInPackage(packageName))
    }

    private fun classesInPackage(packageName: String): JavaClasses {
        return ClassFileImporter().importPackages(packageName)
    }

    fun denyEmptyPackages(packages: List<String>) {
        packages.forEach { packageName ->
            denyEmptyPackage(packageName)
        }
    }

}
