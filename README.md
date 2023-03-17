"# SimpleEditorr" 
** Problem from SonarLint:-

Unnecessarily imports should be removed - Minor

Standard outputs should not be used directly to log anything - Major

Method names should be comply with a naming convention - Minor

Unused “private” methods should be removed - Major - private methods that are never executed are dead code: unnecessary, inoperative code that should be removed. Cleaning out dead code decreases the size of the maintained codebase, making it easier to understand the program and preventing bugs from being introduced.

Multiple variables should not be declared on the same line- Minor

Cognitive Complexity of methods should not be too high  - critical - Cognitive Complexity is a measure of how hard the control flow of a method is to understand. Methods with high Cognitive Complexity will be difficult to maintain.

Static base class members should not be accessed via derived types

Null pointers Exception

String literais should not be duplicated - Critical - Duplicated string literals make the process of refactoring error-prone, since you must be sure to update all occurrences.
On the other hand, constants can be referenced from many places, but only need to be updated in a single place.

Try-catch blocks should not be nested - Major - Nesting try/catch blocks severely impacts the readability of source code because it makes it too difficult to understand which block will catch which exception.

Generic exceptions should never be thrown - Major - Using such generic exceptions as Error, RuntimeException, Throwable, and Exception prevents calling methods from handling true, system-generated exceptions differently than application-generated errors.

class variable fields should not have public accessibility - Minor - Public class variable fields do not respect the encapsulation principle and has three main disadvantages:
Additional behavior such as validation cannot be added.
The internal representation is exposed, and cannot be changed afterwards.
Member values are subject to change from anywhere in the code and may not meet the programmer’s assumptions.
