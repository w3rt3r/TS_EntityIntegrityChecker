# TS_EntityIntegrityChecker

The test validates the integrity of Base Entities instances published by the System under Test (SuT). The information validated is based on a shared predefined scenario between both the IVCT_TestRunner and the SuT. The currently in use scenario describes multiple base entities taken from the GRIM-RPR standard which are then loaded through a csv file.

The TestSuite is divided in 3 testcases which will verify the following elements of a BaseEntity instance:
- EntityIdentifier: The testcase verifies that each entity from the FAD finds a match in ID with the discovered entities.
- EntityType: The testcase verifies that each ID-matched entities present the same Type information.
- Spatial: The testcase verifies that each ID-matched entities present the same Spatial information.


## Requirement 
The use of entities from the GRIM-RPR FOM. 

The SuT and the IVCTtest use the same scenario document and are both connected to the same federation. The SuT has the capability to create the entities as requested in the .csv scenario.

The environment variable IVCT\_CONF is define to the IVCT\_Runtime folder.

## TestCase

The TestCase flow is as follows: 

### Precondition
The SuT needs to connect to the federation and publish every entity in the scenario document. The Simulated System under test does the following steps before the execution of the test: 

### Test Steps

##### The Sut steps
The SuT should do the following operation. For development purpose only, a Simulated System Under Test (SiSut) called EntityAgent execute the following steps base on configuration added in the IVCT\_Runtime/IVCTsut folder.

1. The SuT connects to the federation.
2. The SuT register publishing of the Base Entities and the following attributes: EntityIdentifier, EntityType, Spatial in accordance to the FOM.
3. The SuT reads the scenario document and publish the generated base entities instances listed in the scenario document.

##### The main test steps
The test flow is  the same for each testCase and is described in the following diagram:

1. Operator starts the TestCase using the GUI or the UI then the TestRunner execute the testcase.
2. TestRunner registers the subscription to read the base Entity and their following attributes: EntityIdentifier, EntityType and Spatial.
3. TestRunner discovers the Entities. The federation returns the BaseEntity Handles.
4. For each Entity in the system the TestRunner requests all its attributes.
5. The SuT provides the requested attributes.
6. TestRunner reads the scenario document and validates that all the entities requested respect the scenario document.
7. Enact judgment.

![IntegrityCheckerFlow](https://github.com/MSG134/TS_EntityIntegrityChecker/raw/master/IntegrityChecker.png "IntegrityCheckerFlow")

Note: 
Only 2 dead reckoning algorithms as been implemented: DRM_FPW and DRM_RVW

------

SiSut : Simulated System Under Test provided as EntityAgent in the repository.  
Sut : System Under Test


Note: 
Only 2 dead reckoning algorithm as been implemented: DRM (FPW) and DRM(RVW)

How to Build
-------
First, follow the instructions to install the [IVCT Framework](https://github.com/MSG134/IVCT_Framework) if it is not already done. Make sure the compiled files are present on the local m2 repository as this project is dependent on them.

./gradlew clean  
./gradlew build  
./gradlew eclipse  
./gradlew installDist  

It is important to copy and paste some files to the IVCT\_Runtime folder.
 - TS\_EntityIntegrityChecker/build/install/TS\_EntityIntegrityChecker/ folder needs to be copied into IVCT\_Runtime/TestSuites/.
 - if you modified any element in the EntityAgent/src/main/resources/ folder, copy them into IVCT_Runtime/IVCTsut/EntityAgent/resources/

The badge and the SuT information should be in the IVCT_Runtime.

## Package Contents
The different folders contained in this project refer to different capabilities and features. Refer to this section for a description of each.

### EntityAgent
Contains the SiSuT project. The list of published entities comes from a .csv file refered in src/main/resources/config/config.properties. This project contains dependencies to the IVCT_HLA_BaseEntityModel.

### GrimRprFomObject
Represents the generic data model for GRIM-RPR compliant java objects. No dependencies.

### IVCT\_HLA\_BaseEntity
Contains the necessary libraries to translate java objects into HLA compliant objects and vice-versa. This project contains dependencies to the GrimRprFomObject.

### IVCT_RunTime
The folder IVCT_Runtime contains the required configuration to run the testcases using the IVCT test runner.
More information on this subject can be found in the wiki (https://github.com/MSG134/IVCT_Runtime/wiki)

### TS_EntityIntegrityChecker
This project contains the main testing logic. This project contains dependencies to the IVCT_HLA_BaseEntity and msg134-ivct-framework projects.
